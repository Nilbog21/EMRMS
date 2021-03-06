package edu.psu.sweng500.emrms.service;

import edu.psu.sweng500.emrms.model.HDiagnosis;

import java.util.List;

public interface ManageDiagnosisService {
    public int AddDiagnosis(HDiagnosis diagnosis);
    public int ReviseDiagnosis(HDiagnosis diagnosis) throws Exception;
    public int DeleteDiagnosis(HDiagnosis diagnosis) throws Exception;
    public List<HDiagnosis> GetDiagnosisList(int patientObjId);
}
