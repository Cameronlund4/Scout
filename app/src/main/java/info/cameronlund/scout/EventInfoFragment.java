package info.cameronlund.scout;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.cameronlund.scout.objects.Event;

public class EventInfoFragment extends Fragment implements Userable {
    private Event event;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_info, container, false);
        if (event != null) {
            ((TextView) root.findViewById(R.id.eventViewTitle)).setText(event.getName());
            ((TextView) root.findViewById(R.id.eventViewSku)).setText(event.getSku());
        }
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("Scout", "Options menu selected in frag: " + id + "/" + R.id.action_directions);

        if (event != null) {
            if (id == R.id.action_directions) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.getLocation().makeDirectable());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void updateCurrentUser(ScoutUser user) {
        if (user == null)
            return;
        if (user.getEvents().size() < 1)
            return;
        this.event = user.getEvents().get(0);
        if (getView() == null)
            return;
        if (event != null) {
            ((TextView) getView().findViewById(R.id.eventViewTitle)).setText(event.getName());
            ((TextView) getView().findViewById(R.id.eventViewSku)).setText(event.getSku());
        }
    }
}
