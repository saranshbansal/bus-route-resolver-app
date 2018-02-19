package com.saransh.api;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.LineIterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Route Resolver to find whether a direct route is available.
 * @author sbansal
 *
 */
@Component
public class RouteResolver {
	private static final Logger logger = LogManager.getLogger(RouteResolver.class);

	int[] stationIds;
	int directRouteId;
	int routeCount;

	/**
	 * @param fileData
	 * @param dep_sid
	 * @param arr_sid
	 * @return
	 * @throws IOException
	 */
	public Route resolveRoute(LineIterator fileData, int dep_sid, int arr_sid) throws IOException {
		return new Route(dep_sid, arr_sid, isDirectRouteAvailable(fileData, dep_sid, arr_sid));
	}

	/**
	 * Read file line by line and return result the moment it is found. This will
	 * avoid reading the entire file and performing data mapping operations (in case
	 * there is a direct route).
	 * 
	 * @param fileData
	 * @param dep_sid
	 * @param arr_sid
	 * @return
	 * @throws IOException
	 */
	private boolean isDirectRouteAvailable(LineIterator fileData, int dep_sid, int arr_sid) throws IOException {
		boolean directRouteFound = false;
		try {
			// Can't build a route in this case.
			if(dep_sid == arr_sid) return false;
			
			int count = 0;
			while (fileData.hasNext()) {
				if (count > 0) {
					stationIds = findStationsinRoute(fileData.nextLine());
					if (this.stationIds.length == 0) {
						return false;
					}
					directRouteFound = findDirectRoute(stationIds, dep_sid, arr_sid);
					if (directRouteFound) {
						logger.debug("Route [" + directRouteId + "] has a direct route that serves departure Id [" + dep_sid + "] and arrival Id [" + arr_sid + "]");
						return true;
					}
				} else {
					routeCount = Integer.parseInt(fileData.nextLine());
					logger.debug("Route Count [" + routeCount + "]");
					// Skip first element (route count) as it is not needed for the purpose.
					count++;
				}
			}
		} finally {
			fileData.close();
		}
		return directRouteFound;
	}

	/**
	 * As per the nature of data, skip first index (Route Id) and consider rest of
	 * the elements (station Ids) in the array. Use memory efficient Arrays.copyOf(...)
	 * operation which internally use System.arrayCopy(...) native code.
	 * 
	 * Note: Array copy operation could've been avoided but the search algo uses
	 * sorting, which would've caused us to loose track of the current route Id.
	 * 
	 * @param stationIds
	 * @return
	 */
	private int[] findStationsinRoute(String stationIds) {
		int arr[] = Arrays.stream(stationIds.split(" ")).mapToInt(Integer::parseInt).toArray();
		logger.debug("Searching route [" + arr[0] + "] for direct routes...");
		directRouteId = arr[0];
		return Arrays.copyOfRange(arr, 1, arr.length);
	}

	/**
	 * Use efficient binary search to find matching routes. Return results
	 * immediately instead of looping through entire sets of station Ids.
	 * 
	 * @param stationIds
	 * @param dep_sid
	 * @param arr_sid
	 * @return
	 */
	private boolean findDirectRoute(int[] stationIds, int dep_sid, int arr_sid) {
		Arrays.sort(stationIds);
		int depIdx = Arrays.binarySearch(stationIds, dep_sid);
		if (depIdx > -1) {
			int arrIdx = Arrays.binarySearch(stationIds, arr_sid);
			if (arrIdx > -1) {
				return true;
			}
		}
		return false;
	}
}
