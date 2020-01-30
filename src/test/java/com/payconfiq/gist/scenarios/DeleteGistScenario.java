package com.payconfiq.gist.scenarios;

import com.payconfiq.gist.GitApiClient;
import org.junit.Before;
import org.junit.Test;

import static com.payconfiq.gist.GitApiClient.GIT_API_AUTH_TOKEN;
import static com.payconfiq.gist.TestUtil.readJson;

public class DeleteGistScenario {

    private static String gistId;

    @Before
    public void setup() {
        String create_gist_payload = readJson("CreateGistRequest.json");
        gistId = GitApiClient.postGist(GIT_API_AUTH_TOKEN, create_gist_payload)
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void deleteGist(){
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void deleteGistThatDoesntExist(){
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, "aaaaaaaaaaaaaaaaaaaa")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteAlreadyDeletedGist(){
        //delete gist
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
        //delete gist again
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(404);
    }

}
