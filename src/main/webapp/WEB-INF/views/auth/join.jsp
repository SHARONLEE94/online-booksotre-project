<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원가입 - 온라인 서점</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        background-color: #f5f5f5;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
      }
      .join-container {
        background: white;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 500px;
      }
      .join-title {
        text-align: center;
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 30px;
        color: #333;
      }
      .join-tabs {
        display: flex;
        margin-bottom: 30px;
        border-bottom: 1px solid #ddd;
      }
      .join-tab {
        flex: 1;
        padding: 15px;
        text-align: center;
        cursor: pointer;
        border-bottom: 3px solid transparent;
      }
      .join-tab.active {
        border-bottom-color: #3498db;
        color: #3498db;
        font-weight: bold;
      }
      .join-option {
        margin-bottom: 20px;
        padding: 20px;
        border: 1px solid #ddd;
        border-radius: 5px;
        cursor: pointer;
        transition: all 0.3s;
      }
      .join-option:hover {
        border-color: #3498db;
        background-color: #f8f9fa;
      }
      .join-option h3 {
        margin: 0 0 10px 0;
        color: #333;
      }
      .join-option p {
        margin: 0;
        color: #666;
        font-size: 14px;
      }
      .oauth-buttons {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
      }
      .oauth-button {
        flex: 1;
        padding: 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background: white;
        cursor: pointer;
        text-align: center;
        font-size: 14px;
        transition: all 0.3s;
      }
      .oauth-button:hover {
        background-color: #f8f9fa;
        border-color: #3498db;
      }
      .back-link {
        text-align: center;
        margin-top: 20px;
      }
      .back-link a {
        color: #3498db;
        text-decoration: none;
      }
      .back-link a:hover {
        text-decoration: underline;
      }
    </style>
  </head>
  <body>
    <div class="join-container">
      <h1 class="join-title">회원가입</h1>

      <div class="join-tabs">
        <div class="join-tab active" onclick="selectTab('individual')">
          개인회원
        </div>
        <div class="join-tab" onclick="selectTab('corporate')">법인회원</div>
      </div>

      <div id="individual-content">
        <div class="join-option" onclick="selectJoinMethod('phone')">
          <h3>휴대폰 인증으로 가입</h3>
          <p>휴대폰 번호 인증을 통해 회원가입을 진행합니다.</p>
        </div>

        <div class="join-option" onclick="selectJoinMethod('email')">
          <h3>이메일 인증으로 가입</h3>
          <p>이메일 인증을 통해 회원가입을 진행합니다.</p>
        </div>

        <div class="oauth-buttons">
          <button class="oauth-button" onclick="oauthSignup('naver')">
            네이버로 가입
          </button>
          <button class="oauth-button" onclick="oauthSignup('kakao')">
            카카오로 가입
          </button>
          <button class="oauth-button" onclick="oauthSignup('google')">
            구글로 가입
          </button>
        </div>
      </div>

      <div id="corporate-content" style="display: none">
        <div class="join-option" onclick="selectJoinMethod('corporate')">
          <h3>법인회원 가입</h3>
          <p>사업자등록번호 인증을 통해 법인회원으로 가입합니다.</p>
        </div>
      </div>

      <div class="back-link">
        <a href="/login">로그인으로 돌아가기</a>
      </div>
    </div>

    <script>
      function selectTab(tab) {
        // 탭 스타일 변경
        document
          .querySelectorAll(".join-tab")
          .forEach((t) => t.classList.remove("active"));
        event.target.classList.add("active");

        // 컨텐츠 변경
        if (tab === "individual") {
          document.getElementById("individual-content").style.display = "block";
          document.getElementById("corporate-content").style.display = "none";
        } else {
          document.getElementById("individual-content").style.display = "none";
          document.getElementById("corporate-content").style.display = "block";
        }
      }

      function selectJoinMethod(method) {
        // 회원가입 방법에 따라 다음 단계로 이동
        if (method === "phone" || method === "email") {
          window.location.href = "/join/verify/self";
        } else if (method === "corporate") {
          // 법인회원 가입 로직
          alert("법인회원 가입 기능은 준비 중입니다.");
        }
      }

      function oauthSignup(provider) {
        // OAuth 회원가입 URL로 리다이렉트
        window.location.href = `/oauth/${provider}/signup`;
      }
    </script>
  </body>
</html>
