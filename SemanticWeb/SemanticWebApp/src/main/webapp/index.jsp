<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>Garden Planner</title>
	<script type="text/javascript">
		function loadXMLDoc(){
			var xmlhttp;
			var hardinessZone = document.getElementById('hardinessZone').value;
			var harvestDur = document.getElementById('harvestDur').value;
			var find = document.getElementById('find').value;
			var sunPref;

			var inputs = document.getElementsByName("sunPref");
			for (var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked) {
					sunPref = inputs[i].value;
				}
			}

			var url="PlantRDFViewServlet";
			url = url + "?hardinessZone=" + hardinessZone + "&sunPref=" + sunPref +"&harvestDur=" + harvestDur +"&find=" + find;

			if (window.XMLHttpRequest){
				xmlhttp=new XMLHttpRequest();
			}else{
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange=function(){
				if (xmlhttp.readyState==4 && xmlhttp.status==200){
					document.getElementById("result").innerHTML=xmlhttp.responseText;
				}
			}
			xmlhttp.open("GET",url,true)
			xmlhttp.send();
		}
	</script>
</head>
<body>
<table bgcolor="#006400" border="1">
	<tr>
		<td colspan="4">
			<h1 id="maintitle">Garden Planner</h1>
			<p>This page sends a SPARQL query to a data set model in persistent storage (TDB storage) and displays the results</p>
			<h3>Find a plant for your garden:</h3>
		    <a href="inference.jsp">Inference demo</a>
		</td>

	</tr>
	<tr>
		<form method="get" action="PlantRDFViewServlet">
			<td>
				<p>Minimum hardiness zone:</p>
				<select name="hardinessZone" id="hardinessZone">
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="8" selected>8</option>
				</select>
			</td>
			<td>
				<p>Sun preference:</p>
				<input type="radio" name="sunPref" value="part shade">part shade
				<input type="radio" name="sunPref" value="part sun" checked="checked">part sun
				<input type="radio" name="sunPref" value="full sun">full sun
			</td>
			<td>
				<p class="subheading">Harvest duration:</p>
				<select name="harvestDur" id="harvestDur">
					<option value="1">1</option>
					<option value="8" selected>8</option>
					<option value="13">13</option>
				</select> weeks
			</td>
			<td>
				<input type="button" name="find" id="find" class="btn" value="Search" onclick='loadXMLDoc()'>
			</td>
		</form>
	</tr>
	<tr>
		<td colspan="4">
		<div class="result" id="result" >
		</div>
		</td>
	</tr>
</table>
<p></p>
</body>
</html>