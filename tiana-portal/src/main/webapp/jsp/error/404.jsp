<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="../common/base.jsp"%>
<title>页面不存在</title>
</head>

<body>
	<article class="page-404 minWP text-c">
		<p class="error-title">
			<i class="Hui-iconfont va-m">&#xe688;</i> <span class="va-m"> 404</span>
		</p>
		<p class="error-description">不好意思，您访问的页面不存在~</p>
		<p class="error-info">
			您可以： <a href="javascript:;" onclick="history.go(-1)" class="c-primary">&lt; 返回上一页</a> <span class="ml-20">|</span> <a href="/" class="c-primary ml-20">去首页
				&gt;</a>
		</p>
	</article>
</body>
</html>