package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/samplecontroller")
@RestController
public class SampleController {

	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/sampleapi", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getStaffingColumnListForGlobers(HttpServletRequest request) {
		HttpStatus statusCode = HttpStatus.OK;
		try {
			
		}
		catch(Exception e) {
			LOGGER.error("Exception in getStaffingColumnListForGlobers: ",e);
		}
		LOGGER.info("Exit from getStaffingColumnListForGlobers: "+null);
		return new ResponseEntity<Object>(null, statusCode);
	}
}
