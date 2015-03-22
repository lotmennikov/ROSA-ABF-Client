package hse.zhizh.abfclient;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import hse.zhizh.abfclient.Activities.LoginActivity;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

        private LoginActivity testActivity;
        private TextView userText;
        private TextView passwordText;

        public LoginActivityTest() {
            super(LoginActivity.class);
        }

        @Override
        protected void setUp() throws Exception {
            super.setUp();

            testActivity = getActivity();
            userText = (TextView) testActivity.findViewById(R.id.username);
            passwordText = (TextView) testActivity.findViewById(R.id.username);
        }

    public void testMyFirstTestTextView_labelText() {

//        userText.setText("lotmen");

//        assertEquals("Username check", "creepycheese", userText.getText().toString()); //expected - actual
    }



}
