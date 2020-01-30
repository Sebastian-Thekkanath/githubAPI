package com.payconfiq.gist.scenarios;

import com.payconfiq.gist.GitApiClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.payconfiq.gist.GitApiClient.GIT_API_AUTH_TOKEN;
import static com.payconfiq.gist.TestUtil.readJson;
import static org.hamcrest.Matchers.*;

public class UpdateGistScenario {

    private static String gistId;

    @Before
    public void setup() {
        String create_gist_payload = readJson("CreateGistRequest.json");
        gistId = GitApiClient.postGist(GIT_API_AUTH_TOKEN, create_gist_payload)
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @After
    public void tearDown(){
        GitApiClient.deleteGist(GIT_API_AUTH_TOKEN, gistId)
                .then()
                .statusCode(204);
    }

    @Test
    public void updateGistDescription() {
        String update_gist_payload = readJson("UpdateGistRequest.json");
        GitApiClient.patchGist(GIT_API_AUTH_TOKEN, update_gist_payload,gistId)
                .then()
                .statusCode(200)
                .body("description", equalTo("Payconiq updated example"));
    }

    @Test
    public void updateGistFileContent() {
        String update_gist_file = readJson("UpdateGistRequest.json");
        GitApiClient.patchGist(GIT_API_AUTH_TOKEN, update_gist_file,gistId)
                .then()
                .statusCode(200)
                .body("files.'payconiq.test'.content", equalTo("Hired"));
    }

    @Test
    public void renameGistFile(){
        String rename_gist_file = readJson("RenameGistFile.json");
        GitApiClient.patchGist(GIT_API_AUTH_TOKEN, rename_gist_file,gistId)
                .then()
                .statusCode(200)
                .body("files", aMapWithSize(2))
                .body("files", allOf(hasKey("company.test"),hasKey("payconiq.json"),not(hasKey("payconiq.test"))));

    }

    @Test
    public void addGistFile(){
        String add_gist_file = readJson("AddGistFileRequest.json");
        GitApiClient.patchGist(GIT_API_AUTH_TOKEN, add_gist_file,gistId)
                .then()
                .statusCode(200)
                .body("files", aMapWithSize(3))
                .body("files" , allOf(hasKey("payconiq.test"),hasKey("payconiq.json"),hasKey("payconiq.xml")));
    }


    @Test
    public void renameAndUpdateGistFile(){
        String rename_update_gist_file = readJson("RenameAndUpdateGistFile.json");
        GitApiClient.patchGist(GIT_API_AUTH_TOKEN, rename_update_gist_file,gistId)
                .then()
                .statusCode(200)
                .body("files", aMapWithSize(2))
                .body("files.'company.test'.content", equalTo("company test"))
                .body("files", allOf(hasKey("company.test"),hasKey("payconiq.json")));
    }

    @Test
    public void deleteGistFile(){
        String delete_gist_file = readJson("DeleteGistFileRequest.json");
        GitApiClient.patchGist(GIT_API_AUTH_TOKEN, delete_gist_file,gistId)
                .then()
                .statusCode(200)
                .body("files", not(hasKey("payconiq.test")));
    }
}
