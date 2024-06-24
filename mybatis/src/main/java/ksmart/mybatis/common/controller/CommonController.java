package ksmart.mybatis.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ksmart.mybatis.member.dto.Member;
import ksmart.mybatis.subservice.mapper.SubServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommonController {
	
	private final SubServiceMapper mapper;

	@GetMapping("/")
	public String mainPage(Model model) {
		Member member = mapper.getMemberInfoById("id001");
	
		log.info("member: {}", member);
		
		model.addAttribute("title", "메인화면");
		model.addAttribute("content", "Spring boot Mybatis");
		
		return "index";
	}
}
