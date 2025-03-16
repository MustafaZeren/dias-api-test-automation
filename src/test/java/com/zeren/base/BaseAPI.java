package com.zeren.base;

import com.zeren.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseAPI {
    static {
        RestAssured.baseURI = ConfigReader.getProperty("base_url");
    }

    private static RequestSpecification prepareRequest(String token) {
        RequestSpecification request = given().contentType(ContentType.JSON);
        if (token != null && !token.isEmpty()) {
            request.header("Authorization", "Bearer " + token);
        }
        return request;
    }

    public static Response get(String endpoint, Map<String, Object> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return given()
                    .when()
                    .get(endpoint);
        } else {
            return given()
                    .queryParams(queryParams)
                    .when()
                    .get(endpoint);
        }
    }

    public static Response post(String endpoint, Object body, String token) {
        return prepareRequest(token)
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response put(String endpoint, Object body, String token) {
        return prepareRequest(token)
                .body(body)
                .when()
                .put(endpoint);
    }

    public static Response patch(String endpoint, Object body, String token) {
        return prepareRequest(token)
                .body(body)
                .when()
                .patch(endpoint);
    }

    public static Response delete(String endpoint, String token) {
        return prepareRequest(token)
                .when()
                .delete(endpoint);
    }
}
