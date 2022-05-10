package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "item_img")
public class ItemImg extends BaseEntity{

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    private String imgName; // 이미지 파일명
    private String oriImgName;  //원본 이미지 파일명
    private String imgUrl;  //이미지 조회 경로
    private String repimgYn;    // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)  /// 상품엔티티와 다대일 단방향 관계로 맵핑. 지연로딩 설정으로 상품엔티티 정보필요시 데이터 조회
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String imgName, String oriImgName, String imgUrl) {   //이미지 정보를 업데이트하는 메소드
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
