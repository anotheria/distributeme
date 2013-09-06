<%@ page contentType="text/html;charset=utf-8" session="true" isELIgnored="false" 
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" 
%><div class="header">
<div class="logo"><img src="../images/logo.png" alt="DistributeMe Logo" valign="center"/>&nbsp;DistributeMe UI <%--<ano:write name="title"/>--%></div>
	<ul>
		<li <ano:equal name="section" value="registry">class="active"</ano:equal>><a href="registry">Service Registry</a></li>
		<li <ano:equal name="section" value="esregistry">class="active"</ano:equal>><a href="esregistry">Event Service Registry</a></li>
		<li <ano:equal name="section" value="cluster">class="active"</ano:equal>><a href="showcluster">Cluster</a></li>
		<li><a href="../mui/">MoSKito</a></li>
	</ul>
	<div class="tar">
		<div>Hello, Admin.<a href="#">Logout</a></div>
		<input type="text" placeholder="Search"/>
		<a href="#" class="button">Search</a>
	</div>
</div>
