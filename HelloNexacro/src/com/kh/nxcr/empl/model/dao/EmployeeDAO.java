package com.kh.nxcr.empl.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kh.nxcr.empl.model.exception.EmployeeSaveException;
import com.kh.nxcr.empl.model.vo.Employee;


public class EmployeeDAO {

	private Properties prop = new Properties();
	
	public EmployeeDAO() {
		try {
			//클래스객체 위치찾기 : 절대경로를 반환한다. 
			// "/" : 루트디렉토리를 절대경로로 URL객체로 반환한다.
			// "./" : 현재디렉토리를 절대경로로 URL객체로 반환한다.
			// "./query.properties : 현재경로의 query.properties파일의 경로를 URL객체로 반환함.
			String fileName = EmployeeDAO.class.getResource("/nxcr-query.properties").getPath();
			prop.load(new FileReader(fileName));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public int insertEmployee(Connection conn, Employee empl) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertEmployee");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, empl.getEmplId());
			pstmt.setString(2, empl.getFullName());
			pstmt.setString(3, empl.getDeptCode());
			pstmt.setString(4, empl.getPosCode());
			pstmt.setDate(5, empl.getHireDate());
			pstmt.setString(6, empl.getGender());
			pstmt.setBoolean(7, empl.isMarried());
			pstmt.setInt(8, empl.getSalary());
			pstmt.setString(9, empl.getMemo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new EmployeeSaveException("insertEmployee 서비스 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateEmployee(Connection conn, Employee empl) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateEmployee");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, empl.getFullName());
			pstmt.setString(2, empl.getDeptCode());
			pstmt.setString(3, empl.getPosCode());
			pstmt.setDate(4, empl.getHireDate());
			pstmt.setString(5, empl.getGender());
			pstmt.setBoolean(6, empl.isMarried());
			pstmt.setInt(7, empl.getSalary());
			pstmt.setString(8, empl.getMemo());
			pstmt.setString(9, empl.getEmplId());
			
			result = pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw new EmployeeSaveException("updateEmployee 서비스 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteEmployee(Connection conn, String emplId) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteEmployee");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emplId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new EmployeeSaveException("deleteEmployee 서비스 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public List<Employee> selectAll(Connection conn) {
		List<Employee> list = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			list = new ArrayList<Employee>();
			
			while(rset.next()) {
				Employee emp = new Employee(
						rset.getString("EMPL_ID"), 
						rset.getString("FULL_NAME"),
						rset.getString("DEPT_CD"),
						rset.getString("POS_CD"),
						rset.getDate("HIRE_DATE"),
						rset.getInt("SALARY"),
						rset.getString("GENDER"),
						rset.getBoolean("MARRIED"),
						rset.getString("MEMO")    
					);
				
				list.add(emp);
			}
			
		} catch(SQLException e) {
			throw new EmployeeSaveException("selectAll 서비스 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public List<Employee> searchEmpl(Connection conn, String search) {
		List<Employee> list = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("searchEmpl");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + search + "%");
			
			rset = pstmt.executeQuery();
			list = new ArrayList<Employee>();
			while(rset.next()) {
				Employee emp = new Employee(
						rset.getString("EMPL_ID"), 
						rset.getString("FULL_NAME"),
						rset.getString("DEPT_CD"),
						rset.getString("POS_CD"),
						rset.getDate("HIRE_DATE"),
						rset.getInt("SALARY"),
						rset.getString("GENDER"),
						rset.getBoolean("MARRIED"),
						rset.getString("MEMO")    
					);
				
				list.add(emp);
			}
			
		} catch(SQLException e) {
			throw new EmployeeSaveException("searchEmpl 서비스 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}
}
