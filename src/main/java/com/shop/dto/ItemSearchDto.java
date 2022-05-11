package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;
    //현재 시간과 등록일 비교 후 상품 데이터 조회
    /**
     * all: 상품 등록일 전체
     * 1d: 하루
     * 1w: 최근 일주일
     * 1m: 최근 한달
     * 6m: 최근 6개월
     */

    private ItemSellStatus searchSellStatus;
    //상품 판매 상태를 기준으로 데이터 조회

    private String searchBy;
    //상품 조회 시 어떤 유형으로 조회할 지 선택 itemNm : 상품명, createdBy: 상품 등록자 아이디

    private String searchQuery = "";
    //조회할 검색어 저장 변수. searchBy가 itemNm일 경우 상품명 기준 검색, createBy일 경우 상품 등록자 아이디 기준으로 검색
}
