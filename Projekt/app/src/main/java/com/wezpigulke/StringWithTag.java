package com.wezpigulke;

public class StringWithTag {
    public String string;
    Object tag;

    StringWithTag(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }
}