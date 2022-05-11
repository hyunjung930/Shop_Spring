package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 상품하나는 여러 주문 상품으로 들어갈 수 있음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;    //주문 하나에는 여러 상품을 주문 할 수 있음. 주문 상품엔티티와 주문 엔티티를 다대일 매핑을 먼저 설정

    private int orderPrice;

    private int count;

    /**
     * 주문할 상품과 주문 수량을 통해 OrderItem 객체를 만드는 메소드를 작성.
     */
    public static OrderItem createOrderItem(Item item, int count){

        OrderItem orderItem = new OrderItem();  // 주문할 상품과 수량 셋팅.
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);    //주문 수량 만큼 상품의 재고 수량을 감소
        return orderItem;

    }
    public int getTotalPrice(){
        //주문 가격과 주문 수량을 곱해 해당 상품을 주문한 총 가격을 계산.
        return orderPrice*count;
    }
}
