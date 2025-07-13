<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>오류 발생</title>
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
      .error-container {
        background: white;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        text-align: center;
        max-width: 500px;
      }
      .error-code {
        font-size: 72px;
        font-weight: bold;
        color: #e74c3c;
        margin: 0;
      }
      .error-message {
        font-size: 18px;
        color: #333;
        margin: 20px 0;
      }
      .error-details {
        color: #666;
        margin: 20px 0;
      }
      .back-button {
        background-color: #3498db;
        color: white;
        padding: 12px 24px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        text-decoration: none;
        display: inline-block;
        margin-top: 20px;
      }
      .back-button:hover {
        background-color: #2980b9;
      }
    </style>
  </head>
  <body>
    <div class="error-container">
      <h1 class="error-code">${status}</h1>
      <h2 class="error-message">${errorMessage}</h2>
      <p class="error-details">
        오류 코드: ${errorCode}<br />
        요청 시간: ${timestamp}
      </p>
      <a href="/main" class="back-button">홈으로 돌아가기</a>
    </div>
  </body>
</html>
