<%@ page contentType="text/html;charset=utf-8" session="true" 
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" 
%><%@ page isELIgnored="false" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="Head.jsp" flush="true">
	<jsp:param name="title" value="EventService Registry"/>
</jsp:include>
<body>
<jsp:include page="Header.jsp" flush="true"/>
<div class="content">
	<h1>Existing event channels (${numberOfChannels}).</h1>
	<p>Click on a tab to get a list of consumers and supplier for that channel.</p>
	<ul class="tabs">
		<ano:iterate name="channelnames" type="java.lang.String" id="channel"
			><li <ano:equal name="channel" name2="selectedname" value="">class="active"</ano:equal>><a href="eschannel?channelname=<ano:write name="channel" filter="true"/>"><ano:write name="channel" filter="true"/></a></li
		></ano:iterate>
	</ul>
<%--
	<ul class="tabs">
		<li class="active"><a href="#">Some text 1</a></li>
		<li><a href="#">Some text 2</a></li>
		<li><a href="#">Some text 3</a></li>
	</ul>
	<a href="#" class="button">Do something</a>
	<a href="#" class="button flr">Export</a>
	
--%>
	<div class="clear"></div>
	<div class="box">
		<ano:present name="suppliers">
			<h2>Suppliers</h2>
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<thead>
				<tr>
					<th>Protocol</th>
					<th>Host</th>
					<th>Port</th>
					<th>InstanceId</th>
					<th>Global serviceId</th>
					<th>&nbsp;</th>
				</tr> 
				</thead>
				<tbody>
				<ano:iterate id="service" name="suppliers" type="org.distributeme.core.ServiceDescriptor">
					<tr>
						<td><ano:write name="service" property="protocol"/></td>
						<td><ano:write name="service" property="host"/></td>
						<td><ano:write name="service" property="port"/></td>
						<td><ano:write name="service" property="instanceId"/></td>
						<td><ano:write name="service" property="registrationString"/></td>
						<td><a class="delete" href="removeSupplier?channel=<ano:write name="selectedname" filter="true"/>&id=<ano:write name="service" property="registrationString"/>" title="Remove"></a></td>
					</tr>
				</ano:iterate>
				</tbody>
			</table>
		</ano:present>
		<ano:present name="consumers">
		<h2>Consumers</h2>
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<thead>
			<tr>
				<th>Protocol</th>
				<th>Host</th>
				<th>Port</th>
				<th>InstanceId</th>
				<th>Global serviceId</th>
				<th>&nbsp;</th>
			</tr> 
			</thead>
			<tbody>
			<ano:iterate id="service" name="consumers" type="org.distributeme.core.ServiceDescriptor">
				<tr>
					<td><ano:write name="service" property="protocol"/></td>
					<td><ano:write name="service" property="host"/></td>
					<td><ano:write name="service" property="port"/></td>
					<td><ano:write name="service" property="instanceId"/></td>
					<td><ano:write name="service" property="registrationString"/></td>
					<td><a class="delete" href="removeConsumer?channel=<ano:write name="selectedname" filter="true"/>&id=<ano:write name="service" property="registrationString"/>" title="Remove"></a></td>
				</tr>
			</ano:iterate>
			</tbody>
		</table>
	</ano:present>
	</div>
	<a href="http://anotheria.net"><img src="../images/powered.png" alt=""/></a>
</div>
</body>
</html>
