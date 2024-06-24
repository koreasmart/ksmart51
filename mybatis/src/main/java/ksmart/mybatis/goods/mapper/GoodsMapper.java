package ksmart.mybatis.goods.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.goods.dto.Goods;

@Mapper
public interface GoodsMapper {
	
	// 상품등록
	int addGoods(Goods goods);

	// 상품조회
	List<Goods> getGoodsList();
	
	// 판매자가 등록한 상품 삭제
	int removeGoodsBySellerId(String sellerId);

	// 다중 조건 상품 검색
	List<Goods> getSearchList(List<Map<String, Object>> searchList);
}
