package edu.psu.sweng500.emrms.model;

public class HName {
    private long ObjectID;
    private String userId;
    private String creationDateTime;
    private String lastName;
    private String firstName;
    private Boolean active;
    private String middleName;
    private String title;
    private String genQualifier;
    private int hPersonId;

    public long getObjectID() {
        return ObjectID;
    }

    public void setObjectID(long objectID) {
        ObjectID = objectID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenQualifier() {
        return genQualifier;
    }

    public void setGenQualifier(String genQualifier) {
        this.genQualifier = genQualifier;
    }

    public int getHPersonId() {
        return hPersonId;
    }

    public void setHPersonId(int hPersonId) {
        this.hPersonId = hPersonId;
    }

    public static String getFirstMiddleInitialLastTitle(HName name) {
        String returnValue = name.getFirstName();

        String middle = name.getMiddleName();
        if (middle != null && !middle.isEmpty()) {
            returnValue += " " + middle.charAt(0);
        }

        returnValue += " " + name.getLastName();

        String title = name.getTitle();
        if (title != null && !title.isEmpty()) {
            returnValue += " " + title;
        }

        return returnValue;
    }

    public static String getLastCommaFirstMiddleInitial(HName name) {
        String returnValue = name.getLastName() + ", " + name.getFirstName();

        String middleName = name.getMiddleName();
        if (middleName != null && !middleName.isEmpty()) {
            returnValue += " " + middleName.charAt(0);
        }

        return returnValue;
    }
}
