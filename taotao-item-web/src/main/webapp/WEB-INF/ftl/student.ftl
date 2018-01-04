<html>
<head>
	<title>测试student</title>
</head>
<body>
	<h1>学生成绩基本信息</h1>
	学生id：${student.id}<br/>
	学生姓名：${student.name}<br/>
	学生地址：{student.address}<br/>
	学生年龄：${student.age}
	<table border="1">
	 <tr>
	    <th>序号</th>
	 	<th>id</th>
	 	<th>age</th>
	 	<th>address</th>
	 	<th>name</th>
	 </tr>
	 <#list list as student>
	 <#if student_index%2==0>
	 <tr bgcolor="red">
	 <#else>
	 <tr bgcolor="blue">
	 </#if>
	 	<td>${student_index}</td>
	 	<td>${student.id}</td>
	 	<td>${student.age}</td>
	 	<td>${student.address}</td>
	 	<td>${student.name}</td>
	 </tr>
	 </#list>
	</table>
	null值：${value!"默认值"}
</body>
</html>