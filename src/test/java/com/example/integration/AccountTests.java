package com.example.integration;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by Marvin Dean on 2/12/18.
 * These tests all require a sessionid, which is inherited from the base class.  APIKey, SessionID are inherited as well.
 * This kind of setup lends itself well to parallelizing execution of tests at the class level if that is important to you.
 * Based on my experience, Parallelization is great especially for longer running tests.  These Integration tests may
 * fall under that category.  I've set this test class up to only run if the getRequestTokenAndSession test runs
 * successfully in the base class since it depends on the sessionid.
 *
 * My Goal here is not to setup a large amount of test coverage for the Account endpoints but to merely demonstrate some of
 * the abilities I have with the restassured library.
 **/

@Test(dependsOnMethods = "getRequestTokenAndSession")
public class AccountTests extends IntegrationTestBase {

  //Full request path is GET https://api.themoviedb.org/3/account?QueryParams...
  public void testGetAccount() {
    given().
      request().
        queryParam("api_key", apiKey).
        queryParam("session_id", sessionId).
    expect().
        statusCode(200).
        body("id", equalTo(7776177)).
        body("username", equalTo("mldean")).
    when().
        get("account");
  }

  @Test
  public void testPostMarkMovieAsFavorite() {
    //Setup and get acctid for use in the POST
    Response response = given().
       request().
          queryParam("api_key", apiKey).
          queryParam("session_id", sessionId).
       when().
          get("account").prettyPeek();

    int accountId = response.jsonPath().get("id");

    given().
      request().
        queryParam("api_key", apiKey).
        queryParam("session_id", sessionId).
        header("Content-Type","application/json").
      body("{\n" +
            "  \"media_type\": \"movie\"," +
            "  \"media_id\": 500," +
            "  \"favorite\": true" +
            "\n" +
        "}").
    expect().
      statusCode(201).
    when().
      post("account/{acctId}/favorite", accountId);
  }

  @Test
  public void testGetFavorites() {
    //Setup and get acctid for use in the POST
    Response response = given().
            request().
              queryParam("api_key", apiKey).
              queryParam("session_id", sessionId).
            when().
              get("account").prettyPeek();

    int accountId = response.jsonPath().get("id");

    given().
            request().
              queryParam("api_key", apiKey).
              queryParam("session_id", sessionId).
              header("Content-Type","application/json").
            expect().
              statusCode(200).
              body("results[0].id", equalTo(500)).
            when().
              get("/account/{acctId}/favorite/movies", accountId);
  }

  /**
   * Created by Marvin Dean on 2/12/18.
   * Normally I would go through a large battery of negative type testing, fault tolerance and error checking.  Here you can see one strategy
   * for handling non expected response codes.  There are many ways to accomplish this.
   **/

  @Test(expectedExceptions = AssertionError.class)
  public void testGetFavoritesWithIncorrectStatusCode() {
    //Setup and get acctid for use in the POST
    Response response = given().
            request().
            queryParam("api_key", apiKey).
            queryParam("session_id", sessionId).
            when().
            get("account").prettyPeek();

    int accountId = response.jsonPath().get("id");

    given().
            request().
            queryParam("api_key", apiKey).
            queryParam("session_id", sessionId).
            header("Content-Type","application/json").
            expect().
            statusCode(2000).
            body("results[0].id", equalTo(500)).
            when().
            get("/account/{acctId}/favorite/movies", accountId);

  }
}
