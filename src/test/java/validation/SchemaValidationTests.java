package validation;

import io.restassured.response.Response;
import objectmapper.AuthenticateUser;
import objectmapper.CreateBook;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.BooksLibraryApiClient;
import utils.JsonSchemaValidator;
import utils.RetryListener;
import utils.TestBase;

public class SchemaValidationTests extends TestBase {
    BooksLibraryApiClient booksLibraryApiClient;
    JsonSchemaValidator jsonSchemaValidator;

    @BeforeClass
    public void initializeClient() {
        booksLibraryApiClient = new BooksLibraryApiClient(executionEnvironment);
        jsonSchemaValidator = new JsonSchemaValidator();
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"regression,smoke"})
    public void testAuthenticateUserResponseSchema() throws Exception {
        AuthenticateUser loginUserPayload = testDataProvider.loadValidUserLoginPayload();

        Response response = booksLibraryApiClient.authenticateUser(loginUserPayload);
        boolean isSchemaValid = jsonSchemaValidator.validate(response, "AuthenticateUserSchema.json");
        Assert.assertTrue(isSchemaValid, "FAILED || Authentice User Schema Validation Failed");

        authorization = "Bearer " + response.jsonPath().get("token");
    }

    @Test(retryAnalyzer = RetryListener.class, dependsOnMethods = "testAuthenticateUserResponseSchema", groups = {"regression,smoke"})
    public void testCreateBookResponseSchema() throws Exception {
        CreateBook createBookPayload = testDataProvider.getCreateBookPayload();

        Response response = booksLibraryApiClient.createBook(authorization, createBookPayload);
        boolean isSchemaValid = jsonSchemaValidator.validate(response, "CreateBookResponseSchema.json");
        Assert.assertTrue(isSchemaValid, "FAILED || Book Schema Validation Failed");
    }

    @Test(retryAnalyzer = RetryListener.class, dependsOnMethods = "testAuthenticateUserResponseSchema", groups = {"regression"})
    public void testCreateBookWithInvalidISBNResponseSchema() throws Exception {
        CreateBook createBookPayload = testDataProvider.getCreateBookInvalidISBNPayload();

        Response response = booksLibraryApiClient.createBook(authorization, createBookPayload);
        boolean isSchemaValid = jsonSchemaValidator.validate(response, "CreateBookInvalidISBNSchema.json");
        Assert.assertTrue(isSchemaValid, "FAILED || Invalid ISBN User Schema Validation Failed");
    }


}
