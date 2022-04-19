package com.threedotthree.domain.model.shared;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@MappedSuperclass
public class UserCommonColumns {

    @Column(name = "refreshToken", length = 200)
    public String refreshToken;

    @Column(name = "salt", length = 30)
    public String salt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "logindate")
    public Date logindate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regdate", nullable = false)
    public Date regdate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "moddate")
    public Date moddate;

}
