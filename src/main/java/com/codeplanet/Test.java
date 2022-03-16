package com.codeplanet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.SplittableRandom;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	String query1 = "Select * from signup where email=?";
	PreparedStatement stmt = con.prepareStatement(query1);
	stmt.setString(1,email);
	ResultSet rs = stmt.executeQuery();
	if (rs.next()) {
		req.setAttribute("test", "You are already signedup");
	} else {
	  String otp= "";
	  otp = generateOtp(6);
	  System.out.println("your otp is " + otp);
	  String query2 = "insert into signup(EMAIL,PSW, otp) values(?,?,?)";
	  PreparedStatement stmt1 = con.prepareStatement(query2);
	  stmt1.setString(1,email);
	  stmt1.setString(2, psw);
	  stmt1.setString(3, otp);
	  int row = stmt1.executeUpdate();
	  if (row >=1) {
		sendMail(email, "Youyr Otp for our Portal is " + otp, "OTP for Verification");
		req.setAttribute("email", email);
	  }
	}
	
	return "SignupSuccess";
  }

  private static void sendMail(String emailTo, String body, String subject) {
    Properties p = new Properties();
	p.put("mail.smtp.host", "smtp.gmail.com");
	p.put("mail.smtp.port", "465");
	p.put("mail.smtp.ssl.enable", "true");
	p.put("mail.smtp.auth", "true");
	
	
	MailAuthenticator m = new MailAuthenticator("testcodeplanet@gmail.com", "9806584240");
	
	Session session = Session.getInstance(p, m);
	session.setDebug(true);
	
	MimeMessage msg = new MimeMessage(session);
	
	try {
		msg.setFrom("testcodeplanet@gmail.com");
		
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
		
		msg.setSubject(subject);
		
		msg.setText(body);
		
		Transport.send(msg);
		
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	
	
	
	
	
  }

public String generateOtp(int size) {
	StringBuilder sb = new StringBuilder();
	SplittableRandom sp = new SplittableRandom();
	 for (int i =0 ; i<size; i++) {
		 int rn = sp.nextInt(0,9);
		 sb.append(rn);
	 }
	return sb.toString();
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

@PostMapping("/otpVerification")
public String otpVerification(HttpServletRequest req) throws SQLException, ClassNotFoundException {
	
		String email = req.getParameter("email");
		String otp = req.getParameter("otp");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "admin123");
		Statement stmt = con.createStatement();
		String query1 = "Select otp from signup where email='"+ email + "'";
		ResultSet rs = stmt.executeQuery(query1);
		if(rs.next()) {
			if(rs.getString("otp").equals(otp)) {
				Statement stmt1 = con.createStatement();
				String query2 = "update signup set is_verify=1 where email='"+ email + "'";
				stmt1.executeUpdate(query2);
				req.setAttribute("test", "Your account is verified now");
			} else {
				req.setAttribute("test", "Your otp is not valid");
			}
		} else {
			req.setAttribute("test", "Your otp is not generated");
			
		}
		return "First";
		}
}
