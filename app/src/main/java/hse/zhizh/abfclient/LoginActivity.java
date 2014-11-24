package hse.zhizh.abfclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Session.Session;
import hse.zhizh.abfclient.Session.SessionImpl;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    EditText mUsernameView;
    EditText mPasswordView;
    View mLoginFormView;
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void attemptLogin() {
        Intent login_result = new Intent();
        login_result.putExtra("Username", mUsernameView.getText().toString());
        login_result.putExtra("Password", mPasswordView.getText().toString());
        System.out.println("test");
        int code=0;
        try {
            //создание сессии и получение кода ответа при попытке создать соединение
          SessionImpl s = new SessionImpl("creepycheese","ewqforce1");
            //код ответа, если 200 то ОК
          code = s.createConnection().getResponseCode();
          s.requestContent(s.createConnection());
        } catch(Exception e){
            e.printStackTrace();
        }
        int res = (code == 200) ? RESULT_OK : RESULT_CANCELED;
        //show toast======
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "code:" + res, duration);
        toast.show();
        //show toast end ====
        setResult(res, login_result);
        finish();
    }

}



