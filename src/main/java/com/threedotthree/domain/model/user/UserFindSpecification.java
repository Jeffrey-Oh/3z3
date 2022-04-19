package com.threedotthree.domain.model.user;


import com.threedotthree.infrastructure.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFindSpecification {

    private final UserJpaRepository userJpaRepository;

    /**
     * 유저 정보 검증
     * @param userId : 아이디
     * @return User
     */
    public User findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId).orElseThrow(() -> new NotFoundDataException("조회된 유저정보가 없습니다.", "User"));
    }

}
