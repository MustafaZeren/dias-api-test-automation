package com.zeren.tests;

import com.zeren.base.BaseAPI;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class  TestsAPI {
    private static String token;

    @BeforeClass
    public static void setup() {
        healthCheck();
        authenticate();
    }

    private static void healthCheck() {
        Response response = BaseAPI.get("/ping", null)
                .then()
                .statusCode(201)
                .extract()
                .response();
        System.out.println("✅ API Health Check başarılı! Status Code: " + response.statusCode());
        response.prettyPrint();
    }

    private static void authenticate() {
        Response response = BaseAPI.post("/auth", Map.of(
                "username", "admin",
                "password", "password123"
        ), null);
        response.then().statusCode(200);
        token = response.jsonPath().getString("token");
        assertNotNull("Token alınamadı!", token);
        System.out.println("Authentication başarılı! Token alındı: " + token);
        response.prettyPrint();
    }

    @Test
    public void testGetAllBookings() {
        Response response = BaseAPI.get("/booking", null);
        response.then().statusCode(200);
        assertFalse("Boş rezervasyon listesi döndü!", response.jsonPath().getList("bookingid").isEmpty());
        System.out.println("Tüm rezervasyonlar başarıyla getirildi!");
        response.prettyPrint();
    }

    @Test
    public void testGetBookingsByName() {
        Response response = BaseAPI.get("/booking", Map.of(
                "firstname", "sally",
                "lastname", "brown"
        ));
        response.then().statusCode(200);
        assertFalse("Belirtilen isim için rezervasyon bulunamadı!", response.jsonPath().getList("bookingid").isEmpty());
        System.out.println("İsme göre rezervasyon başarıyla getirildi!");
        response.prettyPrint();
    }

    @Test
    public void testGetBookingsByDateRange() {
        Response response = BaseAPI.get("/booking", Map.of(
                "checkin", "2014-03-13",
                "checkout", "2014-05-21"
        ));
        response.then().statusCode(200);
        assertFalse("Belirtilen tarih aralığında rezervasyon bulunamadı!", response.jsonPath().getList("bookingid").isEmpty());
        System.out.println("Tarihe göre rezervasyon başarıyla getirildi!");
        response.prettyPrint();
    }

    @Test
    public void testGetBookingById() {
        int bookingId = 1;
        Response response = BaseAPI.get("/booking/" + bookingId, null);
        response.then().statusCode(200);
        response.prettyPrint();

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.jsonPath().getString("firstname"))
                .as("Firstname hatalı!").isEqualTo("Sally");

        softly.assertThat(response.jsonPath().getString("lastname"))
                .as("Lastname hatalı!").isEqualTo("Brown");

        softly.assertThat(response.jsonPath().getInt("totalprice"))
                .as("Totalprice hatalı!").isEqualTo(111);

        softly.assertThat(response.jsonPath().getBoolean("depositpaid"))
                .as("Depozito ödenmiş olmalı!").isTrue();

        softly.assertThat(response.jsonPath().getString("bookingdates.checkin"))
                .as("Check-in tarihi hatalı!").isEqualTo("2013-02-23");

        softly.assertThat(response.jsonPath().getString("bookingdates.checkout"))
                .as("Check-out tarihi hatalı!").isEqualTo("2014-10-23");

        softly.assertThat(response.jsonPath().getString("additionalneeds"))
                .as("Ekstra ihtiyaçlar hatalı!").isEqualTo("Breakfast");

        softly.assertAll();

        System.out.println("✅ Booking ID " + bookingId + " için tüm detaylar doğrulandı!");
    }

    @Test
    public void testCreateBooking() {
        Response response = BaseAPI.post("/booking", Map.of(
                "firstname", "Jim",
                "lastname", "Brown",
                "totalprice", 111,
                "depositpaid", true,
                "bookingdates", Map.of("checkin", "2018-01-01", "checkout", "2019-01-01"),
                "additionalneeds", "Breakfast"
        ), null);
        response.then().statusCode(200);
        response.prettyPrint();

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.jsonPath().getInt("bookingid"))
                .as("Booking ID geçersiz!").isGreaterThan(0);

        softly.assertAll();

        System.out.println("✅ Yeni rezervasyon başarıyla oluşturuldu!");
    }
    @Test
    public void testUpdateBooking() {
        int bookingId = 1;
        Response response = BaseAPI.put("/booking/" + bookingId, Map.of(
                "firstname", "James",
                "lastname", "Brown",
                "totalprice", 111,
                "depositpaid", true,
                "bookingdates", Map.of("checkin", "2018-01-01", "checkout", "2019-01-01"),
                "additionalneeds", "Breakfast"
        ), token);
        response.then().statusCode(200);
        response.prettyPrint();

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.jsonPath().getString("firstname"))
                .as("Firstname hatalı!").isEqualTo("James");

        softly.assertThat(response.jsonPath().getString("lastname"))
                .as("Lastname hatalı!").isEqualTo("Brown");

        softly.assertThat(response.jsonPath().getInt("totalprice"))
                .as("Totalprice hatalı!").isEqualTo(111);

        softly.assertThat(response.jsonPath().getBoolean("depositpaid"))
                .as("Depozito ödenmiş olmalı!").isTrue();

        softly.assertAll();

        System.out.println("✅ Booking ID " + bookingId + " başarıyla güncellendi!");
    }
    @Test
    public void testPartialUpdateBooking() {
        int bookingId = 1;
        Response response = BaseAPI.patch("/booking/" + bookingId, Map.of(
                "firstname", "James",
                "lastname", "Brown"
        ), token);
        response.then().statusCode(200);
        response.prettyPrint();

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.jsonPath().getString("firstname"))
                .as("Firstname hatalı!").isEqualTo("James");

        softly.assertThat(response.jsonPath().getString("lastname"))
                .as("Lastname hatalı!").isEqualTo("Brown");

        softly.assertAll();

        System.out.println("✅ Booking ID " + bookingId + " için kısmi güncelleme başarılı!");
    }
    @Test
    public void testDeleteBooking() {
        int bookingId = 1;
        Response deleteResponse = BaseAPI.delete("/booking/" + bookingId, token);
        deleteResponse.then().statusCode(201);
        deleteResponse.prettyPrint();

        System.out.println("✅ Booking ID " + bookingId + " başarıyla silindi!");

        Response getResponse = BaseAPI.get("/booking/" + bookingId, null);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(getResponse.statusCode())
                .as("Silinen rezervasyon çağrıldığında 404 bekleniyordu!")
                .isEqualTo(404);

        softly.assertAll();

        System.out.println("✅ Silinen rezervasyon tekrar getirilmeye çalışıldığında 404 hatası alındı!");
    }
}
