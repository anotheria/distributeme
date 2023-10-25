package org.distributeme.registry.ui.bean;

public class NodeServiceData {

    private String serviceId;
    private String registrationString;

    public NodeServiceData(String serviceId, String registrationString) {
        this.serviceId = serviceId;
        this.registrationString = registrationString;
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

    @Override
    public String toString() {
        return "NodeServiceData{" +
                "serviceId='" + serviceId + '\'' +
                ", registrationString='" + registrationString + '\'' +
                '}';
    }
}
