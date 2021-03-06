package edu.psu.sweng500.emrms.mappers;

import edu.psu.sweng500.emrms.model.*;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface PatientDemographicsMapper {

    @Select("SELECT HPERSONID as personId, USERID as userId, "
            + "CREATIONDATETIME as creationDateTime, GENDER as gender, "
            + "BIRTHDATE as birthDate, RACE as race FROM h_person"
            + " where HPERSONID = #{personId}")
    public HPerson getPersonDetails(int personId);

    @Select("SELECT (MAX(HPatientID)+1) As ObjectID FROM h_patient;")
    Integer  getNextPatientObjectID();

    @Select("SELECT HNameID as ObjectID, USERID as userId, "
            + "CREATIONDATETIME as creationDateTime, FirstName as firstName, LastName as lastName, MiddleName as middleName, "
            + "Active as active, HPersonID as hpersonId , Title as title, GenQualifier as genQualifier FROM h_name"
            + " where HPersonID = #{personId}")
    public HName getPersonName(int personId);

    @Select("SELECT HAddressID as addressId,  "
            + "StrAddress as line1, City as city, Zip as zip, State as state, "
            + "Country as country, AddressType as addressType FROM h_address"
            + " where HPersonID = #{personId}")
    public Address getPersonAddress(int personId);
    
    @Select("SELECT HomePhoneNo as number FROM h_address where HPersonID = #{personId}")
    public Phone getHomePhone(int personId);
    
    @Select("SELECT CellPhoneNo as number FROM h_address where HPersonID = #{personId}")
    public Phone getCellPhone(int personId);
    
    @Select("SELECT EmailAddress FROM h_address where HPersonID = #{personId}")
    public String getEmail(int personId);

    @Select("SELECT HPatientId as hPatientId,  "
            + "IDValue as idValue, IdType as idType, IdIssuerName as idIssuerName, IdIssuerID as idIssuerId, "
            + "PatientID as patientId FROM h_patient_ids "
            + " where PatientID = #{patientId} and IdType = 'MRN'")
    public List<HPatientId> getPatientIdentifiers(int patientId);

    @Select("SELECT HPatientID as ObjectID,  "
            + "MPINo as mPINumber, OrganDonor as organDonor, DeceasedInd as deceasedIndicator, IsPatientUnidentified as isPatientUnidentified, "
            + "PrimaryLang as primarylang, MedHistoryConcent as medHistoryConsent, HPersonID as personId FROM h_patient"
            + " where HPatientID = #{patientId}")
    public HPatient getPatientDetails(int patientId);

    @Select("SELECT HEncounterID as hEncounterID, EncStartDateTime as encStartDateTime, ENCEndDateTime as encEndDateTime,  "
            + "ENCStatus as encStatus, EncLocationName as encLocationName, EncounterLocation_ObjectID as encounterLocation_ObjectID,"
            + "EncounterID as encounterID, EncType as encounterType, BedName as bedName, Patient_ObjectID as patient_ObjectID, "
            + "AttendingPhysician_ObjectID as attendingPhysician_ObjectID, Bed_ObjectID as bed_ObjectID, EncounterReason as encounterReason FROM h_encounter"
            + " where Patient_ObjectID = #{patientId} order by EncStartDateTime desc")
    public List<HEncounter> getPatientEncounters(int patientId);

    @Select("SELECT HAllergyID as allergyID,  "
            + "AllergyName as allergyName, AllergyCode as allergyCode, AllergyType as allergyType, Severity as severity, "
            + "PatientID as patientId FROM h_allergy "
            + " where PatientID = #{patientId} order by severity desc")
    public List<HAllergy> getPatientAllergies(int patientId);

    @Select("SELECT HDiagnosisID as diagnosisObjectId,  "
            + "Code as code, Description as description, Priority as priority, EncounterID as encounterID, PatientID as patientID "
            + "FROM h_diagnosis where PatientID = #{patientId} order by priority asc ")
    public List<HDiagnosis> getPatientDiagnoses(int patientId);
}
