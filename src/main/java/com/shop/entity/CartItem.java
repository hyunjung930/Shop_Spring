package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;  //하나의 장바구니에는 여러개의 상품을 담을 수 있음으로

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  // 장바구니에 담을 상품의 정보를 알기 위한 상품 엔티티 맵핑. 하나의 상품은 여러 장바구니 상품으로 담길 수 있음으로 다대일 관계 맵핑

    private int count;  //같은 상품을 몇개 담을지
}
