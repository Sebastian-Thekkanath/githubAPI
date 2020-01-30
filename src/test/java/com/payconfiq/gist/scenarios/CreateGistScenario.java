package com.payconfiq.gist.scenarios;

import com.payconfiq.gist.GitApiClient;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static com.payconfiq.gist.GitApiClient.GIT_API_AUTH_TOKEN;
import static com.payconfiq.gist.TestUtil.readJson;
import static org.hamcrest.Matchers.equalTo;

public class CreateGistScenario {

    private static String gistId;

    @After
    public void tearDown(){
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void createPublicGist() {
        String create_gist_payload = readJson("CreateGistRequest.json");
        Response response = GitApiClient.postGist(GIT_API_AUTH_TOKEN, create_gist_payload);
        gistId = response.then()
                .statusCode(201)
                .extract().path("id");
        response.then()
                .header("location", "https://api.github.com/gists/" + gistId)
                .body("description", equalTo("Payconiq test example"))
                .body("public", equalTo(true));
    }

    @Test
    public void createPrivateGist() {
        String create_gist_payload = readJson("CreatePrivateGistRequest.json");
        Response response = GitApiClient.postGist(GIT_API_AUTH_TOKEN, create_gist_payload);
        gistId = response.then()
                .statusCode(201)
                .extract().path("id");
        response.then()
                .header("location", "https://api.github.com/gists/" + gistId)
                .body("description", equalTo("Payconiq test example"))
                .body("public", equalTo(false));
    }

}
