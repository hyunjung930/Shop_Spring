package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable =false, length = 50)
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    //상품을 업데이트 하는 로직
    //엔티티 클래스에 비즈니스 로직 추가로 객체지향적으로 코딩 및 코드 재활용, 한군데에서 관리 가능
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm =itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    //상품 주문시 상품의 재고를 감소시키는 로직 작성
    public void removeStock(int stockNumber){

        int restStock = this.stockNumber - stockNumber; //restStock : 재고 수량에서 주문 후 남은 수량
        if(restStock<0){

            throw new OutOfStockException("상품의 재고가 부족 합니다.(현재 재고수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;   //주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당
    }

}