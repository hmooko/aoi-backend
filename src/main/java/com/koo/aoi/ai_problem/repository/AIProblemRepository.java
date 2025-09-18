package com.koo.aoi.ai_problem.repository;

import com.koo.aoi.ai_problem.domain.AIProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIProblemRepository extends JpaRepository<AIProblem, Long> {
}
