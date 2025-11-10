package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/*
Test Listener To Retry Flaky Tests
 */
public class RetryListener implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = 2; // Retry up to 2 times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true; // Retry the test
        }
        return false; // Don't retry
    }
}
