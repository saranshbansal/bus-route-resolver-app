package com.saransh.controller;

import org.apache.commons.io.LineIterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saransh.api.FileReader;
import com.saransh.api.Route;
import com.saransh.api.RouteResolver;

/**
 * A micro service which is able to answer whether there is a bus route
 * providing a direct connection between two given stations.
 * 
 * @author sbansal
 *
 */
@RestController("/api/direct")
public class RouteController {
	private static final Logger logger = LogManager.getLogger(RouteController.class);

	@Autowired
	FileReader fileReader;

	@Autowired
	RouteResolver routeResolver;

	/**
	 * Steps:
	 * 1. Read file and return file cursor for iteration.
	 * 2. Find direct route (if any).
	 * 3. Return results.
	 * 
	 * @param dep_sid
	 * @param arr_sid
	 * @return result
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> hasDirectConnection(@RequestParam(required = true) int dep_sid,
			@RequestParam(required = true) int arr_sid) {
		logger.debug("Finding direct routes between departure ID: " + dep_sid + " and Arrival ID: " + arr_sid);
		Route route = null;
		try {
			// 1. Read file and return file cursor for iteration.
			LineIterator fileData = fileReader.readFile();
			// 2. Find direct route (if any).
			route = routeResolver.resolveRoute(fileData, dep_sid, arr_sid);
		} catch (Exception e) {
			logger.error("Exception while finding direct route :: " + e, e);
			return new ResponseEntity<>("API failure: Server cannot resolve your request.", HttpStatus.NOT_FOUND);
		}
		// 3. Return results.
		Gson gson = new Gson();
		return new ResponseEntity<>(gson.toJson(route), HttpStatus.OK);
	}
}
