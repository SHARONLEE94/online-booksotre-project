## 로그인/로그아웃/회원가입 기능
#### UseCase 문서를 바탕으로 완전한 인증 시스템을 구현

```mermaid
src/main/java/com/bookstore/
├── domain/
│   ├── User.java                    # 사용자 엔티티
│   ├── SignupSession.java           # 회원가입 세션 엔티티
│   └── UserIdentityVerification.java # 본인인증 엔티티
├── dto/
│   ├── requestDTO/
│   │   ├── LoginRequestDTO.java     # 로그인 요청
│   │   └── SignupRequestDTO.java    # 회원가입 요청
│   └── responseDTO/
│       └── LoginResponseDTO.java    # 로그인 응답
├── mapper/
│   ├── UserMapper.java              # 사용자 매퍼
│   └── SignupSessionMapper.java     # 회원가입 세션 매퍼
├── service/
│   ├── AuthService.java             # 인증 서비스
│   └── SignupService.java           # 회원가입 서비스
├── controller/
│   ├── AuthController.java          # 인증 컨트롤러
│   └── SignupController.java        # 회원가입 컨트롤러
└── config/
    └── SecurityConfig.java          # 보안 설정

src/main/webapp/WEB-INF/views/auth/
├── login.jsp                        # 로그인 화면
├── join.jsp                         # 회원가입 초기 화면
└── complete.jsp                     # 가입 완료 화면
```

### UC01. 회원가입 화면 진입 (Enter Sign-Up Screen)

**Actor**
- 비로그인 사용자
    1. 서버는 회원가입 초기 화면을 렌더링하여 반환

**Precondition**
- 메인 화면에 진입해 있어야 함

**Trigger**
- 메인 화면의 “회원 가입” 버튼 클릭
- 메인 → “로그인” → “회원 가입” 클릭

**Main Flow**
1. 클라이언트가 `GET /join` 요청을 보냄
2. 화면에 “개인회원/법인회원” 탭, OAuth 버튼(네이버·카카오·구글), 휴대폰·이메일 가입 버튼을 표시

**Alternate Flow**
- 해당 URL에 접근 권한이 없으면 403 에러 반환

**Postcondition**
- `signup_session` 테이블에 `signup_step=0`인 신규 세션 생성

**주요 입력값**
- 없음 (GET 요청)

**주요 출력값**
- 회원가입 화면 HTML/JSON

### UC02. 본인인증 선택 (Choose Identity Verification)

**Actor**
- 비로그인 사용자

**Precondition**
- UC01 완료, `signup_session` 레코드 존재

**Trigger**
- 화면에서 “본인 인증”(휴대폰/아이핀) 버튼 클릭 → `GET /join/verify/self`

**Main Flow**
1. 서버가 `user_identity_verification` 테이블에
    - `verification_type = PENDING`
    - `request_at` 현재 시각인 신규 레코드를 생성

2. 인증 수단 선택 화면(휴대폰·아이핀 버튼)을 반환

**Alternate Flow**
- 이미 `PENDING` 상태 레코드가 있으면 해당 레코드를 재활용

**Postcondition**
- `signup_session.signup_step`을 1로 업데이트

**주요 입력값**
- 없음 (GET 요청)

**주요 출력값**
- 인증 수단 선택 화면 HTML/JSON

### UC03. 본인인증 요청 및 응답 (Perform Identity Verification)
**Actor**
- 비로그인 사용자

**Precondition**
- UC02 완료, `verification_type` 선택됨

**Trigger**
- “인증 요청” 버튼 클릭 → 인증 요청 API 호출

**Main Flow**
1. 서버가 외부 인증 API(SMS/아이핀) 호출
2. 호출 시각을 `request_at`에 기록
3. 외부 응답 수신 후
- `response_at` 기록
- `status_code`를 SUCCESS 또는 FAIL로 업데이트
4. `user_identity_verification_log`에 호출 로그 저장

**Alternate Flow**
- 외부 API 오류 발생 시
- `status_code = FAIL`, `error_code` 기록
- 사용자에게 “인증에 실패했습니다” 오류 메시지 반환

**Postcondition**
- 인증 성공 시
- `user_identity_verification.status_code = SUCCESS`
- `signup_session.signup_step = 2`

**주요 입력값**
- 없음 (API 내부 처리)

