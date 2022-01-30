package com.asc.yazy.core;

import java.io.Serializable;

public class AdultsCountObserver implements Serializable {

    boolean isEdited;
    private String name;
    private String nationality;
    private String passPortNumber;
    private String passPortEXDate;
    private String BirthDate;
    private String civilID;
    private boolean isCivil;
    private String id;


    public AdultsCountObserver() {
        this.name = "";
        this.nationality = "";
        this.passPortNumber = "";
        this.passPortEXDate = "";
        this.BirthDate = "";
        this.civilID = "";
        this.isCivil = false;
        this.isEdited = false;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassPortNumber() {
        return passPortNumber;
    }

    public void setPassPortNumber(String passPortNumber) {
        this.passPortNumber = passPortNumber;
    }

    public String getPassPortEXDate() {
        return passPortEXDate;
    }

    public void setPassPortEXDate(String passPortEXDate) {
        this.passPortEXDate = passPortEXDate;
    }

    public String getCivilID() {
        return civilID;
    }

    public void setCivilID(String civilID) {
        this.civilID = civilID;
    }

    public boolean isCivil() {
        return isCivil;
    }

    public void setCivil(boolean civil) {
        isCivil = civil;
    }


    public boolean isEmpty() {
        return name.trim().isEmpty() || passPortNumber.trim().isEmpty() && civilID.trim().isEmpty();
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }
}
