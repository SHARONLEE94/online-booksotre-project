<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원가입 완료 - 온라인 서점</title>
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
      .complete-container {
        background: white;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 500px;
        text-align: center;
      }
      .success-icon {
        font-size: 72px;
        color: #27ae60;
        margin-bottom: 20px;
      }
      .complete-title {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 10px;
        color: #333;
      }
      .welcome-message {
        font-size: 18px;
        color: #666;
        margin-bottom: 30px;
      }
      .user-name {
        color: #3498db;
        font-weight: bold;
      }
      .complete-description {
        color: #666;
        margin-bottom: 30px;
        line-height: 1.6;
      }
      .login-button {
        background-color: #3498db;
        color: white;
        padding: 15px 30px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        text-decoration: none;
        display: inline-block;
        margin-right: 10px;
      }
      .login-button:hover {
        background-color: #2980b9;
      }
      .main-button {
        background-color: #95a5a6;
        color: white;
        padding: 15px 30px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        text-decoration: none;
        display: inline-block;
      }
      .main-button:hover {
        background-color: #7f8c8d;
      }
    </style>
  </head>
  <body>
    <div class="complete-container">
      <div class="success-icon">✓</div>
      <h1 class="complete-title">회원가입이 완료되었습니다!</h1>
      <p class="welcome-message">
        환영합니다, <span class="user-name">${userName}</span>님!
      </p>
      <p class="complete-description">
        온라인 서점의 회원이 되신 것을 축하드립니다.<br />
        이제 다양한 도서를 구매하고 리뷰를 작성하실 수 있습니다.
      </p>

      <div>
        <a href="/login" class="login-button">로그인하기</a>
        <a href="/main" class="main-button">메인으로</a>
      </div>
    </div>
  </body>
</html>
