package info.cameronlund.scout;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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
                ((TextView)findViewById(R.id.eventViewTitle)).setText(event.getName());
                ((TextView)findViewById(R.id.eventViewTest)).setText(event.getLocation().makeDirectable());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_directions) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+event.getLocation().makeDirectable());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(EVENT, event);
    }
}
