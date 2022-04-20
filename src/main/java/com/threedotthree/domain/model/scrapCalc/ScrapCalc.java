package com.threedotthree.domain.model.scrapCalc;

import com.threedotthree.domain.model.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "scrapCalc")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
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

    /**
     * 업데이트
     */
    public void update(long income, long tax) {
        this.income = income;
        this.tax = tax;
    }

}
