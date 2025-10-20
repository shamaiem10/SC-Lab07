package twitter;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;

/**
 * Tests for SocialNetwork.
 */
public class SocialNetworkTest {

    /*
     * Testing Strategy:
     * 
     * guessFollowsGraph():
     *  - empty list of tweets → expect empty map
     *  - tweets without mentions → expect empty map
     *  - tweet with single mention → one follow relation
     *  - tweet with multiple mentions → multiple follow relations
     *  - multiple tweets from same user → all mentions combined
     *
     * influencers():
     *  - empty graph → empty list
     *  - single user without followers → user appears in list
     *  - one influencer → influencer first
     *  - multiple influencers → sorted descending
     *  - tied influence → both appear
     */

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with -ea
    }

    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Collections.emptyList());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    @Test
    public void testTweetsWithoutMentions() {
        Tweet tweet = new Tweet(1, "alice", "Hello world!", Instant.now());
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet));
        assertTrue("expected empty graph for tweet with no mentions", followsGraph.isEmpty());
    }

    @Test
    public void testSingleMention() {
        Tweet tweet = new Tweet(1, "alice", "Hi @bob!", Instant.now());
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet));
        assertTrue(followsGraph.containsKey("alice"));
        assertTrue(followsGraph.get("alice").contains("bob"));
    }

    @Test
    public void testMultipleMentions() {
        Tweet tweet = new Tweet(1, "alice", "@bob and @charlie are awesome!", Instant.now());
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet));
        assertEquals(2, followsGraph.get("alice").size());
        assertTrue(followsGraph.get("alice").contains("bob"));
        assertTrue(followsGraph.get("alice").contains("charlie"));
    }

    @Test
    public void testMultipleTweetsSameUser() {
        Tweet t1 = new Tweet(1, "alice", "Hello @bob!", Instant.now());
        Tweet t2 = new Tweet(2, "alice", "Hey @charlie!", Instant.now());
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(t1, t2));
        assertEquals(new HashSet<>(Arrays.asList("bob", "charlie")), followsGraph.get("alice"));
    }

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.contains("alice"));
    }

    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("bob", influencers.get(0));
    }

    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob", "charlie")));
        followsGraph.put("david", new HashSet<>(Arrays.asList("bob")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("bob", influencers.get(0));
    }

    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        followsGraph.put("david", new HashSet<>(Arrays.asList("charlie")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.containsAll(Arrays.asList("bob", "charlie")));
    }
}
