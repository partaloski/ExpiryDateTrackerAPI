package com.example.expirydatetrackerapi.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class ValidationMethods {
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static boolean isEmailInvalid(String email){
        if (isNull(email) || email.isEmpty())
            return true;
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        return !emailMatcher.matches();
    }
}
