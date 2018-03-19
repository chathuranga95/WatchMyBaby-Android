package com.example.chathus.watchmybaby;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import utill.ProductCipher;
import utill.Validate;

import static org.junit.Assert.assertEquals;

/**
 * Created by chathuranga on 3/19/2018.
 */


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidateTest {
    //non numeric inputs are restricted from the UI
    //below are some possible test cases

    private Validate validate;
    private boolean res;

    @Before
    public void createObj() {
        validate = new Validate();
    }

    @Test
    public void testAAvalidTel() {
        res = validate.isTelValid(713604485);
        assertEquals("Valid tel OK", res, true);
    }

    @Test
    public void testABinvalidTel() {
        res = validate.isTelValid(123);
        assertEquals("invalid tel OK", res, false);
    }

    @Test
    public void testACinvalidPswMatch() {
        res = validate.isPswMatch("","");
        assertEquals("empty psw declined", res, false);
    }

    @Test
    public void testADvalidPswMatch() {
        res = validate.isPswMatch("1234","1234");
        assertEquals("matching psw accepted", res, true);
    }
    @Test
    public void testAEvalidPswMismatch() {
        res = validate.isPswMatch("1234","1243");
        assertEquals("mis matching psw declined", res, false);
    }

    @Test
    public void testAFinvalidEmail() {
        res = validate.isEmailVaid("abcd.com");
        assertEquals("Wrong email identified", res, false);
    }

    @Test
    public void testAGvalidEmail() {
        res = validate.isEmailVaid("someone@abcd.com");
        assertEquals("correct email identified", res, true);
    }


}
