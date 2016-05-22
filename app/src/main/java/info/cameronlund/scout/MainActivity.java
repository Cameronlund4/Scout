package info.cameronlund.scout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import info.cameronlund.scout.layout.EventListFragment;
import info.cameronlund.scout.objects.Event;

public class MainActivity extends AppCompatActivity {

    public static String PREFIX = "Scout";
    public static String AUTH_SUFFIX = "Auth";
    public static int REQUEST_LOGIN = 1;
    private EventListFragment listFragment;
    private boolean isLoggedIn = false;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private RequestQueue queue;
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // TODO Handle fragments, somehow...

        /*
        //// Create firebase listeners
        */
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    isLoggedIn = true;
                    hideSoftKeyboard();
                    Log.d(PREFIX + AUTH_SUFFIX, "We're logged in!");
                } else {
                    Log.d(PREFIX + AUTH_SUFFIX, "We're logged out!");
                    isLoggedIn = false;
                    startLoginActivity();
                }
            }
        };

        /*
        //// Register all listeners
         */
        FirebaseAuth.getInstance().addAuthStateListener(authListener); // Decide if needs to log in
        queue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActionBar() != null)
            getActionBar().setTitle(R.string.event_list_title);
        listFragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.eventListFragment);
        getExampleEvents();
        hideSoftKeyboard();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == 0) {
                finish();
            }
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent,REQUEST_LOGIN);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        listFragment.setEvents(events);
    }

    public void getExampleEvents() {
        String url = "http://api.vexdb.io/v1/get_events?";
        try {
            url += "&country=" + URLEncoder.encode("United States", "UTF-8");
            // TODO Add params
        } catch (UnsupportedEncodingException e) {
            Log.e("Scout","Failed to encode URL",e);
        }
        Log.d("Scout","URL: "+url);
        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    List<Event> events = new ArrayList<>();
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Request data", response.toString());
                        try {
                            if (response.getInt("status") != 1) {
                                // Encountered an error
                                Log.d("Request data", "Status was an error");
                                return;
                            }
                            // JSONArray to JsonArray (GSON FTW)
                            JSONArray eventsJson = response.getJSONArray("result");
                            JsonParser jsonParser = new JsonParser();
                            JsonArray gsonArray = (JsonArray)jsonParser.parse(eventsJson.toString());
                            // New event for each part of the Json Array
                            Log.d("Request data", "Size pre: " + gsonArray.size());
                            for (JsonElement object : gsonArray) {
                                events.add(new Event(object.getAsJsonObject()));
                            }
                            Log.d("Request data", "Size post: " + events.size());
                            setEvents(events);
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
        queue.add(jsObjRequest); // TODO Create a RequestQueue somewhere appropriate
    }
}
