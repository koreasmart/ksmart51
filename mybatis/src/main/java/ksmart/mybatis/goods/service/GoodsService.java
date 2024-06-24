package ksmart.mybatis.goods.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart.mybatis.goods.dto.Goods;
import ksmart.mybatis.goods.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GoodsService {
	
	private final GoodsMapper goodsMapper;
	
	public void addGoods(Goods goods) {
		log.info("상품등록 전 goods:{}", goods);
		goodsMapper.addGoods(goods);
		//String goodsCode = goods.getGoodsCode();
		log.info("상품등록 후 goods:{}", goods);
	}
	
	public List<Goods> getGoodsList(){
		return goodsMapper.getGoodsList();
	}

	public List<Goods> getSearchList(List<Map<String, Object>> searchList) {
		searchList.forEach( search -> {
			String searchKey = (String) search.get("searchKey");
			switch(searchKey) {
				case "memberId" -> searchKey = "m.m_id";
				case "memberName" -> searchKey = "m.m_name";
				case "goodsName" -> searchKey = "g.g_name";
				case "goodsPrice" -> searchKey = "g.g_price";
			}
			search.replace("searchKey", searchKey);
		});
		return goodsMapper.getSearchList(searchList);
	}

}












