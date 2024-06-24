package ksmart.mybatis.member.dto;

import java.util.List;

import ksmart.mybatis.goods.dto.Goods;
import lombok.Data;

@Data // @Data = @Setter + @Getter + @ToString
public class Member {
	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberLevel;
	private String memberLevelName;
	private String memberEmail;
	private String memberAddr;
	private String memberRegDate;
	
	// 판매자 상품 리스트
	private List<Goods> goodsList;
}
