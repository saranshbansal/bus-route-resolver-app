package com.saransh.api;

/**
 * Route Object with departure Id, arrival Id and a direct route between the two (if any).
 * @author sbansal
 *
 */
public class Route {
	int depId;
	int arrId;
	boolean direct_bus_route;

	public Route(int depId, int arrId, boolean direct_bus_route) {
		super();
		this.depId = depId;
		this.arrId = arrId;
		this.direct_bus_route = direct_bus_route;
	}

	public int getDepId() {
		return depId;
	}

	public void setDepId(int depId) {
		this.depId = depId;
	}

	public int getArrId() {
		return arrId;
	}

	public void setArrId(int arrId) {
		this.arrId = arrId;
	}

	public boolean isDirect_bus_route() {
		return direct_bus_route;
	}

	public void setDirect_bus_route(boolean direct_bus_route) {
		this.direct_bus_route = direct_bus_route;
	}
}
