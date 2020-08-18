package com.kh.nxcr.movie.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.nxcr.movie.model.service.MovieService;
import com.nexacro17.xapi.data.DataSet;
import com.nexacro17.xapi.data.PlatformData;
import com.nexacro17.xapi.data.VariableList;
import com.nexacro17.xapi.tx.HttpPlatformRequest;
import com.nexacro17.xapi.tx.HttpPlatformResponse;
import com.nexacro17.xapi.tx.PlatformException;
import com.nexacro17.xapi.tx.PlatformType;

/**
 * Servlet implementation class EmplSaveServlet
 */
@WebServlet("/movie/save")
public class MovieSaveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieSaveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println(request.getMethod());
		
		//1. request : 넥사크로 전용 객체 처리
		//HttpPlatformRequest 
		//PlatformData
		HttpPlatformRequest httpPlatformRequest = new HttpPlatformRequest(request);
		try {
			//사용자 요청 데이터 가져오기
			httpPlatformRequest.receiveData();
		} catch (PlatformException e) {
			e.printStackTrace();
		}
		PlatformData reqPlatformData = httpPlatformRequest.getData();
		
		//2. 사용자입력값 처리 strArgument
		
		
		//3. Dataset 처리
		//a. 하나의 데이터셋인 경우
		DataSet inDataSet = reqPlatformData.getDataSet("in_ds");
//		System.out.println("inDataSet = " + inDataSet);//com.nexacro17.xapi.data.DataSet@54426cc7
		//b. 복수개의 데이터셋인 경우
//		DataSetList dataSetList = reqPlatformData.getDataSetList();
//		DataSet dataSet = dataSetList.get("in_ds");
	
		//4. 업무로직
		int errorCode = 0;
		String errorMsg = "SUCCESS";
		
		try {
			MovieService  movieService = new MovieService();
			movieService.saveMovies(inDataSet);
		} catch (Throwable th) {
			th.printStackTrace();
			errorCode = -1;
			//예외중에 예외메세지가 null인 예외객체 처리
			errorMsg = th.getMessage() == null ? "사원정보 저장 오류!" : th.getMessage();
		}
		
		//5. response
		//PlatformData
		//HttpPlatformResponse
		PlatformData resPlatformData = new PlatformData();
		//처리 결과 담기
		VariableList resVarList = resPlatformData.getVariableList();
		//add(name:String, value:any) -> value 매개인자는 데이터타입별 overloading!
		//ErrorCode, ErrorMsg 대소문자 절대 구분!
		resVarList.add("ErrorCode", errorCode);
		resVarList.add("ErrorMsg", errorMsg);
		
		//저장된 Variable확인 
		//get(name:String) Variable객체 리턴
		//get자료형(name:String) 실제 저장된 값 리턴 
//		System.out.println(resPlatformData.getVariableList().get("ErrorCode"));//com.nexacro17.xapi.data.Variable@3f23912d[name=errorCode, type=3, isConvertingToDataType=true]
//		System.out.println(resPlatformData.getVariableList().get("ErrorMsg"));//com.nexacro17.xapi.data.Variable@f423fe0[name=errorMsg, type=2, isConvertingToDataType=true]
		
//		System.out.println(resPlatformData.getVariableList().getInt("ErrorCode"));
//		System.out.println(resPlatformData.getVariableList().getString("ErrorMsg"));
		
		
		HttpPlatformResponse httpPlatformResponse = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "utf-8");
		httpPlatformResponse.setData(resPlatformData);
		try {
			httpPlatformResponse.sendData();
		} catch(PlatformException e) {
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

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doOptions");
		resp.setStatus(200);
	}

	
	
}
