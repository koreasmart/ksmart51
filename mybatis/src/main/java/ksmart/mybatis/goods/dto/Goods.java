package ksmart.mybatis.goods.dto;

import ksmart.mybatis.member.dto.Member;
import lombok.Data;

@Data
public class Goods {	
	private String goodsCode;
	private String goodsName;
	private int goodsPrice;
	private String goodsSellerId;
	private String goodsRegDate;
	
	// 판매자 정보 (1:1관계, 상품과 회원)
	private Member sellerInfo;
}
