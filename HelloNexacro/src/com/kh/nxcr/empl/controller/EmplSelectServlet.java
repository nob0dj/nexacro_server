package com.kh.nxcr.empl.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.nxcr.empl.model.service.EmployeeService;
import com.kh.nxcr.empl.model.vo.Employee;
import com.nexacro17.xapi.data.DataSet;
import com.nexacro17.xapi.data.DataTypes;
import com.nexacro17.xapi.data.PlatformData;
import com.nexacro17.xapi.data.VariableList;
import com.nexacro17.xapi.tx.HttpPlatformResponse;
import com.nexacro17.xapi.tx.PlatformException;
import com.nexacro17.xapi.tx.PlatformType;

/**
 * Servlet implementation class EmplSelectServlet
 */
@WebServlet("/empl/select")
public class EmplSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmplSelectServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		System.out.println(request.getMethod());//POST
		
		//0. 응답에 작성할 에러코드, 에러메세지, PlatformData객체 초기화
		int errorCode = 0;
		String errorMsg = "SUCCESS";
		PlatformData resPlatformData = new PlatformData();
		
		//1. request : 넥사크로 전용 객체 처리
		//2. 사용자입력값 처리 strArgument
		
		try {
			//3. 업무로직
			EmployeeService employeeService = new EmployeeService();
			List<Employee> list = employeeService.selectAll();
			System.out.println("list@servlet = " + list);
			
			//4. 전송할 Dataset 처리
			//컬럼별 사이즈는 생략가능
			DataSet ds = new DataSet("out_ds");
			ds.addColumn("EMPL_ID",    DataTypes.STRING, 10);
			ds.addColumn("FULL_NAME",  DataTypes.STRING, 50);
			ds.addColumn("DEPT_CD",    DataTypes.STRING, 10);
			ds.addColumn("POS_CD",     DataTypes.STRING, 10);
			ds.addColumn("HIRE_DATE",  DataTypes.STRING, 30);
			ds.addColumn("SALARY",     DataTypes.INT,    15);
			ds.addColumn("GENDER",     DataTypes.STRING, 1);
			ds.addColumn("MARRIED",    DataTypes.STRING, 1);
			ds.addColumn("MEMO",       DataTypes.STRING, 256);
			
			for(Employee e : list) {
				//새행 생성 및 인덱스 가져오기
				int row = ds.newRow();
				ds.set(row, "EMPL_ID",   e.getEmplId());
				ds.set(row, "FULL_NAME", e.getFullName());
				ds.set(row, "DEPT_CD",   e.getDeptCode());
				ds.set(row, "POS_CD",    e.getPosCode());
				ds.set(row, "HIRE_DATE", e.getHireDate());
				ds.set(row, "SALARY",    e.getSalary());
				ds.set(row, "GENDER",    e.getGender());
				ds.set(row, "MARRIED",   e.isMarried());
				ds.set(row, "MEMO",      e.getMemo());
			}
			
			resPlatformData.addDataSet(ds);
			
		} catch(Throwable th) {
			th.printStackTrace();
			errorCode = -1;
			errorMsg = th.getMessage();
		}
		
		//응답에 전송할 변수 처리
		//키값 대소문자 구분할 것.
		VariableList resVarList = resPlatformData.getVariableList();
		resVarList.add("ErrorCode", errorCode);
		resVarList.add("ErrorMsg", errorMsg);
		
		//5. response
		//com.nexacro17.xapi.tx.HttpPlatformResponse.HttpPlatformResponse(HttpServletResponse httpRes, String contentType, String charset)
		HttpPlatformResponse httpPlatformResponse = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "utf-8");
		httpPlatformResponse.setData(resPlatformData);
		try {
			httpPlatformResponse.sendData();
		} catch (PlatformException e) {
			e.printStackTrace();
		}
	
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
