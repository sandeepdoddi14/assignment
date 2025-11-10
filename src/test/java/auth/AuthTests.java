package auth;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import objectmapper.AuthenticateUser;
import objectmapper.CreateBook;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.BooksLibraryApiClient;
import utils.RetryListener;
import utils.TestBase;
import org.testng.Assert;

public class AuthTests extends TestBase {
    BooksLibraryApiClient booksLibraryApiClient;

    @BeforeClass
    public void initializeClient() {
        booksLibraryApiClient = new BooksLibraryApiClient(executionEnvironment);
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"smoke,regression"})
    @Feature("Authenticate User API")
    public void testValidUserAuthentication() {
        AuthenticateUser validUserLoginPayload = testDataProvider.loadValidUserLoginPayload();

        Response response = booksLibraryApiClient.authenticateUser(validUserLoginPayload);
        Assert.assertEquals(response.getStatusCode(), 200, "FAILED || Expected Status Code to be 200");
        Assert.assertEquals(response.jsonPath().getString("message"), "Login successful", "FAILED || Expected Login Should Be Successful");
        Assert.assertFalse(response.jsonPath().getString("token").isEmpty(), "FAILED || Expected Token To Be Generated For Valid User Credentials");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"regression"})
    @Feature("Authenticate User API")
    public void testInvalidUserAuthentication() {
        AuthenticateUser inValidUserLoginPayload = testDataProvider.loadInValidUserLoginPayload();

        Response response = booksLibraryApiClient.authenticateUser(inValidUserLoginPayload);
        Assert.assertEquals(response.getStatusCode(), 401, "FAILED || Expected Status Code to be 401 for invalid credentials");
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid username or password", "FAILED || Expected Error Message For Invalid Authentication");
        Assert.assertEquals(response.jsonPath().getString("error"), "Unauthorized", "FAILED || Expected Error Field Is Returned For Invalid Authentication");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"regression"})
    @Feature("Authenticate User API")
    public void testApiWithoutToken() {
        CreateBook createBookPayload = testDataProvider.loadCreateBookPayload();

        Response response = booksLibraryApiClient.createBook("null",createBookPayload);
        Assert.assertEquals(response.getStatusCode(), 401, "FAILED || Expected Status Code to be 401 For request without token");
    }
}

