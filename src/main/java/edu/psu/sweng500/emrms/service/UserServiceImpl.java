package edu.psu.sweng500.emrms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.psu.sweng500.emrms.mappers.ApplicationMapper;
import edu.psu.sweng500.emrms.model.HPerson;

@Service("userService")
public class UserServiceImpl implements  UserService {
	
	@Autowired
	private ApplicationMapper applicationMapper;

	public HPerson getUserDetails() throws Exception {
		HPerson person = applicationMapper.getPersonDetails();
		return person;
	}
	
	public void insertUserDetails(HPerson person) {
		applicationMapper.insertPersonDetails(person);
	}
  
  
}
