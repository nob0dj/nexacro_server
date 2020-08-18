package com.kh.nxcr.celeb.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.nexacro17.xapi.data.ColumnHeader;
import com.nexacro17.xapi.data.DataSet;
import com.nexacro17.xapi.data.DataTypes;
import com.nexacro17.xapi.data.PlatformData;
import com.nexacro17.xapi.data.VariableList;
import com.nexacro17.xapi.tx.HttpPlatformResponse;
import com.nexacro17.xapi.tx.PlatformException;
import com.nexacro17.xapi.tx.PlatformType;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * Servlet implementation class CelebFileUploadServlet
 */
@WebServlet("/celeb/fileUpload")
public class CelebFileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CelebFileUploadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		/******* 파일업로드 로직 시작 ********/
		//enctype="multipart/form-data" 로 전송되었는지 확인. 
		//아래 두패키지에서 제공함.
		//org.apache.commons.fileupload.servlet.ServletFileUpload
		//org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
		if(!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("enctype=\"multipart/form-data\"를 확인하세요!!");
			return;
		}
		

		//응답메세지 준비
		PlatformData resData = new PlatformData();
		VariableList resVarList = resData.getVariableList();
		DataSet ds = new DataSet("out_ds");
		ds.addColumn(new ColumnHeader("fileName", DataTypes.STRING));
		ds.addColumn(new ColumnHeader("fileSize", DataTypes.STRING));
		ds.addColumn(new ColumnHeader("fileType", DataTypes.STRING));
		
		resVarList.add("ErrorCode", 0);
		resVarList.add("ErrorMsg", "Success");
		
		//a.현재 웹 컨테이너에서 구동중인 웹 어플리케이션의 루트 절대경로 알아내기
		//ServletContext javax.servlet.GenericServlet.getServletContext()
		String saveDirectory = getServletContext().getRealPath("/_resource_/_images_/celeb");
		System.out.println("saveDirectory = " + saveDirectory);
		
		//b.파일최대용량 : cos.jar 무료버젼은 업로드할 파일 10MB까지 사용가능.
		//1KB*1KB*10
		int maxPostSize = 1024 * 1024 * 10;
		
		//c. MultipartRequest 객체 생성함 : 자동 파일 업로드됨
		//업로드 파일최대크기를 초과하면 IOException이 발생된다. 반드시 Exception처리해야 한다.(try~catch)
		/* 
		 com.oreilly.servlet.MultipartRequest.MultipartRequest(HttpServletRequest request, 
															   String saveDirectory, 
															   int maxPostSize, 
															   String encoding, 
															   FileRenamePolicy policy) throws IOException
															
		 */
		MultipartRequest mrequest = new MultipartRequest(request, 
														 saveDirectory, 
														 maxPostSize, 
														 "UTF-8", 
														 new DefaultFileRenamePolicy());
		/******* 파일업로드 로직 끝 ********/
		
		//1.파라미터값 가져오기
		//이제 request가 아닌 MultipartRequest객체에서 폼파라미터를 가져와야함.
		Enumeration files = mrequest.getFileNames();
		
		while (files.hasMoreElements()) {
		  String name = (String)files.nextElement();
		  String fileName = mrequest.getFilesystemName(name);//실제 저장된 파일명
		  String fileType = mrequest.getContentType(name);
		  File f = mrequest.getFile(name);
		  System.out.println("fileName:"+fileName);
		  System.out.println("fileType:"+fileType);
		  System.out.println("fileSize:"+f.length());
		  
		  int row = ds.newRow();
		  ds.set(row, "fileName", fileName);
		  ds.set(row, "fileType", fileType);
		  
		  if (f != null){
		   String size = Long.toString(f.length()/1024)+"KB";
		   ds.set(row, "fileSize", size);
		  }  
		
		}
		
		//dataset 처리
		resData.addDataSet(ds);
		 
		 
		HttpPlatformResponse res = new HttpPlatformResponse(response, request);
		res.setContentType(PlatformType.CONTENT_TYPE_XML);
		res.setCharset("UTF-8");
		res.setData(resData);
		try {
			res.sendData();
		} catch (PlatformException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
