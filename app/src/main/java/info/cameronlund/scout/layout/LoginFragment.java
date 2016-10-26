package info.cameronlund.scout.layout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import info.cameronlund.scout.LoginActivity;
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

    /*@Override
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
    }*/
}