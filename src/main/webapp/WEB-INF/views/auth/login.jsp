<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>로그인 - 온라인 서점</title>
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
      .login-container {
        background: white;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 400px;
      }
      .login-title {
        text-align: center;
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 30px;
        color: #333;
      }
      .form-group {
        margin-bottom: 20px;
      }
      .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #333;
      }
      .form-group input {
        width: 100%;
        padding: 12px;
        border: 1px solid #ddd;
        border-radius: 5px;
        font-size: 16px;
        box-sizing: border-box;
      }
      .form-group input:focus {
        outline: none;
        border-color: #3498db;
      }
      .remember-id {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
      }
      .remember-id input {
        width: auto;
        margin-right: 8px;
      }
      .login-button {
        width: 100%;
        background-color: #3498db;
        color: white;
        padding: 12px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        margin-bottom: 20px;
      }
      .login-button:hover {
        background-color: #2980b9;
      }
      .oauth-buttons {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
      }
      .oauth-button {
        flex: 1;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background: white;
        cursor: pointer;
        text-align: center;
        font-size: 14px;
      }
      .oauth-button:hover {
        background-color: #f8f9fa;
      }
      .links {
        text-align: center;
        margin-top: 20px;
      }
      .links a {
        color: #3498db;
        text-decoration: none;
        margin: 0 10px;
      }
      .links a:hover {
        text-decoration: underline;
      }
      .error-message {
        color: #e74c3c;
        text-align: center;
        margin-bottom: 20px;
        font-size: 14px;
      }
    </style>
  </head>
  <body>
    <div class="login-container">
      <h1 class="login-title">로그인</h1>

      <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
      </c:if>

      <form id="loginForm">
        <div class="form-group">
          <label for="id">아이디</label>
          <input type="text" id="id" name="id" required />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input type="password" id="password" name="password" required />
        </div>

        <div class="remember-id">
          <input type="checkbox" id="rememberId" name="rememberId" />
          <label for="rememberId">아이디 기억</label>
        </div>

        <button type="submit" class="login-button">로그인</button>
      </form>

      <div class="oauth-buttons">
        <button class="oauth-button" onclick="oauthLogin('naver')">
          네이버
        </button>
        <button class="oauth-button" onclick="oauthLogin('kakao')">
          카카오
        </button>
        <button class="oauth-button" onclick="oauthLogin('google')">
          구글
        </button>
      </div>

      <div class="links">
        <a href="/join">회원가입</a>
        <a href="/find-password">비밀번호 찾기</a>
      </div>
    </div>

    <script>
      document
        .getElementById("loginForm")
        .addEventListener("submit", function (e) {
          e.preventDefault();

          const formData = {
            id: document.getElementById("id").value,
            password: document.getElementById("password").value,
            rememberId: document.getElementById("rememberId").checked,
          };

          fetch("/api/login", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
          })
            .then((response) => response.json())
            .then((data) => {
              if (data.success) {
                alert("로그인에 성공했습니다.");
                window.location.href = "/main";
              } else {
                alert(data.error.message || "로그인에 실패했습니다.");
              }
            })
            .catch((error) => {
              console.error("Error:", error);
              alert("로그인 중 오류가 발생했습니다.");
            });
        });

      function oauthLogin(provider) {
        // OAuth 로그인 URL로 리다이렉트
        window.location.href = `/oauth/${provider}/authorize`;
      }
    </script>
  </body>
</html>
