package com.uniquedu.json;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public UploadFile() {
		// TODO Auto-generated constructor stub
	}

	private String filePath; // 存放上传文件的目录
	private String tempFilePath; // 存放临时文件的目录

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		filePath = config.getInitParameter("filePath");
		tempFilePath = config.getInitParameter("tempFilePath");
		filePath = getServletContext().getRealPath(filePath);
		tempFilePath = getServletContext().getRealPath(tempFilePath);
		filePath = config.getServletContext().getRealPath("/") + "\\android\\";
		tempFilePath = "c://android";
		System.out.println(filePath + "         " + tempFilePath);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("接收到数据");
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		// 处理汉字
		request.setCharacterEncoding("UTF-8");
		// 向客户端发送响应正文
		PrintWriter outNet = response.getWriter();
		try {
			// 创建一个基于硬盘的FileItem工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置向硬盘写数据时所用的缓冲区的大小，此处为4K
			factory.setSizeThreshold(4 * 1024);
			// 设置临时目录
			factory.setRepository(new File(tempFilePath));

			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置允许上传的文件的最大尺寸，此处为4M
			upload.setSizeMax(4 * 1024 * 1024);

			List /* FileItem */ items = upload.parseRequest(new ServletRequestContext(request));
			System.out.println("得到文件的数目" + items.size());
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					processFormField(item, outNet); // 处理普通的表单域
				} else {
					processUploadedFile(item, outNet); // 处理上传文件
				}
			}
			outNet.close();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void processFormField(FileItem item, PrintWriter outNet) throws UnsupportedEncodingException {
		String name = item.getFieldName();
		String value = item.getString("UTF-8");
		outNet.println(name + ":" + value + "\r\n");
	}

	private void processUploadedFile(FileItem item, PrintWriter outNet) throws Exception {
		String filename = item.getName();
		int index = filename.lastIndexOf("\\");
		filename = filename.substring(index + 1, filename.length());
		long fileSize = item.getSize();

		if (filename.equals("") && fileSize == 0)
			return;
		File isCreat = new File(filePath);
		if (!isCreat.exists()) {// 判断存放上传目录是否存在
			isCreat.mkdirs();
		}
		File uploadedFile = new File(filePath + "/" + filename);
		item.write(uploadedFile);
		outNet.println(filename + " is saved.");

		outNet.println("The size of \\android\\" + filename + " is " + fileSize + "\r\n");
	}

	// doGet
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		doPost(req, res);
	}

}
