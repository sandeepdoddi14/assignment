package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import objectmapper.*;

public class BooksLibraryApiClient {
    String baseUrl;

    public BooksLibraryApiClient(String env) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("Urls.json");

        if (stream == null) {
            throw new RuntimeException("Urls.json not found in classpath");
        }

        Map<String, String> urlMap = null;
        try {
            urlMap = mapper.readValue(stream, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.baseUrl = urlMap.getOrDefault(env, "http://localhost:3000");
    }

    public Response authenticateUser(AuthenticateUser payload) {
        String url = baseUrl + "auth/login";

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = null;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().header("Content-Type", "application/json").body(jsonPayload).when().post(url).then().extract().response();

        return response;
    }

    public Response createBook(String auth, CreateBook payload) {
        String url = baseUrl + "books";

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = null;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().header("Authorization", auth).header("Content-Type", "application/json").body(jsonPayload).when().post(url).then().extract().response();

        return response;
    }

    public Response deleteBook(String auth, String id) {
        String url = baseUrl + "books/" + id;

        Response response = RestAssured.given().header("Authorization", auth).header("Content-Type", "application/json").when().delete(url).then().extract().response();

        return response;
    }

    public Response readBook(String auth, String id) {
        String url = baseUrl + "books/" + id;

        Response response = RestAssured.given().header("Authorization", auth).header("Content-Type", "application/json").when().get(url).then().extract().response();

        return response;
    }

    public Response readAllBook(String auth) {
        String url = baseUrl + "books";

        Response response = RestAssured.given().header("Authorization", auth).header("Content-Type", "application/json").when().get(url).then().extract().response();

        return response;
    }

    public Response updateBook(String auth, String id, CreateBook payload) {
        String url = baseUrl + "books/" + id;

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = null;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().header("Authorization", auth).header("Content-Type", "application/json").body(jsonPayload).when().put(url).then().extract().response();

        return response;
    }

    public Response healthCheck() {
        String url = baseUrl + "health";

        Response response = RestAssured.given().header("Content-Type", "application/json").when().get(url).then().extract().response();

        return response;
    }


}
