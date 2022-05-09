package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  // 상품하나는 여러 주문 상품으로 들어갈 수 있음

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;    //주문 하나에는 여러 상품을 주문 할 수 있음. 주문 상품엔티티와 주문 엔티티를 다대일 매핑을 먼저 설정

    private int orderPrice;

    private int count;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
