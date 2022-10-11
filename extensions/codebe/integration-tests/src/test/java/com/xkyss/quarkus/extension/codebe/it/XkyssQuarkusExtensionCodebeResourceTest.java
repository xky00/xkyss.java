package com.xkyss.quarkus.extension.codebe.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class XkyssQuarkusExtensionCodebeResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/xkyss-quarkus-extension-codebe")
                .then()
                .statusCode(200)
                .body(is("Hello xkyss-quarkus-extension-codebe"));
    }
}
