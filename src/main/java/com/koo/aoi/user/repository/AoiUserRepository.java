package com.koo.aoi.user.repository;

import com.koo.aoi.user.domain.AoiUser;
import com.koo.aoi.user.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AoiUserRepository extends JpaRepository<AoiUser, Long> {
    // Provider는 달라도 이메일은 서로 같을 수 있으므로 이미 생성된 사용자인지 아닌 지는 이 메소드를 통해 판단해야 함
    Optional<AoiUser> findByEmailAndProvider(String email, Provider provider);
}
