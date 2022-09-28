package com.xkyss.quarkus.extension.codegen.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class XkyssQuarkusExtensionCodegenResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/xkyss-quarkus-extension-codegen")
                .then()
                .statusCode(200)
                .body(is("Hello xkyss-quarkus-extension-codegen"));
    }
}
