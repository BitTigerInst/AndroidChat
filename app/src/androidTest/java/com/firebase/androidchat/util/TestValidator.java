package com.firebase.androidchat.util;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestValidator extends AndroidTestCase {

    private static Validator mValidator = Validator.getInstance();

    @Test(expected = Validator.EmptyEmailException.class)
    public void testEmptyEmail() {
        String email = null;
        mValidator.checkEmail(email);
        email = "";
        mValidator.checkEmail(email);
    }

    @Test(expected = Validator.InvalidEmailException.class)
    public void testInvalidEmail() {
        String email = "aa@a";
        mValidator.checkEmail(email);
    }

    @Test(expected = Validator.ShortPasswordException.class)
    public void checkPassword() {
        String password = "123";
        mValidator.checkPassword(password);
    }
}
