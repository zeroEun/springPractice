package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@SessionAttributes("loginUser") //Model에 loginUser 라는 키 값으로 객체가 추가되면 자동으로 세션에 추가해 주는 어노테이션
@Controller
public class MemberController {
	
	/* 에러처리
	 * error-page
	 * exception-type
	 * ExceptionHandler
	 * ControllerAdvice + ExceptionHandler
	 * */
	
	/* 컨트롤러에 작성 시 해당 컨트롤러에서만 에러처리 됨(비효율)
	 * BadSqlGrammarException에러 발생 시 해당 에러 페이지로
	@ExceptionHandler(value = BadSqlGrammarException.class)
	public ModelAndView controllerExceptionHandler(Exception e) {
		
		e.printStackTrace();
		return new ModelAndView("common/errorPageServer").addObject("msg", e.getMessage());
	}
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView controllerExceptionHandler(Exception e) {
		
		e.printStackTrace();
		return new ModelAndView("common/errorPage").addObject("msg", e.getMessage());
	}
	*/
	

	@Autowired //빈스캐닝을 통해 인터페이스를 구현한 클래스(구현체) 중에 @Service로 등록되어 있는 빈을 찾아서 자동으로 주입해준다. new MemberServiceImpl();와 같은 역할
	private MemberService memberService;
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//파라미터를 전송 받는 방법
	/*1. HttpServletRequest 를 통해 전송 받기(기존 jsp/servlet 방식) 
	
	
	@RequestMapping(value="login.me", method=RequestMethod.POST) //@RequestMapping를 붙여줌으로써 handlerMapping에 등록
	public String loginMember(HttpServletRequest request) {
			
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		System.out.println("ID : " + userId);
		System.out.println("userPwd : " + userPwd);
		
		return "main"; //리턴되는 문자열을 servlet-context의 viewresolver에 의해 사용자가 보게될 뷰로 포워딩
	}
	
	*/
	
	/*2. @RequestParam 어노테이션 방식 - 스프링에서 제공하는 파라미터를 받아오는 방식
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String loginMember(@RequestParam("userId") String userId,
							  @RequestParam("userPwd") String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("userPwd : " + userPwd);
		
		return "main"; //리턴되는 문자열을 servlet-context의 viewresolver에 의해 사용자가 보게될 뷰로 포워딩
	}
	*/
	
	/*3. @RequestParam 어노테이션 생략 - 매개변수 name을 동일하게 작성해야 자동으로 값이 주입된다
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String loginMember(String userId, String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("userPwd : " + userPwd);
		
		return "main";
	}
	*/
	
	/*4. @ModelAttribute 이용한 방법 - 요청 파라미터가 많은 경우 객체 타입으로 넘겨 받는데 기본 생성자와 setter를 이용한 주입방식이므로 둘 중 하나라도 없으면 에러
	 *                           - 반드시 name 속성에 있는 값과 필드 명이 동일해야하고 setter 작성하는 규칙에 맞게 작성되어야 한다
	 
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String loginMember(@ModelAttribute Member m) {
		
		System.out.println("ID : " + m.getUserId());
		System.out.println("userPwd : " + m.getUserPwd());
		
		return "main";
	}
	*/
	
	/*5. @ModelAttribute를 생략하고 객체를 바로 전달 받는 방식
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String loginMember(Member m, HttpSession session) {
		
		//System.out.println("ID : " + m.getUserId());
		//System.out.println("userPwd : " + m.getUserPwd());
		
		//Memberservice memberService = new MemberServiceImpl();
		
		Member loginUser;
		try {
			
			loginUser = memberService.loginMember(m);
			System.out.println(loginUser);
			
			session.setAttribute("loginUser", loginUser);
			return "redirect:/"; // '/' -> index.jsp 경로로, redirect:없으면 뷰리졸버에 설정한 경로대로
			
		} catch (Exception e) {
			e.printStackTrace();
			return "common/errorPage";
		}
		
		
	}
	
	*/
	
	
	/*응답페이지에 응답할 데이터가 있는 경우*/
	/*1.Model 이라는 객체를 사용하는 방법 - 응댑뷰로 전달하고자 하는 Map(키, 벨류 ) 형식으로 담을 수 있다
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String loginMember(Member m, HttpSession session, Model model) {
	
		Member loginUser;
		try {
			
			loginUser = memberService.loginMember(m);
			System.out.println(loginUser);
			
			session.setAttribute("loginUser", loginUser);
			return "redirect:/"; // '/' -> index.jsp 경로로, redirect:없으면 뷰리졸버에 설정한 경로대로
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "로그인 실패");
			
			return "common/errorPage";
		}
		
	}
	
	*/
	
