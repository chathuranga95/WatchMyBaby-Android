package com.example.chathus.watchmybaby;

/**
 * Created by chathuranga on 3/9/2018.
 */

/*
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

public class UITest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public UITest() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testCase() throws Exception {
        String vResult = "TestExample";
        EditText vEditText = (EditText) solo.getView(R.id.txtUserName);
        solo.clearEditText(vEditText);
        solo.enterText(vEditText, "TestExample");
        solo.clickOnButton("Submit");
        assertTrue(solo.searchText(vResult));
        TextView textField = (TextView) solo.getView(R.id.txtPass);
        //Assert to verify result with visible value
        assertEquals(vResult, textField.getText().toString());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
*/