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