	/*2. ModelAndView 라는 객체를 사용하는 방법 - 값과 뷰를 모두 지정
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public ModelAndView loginMember(Member m, HttpSession session, ModelAndView mv) {
	
		Member loginUser;
		try {
			
			loginUser = memberService.loginMember(m);
			System.out.println(loginUser);
			
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/");
			
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msg", "로그인 실패"); //키 벨류 형태로 값 담고 ${키값}으로 뽑음
			mv.setViewName("common/errorPage");
		}
		
		return mv;
		
	}
	
	
	
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		session.invalidate();
		return "redirect:/";
		
	}
	*/
	
	
	/*3. @SessionAttributes 어노테이션 사용하기
	@RequestMapping("login.me")
	public String loginMember(Member m, Model model) {
	
		Member loginUser;
		try {
			
			loginUser = memberService.loginMember(m);
			System.out.println(loginUser);
			
			model.addAttribute("loginUser", loginUser); //model의 scope = request
			return "redirect:/"; // '/' -> index.jsp 경로로, redirect:없으면 뷰리졸버에 설정한 경로대로
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "로그인 실패");
			
			return "common/errorPage";
		}
		
	} */
	
	//로그아웃 변경(@SessionAttributes)
	@RequestMapping("logout.me")
	public String logoutMember(SessionStatus status) {
		status.setComplete(); //현재 컨트롤러에 @SessionAttributes에 의해 저장된 오브젝트 제거
		return "redirect:/";
		
	}
	
	@RequestMapping("enrollForm.me")
	public String enrollForm() {
		return "member/memberEnrollForm";
	}
	
	@RequestMapping("insert.me")
	public String insertMember(@ModelAttribute Member m, @RequestParam("post") String post,
													     @RequestParam("address1") String address1,
													     @RequestParam("address2") String address2, HttpSession session) {
		
		m.setAddress(post +"/" + address1 + "/" + address2);
		
		System.out.println("암호화 전  : " + m.getUserPwd());
		
		String encPwd = bCryptPasswordEncoder.encode(m.getUserPwd());
		
		System.out.println("암호화 후  : " + encPwd);
		
		m.setUserPwd(encPwd);
		memberService.insertMember(m);
		
		session.setAttribute("msg", "회원가입 성공");
		return "redirect:/";
	}
	
	
	@RequestMapping("login.me")
	public String loginMember(Member m, Model model) {
	
		Member loginUser;
		
		loginUser = memberService.loginMember(bCryptPasswordEncoder, m);
		System.out.println(loginUser);

		model.addAttribute("loginUser", loginUser); // model의 scope = request
		return "redirect:/"; // '/' -> index.jsp 경로로, redirect:없으면 뷰리졸버에 설정한 경로대로
		
	}
	
	@RequestMapping("myPage.me")
	public String myPage() {
		
		return "member/myPage";
	}
	
	@RequestMapping("update.me")
	public String updateMember(@ModelAttribute Member m, @RequestParam("post") String post,
		     				   @RequestParam("address1") String address1,
		     				   @RequestParam("address2") String address2, 
		     				   HttpSession session, Model model) throws Exception {
		
		m.setAddress(post +"/" + address1 + "/" + address2);
		
		Member userInfo = memberService.updateMember(m);
		model.addAttribute("loginUser", userInfo);
		
		
		
		return "member/myPage";
	}
	
	/*
	@RequestMapping("delete.me")
	public String deleteMember(@ModelAttribute Member m, HttpSession session, SessionStatus status) {
		
		memberService.deleteMember(m);
		
		status.setComplete();
		session.setAttribute("msg", "회원 탈퇴 완료");
		
		return "redirect:/";
	}*/
	
	
	@RequestMapping("delete.me")
	public String deleteMember(String userId) {
		
		memberService.deleteMember(userId);
		return "redirect:logout.me";
		
	}
	
	@RequestMapping("updatePwdForm.me")
	public String updatePwdFrom() {
		
		return "member/pwdUpdateForm";
		
	}
	
	@RequestMapping("updatePwd.me")
	public String updatePwd(@RequestParam("userPwd") String userPwd,
			   				@RequestParam("newPwd") String newPwd,
			   				HttpSession session, Model model) {
		
		
		Member m = (Member) session.getAttribute("loginUser");
		
		String encPwd = bCryptPasswordEncoder.encode(newPwd);
		
		Member updateMem = memberService.updatePwd(bCryptPasswordEncoder, m, userPwd, encPwd);
		
		
		if(updateMem != null) {
			session.setAttribute("sTag", "Y");
			session.setAttribute("msg", "성공적으로 비밀번호를 변경하였습니다.");
			model.addAttribute("loginUser", updateMem);
		}else {
			session.setAttribute("msg", "비밀번호 변경에 실패했습니다.");
		}
		
		return "member/pwdUpdateForm";
		
	}
	
	

	
	
}
