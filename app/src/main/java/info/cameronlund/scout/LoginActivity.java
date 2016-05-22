package info.cameronlund.scout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;

import info.cameronlund.scout.layout.EventListFragment;
import info.cameronlund.scout.layout.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        setPageFragment(new LoginFragment());
    }

    private void setPageFragment(Fragment fragment) {
        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.commit();
    }

    public void loginButtonClicked(View view) {
        assert findViewById(R.id.loginPassword) != null;
        final TextView username = (TextView) findViewById(R.id.loginUsername);
        assert username != null;
        final TextView password = (TextView) findViewById(R.id.loginPassword);
        assert password != null;
        if (password.getText().toString().length() < 6) {
            password.setError("Password too short.");
            return;
        }
        // TODO Change following line to the loading on the loading screen
        /*final TextView statusText = (TextView) findViewById(R.id.loginStatus);
        findViewById(R.id.loginForm).setVisibility(View.GONE);
        findViewById(R.id.loginLoading).setVisibility(View.VISIBLE);
        statusText.setText("Logging in...");*/
        // TODO Pull up a loading screen, or disable the button and show a loader
        final Activity activity = this;
        auth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setPageFragment(new EventListFragment());
                        Log.d(MainActivity.PREFIX + MainActivity.AUTH_SUFFIX, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.d(MainActivity.PREFIX + MainActivity.AUTH_SUFFIX, "signInWithEmail", task.getException());
                            //noinspection ThrowableResultOfMethodCallIgnored
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                // Credentials didn't pass, but there's not an account with username
                                // Try to make an account with it.
                                auth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d(MainActivity.PREFIX + MainActivity.AUTH_SUFFIX, "createWithEmail:onComplete:" + task.isSuccessful());
                                                if (!task.isSuccessful()) {
                                                    // Credentials didn't pass, and we already tried creating an account
                                                    // Tell this to the user.
                                                    // TODO tell the user why they failed
                                                } else {
                                                    // TODO Set up all the default values
                                                }
                                            }
                                        });
                            } else {
                                // Credentials didn't pass, and there is an account with this info.
                                // Tell this to the user.
                                // TODO Tell the user of their error
                            }
                        } else {

                        }
                    }
                });
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
