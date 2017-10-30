package edu.psu.sweng500.emrms.controllers;

import edu.psu.sweng500.emrms.application.ApplicationAuditHelper;
import edu.psu.sweng500.emrms.application.ApplicationSessionHelper;
import edu.psu.sweng500.emrms.format.EMRMSCustomEditor;
import edu.psu.sweng500.emrms.model.*;
import edu.psu.sweng500.emrms.service.CensusService;
import edu.psu.sweng500.emrms.service.PatientDemographicsService;
import edu.psu.sweng500.emrms.service.PatientService;
import edu.psu.sweng500.emrms.util.Constants;
import edu.psu.sweng500.emrms.util.FormatUtils;
import edu.psu.sweng500.emrms.validators.EMRMSBindingErrorProcessor;
import edu.psu.sweng500.emrms.validators.PatientValidator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

/**
 * @author vkumar
 * The Class PatientController.
 */
@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private CensusService censusService;

    @Autowired
    private PatientDemographicsService patientDemographicsService;

    @Autowired
    private ApplicationAuditHelper applicationAuditHelper;

    @Autowired
    private PatientValidator patientValidator;

    @Autowired
    private ApplicationSessionHelper sessionHelper;

    /**
     * Initialize data binder. Support MM/dd/yyyy dates.
     *
     * @param binder the binder to initialize
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setBindingErrorProcessor(new EMRMSBindingErrorProcessor());
        binder.registerCustomEditor(Integer.class, new EMRMSCustomEditor());
    }

    // I needed allergies, diagnoses, physician, and clinic to be separate attributes
    @ModelAttribute("severeAllergyList")
    public List<String> getSevereAllergyList() {
        return sessionHelper.getSevereAllergies();
    }

    @ModelAttribute("primaryDiagnosisList")
    public List<String> getPrimaryDiagnosisList() {
        return sessionHelper.getPrimaryDiagnoses();
    }

    @ModelAttribute("physicianName")
    public String getPhysicianName() {
        return sessionHelper.getPhysicianName();
    }

    @ModelAttribute("clinicName")
    public String getClinicName() {
        return sessionHelper.getClinicName();
    }

    @RequestMapping(value = "/patientLocatorProcess", params = "addPatient", method = RequestMethod.POST)
    public ModelAndView registerPatient(HttpServletRequest request, HttpServletResponse response) {
        applicationAuditHelper.auditEvent(request.getSession(false), "Patient Registration", 1);
        ModelAndView mav = new ModelAndView("patientRegistration");
        HPatient patient = new HPatient();

        mav.addObject("showHeader", true);
        mav.addObject("patient", patient);
        mav.addObject("siteHeader", new SiteHeader());

        return mav;
    }

    @RequestMapping(value = "/patientDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView patientDetails(HttpServletRequest request, @RequestParam(value = "hPatientID", required = false) Integer hPatientID) {
    	HttpSession session = request.getSession(false);
        applicationAuditHelper.auditEvent(session, "Patient Details", 1);
        ModelAndView mav = new ModelAndView("patientDetails");
        HPatient patient = null;

        if (hPatientID != null) {
            patient = getPatientDetails(hPatientID);
            sessionHelper.setActivePatient(hPatientID);
            session.setAttribute(Constants.HPATIENT_ID, hPatientID);
        }

        mav.addObject("showHeader", true);
        mav.addObject("patient", patient);
        mav.addObject("siteHeader", sessionHelper.getSiteHeader());

        return mav;
    }

    @RequestMapping(value = "/patientLocator", method = RequestMethod.GET)
    public ModelAndView showPatientLocator(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("patientLocator");
        mav.addObject("census", new HCensus());
        mav.addObject("showHeader", false);
        mav.addObject("siteHeader", sessionHelper.getSiteHeader());

        return mav;
    }

    @RequestMapping(value = "/patientLocatorProcess", params = "findPatient", method = RequestMethod.POST)
    public ModelAndView findPatient(HttpServletRequest request, HttpServletResponse response,
                                    @ModelAttribute("census") HCensus patient) {
        ModelAndView mav = null;
        HttpSession session = request.getSession(false);

        mav = new ModelAndView("patientLocator");

        List<HCensus> hPatientList = censusService.getPatientListByDemographics(patient.getLastName(), patient.getFirstName(), patient.getGender());

        if (CollectionUtils.isNotEmpty(hPatientList)) {
            mav.addObject("hPatientList", hPatientList);
            sessionHelper.setActivePatient(hPatientList.get(0).gethPatientID());
        }
        applicationAuditHelper.auditEvent(session, "Patient Locator", 4);

        mav.addObject("showHeader", false);
        mav.addObject("siteHeader", sessionHelper.getSiteHeader());

        return mav;
    }

    @RequestMapping(value = "/addPatient", method = RequestMethod.POST)
    public ModelAndView addPatient(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("patient") HPatient patient, BindingResult bindingResult) throws Exception {
        ModelAndView mav = new ModelAndView("patientRegistration");
        HttpSession session = request.getSession(false);
        List<String> validationErrors = patientValidator.validate(patient);

        if (CollectionUtils.isNotEmpty(validationErrors)) {
            mav.addObject("errorOnPage", true);
            mav.addObject("validationErrors", validationErrors);
            mav.addObject("siteHeader", new SiteHeader());
            return mav;
        }

        HPatientId newMrNumberId = new HPatientId();
        int thisPatientId = patient.getObjectID();
        newMrNumberId.setPatientId(thisPatientId);
        newMrNumberId.setIdIssuerId(sessionHelper.getApplicationUserId(request.getSession()));
        newMrNumberId.setIdIssuerName(sessionHelper.getClinicName());
        newMrNumberId.setUserId(sessionHelper.getApplicationUser(request.getSession()));
        newMrNumberId.setCreationDateTime(Date.from(Instant.now()).toString());

        String mrNumber = "MRN";
        newMrNumberId.setIdType(mrNumber);
        if (thisPatientId > 100) {
            mrNumber += "0";
        }

        if (thisPatientId > 10) {
            mrNumber += "0";
        }
        mrNumber += String.valueOf(thisPatientId);
        newMrNumberId.setIdValue(mrNumber);
        patient.setPatientIds(newMrNumberId);

        if(patient.getObjectID() == 0) {
        	patientService.registerPatient(patient);
        } else {
        	patientService.updatePatient(patient);
        	mav = new ModelAndView("patientDetails");      	
        }       	
        
        mav.addObject("saveSuccess", true);
        session.setAttribute(Constants.HPATIENT_ID, patient.getObjectID());

        int personId = patientDemographicsService.getPersonId(patient.getObjectID());
        HPatient patientFromDB = patientDemographicsService.getPatientDemographics(patient.getObjectID());
        HName nameFromDB = patientDemographicsService.getPersonName(personId);
        Address addressFromDB = patientDemographicsService.getPersonAddress(personId);
        List<HEncounter> encounters = patientDemographicsService.getPatientEncounters(patient.getObjectID());
        List<HPatientId> patientIds = patientDemographicsService.getPatientIdentifiers(patient.getObjectID());

        mav.addObject("person", patientFromDB);
        mav.addObject("patientname", nameFromDB);
        mav.addObject("patientaddress", addressFromDB);
        mav.addObject("patientids", patientIds);
        mav.addObject("encounters", encounters);

        sessionHelper.setActivePatient(patientFromDB.getObjectID());
        mav.addObject("showHeader", true);
        mav.addObject("siteHeader", sessionHelper.getSiteHeader());

        return mav;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setCensusService(CensusService censusService) {
        this.censusService = censusService;
    }

    public void setPatientDemographicsService(PatientDemographicsService patientDemographicsService) {
        this.patientDemographicsService = patientDemographicsService;
    }

    public void setApplicationAuditHelper(ApplicationAuditHelper applicationAuditHelper) {
        this.applicationAuditHelper = applicationAuditHelper;
    }

    private HPatient getPatientDetails(Integer hPatientID) {
        int personId = patientDemographicsService.getPersonId(hPatientID);
        HPatient patient = patientDemographicsService.getPatientDemographics(hPatientID);
        HPerson person = patientDemographicsService.getPersonDetails(personId);
        HName patientName = patientDemographicsService.getPersonName(personId);
        Address patientAddress = patientDemographicsService.getPersonAddress(personId);
        Phone homePhone = patientDemographicsService.getHomePhone(personId);
        Phone cellPhone = patientDemographicsService.getCellPhone(personId);
        String email = patientDemographicsService.getEmail(personId);

        if (patient != null) {
            ComplexName name = new ComplexName();
            name.setFirst(patientName.getFirstName());
            name.setLast(patientName.getLastName());
            name.setMiddle(patientName.getMiddleName());
            patient.setName(name);
            Address address = new Address();
            address.setLine1(patientAddress.getLine1());
            address.setLine2(patientAddress.getLine2());
            address.setCity(patientAddress.getCity());
            address.setState(patientAddress.getState());
            address.setZip(patientAddress.getZip());
            patient.setAddress(address);
            patient.setHomePhone(homePhone);
            patient.setCellPhone(cellPhone);
            patient.setEmail(email);
            try {
                patient.setBirthDate(FormatUtils.formatDate(person.getBirthDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            patient = new HPatient();
        }

        return patient;
    }

}
