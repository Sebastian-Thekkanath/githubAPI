package com.payconfiq.gist.scenarios;

import com.payconfiq.gist.GitApiClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.payconfiq.gist.GitApiClient.GIT_API_AUTH_TOKEN;
import static com.payconfiq.gist.TestUtil.readJson;
import static org.hamcrest.Matchers.*;

public class SearchGistsScenario {

    private static String gistId;

    @BeforeClass
    public static void setupClass() {
        String create_gist_payload = readJson("CreateGistRequest.json");
        gistId = GitApiClient.postGist(GIT_API_AUTH_TOKEN, create_gist_payload)
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @AfterClass
    public static void tearDownClass(){
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void getGistOnlyForAuthenticatedUser(){
        GitApiClient.getGist(GIT_API_AUTH_TOKEN, null)
                .then()
                .statusCode(200)
                .body("owner.login", everyItem(equalTo("Sebastian-Thekkanath")));
    }

    @Test
    public void getGistsWhenUserNotAuthorized() {
        GitApiClient.getGist(null, null)
                .then()
                .statusCode(200)
                .body("owner.login", not(everyItem(equalTo("Sebastian-Thekkanath"))));
    }

    @Test
    public void getGistsOnlyFromAFixedDate(){
        GitApiClient.getGist(GIT_API_AUTH_TOKEN, "2020-01-26T10:00:00Z")
                .then()
                .statusCode(200)
                .body("updated_at" , everyItem(greaterThan("2020-01-26T10:00:00Z")));
    }

    @Test
    public void getAllPublicGistsDefaultPageSize(){
        GitApiClient.getPublicGist(GIT_API_AUTH_TOKEN, "2020-01-26T10:00:00Z",null,null)
                .then()
                .statusCode(200)
                .body("$", iterableWithSize(30))
                .body("owner.login", not(everyItem(equalTo("Sebastian-Thekkanath"))))
                .body("public", everyItem(equalTo(true)));
    }

    @Test
    public void getAllPublicGistsWithPageSize(){
        GitApiClient.getPublicGist(GIT_API_AUTH_TOKEN, "2020-01-26T10:00:00Z",null,40)
                .then()
                .statusCode(200)
                .body("$", iterableWithSize(40))
                .body("public", everyItem(equalTo(true)));
    }

    @Test
    public void searchGistOfASpecificUser(){
        GitApiClient.retrieveGistForUser(GIT_API_AUTH_TOKEN,"choco-bot")
                .then()
                .statusCode(200)
                .body("owner.login", hasItem("choco-bot"));

    }

    @Test
    public void retrieveSpecificGist(){
        GitApiClient.retrieveSpecificGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(200)
                .body("owner.login", equalTo("Sebastian-Thekkanath"));

    }

}
