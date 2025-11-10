package utils;

import org.testng.annotations.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    protected static String executionEnvironment;
    protected String authorization;
    private String configPath = "src/main/resources/configuration.properties";
    protected static TestDataProvider testDataProvider;


    @BeforeSuite
    public void initializeEnvironment() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(configPath));
        executionEnvironment = properties.get("env").toString();
    }

    @BeforeSuite(dependsOnMethods = "initializeEnvironment")
    public void initializeTestData() {
        testDataProvider = new TestDataProvider(executionEnvironment);
    }

    @AfterSuite
    public void cleanUps() {
        //ex: delete any books remaining from auto mation run
    }
}
