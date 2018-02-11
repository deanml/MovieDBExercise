package com.example.smoke;

import com.example.integration.IntegrationTestBase;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Marvin Dean on 2/12/18.
 * Would normally hit a getstatus endpoint for this type of test usually, but this simple get on the movie DB will suffice.
 **/

@Test(dependsOnMethods = "getRequestTokenAndSession")
public class DeploymentValidation extends IntegrationTestBase {

        public void testGetMovie() {
            given().
                    request().
                        queryParam("api_key", apiKey).
                    expect().
                        statusCode(200).
                        body("original_title", equalTo("Four Rooms")).
                    when().
                        get("movie/5");
        }
}
