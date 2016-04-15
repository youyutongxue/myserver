package com.uniquedu.json;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.uniquedu.json.bean.BackResult;
import com.uniquedu.json.bean.Student;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StudentServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		Connection connect = ConnectionFactory.newInstance().createConnection();
		PrintWriter out = response.getWriter();
		if (action.equals("add")) {
			addStudent(request, connect, out);
		} else if (action.equals("findlist")) {
			findList(request, connect, out);
		} else if (action.equals("find")) {
			findStudent(request, connect, out);
		}
	}

	private void findStudent(HttpServletRequest request, Connection connect, PrintWriter out) {
		try {
			PreparedStatement state = connect.prepareStatement("select * from student where id=?");
			String id = request.getParameter("id");
			state.setString(1, id);
			ResultSet set = state.executeQuery();
			if (set.next()) {
				Student student = new Student(set.getString("id"), set.getString("name"), set.getString("age"),
						set.getString("sex"));
				out.append(JSON.toJSONString(new BackResult(0, "查询数据成功", student)));
			} else {
				out.append(JSON.toJSONString(new BackResult(-1, "无此id学生", null)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.append(JSON.toJSONString(new BackResult(-1, "查询数据失败", null)));
		}
	}

	private void findList(HttpServletRequest request, Connection connect, PrintWriter out) {
		// 查询学生信息
		try {
			String rows = request.getParameter("row");
			String pages = request.getParameter("page");
			int row = 10;
			int page = 0;
			if (rows != null && !"".equals(rows)) {
				row = Integer.parseInt(rows);
			}
			if (pages != null && "".equals(pages)) {
				page = Integer.parseInt(pages);
			}
			PreparedStatement state = connect.prepareStatement("select * from student  order by id desc limit ?,? ");
			state.setInt(1, page * row);
			state.setInt(2, row);
			ResultSet set = state.executeQuery();
			List<Student> students = new ArrayList<>();
			while (set.next()) {
				Student student = new Student(set.getString("id"), set.getString("name"), set.getString("age"),
						set.getString("sex"));
				students.add(student);
			}
			out.append(JSON.toJSONString(new BackResult(0, "查询数据成功", students)));
		} catch (SQLException e) {
			e.printStackTrace();
			out.append(JSON.toJSONString(new BackResult(-1, "查询数据失败", null)));
		}
	}

	private void addStudent(HttpServletRequest request, Connection connect, PrintWriter out) {
		// 添加学生信息
		try {
			System.out.println(request.getParameter("sex"));
			PreparedStatement state = connect.prepareStatement("insert into student (name ,age,sex)values(?,?,?)");
			String name = request.getParameter("name");
			String sex = request.getParameter("sex");
			String age = request.getParameter("age");
			state.setString(1, name);
			state.setString(2, age);
			state.setString(3, sex);
			state.execute();
			ResultSet set = connect.prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
			if (set.next()) {
				String id = set.getString("LAST_INSERT_ID()");
				Student student = new Student(id, name, age, sex);
				out.append(JSON.toJSONString(new BackResult(0, "添加数据成功", student)));
			} else {
				out.append(JSON.toJSONString(new BackResult(-1, "添加数据失败", null)));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.append(JSON.toJSONString(new BackResult(-1, "添加数据失败", null)));
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
