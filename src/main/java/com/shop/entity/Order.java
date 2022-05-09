package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 회원이 여러개의 주문을 가질 수 있음.

    private LocalDateTime orderDate;    //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;        //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //OrderItem에 있는 Order에 의해 관리, 연관 관계의 주인의 필드인 order를 mappedBy의 값으로 셋팅.
    //cascade = CascadeType.ALL : 부모엔터티의 영속성 상태 변화를 자식 엔티티에 모두 전이.
    //orphanRemoval = true : 고아객체 제거 사용위해 옵션 추가

    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문 상품을 가짐으로 List 자료형 사용.

    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
