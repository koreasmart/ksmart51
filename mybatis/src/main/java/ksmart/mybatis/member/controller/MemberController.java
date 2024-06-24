package ksmart.mybatis.member.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ksmart.mybatis.member.dto.Member;
import ksmart.mybatis.member.dto.MemberLevel;
import ksmart.mybatis.member.dto.Search;
import ksmart.mybatis.member.mapper.MemberMapper;
import ksmart.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
	
	private final MemberService memberService;
	private final MemberMapper memberMapper;
	
	@PostMapping("/login")
	public String login(@RequestParam(value="memberId") String memberId
					   ,@RequestParam(value="memberPw") String memberPw
					   ,RedirectAttributes reAttr
					   ,HttpServletRequest request
					   ,HttpServletResponse response
					   ,HttpSession session) {
		String viewName = "redirect:/";
		
		Map<String, Object> checkMap = memberService.checkMemberInfo(memberId, memberPw);
		boolean isCheck = (boolean) checkMap.get("isCheck");
		if(isCheck) {
			Member memberInfo = (Member) checkMap.get("memberInfo");
			
			// 1. session
			// HttpSession requestGetSession = request.getSession();
			session.setAttribute("SID", memberInfo.getMemberId());
			session.setAttribute("SNAME", memberInfo.getMemberName());
			session.setAttribute("SLEVEL", memberInfo.getMemberLevel());
			
			// 2. cookie
			Cookie cookie = new Cookie("loginId", memberInfo.getMemberId());
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(60*10); //1시간
			
			// 생성된 쿠키를 응답객체에 담아 반환
			response.addCookie(cookie);
			
		}else {
			viewName = "redirect:/member/login";
			reAttr.addAttribute("msg", "회원의 정보가 일치하지 않습니다.");
		}
		
		return viewName;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		
		session.invalidate();
		
		// 2. cookie
		Cookie cookie = new Cookie("loginId", null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0); //1시간
		
		// 생성된 쿠키를 응답객체에 담아 반환
		response.addCookie(cookie);
		
		return "redirect:/member/login";
	}
	
	@GetMapping("/login")
	public String login(Model model, @RequestParam(value="msg", required = false) String msg) {
		model.addAttribute("title", "로그인");
		if(msg != null) model.addAttribute("msg", msg);
		return "admin/member/login";
	}
	
	@GetMapping("/loginHistory")
	public String getLoginHistory(@RequestParam(value="currentPage", required = false, defaultValue = "1") int currentPage
								 ,Model model) {
		Map<String, Object> resultMap = memberService.getLoginHistory(currentPage);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> loginHistory = (List<Map<String, Object>>) resultMap.get("loginHistory");
		int lastPage = (int) resultMap.get("lastPage");
		int startPageNum = (int) resultMap.get("startPageNum");
		int endPageNum = (int) resultMap.get("endPageNum");
		
		model.addAttribute("title", "로그인이력조회");
		model.addAttribute("loginHistory", loginHistory);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		
		return "admin/member/loginHistory";
	}
	
	@PostMapping("/searchList")
	@ResponseBody
	public List<Member> getSearchList(@RequestBody Search search){
		/*
		@RequestParam(value="searchKey") String searchKey
		,@RequestParam(value="searchValue") String searchValue
		log.info("searchKey: {}", searchKey);
		log.info("searchValue : {}", searchValue);
		*/
		log.info("search: {}", search);
		
		return memberService.getSearchList(search);
	}
	
	@GetMapping("/sellerList")
	public String getSellerList(Model model) {
		
		List<Member> sellerList = memberMapper.getSellerList();
		
		log.info("sellerList: {}", sellerList);
		model.addAttribute("title", "판매자 현황조회");
		model.addAttribute("sellerList", sellerList);
		
		return "admin/member/sellerList";
	}
	
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(value="memberId") String memberId
							  ,@RequestParam(value="memberPw") String memberPw
							  ,RedirectAttributes reAttr) {
		String viewPath = "redirect:/member/memberList";
		Map<String, Object> resultMap = memberService.checkMemberInfo(memberId, memberPw);
		boolean isCheck = (boolean) resultMap.get("isCheck");
		// 실습. 회원의 정보가 일치하지 않으면 회원탈퇴 페이지로 이동
		if(isCheck) {
			//탈퇴 프로세스
			Member memberInfo = (Member) resultMap.get("memberInfo");
			int memberLevel = memberInfo.getMemberLevel();
			memberService.removeMember(memberLevel, memberId);
			reAttr.addAttribute("msg", "회원 "+ memberId + "가 탈퇴처리 되었습니다.");
		
		// 회원의 정보가 일치하면 회원목록조회 페이지로 이동
		} else {
			viewPath = "redirect:/member/removeMember";
			reAttr.addAttribute("memberId", memberId);
			reAttr.addAttribute("msg", "회원의 정보가 일치하지 않습니다.");
		}
		
		return viewPath;
	}
	
	@GetMapping("/removeMember")
	public String removeMember(@RequestParam(value="memberId") String memberId
							  ,@RequestParam(value="msg", required = false) String msg
							  ,Model model) {
		log.info("회원탈퇴화면 memberId:{}", memberId);
		
		model.addAttribute("title", "회원탈퇴");
		model.addAttribute("memberId", memberId);
		if(msg != null) model.addAttribute("msg", msg);
		
		return "admin/member/removeMember";
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		log.info("회원수정 Member:{}", member);
		memberService.modifyMember(member);
		return "redirect:/member/memberList";
	}
	
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(value="memberId") String memberId
							  ,Model model) {
		log.info("수정화면 memberId:{}", memberId);
		Member memberInfo = memberService.getMemberInfoById(memberId);
		List<MemberLevel> levelList = memberService.getMemberLevelList();
		
		model.addAttribute("title", "회원수정");
		model.addAttribute("memberInfo", memberInfo);
		model.addAttribute("levelList", levelList);
		
		return "admin/member/modifyMember";
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) {
		
		log.info("회원가입 Member:{}", member);
		
		memberService.addMember(member);
		
		return "redirect:/member/memberList";
	}
	
	@PostMapping("/idCheck")
	@ResponseBody
	public boolean idCheck(@RequestParam(value="memberId") String memberId) {
		log.info("아이디중복체크: memberId {}", memberId);
		boolean isMember = false;
		
		isMember = memberMapper.idCheck(memberId);
		
		return isMember;
	}
	
	@GetMapping("/addMember")
	public String addMember(Model model) {
		
		List<MemberLevel> memberLevelList = memberService.getMemberLevelList();
		
		model.addAttribute("title", "회원가입");
		model.addAttribute("levelList", memberLevelList);
		
		return "admin/member/addMember";
	}

	@GetMapping("/memberList")
	public String getMemberList(Model model
							   ,@RequestParam(value="msg", required = false) String msg) {
		
		// 검색 키워드
		List<Search> searchCate = new ArrayList<Search>();
		Search search1 = new Search();
		search1.setSearchKey("memberId");
		search1.setSearchText("회원아이디");
		Search search2 = new Search();
		search2.setSearchKey("memberName");
		search2.setSearchText("회원이름");
		Search search3 = new Search();
		search3.setSearchKey("memberAddr");
		search3.setSearchText("회원주소");
		searchCate.add(search1);
		searchCate.add(search2);
		searchCate.add(search3);
		
		List<Member> memberList = memberService.getMemberList();
		
		log.info("회원목록조회: {}", memberList);
		
		model.addAttribute("title", "회원목록조회");
		model.addAttribute("memberList", memberList);
		model.addAttribute("searchCate", searchCate);
		if(msg != null) model.addAttribute("msg", msg);
		
		return "admin/member/memberList";
	}
}






