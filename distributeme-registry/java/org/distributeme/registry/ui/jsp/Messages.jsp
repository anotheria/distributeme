<%@ page contentType="text/html;charset=utf-8" session="true" isELIgnored="false" 
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" 
%><ano:present name="messages" scope="request">
<h1>Messages</h1>
	<ano:iterate name="messages" type="java.lang.String" id="message">
		<p><ano:write name="message" filter="true"/>
	</ano:iterate>
</ano:present>