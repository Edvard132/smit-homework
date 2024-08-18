# smit-homework

Backend (Go): https://github.com/Surmus/tire-change-workshop

# Backend Service

This service provides a REST API for viewing available tire change times in London and Manchester workshops.

## Technologies Used

- Java 21
- Spring Boot

## Requirements

- Java 21
- Maven

- ## Installation

To install and run the service, please follow these steps:

1. Clone the repository.
2. Navigate to `/tire-change-app`.
3. Run `TireChangeAppApplication` class.

The default port is 8080.

 ## Endpoints

The application exposes two endpoints:

### GET /api/v1/workshops/{workshopId}/availableTimes

The request has 3 paramaters:

- workshopId: workshop which tire change times are requested.
- from: tire change times that are after provided date.
- until: tire change times that are after provided date.

### POST /api/v1/workshops/{workshopId}/bookTime/{tireChangeTimeId}

The request has 2 paramaters and request body:

- workshopId: workshop.
- tireChangeTimeId: time id.

```json
{
"contactInformation": "john doe"
}
```

## Integration tests
Integration tests can be found in `src/test/java/com/smit/tire_change_app/enpoint` folder
# Frontend Service

Frontend application for the tire change workshop application service. It is built using React and communicates with the backend API to display available times and handle bookings.

## Technologies Used

- Node v20.11.0
- React 18.3.1

- ## Installation
To run the service
1. Navigate to `/tire-change-app-frontend`.
2. Run `npm install` and `npm start`.

The default port is 3000.

# End-to-end tests 
Test results and documentation: https://docs.google.com/spreadsheets/d/1G2q1h7D0urle2apf2o3ohDnblp8Z6OQTozx016Eid5k/edit?usp=sharing

## Technologies Used

- Node v20.11.0

- ## Installation
To run the service
1. Navigate to `/e2e-tests`.
2. Run `npm install` and `npx playwright test`.

Additional information can be found on https://playwright.dev/

