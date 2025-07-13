<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>페이지를 찾을 수 없습니다</title>
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
      .search-box {
        margin: 20px 0;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 5px;
        width: 200px;
      }
    </style>
  </head>
  <body>
    <div class="error-container">
      <h1 class="error-code">404</h1>
      <h2 class="error-message">페이지를 찾을 수 없습니다</h2>
      <p class="error-details">
        요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.<br />
        URL을 다시 확인해주세요.
      </p>
      <input type="text" class="search-box" placeholder="도서 검색..." />
      <br />
      <a href="/main" class="back-button">홈으로 돌아가기</a>
    </div>
  </body>
</html>
