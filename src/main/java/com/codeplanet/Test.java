package com.codeplanet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Test {

  @GetMapping("/signUp")
  public String signUp(HttpServletRequest req) {
	String email = req.getParameter("email");
	String psw = req.getParameter("psw");
	System.out.print("signUpMethod is working" + email + " password is " + psw);
	return "First";
  }
  
  @PostMapping("/signUp")
  public String signUp1(HttpServletRequest req) throws SQLException, ClassNotFoundException {
	String email = req.getParameter("email");
	String psw = req.getParameter("psw");
	Class.forName("com.mysql.jdbc.Driver");
	Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "admin123");
	Statement stmt = con.createStatement();
	String query1 = "Select * from signup where email='"+ email + "'";
	ResultSet rs = stmt.executeQuery(query1);
	if (rs.next()) {
		req.setAttribute("test", "You are already signedup");
	} else {
	  String query2 = "insert into signup(EMAIL,PSW) values('"+email+ "','"+ psw + "')";
	  int row = stmt.executeUpdate(query2);
	  if (row >=1) {
		req.setAttribute("test", "You signedup successfully");
	  }
	}
	
	return "First";
  }

  @GetMapping("/login")
  public String login(HttpServletRequest req) {
    return "login";
  }
  
  @PostMapping("/signin")
  public String signin(HttpServletRequest req) throws SQLException, ClassNotFoundException {
	String email = req.getParameter("email");
	String psw = req.getParameter("psw");
	Class.forName("com.mysql.jdbc.Driver");
	Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "admin123");
	Statement stmt = con.createStatement();
	String query1 = "Select * from signup where email='"+ email + "'";
	ResultSet rs = stmt.executeQuery(query1);
	if (rs.next()) {
	   if(rs.getInt("is_verify") == 0) {
		   req.setAttribute("test", "You are not verified");
			return "First";
	   }
	   if((rs.getString("PSW")).equals(psw)) {
			req.setAttribute("test", "You are Successfully loggedin");
	   } else {
		   req.setAttribute("test", "Your psw is not correct please check");
	   }
	} else {
		req.setAttribute("test", "You are NOT signedup");
	}
	
	return "First";
  }
}
