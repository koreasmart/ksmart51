package ksmart.mybatis.member.service;

import java.util.List;
import java.util.Map;

import ksmart.mybatis.member.dto.Member;
import ksmart.mybatis.member.dto.MemberLevel;
import ksmart.mybatis.member.dto.Search;

public interface MemberService {
	// 로그인 이력 조회
	Map<String, Object> getLoginHistory(int currentPage);
	
	// 회원 검색 리스트 조회
	List<Member> getSearchList(Search search);
	
	// 회원 탈퇴
	void removeMember(int memberLevel, String memberId);
	
	// 회원 정보 확인
	Map<String, Object> checkMemberInfo(String memberId, String memberPw);
	
	// 회원 정보 수정
	int modifyMember(Member member);
	
	// 회원 정보 조회
	Member getMemberInfoById(String memberId);
	
	// 회원가입
	void addMember(Member member);
	
	// 회원등급 조회
	List<MemberLevel> getMemberLevelList();

	// 회원목록 조회
	List<Member> getMemberList();
}
