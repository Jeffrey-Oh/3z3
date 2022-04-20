package com.threedotthree.domain.model.scrapCalc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapCalcJpaRepository extends JpaRepository<ScrapCalc, Integer> {
    
    ScrapCalc save(ScrapCalc scrapCalc);

    Optional<ScrapCalc> findByUserUserSeqId(int userSeqId);

}
