package ksmart.mybatis.order.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
	// 구매자가 구매한 이력 삭제
	int removeOrderByOrderId(String orderId);

	// 판매자가 등록 상품을 구매한 이력 삭제
	int removeOrderBySellerId(String sellerId);
}
