package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import objectmapper.AuthenticateUser;
import objectmapper.CreateBook;

import java.io.IOException;
import java.io.InputStream;

/*
  Loads Test Data/Payloads Dynamically
  Based On Env Provided In Config.props
 */
public class TestDataProvider {

    String env;

    public TestDataProvider(String env) {
        this.env = env;
    }

    public AuthenticateUser loadValidUserLoginPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "testData/" + env + "/userlogin/ValidUserLogin.json";

        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Test data file not found: " + path);
        }
        try {
            return mapper.readValue(stream, AuthenticateUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticateUser loadInValidUserLoginPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "testData/" + env + "/userlogin/InValidUserLogin.json";

        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Test data file not found: " + path);
        }
        try {
            return mapper.readValue(stream, AuthenticateUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateBook loadCreateBookPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "testData/" + env + "/book/CreateBook.json";

        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Test data file not found: " + path);
        }
        try {
            return mapper.readValue(stream, CreateBook.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateBook loadUpdateBookPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "testData/" + env + "/book/UpdateBook.json";

        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Test data file not found: " + path);
        }
        try {
            return mapper.readValue(stream, CreateBook.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateBook loadCreateBookInvalidISBNPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "testData/" + env + "/book/InvalidISBN.json";

        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Test data file not found: " + path);
        }
        try {
            return mapper.readValue(stream, CreateBook.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
