package info.cameronlund.scout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import info.cameronlund.scout.objects.Event;

public class EventViewActivity extends AppCompatActivity {
    public static String EVENT = "EventViewActivity.event";
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        if (savedInstanceState != null) {
            event = savedInstanceState.getParcelable(EVENT);
        } else {
            if (getIntent() != null && getIntent().getParcelableExtra(EVENT) != null) {
                event = getIntent().getParcelableExtra(EVENT);
            } else {
                Log.e(MainActivity.PREFIX, "Tried to start an event view without an event!");
                finish();
            }
        }

        // TODO Set event contents to the event
        // TODO Read info to tell if we're adding this event, or already have it
        // TODO Add button that returns the activity with state 1, meaning load the scouting
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(EVENT, event);
    }
}