**주요 출력값**
- 인증 성공/실패 상태 JSON

### UC04. 본인인증 완료 확인 (Confirm Verification Completion)
**Actor**
- 비로그인 사용자

**Precondition**
- UC03에서 `status_code = SUCCESS`

**Trigger**
- 인증 완료 팝업의 “확인” 버튼 클릭

**Main Flow**
1. 서버가 `signup_session.signup_step`을 3으로 증가
2. (선택) `user_identity_verification_log`에 “COMPLETE_CLICK” 이벤트 저장
3. 다음 단계(약관동의) 화면으로 리다이렉트

**Alternate Flow**
- 세션이 만료된 경우 UC01 화면으로 이동

**Postcondition**
- 다음 가입 단계 진입 준비 완료

**주요 입력값**
- 없음 (클라이언트 이벤트)

**주요 출력값**
- 약관동의 화면으로의 Redirect

### UC05. 기존 본인인증 연동 허브 (Link Existing Verification)
**Actor**
- 비로그인 사용자

**Precondition**
- UC03에서 `status_code = SUCCESS`이거나 `user_identity_verification.user_id` NULL

**Trigger**
- “기존 계정 연동” 페이지 진입

**Main Flow**
1. 서버가 `user_identity_verification`에서 `user_id` 조회
2. 사용자 정보(이메일, 가입일, 소속 등)를 화면에 표시
3. “로그인” 또는 “비밀번호 재설정” 버튼을 제공

**Alternate Flow**
- `user_id`가 조회되지 않으면
- “계정이 없습니다. 신규 가입을 진행하세요” 메시지 표시

**Postcondition**
- 사용자가 선택한 동작에 따라 UC02(로그인) 또는 UC06(비밀번호 재설정)로 분기

**주요 입력값**
- 없음 (페이지 진입)

**주요 출력값**
- 연동 허브 화면 HTML/JSON


### UC06. 비밀번호 재설정 인증 (Password Reset Verification)
**Actor**
- 비로그인 사용자

**Precondition**
- UC05에서 “비밀번호 재설정” 선택

**Trigger**
- “인증번호 요청” 버튼 클릭

**Main Flow**
1. 서버가 `password_reset_request`에
- `method = EMAIL`
- `verification_code` 6자리 생성·저장
- `request_at` 기록
2. 생성된 코드를 이메일로 발송
3. 사용자가 입력한 코드와 DB 코드를 비교
4. 일치 시 `status_code = SUCCESS`로 업데이트 후 다음 화면(새 비밀번호 입력)으로 이동

**Alternate Flow**
- 코드 불일치 시 “코드가 올바르지 않습니다” 오류
- 재시도 제한 초과 시 “재시도 횟수 초과” 안내

**Postcondition**
- `password_reset_request.status_code = SUCCESS`

**주요 입력값**
- 사용자 입력 `verification_code`

**주요 출력값**
- 인증 성공/실패 상태 JSON

### UC07. SNS 연동 회원가입 (SNS-Based Sign-Up)
**Actor**
- 비로그인 사용자

**Precondition**
- UC01 화면에서 OAuth 버튼 클릭

**Trigger**
- “네이버/카카오/구글” 버튼 클릭 → OAuth2 인증 플로우 시작

**Main Flow**
1. 외부 OAuth2 서버에서 인증 완료 후 콜백
2. 서버가 `user_sns_auth` 테이블에서
- `sns_provider`, `sns_uid`로 조회
3. 기존 레코드가 있으면 UC02(로그인) 수행
4. 없으면
- `member`(또는 `USER`) 테이블에 `oauth_provider`, `oauth_provider_uid` 저장
- 신규 계정 생성 후 로그인 처리

**Alternate Flow**
- OAuth 토큰 취소 또는 실패 시
- “인증에 실패했습니다” 메시지 표시

**Postcondition**
- 계정 생성 또는 로그인 완료 후 메인 페이지로 리다이렉트

**주요 입력값**
- 외부로부터 전달된 OAuth 토큰/코드

**주요 출력값**
- 로그인 토큰 또는 오류 메시지


### UC08. 약관동의 (Agree to Terms)
**Actor**
- 비로그인 사용자

**Precondition**
- UC04 완료, `signup_session.signup_step = 3`

**Trigger**
- “약관동의” 화면 진입 (`GET /join/terms`)

