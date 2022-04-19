package com.threedotthree.domain.model.scrapCalc;

import com.threedotthree.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "scrapCalc")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrapCalc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrapCalcSeqId")
    private int scrapCalcSeqId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userSeqId", nullable = false)
    private User user;
    
    @Column(name = "income", nullable = false)
    private long income;
    
    @Column(name = "tax", nullable = false)
    public long tax;

}
