package com.threedotthree.domain.model.user;

import com.threedotthree.domain.model.shared.UserCommonColumns;
import com.threedotthree.infrastructure.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends UserCommonColumns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userSeqId")
    private int userSeqId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "regNo", nullable = false)
    private String regNo;

    /**
     * RefreshToken 업데이트
     */
    public void refreshTokenUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
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
