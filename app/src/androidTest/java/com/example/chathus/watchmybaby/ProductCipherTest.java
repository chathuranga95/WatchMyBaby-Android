package com.example.chathus.watchmybaby;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import utill.ProductCipher;

/**
 * Created by chathuranga on 3/19/2018.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCipherTest {
//    final String TAG = "mytests";
//The Product Cipher's encrypt method is only used.
//it accepts a string as user name and a numeric string as inputs and produces
//a cipher text string.
//UI restrict the user to input the data not empty and in correct format

    //below are some possible test cases
    //cipher happening without any runtime error is the pass criteria
    private ProductCipher ps;

    @Before
    public void createObj() {
        ps = new ProductCipher();
    }

    @Test
    public void testAA() {
        System.out.println(ps.Encrypt("watch my baby username chathu", "123456"));
    }

    //not possible to occur by user inputs
    @Test
    public void testAB() {
        System.out.println(ps.Encrypt("watch my baby username chathu", ""));
    }

    //not possible to occur by user inputs
    @Test
    public void testAC() {
        System.out.println(ps.Encrypt("", "123456"));
    }

    //not possible to occur by user inputs
    @Test
    public void testAD() {
        System.out.println(ps.Encrypt("", ""));
    }

    @Test
    public void testAE() {
        System.out.println(ps.Encrypt("watch my baby username chathu", "0"));
    }

    //not possible to occur by user inputs
    @Test
    public void testAF() {
        System.out.println(ps.Encrypt("", "0"));
    }


}
