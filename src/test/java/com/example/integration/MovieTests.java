package com.example.integration;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Marvin Dean on 2/12/18.
 * With this class I just wanted to demonstrate using data providers in TestNG.  With a data provider, I can hit a particular
 * endpoint with permutations of data, (including diacritics, international characters as well as double byte characters) and make sure
 * that the endpoint can handle multiple data sets that may be of concern.  Expanding this test is easy to do by checking response
 * bodies and adding problematic data to the data provider object array as seen below
 **/

public class MovieTests extends IntegrationTestBase {
    @Test(dataProvider = "movieDataProvider")
    public void getMovieTitles(int id, String expectedName) {
            given().
                    request().
                    queryParam("api_key", apiKey).
                    expect().
                    statusCode(200).
                    body("original_title", equalTo(expectedName)).
                    when().
                    get("movie/"+id);
        }

    @DataProvider(name = "movieDataProvider")
    public Object[][] provideData() {

        return new Object[][] {
                { 5, "Four Rooms" },
                { 100, "Lock, Stock and Two Smoking Barrels" },
                { 200, "Star Trek: Insurrection" },
                { 300, "La science des rêves"},
                { 400, "Things to Do in Denver When You're Dead"},
                { 500, "Reservoir Dogs"}
        };
    }
}
