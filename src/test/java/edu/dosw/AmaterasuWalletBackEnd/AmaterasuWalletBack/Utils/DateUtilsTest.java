package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    @DisplayName("Should format Date correctly")
    void shouldFormatDateCorrectly() {
      
        Date date = new Date();

       
        String formattedDate = DateUtils.formatDate(date, DateUtils.ISO_DATE_FORMAT);

       
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    @DisplayName("Should format Date with timestamp format")
    void shouldFormatDateWithTimestampFormat() {
       
        Date date = new Date();

        
        String formattedDate = DateUtils.formatDate(date, DateUtils.TIMESTAMP_FORMAT);

        
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"));
    }

    @Test
    @DisplayName("Should parse date string correctly")
    void shouldParseDateStringCorrectly() {
        
        String dateString = "2025-01-15";

        
        Date parsedDate = DateUtils.parseDate(dateString, DateUtils.ISO_DATE_FORMAT);

        assertNotNull(parsedDate);
    }

    @Test
    @DisplayName("Should throw exception when parsing invalid date")
    void shouldThrowExceptionWhenParsingInvalidDate() {
        
        String invalidDateString = "invalid-date";

       
        assertThrows(RuntimeException.class, () -> {
            DateUtils.parseDate(invalidDateString, DateUtils.ISO_DATE_FORMAT);
        });
    }

    @Test
    @DisplayName("Should format LocalDateTime correctly")
    void shouldFormatLocalDateTimeCorrectly() {
      
        LocalDateTime dateTime = LocalDateTime.now();

        
        String formatted = DateUtils.formatLocalDateTime(dateTime, DateUtils.ISO_DATETIME_FORMAT);

     
        assertNotNull(formatted);
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should parse LocalDateTime correctly")
    void shouldParseLocalDateTimeCorrectly() {
       
        String dateTimeString = "2025-01-15 10:30:45";

      
        LocalDateTime parsed = DateUtils.parseLocalDateTime(dateTimeString, DateUtils.ISO_DATETIME_FORMAT);

        
        assertNotNull(parsed);
        assertEquals(2025, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(15, parsed.getDayOfMonth());
    }

    @Test
    @DisplayName("Should convert to ISO string")
    void shouldConvertToIsoString() {
       
        LocalDateTime dateTime = LocalDateTime.now();

        
        String isoString = DateUtils.toIsoString(dateTime);

       
        assertNotNull(isoString);
        assertTrue(isoString.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should parse from ISO string")
    void shouldParseFromIsoString() {
      
        String isoString = "2025-01-15 10:30:45";

      
        LocalDateTime parsed = DateUtils.fromIsoString(isoString);

       
        assertNotNull(parsed);
        assertEquals(2025, parsed.getYear());
    }

    @Test
    @DisplayName("Should get current ISO datetime")
    void shouldGetCurrentIsoDateTime() {
    
        String currentDateTime = DateUtils.currentIsoDateTime();

       
        assertNotNull(currentDateTime);
        assertTrue(currentDateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should convert to display string")
    void shouldConvertToDisplayString() {
        
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 15, 10, 30, 45);

        
        String displayString = DateUtils.toDisplayString(dateTime);

      
        assertNotNull(displayString);
        assertTrue(displayString.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should get current datetime as string")
    void shouldGetCurrentDateTimeAsString() {
       
        String currentDateTime = DateUtils.currentDateTimeAsString(DateUtils.ISO_DATETIME_FORMAT);

       
        assertNotNull(currentDateTime);
        assertTrue(currentDateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should format with display date format")
    void shouldFormatWithDisplayDateFormat() {
      
        Date date = new Date();

       
        String formatted = DateUtils.formatDate(date, DateUtils.DISPLAY_DATE_FORMAT);

        
        assertNotNull(formatted);
        assertTrue(formatted.matches("\\d{2}/\\d{2}/\\d{4}"));
    }

    @Test
    @DisplayName("Should format with display datetime format")
    void shouldFormatWithDisplayDateTimeFormat() {
    
        Date date = new Date();

        
        String formatted = DateUtils.formatDate(date, DateUtils.DISPLAY_DATETIME_FORMAT);

        assertNotNull(formatted);
        assertTrue(formatted.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }
}
