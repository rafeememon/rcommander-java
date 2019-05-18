package com.rbase.rcommander.impl;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.rbase.rcommander.RCommanderException;

public class RCommanderErrorMatcher<T> extends BaseMatcher<T> {

    private final String errorMessage;

    public RCommanderErrorMatcher(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean matches(Object item) {
        if (item == null || !(item instanceof RCommanderException)) {
            return false;
        }
        return ((RCommanderException) item).getResult().getError().contains(errorMessage);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("error contains ").appendValue(errorMessage);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendValue(((RCommanderException) item).getResult().getError());
    }

    public static <T> RCommanderErrorMatcher<T> errorContains(String errorMessage) {
        return new RCommanderErrorMatcher<>(errorMessage);
    }

}
