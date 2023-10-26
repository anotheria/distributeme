<%@ page contentType="text/html;charset=utf-8" session="true"
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
%><%@ page isELIgnored="false"
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="Head.jsp" flush="true">
    <jsp:param name="title" value="RegistryTreeView"/>
</jsp:include>
<body>
<jsp:include page="Header.jsp" flush="true"/>
<link href="../styles/treeview.css" type="text/css" rel="stylesheet"/>
<div class="content">
    <jsp:include page="Messages.jsp" flush="true"/>
    <div class="header-box">
        <h1>Registered nodes (${numberOfNodes}).</h1>
        <ano:iF test="${nodeParent eq 'instanceid'}">
            <a href="registryTreeView?nodeParent=host">Tree view by host</a>
        </ano:iF>
        <ano:iF test="${nodeParent eq 'host'}">
            <a href="registryTreeView?nodeParent=instanceid">Tree view by instanceId</a>
        </ano:iF>
    </div>

    <p>The list below contains all registered services grouped by nodes.</p>
    <div class="box">
            <ul id="myUL">
                <ano:iF test="${nodeParent eq 'instanceid'}">
                    <ano:iterate id="node" name="nodes">
                        <ano:iF test="${node.nodeDataList.size() == 1}"><li> <span class="caret-down caret">${node.title}</span>
                            <div>${node.nodeDataList.get(1).getServiceId()}</div>
                            <div>
                                <a class="unbind" title="unbind" href="unbind?id=${node.nodeDataList.get(1).getRegistrationString()}" onclick="return confirm('Are you sure you want to unbind ${node.nodeDataList.get(1).getServiceId()}?')"></a>
                                <a class="ping" title="ping" href="ping?id=${node.nodeDataList.get(1).getRegistrationString()}"></a>
                                <a class="shutdown" title="shutdown" href="shutdown?id=${node.nodeDataList.get(1).getRegistrationString()}" onclick="return confirm('Are you sure you want to shut down ${node.nodeDataList.get(1).getServiceId()}?')"></a>
                            </div>
                            </li>
                        </ano:iF>
                        <ano:iF test="${node.nodeDataList.size() > 1}">
                            <li><span class="caret-down caret">${node.title}</span>
                                <ul class="nested active">
                                    <ano:iterate id="service" name="node" property="nodeDataList"><li>
                                        <div>${service.getServiceId()}</div>
                                        <div>
                                            <a class="unbind" title="unbind" href="unbind?id=${service.getRegistrationString()}" onclick="return confirm('Are you sure you want to unbind ${service.getServiceId()}?')"></a>
                                            <a class="ping" title="ping" href="ping?id=${service.getRegistrationString()}"></a>
                                            <a class="shutdown" title="shutdown" href="shutdown?id=${service.getRegistrationString()}" onclick="return confirm('Are you sure you want to shut down ${service.getServiceId()}?')"></a>
                                        </div>
                                    </li></ano:iterate>
                                </ul>
                            </li>
                        </ano:iF>
                    </ano:iterate>
                </ano:iF>

                <ano:iF test="${nodeParent eq 'host'}">
                    <ano:iterate id="node" name="nodes">
                        <ano:iF test="${node.nodeDataList.size() == 1}"><li> <span class="caret-down caret">${node.title}</span>
                            <div>${node.nodeDataList.get(1).getServiceId()}</div>
                            <div>${node.nodeDataList.get(1).getInstanceId()}</div>
                            <div>${node.nodeDataList.get(1).getPort()}</div>
                            <div>
                                <a class="unbind" title="unbind" href="unbind?id=${node.nodeDataList.get(1).getRegistrationString()}" onclick="return confirm('Are you sure you want to unbind ${node.nodeDataList.get(1).getServiceId()}?')"></a>
                                <a class="ping" title="ping" href="ping?id=${node.nodeDataList.get(1).getRegistrationString()}"></a>
                                <a class="shutdown" title="shutdown" href="shutdown?id=${node.nodeDataList.get(1).getRegistrationString()}" onclick="return confirm('Are you sure you want to shut down ${node.nodeDataList.get(1).getServiceId()}?')"></a>
                            </div>
                        </li>
                        </ano:iF>
                        <ano:iF test="${node.nodeDataList.size() > 1}">
                            <li><span class="caret-down caret">${node.title}</span>
                                <ul class="nested active">
                                    <ano:iterate id="service" name="node" property="nodeDataList"><li>
                                        <div>${service.getServiceId()}</div>
                                        <div>${service.getInstanceId()}</div>
                                        <div>${service.getPort()}</div>
                                        <div>
                                            <a class="unbind" title="unbind" href="unbind?id=${service.getRegistrationString()}" onclick="return confirm('Are you sure you want to unbind ${service.getServiceId()}?')"></a>
                                            <a class="ping" title="ping" href="ping?id=${service.getRegistrationString()}"></a>
                                            <a class="shutdown" title="shutdown" href="shutdown?id=${service.getRegistrationString()}" onclick="return confirm('Are you sure you want to shut down ${service.getServiceId()}?')"></a>
                                        </div>
                                    </li></ano:iterate>
                                </ul>
                            </li>
                        </ano:iF>
                    </ano:iterate>
                </ano:iF>
            </ul>
    </div>
    <br>
    <a href="shutdownall" class="shutdown_all" onclick="return confirm('Are you REALLY REALLY REALLY sure you want to shut down the whole system?!')"><span>Shutdown all</span></a>
    <a href="pingall" class="ping_all"><span>Ping all</span></a>
    <a href="forceRegister" class="ping_all"><span>Force re-register</span></a>
    <br/><br/><br/>
    <a href="http://anotheria.net"><img src="../images/powered.png" alt=""/></a>
</div>
</body>
<script type="text/javascript" src="../js/carettoggler.js"></script>
</html>
