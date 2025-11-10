package book;

import io.restassured.response.Response;
import objectmapper.AuthenticateUser;
import objectmapper.CreateBook;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.BooksLibraryApiClient;
import utils.RetryListener;
import utils.TestBase;

public class BookCrudTests extends TestBase {
    BooksLibraryApiClient booksLibraryApiClient;
    CreateBook createBookPayload;
    CreateBook updateBookPayload;
    Response createBookResponse;

    @BeforeClass
    public void initializeClient()  {
        booksLibraryApiClient = new BooksLibraryApiClient(executionEnvironment);
    }

    @BeforeClass(dependsOnMethods = "initializeClient")
    public void login() {
        AuthenticateUser validUserLoginPayload = testDataProvider.loadValidUserLoginPayload();
        Response response = booksLibraryApiClient.authenticateUser(validUserLoginPayload);
        authorization = "Bearer " + response.jsonPath().get("token");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"smoke,regression"}, priority = 1)
    public void testCreateBook()  {
        createBookPayload = testDataProvider.loadCreateBookPayload();

        createBookResponse = booksLibraryApiClient.createBook(authorization, createBookPayload);
        Assert.assertEquals(createBookResponse.getStatusCode(), 201, "FAILED || Expected Status Code to be 201");
        Assert.assertEquals(createBookResponse.jsonPath().getString("message"), "Book created successfully", "FAILED || Expected Book Should Be Created Successfully");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"smoke,regression"}, priority = 2, dependsOnMethods = "testCreateBook")
    public void testGetBook() {
        Response getBookResponse = booksLibraryApiClient.readBook(authorization, createBookResponse.jsonPath().get("data.id"));

        /*
        verify data in get object with created object
         */
        Assert.assertEquals(getBookResponse.jsonPath().get("data.id").toString(), createBookResponse.jsonPath().get("data.id").toString(), "FAILED || id mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.title").toString(), createBookResponse.jsonPath().get("data.title").toString(), "FAILED || title mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.author").toString(), createBookResponse.jsonPath().get("data.author").toString(), "FAILED || author mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.isbn").toString(), createBookResponse.jsonPath().get("data.isbn").toString(), "FAILED || isbn mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.publishedYear").toString(), createBookResponse.jsonPath().get("data.publishedYear").toString(), "FAILED || published year mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.available").toString(), createBookResponse.jsonPath().get("data.available").toString(), "FAILED || available status mismatch");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"smoke,regression"}, priority = 3, dependsOnMethods = "testCreateBook")
    public void testUpdateBook() {
        updateBookPayload = testDataProvider.loadUpdateBookPayload();

        Response updateBookResponse = booksLibraryApiClient.updateBook(authorization, createBookResponse.jsonPath().get("data.id"), updateBookPayload);
        Assert.assertEquals(updateBookResponse.getStatusCode(), 200, "FAILED || Expected Status Code to be 200");
        Assert.assertEquals(updateBookResponse.jsonPath().getString("message"), "Book updated successfully", "FAILED || Expected Book Should Be Updated Successfully");


        Response getBookResponse = booksLibraryApiClient.readBook(authorization, updateBookResponse.jsonPath().get("data.id"));

         /*
        verify data in get object with updated object
         */
        Assert.assertEquals(getBookResponse.jsonPath().get("data.id").toString(), updateBookResponse.jsonPath().get("data.id").toString(), "FAILED || id mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.title").toString(), updateBookResponse.jsonPath().get("data.title").toString(), "FAILED || title mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.author").toString(), updateBookResponse.jsonPath().get("data.author").toString(), "FAILED || author mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.isbn").toString(), updateBookResponse.jsonPath().get("data.isbn").toString(), "FAILED || isbn mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.publishedYear").toString(), updateBookResponse.jsonPath().get("data.publishedYear").toString(), "FAILED || published year mismatch");
        Assert.assertEquals(getBookResponse.jsonPath().get("data.available").toString(), updateBookResponse.jsonPath().get("data.available").toString(), "FAILED || available status mismatch");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"smoke,regression"}, priority = 4, dependsOnMethods = "testCreateBook")
    public void testDeleteBook() {
        Response deleteBookResponse = booksLibraryApiClient.deleteBook(authorization, createBookResponse.jsonPath().get("data.id"));

        Assert.assertEquals(deleteBookResponse.jsonPath().get("deletedId").toString(), createBookResponse.jsonPath().get("data.id"), "FAILED || id mismatch");
        Assert.assertEquals(deleteBookResponse.jsonPath().get("success").toString(), "true", "FAILED || Expected status to be true after deletion");
        Assert.assertEquals(deleteBookResponse.jsonPath().get("message").toString(), "Book deleted successfully", "FAILED || Expected Book Should Be Deleted Successfully");

        /*
        Verify Deleted Book Should Not Exist In Get All Books
         */
        Response getAllBooks = booksLibraryApiClient.readAllBook(authorization);
        boolean isPresentInGetAllResponse = getAllBooks.jsonPath().getList("data.id").contains(createBookResponse.jsonPath().get("data.id"));

        Assert.assertFalse(isPresentInGetAllResponse, "FAILED || Deleted Book Should Not Be Present");
    }

    @Test(retryAnalyzer = RetryListener.class, groups = {"regression"}, priority = 5)
    public void testGetBook_NonExistentID() {
        Response getBookResponse = booksLibraryApiClient.readBook(authorization, createBookResponse.jsonPath().get("data.id"));

        /*
        Get Call With Deleted Book Should Not Return Any Object
         */
        Assert.assertEquals(getBookResponse.getStatusCode(), 404, "FAILED || Expected Status Code to be 404");
        Assert.assertEquals(getBookResponse.jsonPath().get("error").toString(), "Not Found", "FAILED || Deleted Book Should Not Be Present");
    }


}
