<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>Hello World!</h2>
    <form name="form1" action="${pageContext.request.contextPath}/manage/product/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"/>
        <input type="submit" value="SpringMVC上传文件"/>
    </form>
    富文本图片上传
    <form name="form1" action="${pageContext.request.contextPath}/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"/>
        <input type="submit" value="富文本图片上传"/>
    </form>
</body>
</html>
