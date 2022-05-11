package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>,ItemRepositoryCustom {   //QueryDslPredicateExecutor 조건 판단 근거를 함수로 제공하는 인터페이스

    List<Item> findByItemNm(String itemNm); //findByItemNm: 상품의 이름을 이용한 데이터 조회 메소드

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail); //findByItemNmOrItemDetail: 상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리메소드

    List<Item> findByPriceLessThan(Integer price); //findByPriceLessThan : price 변수 보다 작은 상품 데이터 조회

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); // price변수보다 작은 상품 데이터 조회시 OrderBy를 사용해 내림차순으로 조회

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")  //@Query 문 사용하여 ItemDetail 조회, Like % % 사용.
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); //@Param을 이용해 명시적 높임.

    @Query(value = "select * from Item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)  //nativeQuery를 이용해 기존 쿼리 그대로 사용
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
