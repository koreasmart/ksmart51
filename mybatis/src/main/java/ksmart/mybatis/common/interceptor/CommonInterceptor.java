package ksmart.mybatis.common.interceptor;

import java.util.Set;
import java.util.StringJoiner;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonInterceptor implements HandlerInterceptor{

	/* preHandle: controller 특정 핸들러 메소드 요청전에 수행하는 메소드*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 사용자가 요청하는 파라미터 정리(memberId=id001&memberPw=pw001)
		// log => memberId: id001, memberPw: pw001
		Set<String> keySet = request.getParameterMap().keySet();
		
		StringJoiner param = new StringJoiner(", ");
		
		for(String key : keySet) {
			param.add(key + ": " + request.getParameter(key));
		}
		
		log.info("ACCESS USER INFO ==============================START");
		log.info("PORT 		::::	{}", request.getLocalPort());
		log.info("ServerName 	::::	{}", request.getServerName());
		log.info("HTTP Method 	::::	{}", request.getMethod());
		log.info("URI 		::::	{}", request.getRequestURI());
		log.info("Client IP 	::::	{}", request.getRemoteAddr());
		log.info("Parameter 	::::	{}", param);
		log.info("===============================================END");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	/* postHandle: controller 특정 핸들러 메소드 수행후에 실행하는 메소드*/
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	/* afterCompletion: 뷰가 렌더링 된 후에 실행되는 메소드 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
