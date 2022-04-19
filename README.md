# 삼쩜삼 백엔드 엔지니어 채용 과제

## 프로젝트 생성
- Java 11
- Spring Boot 2.6.4
- JPA
- H2
    - password : test
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
                   "field": "userId",
                   "message": "해당 아이디는 사용할 수 없습니다."
               }
           }
           ```
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
           curl -X 'GET' \
             'http://localhost:8080/szs/me' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I'
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
           curl -X 'GET' \
             'http://localhost:8080/szs/me' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I'
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
           curl -X 'GET' \
             'http://localhost:8080/szs/me' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I'
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

### 유저 스크랩
- 구현완료
- 구현방법
    - `JWT Token` 으로 유저 스크랩 API 요청
        - `Authorization: Bearer 토큰`
    - `Bearer ` Prefix 없는 경우 Exception 발생
    - 토큰이 유효하지 않거나 `ROLE` 값이 다른 경우 Exception 발생
    - RestTemplate Server To Server 통신
    - `총급여액` 및 `산출세액` 항목 DB 저장
- 검증결과
    - [유저 스크랩](http://localhost:8080/swagger-ui/index.html#/szs-controller/scrapUsingPOST)
        1. `200` <br />
           ### Request
           ```shell
           curl -X 'POST' \
             'http://localhost:8080/szs/scrap' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I' \
             -d ''
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
           curl -X 'POST' \
             'http://localhost:8080/szs/scrap' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I' \
             -d ''
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
           curl -X 'POST' \
             'http://localhost:8080/szs/scrap' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I' \
             -d ''
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

### 환급액
- 구현완료
- 구현방법
    - `JWT Token` 으로 환급액 API 요청
        - `Authorization: Bearer 토큰`
    - `Bearer ` Prefix 없는 경우 Exception 발생
    - 토큰이 유효하지 않거나 `ROLE` 값이 다른 경우 Exception 발생
    - 금액 단위 기준으로 한글로 변환하여 제공
- 검증결과
    - [환급액](http://localhost:8080/swagger-ui/index.html#/szs-controller/refundUsingGET)
        1. `200` <br />
           ### Request
           ```shell
           curl -X 'GET' \
             'http://localhost:8080/szs/refund' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I'
           ```

           ### Response
           ```json
           {
                "이름": "홍길동",
                "한도": "74만원",
                "공제액": "92만 5천원",
                "환급액": "74만원"
            }
           ```
        2. `403` - 토큰이 유효하지 않는 경우 Exception 발생
           ### Request
           ```shell
           curl -X 'GET' \
             'http://localhost:8080/szs/refund' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I'
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
           curl -X 'GET' \
             'http://localhost:8080/szs/refund' \
             -H 'accept: application/json' \
             -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODg3MTQsImV4cCI6MTY1MDI5NTkxNH0.2wfQcG-BwqoNTCoZWLH9r1nPpe_cMGRCBYC203OgZ2I'
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

---

# 주관식 답변

1. inmemory 방식으로 mock 데이터를 내부에 생성 혹은 SQL 파일을 만들어서 테스트
---
2. x
---
3. x
---
4. 서비스가 커지면서 필요한지 재차 질문을 던지고 불필요한 경우 그 시점에 의존성을 해제한다.
   자산의 여유가 있다면 이중화 구조로 구축한다.
   장애 격리를 할 수 있는 것을 통해서 장애를 최대한 줄인다. (알럿 메시지, 상품 주문 불가 등)
   모니터링 시스템을 구축하여 상시 서버의 상태를 확인해야한다. (클라우스 서비스 등)
---
5. `총지급액`, `총사용금액`의 포멧이 올바르지 않아서 제대로 계산이 되지 않기 때문에 스크랩 API를 제공하는 곳에서 값의 포멧을 수정해주던지 이용하는 서비스에서 콤마(,)를 제거하고 숫자변환이 되지 않는 경우 Exception을 발생시켜 올바른 값만 DB에 저장될 수 있게 수정한다.
   과제에서는 점(.)이 포함된 총지급액에 대해 수정하지 않았습니다. NumberFormat을 사용하여 콤마(,)를 기준으로 계산되었습니다. 따라서 `49,999.999` 같은 경우 `49999` 값으로 사용되었습니다. <br /><br />
   그리고 환급액 산출에 사용되는 `소득내역` 과 `소득구분` 의 데이터 형식이 List이기 때문에 해당 값들의 제공되는 값은 Size가 1개씩 이지만 데이터 타입에 맞게 처리하기 위해서는 모든 데이터의 합산으로 처리해야하는지 List에서 제일 먼저 등록된 데이터 먼저 처리하는지에 대한 요구사항이 API를 사용하는 사용자에게 제공되어야 한다고 생각합니다. 과제에는 첫 번째 데이터만 처리하는 것으로 적용되었습니다. <br /><br />
   `손오공`의 데이터에서 `userId`만 다른 유저들과 다르게 Response 데이터 타입이 `int`로 제공되고 있는데 스크랩 API에 의존하고 있는 다른 API가 있는 경우 요청하는 입장에서 데이터 타입이 맞지 않아 불필요하게 캐스팅을 해야하는 경우가 있을 것 같습니다.
---
6. 우선순위에 맞는 Task를 구분하여 급한 업무에 대해서는 습관에 맞춰 개발하고 추후 회사 코드 컨벤션에 맞게 리팩토링 진행을 합니다. 우선순위가 낮은 Task인 경우 코드 컨벤션에 맞춰 개발합니다. 우선순위의 낮음에 대한 판단 여부는 기획자 혹은 개발팀장 그렇지 않으면 관계자에게 물어보고 진행합니다. 최대한 코드 컨벤션에 맞게 초기부터 진행하지 않으면 서비스 중에 바꾸는 것은 쉽지 않고 시간도 많이 소모되기 때문에 가능한 할 수 있는 것은 규칙에 맞게 작업하는게 맞다고 생각합니다.
---
7. 암호화 알고리즘에는 양방향 알고리즘인 AES와 단방향 알고리즘인 SHA를 사용합니다.
   과제에서는 비밀번호 같은 경우 단방향 알고리즘을 사용하였으며 그 이유는 개인정보보호법에 따랐으며, 주민등록번호 같은 경우는 양방향 알고리즘을 사용하였는데 단방향으로 처리하지 못한 이유로는 스크랩 API를 사용할 때 요구사항이 주민등록번호의 원본 값이였기 때문입니다.
   그렇기 때문에 AES 알고리즘을 통해 대칭키를 정의하여 암, 복호화가 되는 구조로 만들었습니다.