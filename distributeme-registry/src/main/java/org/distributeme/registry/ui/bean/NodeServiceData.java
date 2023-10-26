package org.distributeme.registry.ui.bean;

public class NodeServiceData {

    private String serviceId;
    private String registrationString;
    private String instanceId;
    private int port;

    public NodeServiceData(String serviceId, String registrationString) {
        this.serviceId = serviceId;
        this.registrationString = registrationString;
    }

    public NodeServiceData(String serviceId, String registrationString, String instanceId, int port) {
        this.serviceId = serviceId;
        this.registrationString = registrationString;
        this.instanceId = instanceId;
        this.port = port;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRegistrationString() {
        return registrationString;
    }

    public void setRegistrationString(String registrationString) {
        this.registrationString = registrationString;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "NodeServiceData{" +
                "serviceId='" + serviceId + '\'' +
                ", registrationString='" + registrationString + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", port=" + port +
                '}';
    }
}
