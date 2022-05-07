package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.shop.entity.QItem.item;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //IoC 컨테이너 등록하여 Test 진행
@TestPropertySource(locations = "classpath:application-test.properties") //Test properties에 우선순위를 부여하여 테스트 코드 진행시 H2 데이터베이스를 사용.
class ItemRepositoryTest {

    @Autowired  //Bean 주입
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;   // 영속성 컨텍스트 사용 및 Entity 빈 주입

    public void createItemList2(){
        for(int i=1;i<=5;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));
        System.out.println(ItemSellStatus.SELL);
        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);   //page 처리 첫번째 인자: 조회할 페이지 번호, 두번째 인자: 한페이지 당 조회할 데이터의 개수
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements ());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }

    //@Test
    @DisplayName("Querydsl 조회 테스트1")
    public void QueryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = item;   //QItem 클래스 사용 객체 이용
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)   //SQL 문과 비슷하게 소스 작성가능
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();    //쿼리 결과를 리스트로 반환

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //@Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    //@Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetail(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }
    //@Test
    @DisplayName("가격 내림차순 조회")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }

    }
   // @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThan(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //@Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetail(){
        this.createItemList();  //이전의 상품 만드는 메소드를 실행하여 조회 대상 생성
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "상품 상세 설명 5"); //테스트 상품1이거나 상품상세설명5이면 itemList에 할당.
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    public void createItemList(){    //상품 조회할 리스트를 반복문 for문을 통해 상품10개를 만든다.
        for(int i=1; i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100); item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);

        }
    }
    //@Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //@Test
    @DisplayName("상품 저장 테스트")   //테스트 코드 실행시 지정한 테스트 코드명 노출
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }


}