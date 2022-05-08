package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // Entity 데이터베이스에 저장할 수 있게 MemberRepository
    Member findByEmail(String email); // 중복회원이 있는지 검사하기 위한 쿼리 메소드 작성
}
