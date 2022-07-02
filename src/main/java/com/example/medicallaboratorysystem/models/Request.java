package com.example.medicallaboratorysystem.models;

public class Request {
    private String requestUID;
    private String patientUID;
    private String requestDate;
    private String requestTime;
    private String result;

    public Request(String requestUID, String patientUID, String requestDate, String requestTime, String result) {
        this.requestUID = requestUID;
        this.patientUID = patientUID;
        this.requestDate = requestDate;
        this.requestTime = requestTime;
        this.result = result;
    }

    public String getRequestUID() {
        return requestUID;
    }

    public void setRequestUID(String requestUID) {
        this.requestUID = requestUID;
    }

    public String getPatientUID() {
        return patientUID;
    }

    public void setPatientUID(String patientUID) {
        this.patientUID = patientUID;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
