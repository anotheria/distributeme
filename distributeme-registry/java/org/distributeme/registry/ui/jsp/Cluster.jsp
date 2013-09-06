<%@ page contentType="text/html;charset=utf-8" session="true" 
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" 
%><%@ page isELIgnored="false" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="Head.jsp" flush="true">
	<jsp:param name="title" value="Cluster"/>
</jsp:include>
<body>
<jsp:include page="Header.jsp" flush="true"/>
<div class="content">
<jsp:include page="Messages.jsp" flush="true"/>
	<h1>Cluster configuration.</h1>
	<p>The table below contains the configuration of the cluster.</p>
	<div class="box">
		<%--<h2>Some text tab 1</h2> --%>
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<thead>
			<tr>
			 	<th></th>
				<th>Host</th>
				<th>Port</th>
				<th>Id</th>
				<th>Status</th>
				<th>First seen</th>
				<th>Last seen</th>
				<th>Last checked</th>
			</tr>
			</thead>
			<tbody>
			<ano:iterate id="entry" name="entries">
				<tr>
					<td><img src="/distributeme/images/${entry.me? "me" : "notme"}.png" height="22" alt="${entry.me? "me" : "network server"}" title="${entry.me? "me" : "network server"}"></td>
					<td>${entry.host}</td>
					<td>${entry.port}</td>
					<td>${entry.identity}</td>
					<td><img src="/distributeme/images/${entry.online? "active" : "inactive"}.png" height="22" alt="${entry.me? "active" : "inactive"}" title="${entry.me? "active" : "inactive"}"></td>
					<td>${entry.firstSeenString}</td>
					<td>${entry.lastSeenString}</td>
					<td>${entry.lastCheckedString}</td>
				</tr>
			</ano:iterate>
			</tbody>
		</table>
	</div>
	<br>
	<br/><br/><br/>
	<a href="http://anotheria.net"><img src="../images/powered.png" alt=""/></a>
</div>
</body>
</html>
