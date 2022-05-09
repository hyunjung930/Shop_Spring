package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   //회원당 하나의 장바구니만 갖을 수 있어 OneToOne 맵핑
    @JoinColumn(name = "member_id")     //맵핑할 외래키 지정. name 속성에서 맵핑할 외래키의 이름을 설정한다.
    private Member member;

}
