package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})    //Auditing 적용을 위해 @EntityListeners 추가
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{

    @CreatedDate    //엔티티가 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy   //변경 시간을 자동으로 저장.
    private String modifiedBy;

}
