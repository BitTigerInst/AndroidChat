package com.firebase.androidchat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antonylhz on 2/14/16.
 * <p/>
 * Validation related functions and exception classes
 */
public class Validator {

    private static final int PASSWORD_MIN_LENGTH = 4;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^[0-9a-zA-Z_]+$",
            Pattern.CASE_INSENSITIVE
    );
    private static volatile Validator instance;

    private Validator() {
    }

    public static Validator getInstance() {
        if (null == instance) {
            synchronized (Validator.class) {
                if (null == instance) {
                    instance = new Validator();
                }
            }
        }
        return instance;
    }

    public void checkEmail(String email) {
        if (null == email || email.length() < 1) {
            throw new EmptyEmailException();
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.find()) {
            throw new InvalidEmailException();
        }
    }

    public void checkPassword(String password) {
        if (null == password ||
                password.length() < PASSWORD_MIN_LENGTH) {
            throw new ShortPasswordException();
        }
    }

    public boolean checkUrl(String url) {
        if (null == url || url.length() < 1) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(url);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    public class EmptyEmailException extends RuntimeException {

    }

    public class InvalidEmailException extends RuntimeException {

    }

    public class ShortPasswordException extends RuntimeException {

    }

}
