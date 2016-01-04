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
			var hardinessZone = document.getElementById('hardinessZone').value;
			var harvestDur = document.getElementById('harvestDur').value;
			var find = document.getElementById('find').value;

			var url="PlantRDFViewServlet";
			url = url + "?hardinessZone=" + hardinessZone + "&sunPref=part+shade&harvestDur=" + harvestDur +"&find=" + find;
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
					document.getElementById("results").innerHTML=xmlhttp.responseText;
				}
			}

			xmlhttp.open("GET",url,true)
			xmlhttp.send();
		}
	</script>
</head>
<body class="gradient">
<h1 id="maintitle">Garden Planner</h1>
<a href="inference.jsp" class="toplink">Inference demo</a>
<p class="intro">Garden Planner helps you choose the right plants for the season</p>
<br>
<h3>Find a plant for your garden:</h3>
<form method="get" action="PlantRDFViewServlet">
	<p class="subheading">Minimum hardiness zone:</p>
	<select name="hardinessZone" id="hardinessZone">
		<option value="2">2</option>
		<option value="3">3</option>
		<option value="4">4</option>
		<option value="8" selected>8</option>
	</select>
	<p class="subheading">Sun preference:</p>
	<input type="radio" name="sunPref" value="part shade">part shade
	<input type="radio" name="sunPref" value="part sun" checked="checked">part sun
	<input type="radio" name="sunPref" value="full sun">full sun
	<p class="subheading">Harvest duration:</p>
	<select name="harvestDur" id="harvestDur">
		<option value="1">1</option>
		<option value="8" selected>8</option>
		<option value="13">13</option>
	</select> weeks

	<input type="button" name="find" id="find" class="btn" value="Search" onclick='loadXMLDoc()'>
</form>
<div class="result" id="results" >

</div>
<p></p>
</body>
</html>