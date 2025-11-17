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

}
