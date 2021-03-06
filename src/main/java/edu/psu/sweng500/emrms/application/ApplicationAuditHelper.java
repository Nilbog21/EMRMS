package edu.psu.sweng500.emrms.application;

import edu.psu.sweng500.emrms.model.HAuditRecord;
import edu.psu.sweng500.emrms.service.AuditEventService;
import edu.psu.sweng500.emrms.service.AuditEventServiceImpl;
import edu.psu.sweng500.emrms.util.PersonPatientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Methods for auditing the application.
 */
@Component
public class ApplicationAuditHelper {

    /**
     * The log.
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationSessionHelper applicationSessionHelper;

    @Autowired
    private AuditEventServiceImpl auditEventService;

    @Autowired
    private PersonPatientUtils personPatientUtils;

    public void auditEvent(HttpSession session, String eventName, int policyId, int patientObjectID, int encounterObjectID) {
        HAuditRecord auditRecord = new HAuditRecord();
        auditRecord.setEventName(eventName);
        auditRecord.setPolicyId(policyId);
        auditRecord.setUserId(applicationSessionHelper.getPhysicianName());
        auditRecord.setCreationDateTime(getCurrentDateTime());
        auditRecord.setPatient_ObjectID(patientObjectID);
        if(patientObjectID != 0)
        {
            auditRecord.setPatientName(personPatientUtils.getPatientName(patientObjectID));
        }
        auditRecord.setEncounter_ObjectID(encounterObjectID);
        auditEventService.auditEvent(auditRecord);
    }

    public String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void setApplicationSessionHelper(ApplicationSessionHelper applicationSessionHelper) {
        this.applicationSessionHelper = applicationSessionHelper;
    }

    public void setAuditEventService(AuditEventServiceImpl auditEventService) {
        this.auditEventService = auditEventService;
    }
}
