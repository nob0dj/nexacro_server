<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>
<%@ page import="com.nexacro17.xapi.data.*"%>
<%@ page import="com.nexacro17.xapi.tx.*"%>

<%
	//http://www.tutorialspoint.com/jsp/jsp_file_uploading.htm
	//https://www.chromestatus.com/feature/5629709824032768
	response.setHeader("Access-Control-Allow-Headers",
			"accept, cache-control, content-type, expires, if-modified-since, pragma, x-requested-with");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS, HEAD");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Credentials", "true");

	//응답메세지 준비
	PlatformData resData = new PlatformData();
	VariableList resVarList = resData.getVariableList();
	DataSet ds = new DataSet("Dataset00");
	ds.addColumn(new ColumnHeader("fieldName", DataTypes.STRING));
	ds.addColumn(new ColumnHeader("fileName", DataTypes.STRING));
	ds.addColumn(new ColumnHeader("fileSize", DataTypes.STRING));
	ds.addColumn(new ColumnHeader("fileType", DataTypes.STRING));
	
	resVarList.add("ErrorCode", 0);
	resVarList.add("ErrorMsg", "Success");

	File file;
	int maxFileSize = 1024 * 1024 * 10;
	int maxMemSize = 1024 * 1024 * 5;
	ServletContext context = pageContext.getServletContext();
	String saveDirectory = context.getRealPath("/upload");
	//System.out.println("saveDirectory = " + saveDirectory);

	// Verify the content type :  multipart/form-data
	String contentType = request.getContentType();
	System.out.println("contentType = " + contentType);

	if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);

		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("c:\\temp"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List<FileItem> fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator<FileItem> i = fileItems.iterator();

			while (i.hasNext()) {
			while (i.hasNext()) {

				FileItem fi = i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();
					String fileType = fi.getContentType();

					System.out.println("fieldName = " + fieldName);	//name값
					System.out.println("fileName = " + fileName);	//업로드한 파일명
					System.out.println("isInMemory = " + isInMemory);	//업로드한 파일명
					System.out.println("sizeInBytes = " + sizeInBytes);	//업로드한 파일명

					// Write the file
					if (fileName.lastIndexOf("\\") >= 0) {
						file = new File(saveDirectory, fileName.substring(fileName.lastIndexOf("\\")));
					} else {
						file = new File(saveDirectory, fileName);
					}
					//System.out.println("file = " + file);

					fi.write(file);
					
				  	int row = ds.newRow();
				  	ds.set(row, "fieldName", fieldName);
				  	ds.set(row, "fileName", fileName);
				  	ds.set(row, "fileSize", sizeInBytes);
				  	ds.set(row, "fileType", fileType);
				}

			}

		} catch (Exception ex) {

			resVarList.add("ErrorCode", -1);
			resVarList.add("ErrorMsg", ex.getMessage());

		}
	}
	
	//dataset 처리
	resData.addDataSet(ds);
	 
	 
	HttpPlatformResponse res = new HttpPlatformResponse(response, request);
	res.setContentType(PlatformType.CONTENT_TYPE_XML);
	res.setCharset("UTF-8");
	res.setData(resData);
	res.sendData();
%>