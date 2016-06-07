package info.cameronlund.scout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import info.cameronlund.scout.layout.EventListFragment;
import info.cameronlund.scout.objects.Event;

public class FilterActivity extends AppCompatActivity {
    public static String EVENT = "EventViewActivity.event";
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        if (savedInstanceState != null) {
            String filteredData = savedInstanceState.getString("filterData");
            // TODO Set the filter from this info
        }

        // TODO Set event contents to the event
        // TODO Read info to tell if we're adding this event, or already have it
        // TODO Add button that returns the activity with state 1, meaning load the scouting
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("filterData",parseFilterData());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return true;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String parseFilterData() {
        // TODO Return parsed
        return "";
    }
}
