package info.cameronlund.scout.layout;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import info.cameronlund.scout.LoginActivity;
import info.cameronlund.scout.MainActivity;
import info.cameronlund.scout.R;

public class LoginFragment extends Fragment {

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Handle the "done" press on password (hits the login button)hideSoftKeyboard();
        ((EditText)getActivity().findViewById(R.id.loginPassword)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) && getActivity() instanceof LoginActivity) {
                    ((LoginActivity) getActivity()).loginButtonClicked(getActivity().findViewById(R.id.loginButton));
                    ((LoginActivity) getActivity()).hideSoftKeyboard();
                }
                return false;
            }
        });
    }
}