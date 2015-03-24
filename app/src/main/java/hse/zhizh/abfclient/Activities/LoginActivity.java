package hse.zhizh.abfclient.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Database.FeedProjectsDbHelper;
import hse.zhizh.abfclient.Database.ProjectsContract;
import hse.zhizh.abfclient.Model.Architecture;
import hse.zhizh.abfclient.Model.BuildResponse;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.Session.Session;
import hse.zhizh.abfclient.Session.SessionImpl;
import hse.zhizh.abfclient.api.ArchesRequest;
import hse.zhizh.abfclient.api.CreateBuildRequest;
import hse.zhizh.abfclient.api.PlatformsRequest;
import hse.zhizh.abfclient.api.ProjectsRequest;
import hse.zhizh.abfclient.common.Settings;


/**
 * A login screen that offers login via username/password.
 *
 */
public class LoginActivity extends ActionBarActivity {

    private UserLoginTask mAuthTask = null;

    EditText mUsernameView;
    EditText mPasswordView;
    View mLoginFormView;
    View mProgressView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        String username = Settings.getLastUsername(getApplicationContext());
        if (!username.equals("")) {
            String password = Settings.getFromTempPasswordHolder(getApplicationContext(), username);
            mUsernameView.setText(username);
            mPasswordView.setText(password);
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in...");

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void attemptLogin() {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        Log.d(Settings.TAG, "LoginActivity" + " Login Attempt");

        mAuthTask = new UserLoginTask(username, password);
        mAuthTask.execute((Void) null);
    }


    /**
     * Asynchronous login task.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private String mEmail;

        private int f_code;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
            mEmail = null;
            f_code = -1;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mAuthTask.cancel(true);
                    dialog.dismiss();
                    Log.d(Settings.TAG, "LoginActivity:" + " login cancelled");
                }
            });
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mEmail = null;

            int code=0;
            try {
                //создание сессии и получение кода ответа при попытке создать соединение
                SessionImpl s = new SessionImpl(mUsername, mPassword);

                //код ответа, если 200 то ОК
                code = s.createConnection().getResponseCode();
                if (code == 200) {
                    String response = s.requestContent(s.createConnection());
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user = jsonObject.getJSONObject("user");
                    mEmail = user.getString("email");
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            f_code = code;

            return (code == 200);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progressDialog.dismiss();
            mAuthTask = null;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            if (success) {
                if (mEmail == null || mEmail.equals(""))
                    mEmail = "https://abf.io/" + mUsername; // just webpage

                Log.d(Settings.TAG, "LoginActivity" + " Login Success " + mEmail);
                Settings.authSuccess(getApplicationContext(), mUsername, mPassword, true);
                Settings.repo_email = mEmail;

                Intent projects_intent = new Intent(LoginActivity.this, ProjectsActivity.class);
                startActivityForResult(projects_intent, 1);

            } else {
                mPasswordView.setError(getString(R.string.error_invalid_credentials));
                mPasswordView.requestFocus();

                Toast.makeText(context, "code:" + f_code, duration).show();
                Log.d(Settings.TAG, "LoginActivity" + " Login Fail");
            }
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
            mAuthTask = null;
        }
    }


    @Override
    protected void onActivityResult(int rcode, int rescode, Intent res_intent) {
        this.finish(); // just finish
    }

}



