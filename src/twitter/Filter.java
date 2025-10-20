package twitter;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    /**
     * Find tweets written by a particular user.
     *
     * @param tweets list of tweets with distinct ids, not modified by this method
     * @param username Twitter username (case-insensitive comparison)
     * @return list of tweets written by username
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet t : tweets) {
            if (t.getAuthor().equalsIgnoreCase(username)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     *
     * @param tweets list of tweets with distinct ids, not modified by this method
     * @param timespan timespan
     * @return list of tweets whose timestamp is in the timespan
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet t : tweets) {
            if (!t.getTimestamp().isBefore(timespan.getStart())
                    && !t.getTimestamp().isAfter(timespan.getEnd())) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Find tweets that contain at least one of the words.
     *
     * @param tweets list of tweets with distinct ids, not modified by this method
     * @param words list of words to search for (case-insensitive)
     * @return list of tweets containing at least one of the words
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet t : tweets) {
            String text = t.getText().toLowerCase();
            for (String word : words) {
                if (text.contains(word.toLowerCase())) {
                    result.add(t);
                    break; // stop after first match
                }
            }
        }
        return result;
    }
}
