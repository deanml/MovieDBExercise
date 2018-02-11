package com.example.integration;

import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

/**
 * Created by Marvin Dean on 2/12/18.
 *
 * This class has a test which will walk through the auth mechanism, grabbing sessionid so that I can hit endpoints that require session auth.  All classes that
 * inherit from this base class will have access to the the baseURI and BasePath as well as the sessionid.  This class can also
 * be used to force inheritance for testNG related annotations and setup (like the @beforeClass annotation) leaving the test classes as POJO's for
 * better readability.
 *
 * I debated using a base test class but for the sake of demonstration, decided to put it in.  Using base classes can sometimes
 * lead to dicey results in TestNG, especially when you start running in parallel or use child annotations that may overwrite
 * parent class annotations.  But for this activity I wanted to show test execution dependencies and inheritance as setup in
 * the AccountTests and MovieTests classes.  Please see comments in those classes as well for an explanation.
 *
 * One of the things I really like with restassured is the flow of the tests and BDD style syntax for testing complex flows, including
 * using output of one call as input to another call which is what I am doing below to end up with the sessionid.
 **/

public class IntegrationTestBase {

    protected String requestToken;
    protected String sessionId;
    protected String apiKey;

    @BeforeClass
    @Parameters ({ "APIKey", "BaseURI", "BasePath" })
    public void setUp(@Optional("8cc3fa11bd251c224e4df93cfdf378c0") String APIKey,
                      String BaseURI,
                      String BasePath) {

        Parser defaultParser = Parser.JSON;
        enableLoggingOfRequestAndResponseIfValidationFails();
        baseURI = BaseURI;
        basePath = BasePath;
        apiKey = APIKey;
    }

    /**
     * Would normally encrypt username and password but for the sake of time.....
     **/

    @Test
    @Parameters ({ "UserName", "Password" })
    public void getRequestTokenAndSession( String UserName,
                                           String Password) {

        //Get Request Token
        Response response = given().
           request().
                queryParam("api_key", apiKey).
           when().
             get("authentication/token/new").prettyPeek();

//        {
//            "success": true,
//                "expires_at": "2018-02-10 15:48:16 UTC",
//                "request_token": "724b1c6a723f0fd228f53a10670fab7bc166604c"
//        }

        requestToken = response.jsonPath().get("request_token");

        //Validate Token
        given().
          request().
            queryParam("api_key", apiKey).
            queryParam("request_token", requestToken).
            queryParam("username", UserName).
            queryParam("password", Password).
          expect().
            statusCode(200).
          when().
            get("authentication/token/validate_with_login");


        //Pull out sessionid
        Response sessionResponse = given().
          request().
            queryParam("api_key", apiKey).
            queryParam("request_token",requestToken).
          when().
            get("authentication/session/new").prettyPeek();

//        {
//          "success": true,
//          "session_id": "c133dd88fbb873940879f18dd4893fc841cf93c6"
//        }

        sessionId = sessionResponse.jsonPath().get("session_id");
    }
}
