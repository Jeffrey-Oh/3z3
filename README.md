# 삼쩜삼 백엔드 엔지니어 채용 과제

## 프로젝트 생성
- Java 11
- Spring Boot 2.6.4
- JPA
- H2
- Gradle

---

## 요구사항

### 회원가입
- 구현완료
- 구현방법
    - 회원가입 가능한 유저의 정보를 Map으로 미리 설정
    - 가입 가능한 유저의 정보인지 Map에서 확인
    - JPA로 `이름` 과 `주민등록번호` 를 이용하여 회원정보 조회
    - 조회된 데이터가 없으면 회원가입 진행
- 검증결과
    - [회원가입](http://localhost:8080/swagger-ui/index.html#/szs-controller/signupUsingPOST)
        1. `200` <br />
           ### Request
           ```json
           {
               "name": "홍길동",
               "password": "qlqjs123",
               "regNo": "860824-1655068",
               "userId": "test"
           }
           ```

           ### Response
           ```json
           {
                "rt": 200,
                "rtMsg": "API Call successful",
                "signUpResultDTO": {
                    "seqId": 1,
                    "userId": "test",
                    "name": "홍길동"
                }
            }
           ```

        2. `401` - 가입 가능한 회원정보가 아닌 경우 Exception 발생
           ### Request
           ```json
           {
               "name": "삼쩜삼",
               "password": "szs123",
               "regNo": "123456-9876543",
               "userId": "test"
           }
           ```

           ### Response
           ```json
           {
               "rt": 401,
               "errors": {
                   "field": null,
                   "message": "회원가입 권한이 없습니다."
               }
           }
           ```
        3. `409` - 이미 가입한 데이터로 회원가입 시도 시 Exception 발생
           ### Request
           ```json
           {
               "name": "홍길동",
               "password": "qlqjs123",
               "regNo": "860824-1655068",
               "userId": "test"
           }
           ```

           ### Response
           ```json
           {
               "rt": 409,
               "errors": {
                   "field": null,
                   "message": "이미 회원가입된 정보가 존재합니다."
               }
           }
           ```

### 로그인
- 구현완료
- 구현방법
    - 회원가입 완료된 정보를 가지고 로그인 시도
    - `아이디`로 먼저 조회
        - 조회된 정보가 없으면 Exception 발생
    - `비밀번호 + salt` 조합으로 비밀번호 확인
        - 일치하지 않으면 Exception 발생
    - 로그인 처리 시 `JWT Token` 발급
- 검증결과
    - [로그인](http://localhost:8080/swagger-ui/index.html#/szs-controller/loginUsingPOST)
        1. `200` <br />
           ### Request
           ```json
           {
               "userId": "test",
               "password": "qlqjs123"
           }
           ```

           ### Response
           ```json
           {
                "rt": 200,
                "rtMsg": "API Call successful",
                "loginResult": {
                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAwOTQxNDEsImV4cCI6MTY1MDEwMTM0MX0.PSlkwyjS8CqT539J_PwCrvsU2Md1GbF26QDhx9S-EN4",
                    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAwOTQxNDEsImV4cCI6MTY1MTMwMzc0MX0._GqGoJZcQD72fARjyEpMh7tOxwrnsIyKR-hVIzdIdBY",
                    "seqId": 1,
                    "userId": "test",
                    "name": "홍길동"
                }
            }
           ```
        2. `400` - 비밀번호가 일치하지 않는 경우 Exception 발생
           ### Request
           ```json
           {
               "userId": "test",
               "password": "qlqjs1234"
           }
           ```

           ### Response
           ```json
           {
                "rt": 400,
                "errors": {
                    "field": "password",
                    "message": "비밀번호가 일치하지 않습니다."
                }
            }
           ```

        3. `422` - 회원가입된 정보가 없는 경우 Exception 발생
           ### Request
           ```json
           {
               "userId": "삼쩜삼",
               "password": "qlqjs123"
           }
           ```

           ### Response
           ```json
           {
                "rt": 422,
                "errors": {
                    "field": "User",
                    "message": "조회된 유저정보가 없습니다."
                }
            }
           ```

### 회원정보 조회
- 구현완료
- 구현방법
    - `JWT Token` 으로 회원정보 조회
        - `Authorization: Bearer 토큰`
    - `Bearer ` Prefix 없는 경우 Exception 발생
    - 토큰이 유효하지 않거나 `ROLE` 값이 다른 경우 Exception 발생
- 검증결과
    - [회원정보 조회](http://localhost:8080/swagger-ui/index.html#/szs-controller/meUsingGET)
        1. `200` <br />
           ### Request
           ```shell
           curl -X GET "http://localhost:8080/szs/me" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAxMDA4ODcsImV4cCI6MTY1MDEwODA4N30.NLlD-OcE_rYeKVcwbkh7w9rsAYwSP8ui21ug3eFKEn4"
           ```

           ### Response
           ```json
           {
               "rt": 200,
               "rtMsg": "API Call successful",
               "userViewDTO": {
                   "seqId": 1,
                   "userId": "test",
                   "name": "홍길동",
                   "regNo": "860824-1655068"
               }
           }
           ```
        2. `403` - 토큰이 유효하지 않는 경우 Exception 발생
           ### Request
           ```shell
           curl -X GET "http://localhost:8080/szs/me" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAxMDA4ODcsImV4cCI6MTY1MDEwODA4N30.NLlD-OcE_rYeKVcwbkh7w9rsAYwSP8ui21ug3eFKEn4"
           ```

           ### Response
           ```json
           {
                "rt": 403,
                "errors": {
                    "field": "password",
                    "message": "Permission error"
                }
            }
           ```

        3. `422` - 회원가입된 정보가 없는 경우 Exception 발생 <br />
           (토큰 생성 직후 회원정보를 삭제한 경우)
           ### Request
           ```shell
           curl -X GET "http://localhost:8080/szs/me" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAxMDA4ODcsImV4cCI6MTY1MDEwODA4N30.NLlD-OcE_rYeKVcwbkh7w9rsAYwSP8ui21ug3eFKEn4"
           ```

           ### Response
           ```json
           {
                "rt": 422,
                "errors": {
                    "field": "User",
                    "message": "조회된 유저정보가 없습니다."
                }
            }
           ```