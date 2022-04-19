package com.threedotthree.application.user;


import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.domain.model.user.User;
import com.threedotthree.domain.model.user.UserJpaRepository;
import com.threedotthree.infrastructure.exception.AlreadyDataException;
import com.threedotthree.infrastructure.exception.UnauthorizedException;
import com.threedotthree.infrastructure.utils.SecurityUtil;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserApplication {

    private final UserJpaRepository userJpaRepository;

    /**
     * 회원가입
     * @param request : 회원가입 정보
     * @return SignUpResponse
     */
    @Transactional(rollbackFor = Exception.class)
    public SignUpResultDTO signUp(SignUpRequest request) throws Exception {

        Map<String, String> acceptUser = new HashMap<>();
        acceptUser.put("홍길동", "860824-1655068");
        acceptUser.put("김둘리", "921108-1582816");
        acceptUser.put("마징가", "880601-2455116");
        acceptUser.put("베지터", "910411-1656116");
        acceptUser.put("손오공", "820326-2715702");

        // 회원가입 가능한 유저인지 체크
        if (acceptUser.containsKey(request.getName()) && acceptUser.get(request.getName()).equals(request.getRegNo())) {
            // 회원정보 조회
            User user = userJpaRepository.findByNameAndRegNo(request.getName(), SecurityUtil.strToEncrypt(request.getRegNo())).orElse(null);

            // 회원가입 한 적이 없다면 ?
            if (user == null) {
                // 아이디 중복검사
                User duplicateUser = userJpaRepository.findByUserId(request.getUserId()).orElse(null);
                if (duplicateUser != null) throw new AlreadyDataException("해당 아이디는 사용할 수 없습니다.", "userId");

                user = userJpaRepository.save(User.builder()
                    .userId(request.getUserId())
                    .password(SecurityUtil.passwordHash(request.getPassword(), "SZS"))
                    .name(request.getName())
                    .regNo(SecurityUtil.strToEncrypt(request.getRegNo()))
                    .build());

                // 비밀번호 보안처리
                user.passwordAddSalt(request.getPassword());

                return SignUpResultDTO.builder()
                    .userSeqId(user.getUserSeqId())
                    .userId(user.getUserId())
                    .name(user.getName())
                    .build();
            } else
                throw new AlreadyDataException("이미 회원가입된 정보가 존재합니다.", "user");
        } else throw new UnauthorizedException("회원가입 권한이 없습니다.");

    }

}
