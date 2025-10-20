package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class FilterTest {

    private Tweet tweet(long id, String author, String text, String time) {
        return new Tweet(id, author, text, Instant.parse(time));
    }

    /* --------- Tests for writtenBy --------- */

    @Test
    public void testWrittenBySingleMatch() {
        Tweet t1 = tweet(1, "Maryam", "Hello world", "2025-10-06T09:00:00Z");
        Tweet t2 = tweet(2, "Ali", "Another tweet", "2025-10-06T10:00:00Z");

        List<Tweet> result = Filter.writtenBy(Arrays.asList(t1, t2), "maryam");
        assertEquals(1, result.size());
        assertEquals(t1, result.get(0));
    }

    @Test
    public void testWrittenByNoMatch() {
        Tweet t1 = tweet(1, "Ali", "Hello", "2025-10-06T09:00:00Z");
        List<Tweet> result = Filter.writtenBy(Arrays.asList(t1), "Maryam");
        assertTrue(result.isEmpty());
    }

    /* --------- Tests for inTimespan --------- */

    @Test
    public void testInTimespanIncludesTweet() {
        Instant start = Instant.parse("2025-10-06T09:00:00Z");
        Instant end = Instant.parse("2025-10-06T11:00:00Z");
        Timespan span = new Timespan(start, end);

        Tweet t = tweet(1, "Maryam", "Within time", "2025-10-06T10:00:00Z");

        List<Tweet> result = Filter.inTimespan(Arrays.asList(t), span);
        assertEquals(1, result.size());
        assertEquals(t, result.get(0));
    }

    @Test
    public void testInTimespanExcludesTweet() {
        Instant start = Instant.parse("2025-10-06T09:00:00Z");
        Instant end = Instant.parse("2025-10-06T11:00:00Z");
        Timespan span = new Timespan(start, end);

        Tweet t = tweet(1, "Maryam", "Outside time", "2025-10-06T12:00:00Z");

        List<Tweet> result = Filter.inTimespan(Arrays.asList(t), span);
        assertTrue(result.isEmpty());
    }

    /* --------- Tests for containing --------- */

    @Test
    public void testContainingWordFound() {
        Tweet t1 = tweet(1, "Maryam", "This is a test tweet", "2025-10-06T09:00:00Z");
        Tweet t2 = tweet(2, "Ali", "Another one", "2025-10-06T09:30:00Z");

        List<Tweet> result = Filter.containing(Arrays.asList(t1, t2), Arrays.asList("test"));
        assertEquals(1, result.size());
        assertEquals(t1, result.get(0));
    }

    @Test
    public void testContainingCaseInsensitive() {
        Tweet t1 = tweet(1, "Maryam", "I love JAVA", "2025-10-06T09:00:00Z");

        List<Tweet> result = Filter.containing(Arrays.asList(t1), Arrays.asList("java"));
        assertEquals(1, result.size());
    }

    @Test
    public void testContainingNoMatch() {
        Tweet t1 = tweet(1, "Ali", "Hello world", "2025-10-06T09:00:00Z");

        List<Tweet> result = Filter.containing(Arrays.asList(t1), Arrays.asList("python"));
        assertTrue(result.isEmpty());
    }
}
