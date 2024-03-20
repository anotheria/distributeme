<%@ page contentType="text/html;charset=utf-8" session="true" 
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" 
%><%@ page isELIgnored="false" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="Head.jsp" flush="true">
	<jsp:param name="title" value="Registry"/>
</jsp:include>
<body>
<jsp:include page="Header.jsp" flush="true"/>
<div class="content">
<jsp:include page="Messages.jsp" flush="true"/>
	<h1>Registered services (${numberOfBindings}).</h1>
	<p>The table below contains all registered services.</p>
<%--
	<ul class="tabs">
		<li class="active"><a href="#">Some text 1</a></li>
		<li><a href="#">Some text 2</a></li>
		<li><a href="#">Some text 3</a></li>
	</ul>
	<a href="#" class="button">Do something</a>
	<a href="#" class="button flr">Export</a>
--%>	
	<div class="box">
		<%--<h2>Some text tab 1</h2> --%>
		<table class="sortable" cellpadding="0" cellspacing="0" border="0" width="100%">
			<thead>
			<tr>
				<th>Service ID</th>
				<th>Host</th>
				<th>Port</th>
				<th class="sorttable_nosort">Protocol</th>
				<th>Instance ID</th>
				<th class="sorttable_nosort">Global service ID</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			</tr>
			</thead>
			<tbody>
			<ano:iterate id="binding" name="bindings">
				<tr>
					<td>${binding.serviceId}</td>
					<td>${binding.host}</td>
					<td>${binding.port}</td>
					<td>${binding.protocol}</td>
					<td>${binding.instanceId}</td>
					<td>${binding.globalServiceId}</td>
					<td><a class="unbind" title="unbind" href="unbind?id=${binding.registrationString}" onclick="return confirm('Are you sure you want to unbind ${binding.serviceId}?')"></a></td>
					<td><a class="ping" title="ping" href="ping?id=${binding.registrationString}"></a></td>
					<td><a class="shutdown" title="shutdown" href="shutdown?id=${binding.registrationString}" onclick="return confirm('Are you sure you want to shut down ${binding.serviceId}?')"></a></td>
				</tr>
			</ano:iterate>
			</tbody>
		</table>
	</div>
	<br>
	<a href="shutdownall" class="shutdown_all" onclick="return confirm('Are you REALLY REALLY REALLY sure you want to shut down the whole system?!')"><span>Shutdown all</span></a>
	<a href="pingall" class="ping_all"><span>Ping all</span></a>
    <a href="forceRegister" class="ping_all"><span>Force re-register</span></a>
	<br/><br/><br/>
	<a href="http://anotheria.net"><img src="../images/powered.png" alt=""/></a>
</div>
</body>
<script type="text/javascript" src="../js/sorttable.js"></script>
</html>
