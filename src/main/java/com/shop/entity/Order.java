package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity{

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

    /**
     * 생성한 주문 상품 객체를 이용해 주문 객체를 만드는 메소드 작성
     */
    public void addOrderItem(OrderItem orderItem){  // 주문 상품 정보 담음

        orderItems.add(orderItem);
        orderItem.setOrder(this);   //Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계로, orderItem 객체에도 order객체를 셋팅.

    }
    public static Order createOrder(Member member, List<OrderItem> orderItemList){

        Order order = new Order();
        order.setMember(member);    //상품을 주문한 회원의 정보 셋팅.
        for(OrderItem orderItem : orderItemList){   // 상품페이지 - 1개 주문 but 장바구니 페이지 - 여러개의 상품 준비. --> List형태로 파라미터 값을 받음.
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice(){ // 총 주문 금액 구하는 메소드
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
