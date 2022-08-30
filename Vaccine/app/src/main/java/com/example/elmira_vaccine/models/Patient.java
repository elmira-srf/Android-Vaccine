package com.example.elmira_vaccine.models;

public class Patient {

    private String name;
    private String healthNumber;
    private boolean first_dose;
    private boolean second_dose;
    private boolean third_dose;
    private String nurse = "";
    private String id;

    public Patient(){
    }

    public Patient(String name, String healthCard, boolean dose1, boolean dose2, boolean dose3, String nurseName, String id) {
        this.name = name;
        this.healthNumber = healthCard;
        this.first_dose = dose1;
        this.second_dose = dose2;
        this.third_dose = dose3;
        this.nurse = nurseName;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealthCard() {
        return healthNumber;
    }

    public void setHealthCard(String healthCard) {
        this.healthNumber = healthCard;
    }

    public boolean isDose1() {
        return first_dose;
    }

    public void setDose1(boolean dose1) {
        this.first_dose = dose1;
    }

    public boolean isDose2() {
        return second_dose;
    }

    public void setDose2(boolean dose2) {
        this.second_dose = dose2;
    }

    public boolean isDose3() {
        return third_dose;
    }

    public void setDose3(boolean dose3) {
        this.third_dose = dose3;
    }

    public String getNurseName() {
        return nurse;
    }

    public void setNurseName(String nurseName) {
        this.nurse = nurseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", healthCard='" + healthNumber + '\'' +
                ", dose1=" + first_dose +
                ", dose2=" + second_dose +
                ", dose3=" + third_dose +
                ", nurseName='" + nurse + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

