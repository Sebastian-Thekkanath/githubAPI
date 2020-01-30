package com.payconfiq.gist;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class GitApiClient {

    public static final String GIT_API_URL = "https://api.github.com";
    public static final String GIT_API_AUTH_TOKEN = null;//token will be provided separately

    public static Response getGist(String authtoken, String since){
        RequestSpecification given = given().log().all();
        if (authtoken != null){
            given.header("Authorization", "token " + authtoken);
        }
        if (since != null){
            given.queryParam("since", since);
        }
        return given.when().request("GET", GIT_API_URL+"/gists");
    }


    public static Response getPublicGist(String authtoken, String since, String page, Integer pageSize){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        if (since != null){
            given.queryParam("since", since);
        }
        if (page != null){
            given.queryParam("pages", page);
        }
        if (pageSize != null){
            given.queryParam("per_page", pageSize);
        }
        return given.when().request("GET", GIT_API_URL+"/gists/public");
    }


    public static Response postGist(String authtoken, String body){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().log().all().body(body).request("POST", GIT_API_URL+"/gists");
    }

    public static Response patchGist(String authtoken, String body, String id){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().log().all().body(body).request("Patch", GIT_API_URL+"/gists/"+id);
    }

    public static Response retrieveSpecificGist(String authtoken, String id){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().log().all().request("GET", GIT_API_URL+"/gists/"+id);
    }

    public static Response retrieveGistForUser(String authtoken,String user){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().log().all().request("GET", GIT_API_URL+"/users/"+user+"/gists");
    }

    public static Response deleteGist(String authtoken, String id){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().request("DELETE", GIT_API_URL+"/gists/"+id);
    }

    public static Response getAllStarred(String authtoken){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().request("GET", GIT_API_URL+"/gists/starred");
    }

    public static Response getStarred(String authtoken, String id){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().request("GET", GIT_API_URL+"/gists/"+id+"/star");
    }

    public static Response putStarred(String authtoken, String id){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().request("PUT", GIT_API_URL+"/gists/"+id+"/star");
    }

    public static Response removeStarred(String authtoken, String id){
        RequestSpecification given = given().log().all().header("Authorization", "token " + authtoken);
        return given.when().request("DELETE", GIT_API_URL+"/gists/"+id+"/star");
    }


}
