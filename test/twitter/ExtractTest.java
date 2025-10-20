package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class ExtractTest {

    private Tweet tweet(long id, String author, String text, String time) {
        return new Tweet(id, author, text, Instant.parse(time));
    }

    /* --------- Tests for getTimespan --------- */

    @Test
    public void testGetTimespanSingleTweet() {
        Tweet t = tweet(1, "maryam", "hello", "2025-10-06T10:00:00Z");
        Timespan span = Extract.getTimespan(Arrays.asList(t));
        assertEquals(Instant.parse("2025-10-06T10:00:00Z"), span.getStart());
        assertEquals(Instant.parse("2025-10-06T10:00:00Z"), span.getEnd());
    }

    @Test
    public void testGetTimespanMultipleTweets() {
        Tweet t1 = tweet(1, "a", "first", "2025-10-06T09:00:00Z");
        Tweet t2 = tweet(2, "b", "second", "2025-10-06T11:30:00Z");
        Tweet t3 = tweet(3, "c", "third", "2025-10-06T10:15:00Z");
        Timespan span = Extract.getTimespan(Arrays.asList(t2, t1, t3));
        assertEquals(Instant.parse("2025-10-06T09:00:00Z"), span.getStart());
        assertEquals(Instant.parse("2025-10-06T11:30:00Z"), span.getEnd());
    }

    /* --------- Tests for getMentionedUsers --------- */

    @Test
    public void testNoMentions() {
        Tweet t = tweet(1, "maryam", "no mentions here", "2025-10-06T09:00:00Z");
        assertTrue(Extract.getMentionedUsers(Arrays.asList(t)).isEmpty());
    }

    @Test
    public void testMentionsCaseInsensitive() {
        Tweet t1 = tweet(1, "a", "Hi @Ali", "2025-10-06T09:00:00Z");
        Tweet t2 = tweet(2, "b", "Hello @ali", "2025-10-06T09:05:00Z");
        Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(t1, t2));
        assertTrue(mentions.contains("ali"));
        assertEquals(1, mentions.size());
    }

    @Test
    public void testMentionsIgnoreEmails() {
        Tweet t1 = tweet(1, "a", "Contact me at me@mit.edu", "2025-10-06T09:00:00Z");
        Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(t1));
        assertTrue(mentions.isEmpty());
    }

    @Test
    public void testMentionsWithPunctuation() {
        Tweet t1 = tweet(1, "a", "@Maryam, check this!", "2025-10-06T09:00:00Z");
        Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(t1));
        assertTrue(mentions.contains("maryam"));
    }
}
