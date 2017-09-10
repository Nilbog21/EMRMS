  Use EMRMS;

DELIMITER $$
  CREATE PROCEDURE Emrms_GetUserCensusList (IN UserObjectID INT)

  BEGIN

  /*DECLARE @staffid = Select huser.HPersonID from h_User huser left outer join h_staff hs on hs.StaffID = huser.HPersonID*/
  DECLARE staffid INT;
  SELECT hs.HStaffID into staffid from h_staff hs ;

  Select hn.LastName, hn.FirstName, hp.Birthdate, hp.Gender, hpat.MPINo, henc.EncStartdateTime, henc.encStatus
  from h_name hn INNER JOIN h_person hp on hn.HpersonID= hp.HPersonID
  INNER JOIN H_Patient hpat on hpat.HPatientID = hp.HPersonID
  Left OUTER JOIN h_encounter henc on henc.Patient_ObjectID = hpat.HPatientID
  Where henc.encStatus = 1 and henc.AttendingPhysician_ObjectID = staffid;

  END
  $$