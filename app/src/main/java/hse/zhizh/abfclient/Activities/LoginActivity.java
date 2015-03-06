package hse.zhizh.abfclient.Activities;

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
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
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

import hse.zhizh.abfclient.Database.FeedProjectsDbHelper;
import hse.zhizh.abfclient.Database.ProjectsContract;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.Session.Session;
import hse.zhizh.abfclient.Session.SessionImpl;
import hse.zhizh.abfclient.api.ProjectsRequest;


/**
 * A login screen that offers login via username/password.
 *
 *
 * TODO Нужно приделать Account Manager
 */
public class LoginActivity extends ActionBarActivity {

    private UserLoginTask mAuthTask = null;

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
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // -- TODO remove
        if (username.equals("")) { username = "lotmen"; password = "fab688"; }
        // --

        Log.d("ABF Client LoginActivity", "Login Attempt");

        mAuthTask = new UserLoginTask(username, password);
        mAuthTask.execute((Void) null);
    }


    /**
     * Asynchronous login task.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        private int f_code;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
            f_code = -1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int code=0;
            try {
                //создание сессии и получение кода ответа при попытке создать соединение
                SessionImpl s = new SessionImpl(mUsername, mPassword);//"creepycheese","ewqforce1");
                //код ответа, если 200 то ОК
                code = s.createConnection().getResponseCode();
                ProjectsRequest pr = new ProjectsRequest();
                //pr.sendRequest();
                System.out.println("RESULTS OF REQUEST: " +pr.sendRequest());
                s.requestContent(s.createConnection());
            } catch(Exception e){
                e.printStackTrace();
            }
            f_code = code;

            return (code == 200);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            Context context = getApplicationContext();
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "code:" + f_code, duration);
            toast.show();
            if (success) {

                Log.d("ABF Client LoginActivity", "Login Success");

                Intent login_result = new Intent();
                login_result.putExtra("Username", mUsername);
                login_result.putExtra("Password", mPassword);
                setResult(RESULT_OK, login_result);
                finish();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();

                Log.d("ABF Client LoginActivity", "Login Fail");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}



