package edu.psu.sweng500.emrms.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.psu.sweng500.emrms.model.HAuditRecord;
import edu.psu.sweng500.emrms.service.AuditEventService;
import javax.servlet.http.HttpSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Methods for auditing the application.
 */
@Component
public class ApplicationAuditHelper {

	/** The log. */
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private ApplicationSessionHelper applicationSessionHelper;
	
    @Autowired
    private AuditEventService auditEventService;

	public void auditEvent(HttpSession session, String eventName, int policyId) {
		HAuditRecord auditRecord = new HAuditRecord();
        auditRecord.setEventName(eventName);
        auditRecord.setPolicyId(policyId);
        auditRecord.setUserId(applicationSessionHelper.getApplicationUser(session));
        auditRecord.setCreationDateTime(getCurrentDateTime());
        auditEventService.auditEvent(auditRecord);
	}
	
	public String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
	}
	
}