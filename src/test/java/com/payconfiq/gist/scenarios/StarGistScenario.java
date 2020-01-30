package com.payconfiq.gist.scenarios;

import com.payconfiq.gist.GitApiClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.payconfiq.gist.GitApiClient.GIT_API_AUTH_TOKEN;
import static com.payconfiq.gist.TestUtil.readJson;
import static org.hamcrest.Matchers.*;

public class StarGistScenario {

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

    @Before
    public void setup(){
        GitApiClient.removeStarred(GIT_API_AUTH_TOKEN, gistId);
    }

    @Test
    public void starNewGist(){
        GitApiClient.putStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void starGistThatIsAlreadyStarred(){
        GitApiClient.putStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
        GitApiClient.putStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void checkThatGistIsStarred(){
        GitApiClient.getStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(404);
        GitApiClient.putStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
        GitApiClient.getStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void removeStar(){
        GitApiClient.putStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
        GitApiClient.removeStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void removeStarFromGistThatIsNotStarred(){
        GitApiClient.removeStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void getAllStarredGists(){
        GitApiClient.putStarred(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
        GitApiClient.getAllStarred(GIT_API_AUTH_TOKEN)
                .then()
                .statusCode(200)
                .body("id", iterableWithSize(1))
                .body("id", hasItem(gistId));
    }


}
