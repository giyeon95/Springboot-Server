<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spring Boot Application with JSP</title>
</head>
<body>

<h1>파일 업로드 예제</h1>

<form method="post" action="upload" enctype="multipart/form-data">
    <label>email:</label>
    <input type="text" name="id">


    <br><br>

    <label>파일:</label>
    <input type="file" name="file1">

    <br><br>

    <input type="submit" value="upload">
</form>

</body>


