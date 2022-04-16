package com.core;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.assertj.core.api.AbstractAssert;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static com.util.PathFinder.getFilePathForFile;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.settings;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT_ORDER;
import static org.testng.AssertJUnit.assertEquals;

public class AssertWebService extends AbstractAssert<AssertWebService, Response> {

    protected final ValidationConfiguration validationConfig = ValidationConfiguration.newBuilder()
            .setDefaultVersion(DRAFTV4).freeze();

    protected final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(validationConfig).freeze();

    protected JsonValidator validator = jsonSchemaFactory.getValidator();

    public AssertWebService(Response response) {
        super(response, AssertWebService.class);
    }

    public static AssertWebService assertThat(Response response) {
        return new AssertWebService(response);
    }


    public AssertWebService hasValidStatusCode(int expectedHttpSatus) {
        isNotNull();
        actual.then().assertThat().statusCode(expectedHttpSatus);
        return this;
    }


    public AssertWebService hasValidJsonSchema(String schemaFilename) {
        isNotNull();
        actual.then()
                .body(matchesJsonSchema(getFilePathForFile(schemaFilename).toFile())
                        .using(settings));
        return this;
    }


    @SneakyThrows
    public <T> AssertWebService hasValidJsonData(String jsonFilename, Class<T> type) {
        isNotNull();
        assertEquals(actual.as(type).toString(), getFilePathForFile(jsonFilename).toFile().toString(), STRICT_ORDER);
        return this;
    }


}

