package com.github.brokkko.openweathermap.jdk.request;

import com.github.brokkko.openweathermap.jdk.enums.Language;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestSettingsTest {

    @Test
    void testApiKeyIsStored() {
        RequestSettings rs = new RequestSettings("key123");
        assertEquals("key123", rs.getRequestParameters().get("appid"));
    }

    @Test
    void testPutAndRemoveParameters() {
        RequestSettings rs = new RequestSettings("key123");

        rs.putRequestParameter("q", "London");
        assertEquals("London", rs.getRequestParameters().get("q"));

        rs.removeRequestParameter("q");
        assertFalse(rs.getRequestParameters().containsKey("q"));
    }

    @Test
    void testAppendToUrl() {
        RequestSettings rs = new RequestSettings("key123");
        rs.appendToURL("/weather");

        assertEquals("/weather", rs.getUrlBuilder().toString());
    }

    @Test
    void testSetLanguage() {
        RequestSettings rs = new RequestSettings("key123");
        rs.setLanguage(Language.RUSSIAN);

        assertEquals(Language.RUSSIAN, rs.getLanguage());
        assertEquals("ru", rs.getRequestParameters().get("lang"));
    }

    @Test
    void testSetUnitSystem() {
        RequestSettings rs = new RequestSettings("key123");
        rs.setUnitSystem(UnitSystem.METRIC);

        assertEquals(UnitSystem.METRIC, rs.getUnitSystem());
        assertEquals("metric", rs.getRequestParameters().get("units"));
    }

    @Test
    void testCacheKeySorted() {
        RequestSettings rs = new RequestSettings("key123");
        rs.putRequestParameter("z", "1");
        rs.putRequestParameter("a", "2");

        String key = rs.cacheKey();

        assertEquals("a=2&appid=key123&z=1", key);
    }

    @Test
    void testCopyCreatesIndependentClone() {
        RequestSettings rs = new RequestSettings("key123");
        rs.putRequestParameter("q", "London");
        rs.appendToURL("/weather");

        RequestSettings copy = rs.copy();

        assertNotSame(rs, copy);
        assertEquals(rs.getRequestParameters(), copy.getRequestParameters());
        assertEquals(rs.getUrlBuilder().toString(), copy.getUrlBuilder().toString());
    }
}