<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<title>Garden Planner</title>

	<script type="text/javascript">
		function loadXMLDoc(){
			var xmlhttp;

			var url="PlantInferenceServlet";
			url = url + "?querySunPref=Display+sun+preference+"

			if (window.XMLHttpRequest)
			{
				xmlhttp=new XMLHttpRequest();
			}else
			{
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange=function()
			{
				if (xmlhttp.readyState==4 && xmlhttp.status==200)
				{
					document.getElementById("result").innerHTML=xmlhttp.responseText;
				}
			}

			xmlhttp.open("GET",url,true)
			xmlhttp.send();
		}
	</script>

</head>
<body class="gradient">
<h1 id="maintitle">Garden Planner</h1>
<a href="index.jsp">Home</a>
<p>This page sends a SPARQL query to an inference model in persistent storage (TDB storage) and displays the results</p>
<form method="get" action="PlantInferenceServlet">
	<input type="button" name="queryProperties" class="btn" value="Display plant properties">
	<input type="button" name="querySunPref" class="btn" value="Display sun preference " onclick="loadXMLDoc()">
</form>
<div id="result">

</div>
</body>
</html>