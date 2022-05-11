package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

//ItemRepositoryCustom 인터페이스 구현하는 클래스 작성.
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{  //ItemRepositoryCustom 상속

    private JPAQueryFactory queryFactory;   //동적 쿼리 작성을 위해 JPAQueryFactory 사용.

    public ItemRepositoryCustomImpl(EntityManager em){  //JPAQueryFactory의 생성자로 EntityManager 객체 사용
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        //상품 판매 조건이 Null이면 null 리턴. 상품 판매 조건이 판매중 or 품절 상태라면 해당 조건의 상품만 조회
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        //searchDateType의 값에 따라 dateTime의 값을 이전 시간의 값으로 셋팅 후 해당 시간 이후로 등록된 상품만 조회

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        //searchBy의 값에 따라 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어 포함하고 있는 상품을 조회하도록 조건값을 반환.
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        //queryFactory를 이용해 쿼리 생성.
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item) //상품 데이터를 조회하기 위해 Qitem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())   //데이터를 가지고 올 인덱스 지정
                .limit(pageable.getPageSize())  //한번에 가져올 최대 개수
                .fetchResults();    //조회한 리스트 및 전체 개수 반환

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);    //조회한 데이터를 Page클래스의 구현체인 PageImpl 객체로 반환
    }
}