**Main Flow**
1. 서버가 이용약관·개인정보처리방침 등 체크박스 목록 렌더링
2. 사용자가 필수 약관 최소 2개를 체크 후 “다음” 클릭
3. 선택 약관(마케팅 등) 추가 체크 가능
4. `member_sign` 테이블에 `agreed_terms` 저장
5. `signup_session.signup_step`을 4로 업데이트 후 개인정보 입력 화면으로 이동

**Alternate Flow**
- 필수 약관 미동의 시 “필수 약관에 동의해주세요” 알림

**Postcondition**
- `member_sign`에 약관 동의 이력 기록

**주요 입력값**
- `terms_ids`

**주요 출력값**
- 개인정보 입력 화면으로의 Redirect

### UC09. 개인정보 입력 (Enter Personal Info)

**Actor**
- 비로그인 사용자

**Precondition**
- UC08 완료, `signup_session.signup_step = 4`

**Trigger**
- 개인정보 입력 화면 진입 (`GET /join/info`)
- “ID 중복확인” 버튼 클릭 (`GET /api/join/check-id?id={userId}`)
- “가입 완료” 버튼 클릭 (`POST /api/join/complete`)

**Main Flow**
1. 서버가 입력 폼(ID, 비밀번호, 이름, 이메일, 연락처, 주소 등) 렌더링
2. 사용자가 ID 입력 후 "ID 중복확인" 클릭 시, 서버는 해당 ID의 사용 가능 여부를 실시간으로 반환
3. 사용자가 필수·선택 정보를 모두 입력 후 “가입 완료” 클릭
4. 서버가 유효성 검사 후 `USER`(또는 `MEMBER`) 테이블에 최종 레코드 INSERT (`status_code` = ACTIVE)
5. 관련 중간 테이블(`signup_session` 등) 만료 또는 삭제
6. 가입 완료 화면으로 Redirect

**Alternate Flow**
- **ID 중복:** "ID 중복확인" 시 이미 사용 중인 ID이면 "이미 사용 중인 ID입니다" 메시지 반환
- **필수 입력 누락:** 필드별 유효성 검사 실패 시 해당 필드 아래에 오류 메시지 표시
- **API 오류:** 이메일 중복 등 DB 제약조건 위배 시 "가입 중 오류가 발생했습니다" 알림 Postcondition

**Postcondition**
- `USER` 테이블에 신규 `ACTIVE` 계정 생성

**주요 입력값**
- `id, password, name, email, phone, address`

**주요 출력값**
- ID 사용 가능 여부(JSON), user_id, 가입 완료 메시지

### UC10. 가입 완료 (Complete Sign-Up)
**Actor**
- 비로그인 사용자

**Precondition**
- UC09 성공

**Trigger**
- `POST /api/join/complete` 응답 수신 후 가입 완료 화면 표시

**Main Flow**
1. 시스템이 “환영합니다, [이름]님!” 메시지와 함께 로그인 버튼을 렌더링
2. 사용자가 “로그인” 클릭 시 UC02(로그인)로 이동

**Alternate Flow**
- 가입 처리 직후 오류 발생 시 “오류가 발생했습니다. 다시 시도해주세요” 알림

**Postcondition**

**주요 출력값**
- 신규 세션에 `ROLE_USER` 권한 설정

**주요 입력값**
- 없음 (가입 완료 화면)
- “환영합니다” 메시지, 로그인 화면 Redirect 링크

### UC11. 관리자: 사용자 정보 수정 (Admin Edit User)
**Actor**
- 관리자

**Precondition**
- 관리자 로그인, UC05(회원 상세조회) 완료

**Trigger**
- 상세 화면에서 “정보 수정” 클릭 (`PUT /api/admin/users/{userId}`)

**Main Flow**
1. 서버가 편집 폼에 현재 사용자 정보를 채워 렌더링
2. 관리자가 상태, 역할, 연락처 등을 수정 후 “저장” 클릭
3. 서버가 입력값 유효성 검사 후 `USER`(또는 `member`) 테이블 업데이트 (`updated_at` 갱신)
4. “수정이 완료되었습니다” 메시지 반환

**Alternate Flow**
- 유효성 검사 실패 시 필드별 오류 메시지
- 권한 부족 시 403 에러

**Postcondition**
- `admin_action_log`에 수정 이력 기록

