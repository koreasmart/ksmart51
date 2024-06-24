package ksmart.mybatis.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ksmart.mybatis.member.dto.Member;
import ksmart.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor{
	
	private final MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		
		String sessionId = (String) session.getAttribute("SID");
		
		if(sessionId == null) {
			
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					String key =  cookie.getName();
					String value = cookie.getValue();
					if("loginId".equals(key) && value != null) {
						Member memberInfo = memberService.getMemberInfoById(value);
						session.setAttribute("SID", memberInfo.getMemberId());
						session.setAttribute("SNAME", memberInfo.getMemberName());
						session.setAttribute("SLEVEL", memberInfo.getMemberLevel());
						return true;
					}
				}
			}
			
			response.sendRedirect("/member/login");
			
			return false;
		}
		
		
		return true;
	}
}





