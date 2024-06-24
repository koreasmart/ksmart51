package ksmart.mybatis.subservice.mapper;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.member.dto.Member;

@Mapper
public interface SubServiceMapper {

	Member getMemberInfoById(String memberId);
}
