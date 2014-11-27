package com.github.fromi.chess.material.util;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.github.fromi.chess.material.IllegalMove;

public class IllegalMoveMatcher extends TypeSafeDiagnosingMatcher<IllegalMove> {

    public static IllegalMoveMatcher illegalMoveBecause(IllegalMove.MoveRule moveRule) {
        return new IllegalMoveMatcher(moveRule);
    }

    private final Matcher<IllegalMove.MoveRule> moveRulesMatcher;

    private IllegalMoveMatcher(IllegalMove.MoveRule moveRule) {
        moveRulesMatcher = equalTo(moveRule);
    }

    @Override
    protected boolean matchesSafely(IllegalMove exception, Description mismatchDescription) {
        return moveRulesMatcher.matches(exception.reason());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(IllegalMove.class.getSimpleName() + " for the following reasons: ");
        moveRulesMatcher.describeTo(description);
    }
}
