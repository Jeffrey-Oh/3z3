package com.threedotthree.domain.model.scrapCalc;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapCalcJpaRepository extends JpaRepository<ScrapCalc, Integer> {
    
    ScrapCalc save(ScrapCalc scrapCalc);

}
