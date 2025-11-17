package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AtmosphericPressureTest {

    @Test
    void shouldCreateInstanceWithValidValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1013.25);
        assertEquals(1013.25, p.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        assertThrows(InvalidWeatherValueException.class,
                () -> AtmosphericPressure.withValue(-1));
    }

    @Test
    void shouldSetValidValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        p.setValue(900);
        assertEquals(900, p.getValue());
    }

    @Test
    void shouldThrowWhenSettingNegativeValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        assertThrows(InvalidWeatherValueException.class, () -> p.setValue(-5));
    }

    @Test
    void shouldSetSeaLevelValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        p.setSeaLevelValue(1020);
        assertEquals(1020, p.getSeaLevelValue());
    }

    @Test
    void shouldThrowWhenSettingNegativeSeaLevelValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        assertThrows(InvalidWeatherValueException.class, () -> p.setSeaLevelValue(-10));
    }

    @Test
    void shouldSetGroundLevelValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        p.setGroundLevelValue(980);
        assertEquals(980, p.getGroundLevelValue());
    }

    @Test
    void shouldThrowWhenSettingNegativeGroundLevelValue() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        assertThrows(InvalidWeatherValueException.class, () -> p.setGroundLevelValue(-15));
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        AtmosphericPressure p1 = AtmosphericPressure.withValue(1000);
        p1.setSeaLevelValue(1010);
        p1.setGroundLevelValue(990);

        AtmosphericPressure p2 = AtmosphericPressure.withValue(1000);
        p2.setSeaLevelValue(1010);
        p2.setGroundLevelValue(990);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void toStringShouldContainValueAndUnit() {
        AtmosphericPressure p = AtmosphericPressure.withValue(1000);
        String text = p.toString();
        assertTrue(text.contains("Pressure: 1000"));
        assertTrue(text.contains("hPa"));
    }
}
