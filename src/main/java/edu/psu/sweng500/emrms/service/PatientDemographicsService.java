package edu.psu.sweng500.emrms.service;

import edu.psu.sweng500.emrms.model.*;

import java.util.List;

public interface PatientDemographicsService {

    public int getPersonId(int patientObjectId);

    public int getNextPatientObjectID();

    public HPatient getPatientDemographics(int patientObjectId);

    public HPerson getPersonDetails(int patientObjectId);

    public HName getPersonName(int personId);

    public Address getPersonAddress(int personId);
    
    public Phone getHomePhone(int personId);
    
    public Phone getCellPhone(int personId);
    
    public String getEmail(int personId);

    public List<HPatientId> getPatientIdentifiers(int patientObjectId);

    public List<HEncounter> getPatientEncounters(int patientObjectId);

    public List<HAllergy> getPatientAllergies(int patientObjectId);

    public List<HDiagnosis> getPatientDiagnoses(int patientObjectId);

}
