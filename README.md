# Cab-booking

## How to run
* Run following command in the system
    * >mvn clean install 
    * >java -jar target/captain-fresh-assignment-0.0.1-SNAPSHOT.jar
  

### API's
* There are 5 API that has been exposed
    * Add User API
        * Sample Request
            > curl --location --request PUT 'localhost:8080/api/v1/add-user' \
            --header 'Accept: application/json' \
            --header 'Content-Type: application/json' \
            --header 'Authorization: Basic a2JfdGVzdDpraWxsYmlsbF90ZXN0' \
            --data-raw '{
                "name":"Harvi",
                "age":26,
                "gender":"MALE"
            }'
        * Sample Response
            > {
                  "message": "User has been added"
              }
            * statusCode=201
            
        * Error response
            * >{
                   "message": "User with given name already existing"
               }
            * statusCode=405
            
    * Add vehicle API
        * Sample Request
            > curl --location --request PUT 'localhost:8080/api/v1/add-vehicle' \
              --header 'Authorization: Basic OnBhc3N3b3Jk' \
              --header 'Content-Type: application/json' \
              --data-raw '{
                  "userName" : "Atul",
                  "cabCompnany" : "BMW",
                  "vehicleNumber" : ["KA01-1111","KA01-1112", "KA01-1113"]
              }'
        * Sample Response
            > {
                  "message": "Vehicle has been added"
              }
        * Error Response
            > {
                  "message": "One or more vehicle already exsited in system , No vehicle has been added"
              }
            * statusCode : 405: OperationNotAllowed
    
    * Offer Ride
        * An API to offer-ride if cab is already in booking state will return an error mesage in repsonse
            * Sample request
            > curl --location --request POST 'http://localhost:8080/api/v1/offer-ride' \
              --header 'Content-Type: application/json' \
              --data-raw '{
                  "vehicleNumber" : "KA01-1113",
                  "origin" :"goa",
                  "destination":"delhi",
                  "userName":"sonveer3",
                  "seatCount":12
              }'
            
            * Sample response OK-
                > {
                      "message": "Status has been updated to offered"
                  }
                * statusCode = 201

            * Error response 
                > {
                      "message": "Vehicle is already in booking/offered status"
                  }   
                * statusCode = 405
                
    * Select Ride
        * API to select the booking 
            * Sample Request 
            > curl --location --request POST 'http://localhost:8080/api/v1/select-ride' \
              --header 'Content-Type: application/json' \
              --data-raw '{
                  "preferredCat":"MOST_VACANT",
                  "origin":"goa",
                  "destination":"delhi",
                  "userName":"sitaRam3",
                  "seatCount":2
              }'
            * Sample Response                                                           
                * statusCode = 201
                > {
                      "message": "Bingo :)First Vehicle has been booked for your request",
                      "selectedVehicleInfoList": [
                          {
                              "vehicleNumber": "KA01-1113",
                              "vehicleCompany": "BMW",
                              "riderName": "KA01-1113",
                              "numberOfSeats": 12
                          }
                      ]
                  }
            * No booking Response   
                > {
                      "message": "OOPS! No vehicle avaible for the locations",
                      "selectedVehicleInfoList": []
                  }                                                                                                         
            * Error Response  
                > {
                      "message": "Resource already booked/offered state can't offerRide",
                      "selectedVehicleInfoList": null
                  }
                * statusCode : 400
        
    * End-Ride
        * This API will end the active ride and remove the offered state of the cab/Vehicle, So If we want to book again then we have to call offered API again
            * Sample Request
                > curl --location --request DELETE 'http://localhost:8080/api/v1/end-ride/sitaRam3'
            * Sample Response
                > {
                      "message": "have ended the ride"
                  }
                * statusCode : 202
                
            * Another response
                > {
                      "message": "No booking assigned to user"
                  }
                * statusCode : 405
    
    * getStatsCount
        * This API will return the status of all user i.e. booking/offered count per user
            * Sample request
                > curl --location --request GET 'http://localhost:8080/api/v1/ride-stats'
            
            * Sample Response
                > {
                      "Atul": {
                          "offeredRide": 0,
                          "rideTaken": 0
                      },
                      "Harvi": {
                          "offeredRide": 0,
                          "rideTaken": 0
                      },
                      "Swapnil": {
                          "offeredRide": 0,
                          "rideTaken": 0
                      }
                  }
                * statusCode =200
                
                
                
### Test Coverage
    * Approx 80%   

### Assumption
    * vehicle should registred before booking/offered ride.   
    * A user can booking one ride at a time
    * Driver/Rider can same name but vehicle number will be diffrent
    * endRide will end activebooking and offered status as well, So for that vehicle booking we have to call offer-booking API again                                                                                    
                           
    