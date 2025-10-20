/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Pattern mentionPattern = Pattern.compile("(?<=@)\\w+");

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
            Matcher matcher = mentionPattern.matcher(tweet.getText());

            // Collect all @-mentions in the tweet
            Set<String> mentions = new HashSet<>();
            while (matcher.find()) {
                String mentionedUser = matcher.group().toLowerCase();
                if (!mentionedUser.equals(author)) { // user cannot follow themselves
                    mentions.add(mentionedUser);
                }
            }

            // Add author → mentions to follows graph
            if (!mentions.isEmpty()) {
                if (!followsGraph.containsKey(author)) {
                    followsGraph.put(author, new HashSet<>());
                }
                followsGraph.get(author).addAll(mentions);
            }
        }

        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();

        // Step 1: Initialize follower count for all users
        for (String user : followsGraph.keySet()) {
            followerCount.putIfAbsent(user, 0); // ensures author is included
            for (String followed : followsGraph.get(user)) {
                followerCount.put(followed, followerCount.getOrDefault(followed, 0) + 1);
            }
        }

        // Step 2: Convert map to list
        List<String> users = new ArrayList<>(followerCount.keySet());

        // Step 3: Sort by descending follower count
        Collections.sort(users, new Comparator<String>() {
            public int compare(String u1, String u2) {
                return followerCount.get(u2) - followerCount.get(u1);
            }
        });

        return users;
    }

}

