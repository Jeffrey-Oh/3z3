package com.threedotthree.domain.model.user;

import com.threedotthree.domain.model.scrapCalc.ScrapCalc;
import com.threedotthree.infrastructure.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userSeqId")
    private int userSeqId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @Column(name = "regNo", nullable = false, unique = true)
    private String regNo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<ScrapCalc> scrapCalcList;

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

    /**
     * RefreshToken 업데이트
     */
    public void refreshTokenUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
        this.logindate = new Date();
    }

    /**
     * 비밀번호 확인
     */
    public Boolean passwordVerify(String inputPassword) {
        return SecurityUtil.passwordHash(inputPassword, getSalt()).equals(this.getPassword());
    }

    /**
     * salt 처리
     */
    public void passwordAddSalt(String password) {
        String salt = SecurityUtil.generateSalt();
        String hashPassword = SecurityUtil.passwordHash(password.trim(), salt);
        this.salt = salt;
        this.password = hashPassword;
    }

}