**주요 입력값**
- 변경된 사용자 속성들 (e.g. `status_code`, `role_code`, `phone`)

**주요 출력값**
- 수정 성공 메시지

### UC12. 관리자: 사용자 삭제/비활성화 (Admin Deactivate User)
**Actor**
- 관리자

**Precondition**
- 관리자 로그인, UC05 완료

**Trigger**
- 상세 화면의 “비활성화” 버튼 클릭 (`PATCH /api/admin/users/{userId}/deactivate`)

**Main Flow**시스템이 확인 모달(“정말 비활성화하시겠습니까?”) 표시
1. 관리자가 “확인” 클릭
2. 서버가 `USER` 테이블의 `status_code`를 SUSPENDED로 변경, `updated_at` 갱신`
3. “비활성화 되었습니다” 메시지 반환

**Alternate Flow**
- 이미 비활성화 상태면 “이미 비활성화된 사용자입니다” 알림

**Postcondition**

- `admin_action_log`에 비활성화 이력 기록

**주요 입력값**
- 없음 (유저 ID는 URL 파라미터)

**주요 출력값**
- 비활성화 성공 메시지

### UC13. 로그인 (신규)
**Actor**
비회원 또는 로그아웃 상태의 사용자

**Precondition**
(없음)

**Trigger**
헤더의 "로그인" 버튼 클릭 → 로그인 화면으로 이동

**Main Flow**
1. 서버가 ID, 비밀번호 입력 필드와 "아이디 기억", "로그인", "비밀번호 찾기" 버튼이 포함된 화면을 반환
2. 사용자가 ID와 비밀번호를 입력하고 "로그인" 버튼 클릭 (POST /api/login)
3. 서버는 사용자 정보가 일치하는지 확인
4. 인증 성공 시, JWT(또는 세션 쿠키)를 생성하여 클라이언트에 반환하고 메인 페이지로 리다이렉트
5. 사용자가 "아이디 기억" 체크박스를 선택했다면, 클라이언트는 쿠키에 사용자 ID를 저장
   Alternate Flow
- **인증 실패:** "아이디 또는 비밀번호가 일치하지 않습니다" 메시지 표시
- **휴면 계정:** 휴면 상태의 계정일 경우, 휴면 해제 절차 안내 페이지로 이동

**Postcondition**
사용자 인증 완료, 서버로부터 접근 토큰 발급

**주요 입력값**
id, password, rememberId (boolean)

**주요 출력값**
로그인 성공/실패 상태, 접근 토큰 (JWT)

---

## 📁 구현된 구조
### 🚀 주요 기능들
#### 1. 로그인 (UC13)
   ID/비밀번호 로그인
   OAuth 로그인 (네이버, 카카오, 구글)
   아이디 기억 기능
   세션 기반 인증

#### 2. 회원가입 (UC01~UC10)
   UC01: 회원가입 화면 진입
   UC02: 본인인증 선택
   UC03: 본인인증 요청/응답
   UC04: 본인인증 완료 확인
   UC07: SNS 연동 회원가입
   UC08: 약관동의
   UC09: 개인정보 입력 (ID 중복확인 포함)
   UC10: 가입 완료

#### 3. 보안 기능
   BCrypt 비밀번호 암호화
   세션 관리
   계정 상태 관리 (ACTIVE, SUSPENDED, DORMANT)
   권한 관리 (USER, ADMIN)

#### 4. 예외 처리
   글로벌 예외 처리 시스템 활용
   체계적인 에러 코드 관리
   사용자 친화적인 오류 메시지
   🎯 UseCase 구현 완료도
   ✅ UC01: 회원가입 화면 진입
   ✅ UC02: 본인인증 선택
   ✅ UC03: 본인인증 요청/응답
   ✅ UC04: 본인인증 완료 확인
   ✅ UC07: SNS 연동 회원가입
   ✅ UC08: 약관동의
   ✅ UC09: 개인정보 입력
   ✅ UC10: 가입 완료
   ✅ UC13: 로그인
  
5. 추가 구현 필요사항
  ①. 데이터베이스 테이블 생성
  ②. OAuth 실제 연동 (네이버, 카카오, 구글 API)
  ③. 본인인증 API 연동 (SMS, 아이핀)
  ④. 이메일 발송 기능
  ⑤. 관리자 기능 (UC11, UC12)