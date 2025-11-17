package com.github.brokkko.openweathermap.jdk.http;

import com.github.brokkko.openweathermap.jdk.exceptions.*;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherHttpExecutorTest {

    private HttpClient httpClient;
    private RetryPolicy retryPolicy;
    private WeatherLogger logger;
    private WeatherHttpExecutor executor;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        retryPolicy = mock(RetryPolicy.class);
        logger = mock(WeatherLogger.class);

        when(retryPolicy.executeWithRetry(any()))
                .thenAnswer(invocation -> {
                    var op = invocation.getArgument(
                            0,
                            com.github.brokkko.openweathermap.jdk.retries.RetryableOperation.class
                    );
                    return op.run();
                });

        executor = new WeatherHttpExecutor(httpClient, retryPolicy, logger);
    }

    @Test
    void testExecute_success200() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"ok\":true}");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        RequestSettings s = new RequestSettings("key");
        s.appendToURL("https://api.test.com/weather");
        s.putRequestParameter("q", "London");

        String result = executor.execute(s);

        assertEquals("{\"ok\":true}", result);
    }

    @Test
    void testExecute_timeout() throws Exception {
        when(httpClient.send(any(), any()))
                .thenThrow(new HttpTimeoutException("Timeout"));

        RequestSettings s = new RequestSettings("key");
        s.appendToURL("https://api.test.com/weather");

        assertThrows(WeatherTimeoutException.class, () -> executor.execute(s));
    }

    @Test
    void testExecute_unknownHost() throws Exception {
        when(httpClient.send(any(), any()))
                .thenThrow(new UnknownHostException("host"));

        RequestSettings s = new RequestSettings("k");
        s.appendToURL("https://api.test.com/weather");

        assertThrows(WeatherNetworkException.class, () -> executor.execute(s));
    }

    @Test
    void testExecute_connectException() throws Exception {
        when(httpClient.send(any(), any()))
                .thenThrow(new ConnectException("fail"));

        RequestSettings s = new RequestSettings("k");
        s.appendToURL("https://api.test.com/weather");

        assertThrows(WeatherNetworkException.class, () -> executor.execute(s));
    }

    @Test
    void testExecute_ioException() throws Exception {
        when(httpClient.send(any(), any()))
                .thenThrow(new java.io.IOException("IO err"));

        RequestSettings s = new RequestSettings("k");
        s.appendToURL("https://api.test.com/weather");

        assertThrows(WeatherNetworkException.class, () -> executor.execute(s));
    }

    @Test
    void testExecute_interruptedException() throws Exception {
        when(httpClient.send(any(), any()))
                .thenThrow(new InterruptedException("Interrupted"));

        RequestSettings s = new RequestSettings("k");
        s.appendToURL("https://api.test.com/weather");

        assertThrows(WeatherSdkException.class, () -> executor.execute(s));
        assertTrue(Thread.interrupted()); // Verify interrupt flag was restored
    }

    @Test
    void testExecute_apiError400() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(400);
        when(response.body()).thenReturn("{\"error\":\"Bad Request\"}");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        RequestSettings s = new RequestSettings("key");
        s.appendToURL("https://api.test.com/weather");

        WeatherApiException ex = assertThrows(WeatherApiException.class, () -> executor.execute(s));
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    void testExecute_apiError401() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(401);
        when(response.body()).thenReturn("{\"error\":\"Unauthorized\"}");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        RequestSettings s = new RequestSettings("key");
        s.appendToURL("https://api.test.com/weather");

        WeatherApiException ex = assertThrows(WeatherApiException.class, () -> executor.execute(s));
        assertEquals(401, ex.getStatusCode());
    }

    @Test
    void testExecute_apiError500() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(500);
        when(response.body()).thenReturn("{\"error\":\"Internal Server Error\"}");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        RequestSettings s = new RequestSettings("key");
        s.appendToURL("https://api.test.com/weather");

        WeatherApiException ex = assertThrows(WeatherApiException.class, () -> executor.execute(s));
        assertEquals(500, ex.getStatusCode());
    }

    @Test
    void testExecute_success299() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(299);
        when(response.body()).thenReturn("{\"ok\":true}");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        RequestSettings s = new RequestSettings("key");
        s.appendToURL("https://api.test.com/weather");

        String result = executor.execute(s);

        assertEquals("{\"ok\":true}", result);
    }

    @Test
    void testExecute_withMultipleParameters() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"weather\":\"data\"}");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        RequestSettings s = new RequestSettings("key123");
        s.appendToURL("https://api.test.com/weather");
        s.putRequestParameter("q", "London");
        s.putRequestParameter("units", "metric");
        s.putRequestParameter("lang", "en");

        String result = executor.execute(s);

        assertEquals("{\"weather\":\"data\"}", result);
        verify(logger).info(contains("https://api.test.com/weather"));
    }

}
