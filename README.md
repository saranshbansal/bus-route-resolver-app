# bus-route-problem
The bus route data file provided by the bus provider contains a list of bus routes. These routes consist of an unique identifier and a list of stations (also just unique identifiers). A bus route connects its list of stations.  This micro service is able to answer whether there is a bus route providing a direct connection between two given stations.

#### Installation
1. Clone this repo `git clone https://github.com/saranshbansal/bus-route-problem.git`. This will add the required folder/files on your local machine.

2. Run `mvn spring-boot:run` inside of bus-route-problem root directory to install project dependencies and build the application.
This will also start up your local environment and opens a new tab at http://localhost:8080.

3. You can then hit the micro-service to test the application:
`localhost:8080/api/direct?dep_sid=<departure_id>&arr_sid=<arrival_id>`