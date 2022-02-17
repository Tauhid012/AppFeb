package com.codeplanet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Test {

  @RequestMapping(value = "/signUp", method = RequestMethod.DELETE)
  @GetMapping("/signUp")
  public String signUp(HttpServletRequest req) {
	String email = req.getParameter("email");
	String psw = req.getParameter("psw");
	System.out.print("signUpMethod is working" + email + " password is " + psw);
	return "First";
  }
  
  @PostMapping("/signUp")
  public String signUp1(HttpServletRequest req) {
	String email = req.getParameter("email");
	String psw = req.getParameter("psw");
	System.out.print("signUpMethod is working" + email + " password is " + psw);
	return "First";
  }

}
