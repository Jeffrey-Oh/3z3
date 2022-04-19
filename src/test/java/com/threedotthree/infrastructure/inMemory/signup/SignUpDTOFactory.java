package com.threedotthree.infrastructure.inMemory.signup;

import java.util.HashMap;
import java.util.Map;

public class SignUpDTOFactory {

    /**
     * 회원가입 가능한 명단
     * @return Map<String, String>
     */
    public static Map<String, String> mockSignUpAcceptUser() {
        Map<String, String> acceptUser = new HashMap<>();
        acceptUser.put("홍길동", "860824-1655068");
        acceptUser.put("김둘리", "921108-1582816");
        acceptUser.put("마징가", "880601-2455116");
        acceptUser.put("베지터", "910411-1656116");
        acceptUser.put("손오공", "820326-2715702");

        return acceptUser;
    }

}
