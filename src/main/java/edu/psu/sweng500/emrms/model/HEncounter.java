package edu.psu.sweng500.emrms.model;

public class HEncounter {

    private int hEncounterID;
    private String userID;
    private String creationDateTime;
    private String encStartDateTime;
    private String encEndDateTime;
    private int encStatus;
    private String encLocationName;
    private int encounterLocation_ObjectID;
    private String encounterID;
    private String encounterType;
    private String bedName;
    private int patient_ObjectID;

    public int getPatient_ObjectID() {
        return patient_ObjectID;
    }

    public void setPatient_ObjectID(int patient_ObjectID) {
        this.patient_ObjectID = patient_ObjectID;
    }

    public int getAttendingPhysician_ObjectID() {
        return attendingPhysician_ObjectID;
    }

    public void setAttendingPhysician_ObjectID(int attendingPhysician_ObjectID) {
        this.attendingPhysician_ObjectID = attendingPhysician_ObjectID;
    }

    public int getBed_ObjectID() {
        return bed_ObjectID;
    }

    public void setBed_ObjectID(int bed_ObjectID) {
        this.bed_ObjectID = bed_ObjectID;
    }

    private int attendingPhysician_ObjectID;
    private int bed_ObjectID;

    public int gethEncounterID() {
        return hEncounterID;
    }

    public void sethEncounterID(int hEncounterID) {
        this.hEncounterID = hEncounterID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getEncStartDateTime() {
        return encStartDateTime;
    }

    public void setEncStartDateTime(String encStartDateTime) {
        this.encStartDateTime = encStartDateTime;
    }

    public String getEncEndDateTime() {
        return encEndDateTime;
    }

    public void setEncEndDateTime(String encEndDateTime) {
        this.encEndDateTime = encEndDateTime;
    }

    public int getEncStatus() {
        return encStatus;
    }

    public void setEncStatus(int encStatus) {
        this.encStatus = encStatus;
    }

    public String getEncLocationName() {
        return encLocationName;
    }

    public void setEncLocationName(String encLocationName) {
        this.encLocationName = encLocationName;
    }

    public int getEncounterLocation_ObjectID() {
        return encounterLocation_ObjectID;
    }

    public void setEncounterLocation_ObjectID(int encounterLocation_ObjectID) {
        this.encounterLocation_ObjectID = encounterLocation_ObjectID;
    }

    public String getEncounterID() {
        return encounterID;
    }

    public void setEncounterID(String encounterID) {
        this.encounterID = encounterID;
    }

    public String getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

}
