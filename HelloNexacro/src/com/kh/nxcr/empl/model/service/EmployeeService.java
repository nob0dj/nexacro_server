package com.kh.nxcr.empl.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.kh.nxcr.empl.model.dao.EmployeeDAO;
import com.kh.nxcr.empl.model.exception.EmployeeSaveException;
import com.kh.nxcr.empl.model.vo.Employee;
import com.nexacro17.xapi.data.DataSet;

public class EmployeeService {

	EmployeeDAO employeeDAO = new EmployeeDAO();

	public void saveEmployee(DataSet ds) {
		Connection conn = getConnection();

		try{
			//1.insert | update
			for(int i = 0; i < ds.getRowCount(); i++) {
				int result = 0;

				//rowType 구분
				//0. ROW_TYPE_NORMAL
				//1. ROW_TYPE_INSERTED
				//2. ROW_TYPE_UPDATED
				int rowType = ds.getRowType(i);
				
				//변경되지 않은 내역
				if(rowType == DataSet.ROW_TYPE_NORMAL)
					continue;
				
				//dataset.row -> Employee객체
				Employee empl = new Employee();
				empl.setEmplId(ds.getString(i, "EMPL_ID"));
				empl.setFullName(ds.getString(i, "FULL_NAME"));
				empl.setDeptCode(ds.getString(i, "DEPT_CD"));
				empl.setPosCode(ds.getString(i, "POS_CD"));
				empl.setGender(ds.getString(i, "GENDER"));
				empl.setMarried(ds.getBoolean(i, "MARRIED"));
				empl.setSalary(ds.getInt(i, "SALARY"));
				empl.setMemo(ds.getString(i, "MEMO"));
				
				//HIRE_DATE 처리
				//dataset.getDateTime():java.util.Date -> java.sql.Date
				java.util.Date temp = ds.getDateTime(i, "HIRE_DATE");
				java.sql.Date hireDate = new java.sql.Date(temp.getTime());
				empl.setHireDate(hireDate);
				
				//rowType에 따른 분기처리
				//행추가
				if(rowType == DataSet.ROW_TYPE_INSERTED)
					result = employeeDAO.insertEmployee(conn, empl);
				//행수정
				else if(rowType == DataSet.ROW_TYPE_UPDATED) 
					result = employeeDAO.updateEmployee(conn, empl);

				//정상처리 되지 않은 경우
				if(result == 0)
					throw new EmployeeSaveException("사원정보 저장 오류 : " + rowType);

			}
			
			//2. delete
			//empl_id
			for(int i = 0; i < ds.getRemovedRowCount(); i++) {
				String emplId = (String)ds.getRemovedData(i, "EMPL_ID");
				int result = employeeDAO.deleteEmployee(conn, emplId);
				
				if(result == 0) 
					throw new EmployeeSaveException("사원 정보 삭제 오류 ");
			}
			//트랜잭션 처리
			commit(conn);
			
		} catch(Exception e) {
			//하나라도 오류가 발생하면 전체 롤백처리
			rollback(conn);
			
			//다시 예외를 던져서 controller에서 분기처리하도록함.
			throw e;
		}
	}
	
	public void saveEmployee_(DataSet ds) {
		Connection conn = getConnection();
		int result = 0;
		
		//insert | update
		for(int i = 0; i < ds.getRowCount(); i++) {
			//row 상태 
			// 0 : Not modified, 1 : Insert, 2 : Update
			int rowType = ds.getRowType(i);
			System.out.println("rowType(" + i +") : " + rowType);
			
			Employee empl = new Employee();
			empl.setEmplId(ds.getString(i, "EMPL_ID"));
			empl.setFullName(ds.getString(i, "FULL_NAME"));
			empl.setDeptCode(ds.getString(i, "DEPT_CD"));
			empl.setPosCode(ds.getString(i, "POS_CD"));
			empl.setGender(ds.getString(i, "GENDER"));
			empl.setMarried(Boolean.parseBoolean(ds.getString(i, "MARRIED")));
			empl.setSalary(Integer.parseInt(ds.getString(i, "SALARY")));
			empl.setMemo(ds.getString(i, "MEMO"));

			//날짜변환 처리
			String temp = ds.getString(i, "HIRE_DATE");
			//System.out.println(temp);//20160202000000000
			
			// yyyy-MM-dd
			String yyyy = temp.substring(0, 4);
			String MM = temp.substring(4, 6);
			String dd = temp.substring(6, 8); //endIndex 필수
			empl.setHireDate(Date.valueOf(yyyy + "-" + MM + "-" + dd));

			//dao 요청
			if(rowType == DataSet.ROW_TYPE_INSERTED) {
				result = employeeDAO.insertEmployee(conn, empl);
				System.out.println("[" + empl.getEmplId() + "] 추가 " + (result > 0 ? "성공!" : "실패!"));
			}
			else if(rowType == DataSet.ROW_TYPE_UPDATED) {
				result = employeeDAO.updateEmployee(conn, empl);
				System.out.println("[" + empl.getEmplId() + "] 수정 " + (result > 0 ? "성공!" : "실패!"));
			}
			
			//DataSet.ROW_TYPE_REMOVED deprecated
//			else if(rowType == DataSet.ROW_TYPE_REMOVED)
//				result = employeeDAO.deleteEmployee(conn, emplId);
				
		}
		
		//delete
		for(int i = 0; i < ds.getRemovedRowCount(); i++) {
			String emplId = (String)ds.getRemovedData(i, "EMPL_ID");
			result = employeeDAO.deleteEmployee(conn, emplId);
			System.out.println("[" + emplId + "] 삭제 " + (result > 0 ? "성공!" : "실패!"));
		}
		
		//트랜잭션처리
		commit(conn);
//		rollback(conn);
		
		close(conn);
	}

	public List<Employee> selectAll() {
		Connection conn = getConnection();
		List<Employee> list = employeeDAO.selectAll(conn);
		close(conn);
		return list;
	}

	public List<Employee> searchEmpl(String search) {
		Connection conn = getConnection();
		List<Employee> list = employeeDAO.searchEmpl(conn, search);
		close(conn);
		return list;
	}
	
}
