package ksmart.mybatis.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ksmart.mybatis.common.interceptor.CommonInterceptor;
import ksmart.mybatis.common.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
	
	private final CommonInterceptor commonInterceptor;
	private final LoginInterceptor loginInterceptor;

	/* addInterceptors: 사용자정의된 interceptor 적용시키는 메소드 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		// interceptor 적용시에는 주의할점
		// 주소중 동적인페이지 요청을 제외한 정적리소스는 제외시켜줘야한다.
		registry.addInterceptor(commonInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/favicon.ico");
		
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/favicon.ico")
				.excludePathPatterns("/member/login")
				.excludePathPatterns("/member/addMember")
				.excludePathPatterns("/member/idCheck");
		
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}









