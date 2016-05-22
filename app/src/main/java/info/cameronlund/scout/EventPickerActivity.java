package info.cameronlund.scout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import info.cameronlund.scout.layout.EventListFragment;
import info.cameronlund.scout.layout.LoginFragment;
import info.cameronlund.scout.objects.Event;

public class EventPickerActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private EventListFragment listFragment;
    private RequestQueue queue;
    private FirebaseAuth.AuthStateListener authListener;
    private String url = "http://api.vexdb.io/v1/get_events?limit_start=50";
    private String filteredUrl = url;
    private ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_picker);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        queue = Volley.newRequestQueue(this);

        if (savedInstanceState != null) {
            filteredUrl = savedInstanceState.getString("filteredUrl");
            events = savedInstanceState.getParcelableArrayList("events");
            showLoading(false,"");
        } else {
            updateEvents();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("filteredUrl",filteredUrl);
        savedInstanceState.putParcelableArrayList("events",events);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        listFragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.eventListFragment);
        hideSoftKeyboard();
    }
    public void showLoading(boolean loading, String message) {
        assert findViewById(R.id.pickerLoading) != null;
        assert findViewById(R.id.contentPicker) != null;
        findViewById(R.id.pickerLoading).setVisibility(loading ? View.VISIBLE : View.GONE);
        findViewById(R.id.contentPicker).setVisibility(!loading ? View.VISIBLE : View.GONE);
        if (loading)
            ((TextView) findViewById(R.id.pickerLoadingSub)).setText(message);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        listFragment.setEvents(events);
    }

    public void updateEvents() {
        showLoading(true, "Grabbing events based on filter...");
        String url = "http://api.vexdb.io/v1/get_events?limit_start=50";
        try {
            url += "&country=" + URLEncoder.encode("United States", "UTF-8");
            // TODO Add params
        } catch (UnsupportedEncodingException e) {
            Log.e("Scout", "Failed to encode URL", e);
        }
        Log.d("Scout", "URL: " + url);
        // Request a string response from the provided URL.
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    ArrayList<Event> events = new ArrayList<>();

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Request data", response.toString());
                        try {
                            showLoading(true, "Parsing returned data...");
                            if (response.getInt("status") != 1) {
                                // Encountered an error
                                Log.d("Request data", "Status was an error");
                                return;
                            }
                            // JSONArray to JsonArray (GSON FTW)
                            JSONArray eventsJson = response.getJSONArray("result");
                            JsonParser jsonParser = new JsonParser();
                            JsonArray gsonArray = (JsonArray) jsonParser.parse(eventsJson.toString());
                            // New event for each part of the Json Array
                            Log.d("Request data", "Size pre: " + gsonArray.size());
                            for (JsonElement object : gsonArray) {
                                events.add(new Event(object.getAsJsonObject()));
                            }
                            Log.d("Request data", "Size post: " + events.size());
                            setEvents(events);
                            listFragment.showLoading(false, "Pushing example data...");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsObjRequest);
    }
}
