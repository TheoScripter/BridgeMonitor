package fr.esisar.bridgemonitor.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class JsonKeyMatcher<T extends List<String>> extends TypeSafeMatcher<T> {

    private final List<String> expectedStrings;

    public JsonKeyMatcher(List<String> expectedStrings) {
        this.expectedStrings = expectedStrings;
    }

    @Override
    protected boolean matchesSafely(T actual) {
        if (actual == null || actual.isEmpty()) {
            return false;
        }

        for (String expectedString : expectedStrings) {
            if (!actual.contains(expectedString)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a list containing all of json object keys: ");
        for (String expectedString : expectedStrings) {
            description.appendText(expectedString).appendText(", ");
        }
    }

    public static <T extends List<String>> Matcher<T> containsAllStrings(List<String> expectedStrings) {
        return new JsonKeyMatcher<>(expectedStrings);
    }
}