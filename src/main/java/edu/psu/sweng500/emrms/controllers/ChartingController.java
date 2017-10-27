package edu.psu.sweng500.emrms.controllers;

import edu.psu.sweng500.emrms.application.ApplicationAuditHelper;
import edu.psu.sweng500.emrms.application.ApplicationSessionHelper;
import edu.psu.sweng500.emrms.exceptions.PatientNotFoundException;
import edu.psu.sweng500.emrms.format.EMRMSCustomEditor;
import edu.psu.sweng500.emrms.model.HAllergy;
import edu.psu.sweng500.emrms.service.PatientDemographicsService;
import edu.psu.sweng500.emrms.service.PatientService;
import edu.psu.sweng500.emrms.validators.EMRMSBindingErrorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChartingController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDemographicsService patientDemographicsService;

    @Autowired
    private ApplicationAuditHelper applicationAuditHelper;


    @Autowired
    private ApplicationSessionHelper sessionHelper;

    private ModelAndView mav;
    private Integer patientId;

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

    @RequestMapping(value = "/charting", method = RequestMethod.GET)
    public ModelAndView showCharting(HttpServletRequest request, HttpServletResponse response) {
        mav = new ModelAndView("chartingTabShell");
        mav.addObject("siteHeader", sessionHelper.getSiteHeader());

        addAllergiesToMav();

        return mav;
    }

    private void addAllergiesToMav() {
        List<HAllergy> allergyList;

        try {
            int patientId = sessionHelper.getPatientId();
            allergyList = patientDemographicsService.getPatientAllergies(patientId);
        } catch (PatientNotFoundException e) {
            allergyList = new ArrayList<>();
        }

        mav.addObject("allergyList", allergyList);
    }
}