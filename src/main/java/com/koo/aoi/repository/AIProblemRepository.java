package com.koo.aoi.repository;

import com.koo.aoi.domain.AIProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIProblemRepository extends JpaRepository<AIProblem, Long> {
}
