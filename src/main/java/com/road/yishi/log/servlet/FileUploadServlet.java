package com.road.yishi.log.servlet;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import com.road.yishi.log.Log;
import com.road.yishi.log.handler.servlet.AbstractServlet;

@WebServlet(name="fileUpload",asyncSupported=true,urlPatterns={"/fileUpload.do"})
public class FileUploadServlet extends AbstractServlet {

	/**
	 * <pre>
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		String filePath = "D:/upload/";
		File uploadFile = new File(filePath);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}

		System.out.println("Come on, baby .......");

		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			Log.error("", e1);
		}
		response.setCharacterEncoding("utf-8");

		// 检测是不是存在上传文件
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// 指定在内存中缓存数据大小,单位为byte,这里设为1Mb
			factory.setSizeThreshold(1024 * 1024);

			// 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
			factory.setRepository(new File(filePath));
			factory.setFileCleaningTracker(new FileCleaningTracker());
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 指定单个上传文件的最大尺寸,单位:字节，这里设为50Mb
			upload.setFileSizeMax(100 * 1024 * 1024);

			// 指定一次上传多个文件的总尺寸,单位:字节，这里设为50Mb
			upload.setSizeMax(100 * 1024 * 1024);
			upload.setHeaderEncoding("UTF-8");

			List<FileItem> items = null;

			try {
				// 解析request请求
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

			if (items != null) {
				// 解析表单项目
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();
					// 如果是普通表单属性
					if (!item.isFormField()) {
						// 属性名
						String fieldName = item.getFieldName();
						System.out.println("fieldName：" + fieldName);
						// 上传文件路径
						String fileName = item.getName();
						// 上传文件路径
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1);// 获得上传文件的文件名

						try {
							item.write(new File(filePath, fileName));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				List<String> tmpFileName = factory.getFileCleaningTracker().getDeleteFailures();
				for(String fn:tmpFileName){
					System.out.println("tmp name:"+fn);
				}
			}
		}
		response.addHeader("token", "hello");
	}

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}
}
