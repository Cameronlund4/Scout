package info.cameronlund.scout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import info.cameronlund.scout.objects.Event;

public class EventsFragment extends EventListFragment implements Userable {

    private static final String TAG = "EventsFragment";

    protected ArrayList<Event> events;
    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private SharedPreferences getSharedPrefs() {
        return getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    public void getExampleEvents(final ScoutUser user) {
        showLoading(true, "Pulling example events...");
        String url = "http://api.vexdb.io/v1/get_events?limit_number=50";
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
                            showLoading(false, "Pushing example data...");
                            DatabaseReference eventRef = FirebaseDatabase.getInstance().
                                    getReference().child("users").child(user.getFirebaseUser()
                                    .getUid())
                                    .child("Nothing But Net");
                            for (Event event : events) {
                                event.saveToFirebase(eventRef.child(event.getSku()));
                            }
                            showLoading(false, "Pulling example events...");
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

    @Override
    public void updateCurrentUser(final ScoutUser user) {
        Log.i("Scout","Updated current user");
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getFirebaseUser()
                    .getUid()).child("Nothing But Net").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    events = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() < 1) {
                        showLoading(true, "Pulling fake data...");
                        getExampleEvents(user);
                    }
                    for (DataSnapshot event : dataSnapshot.getChildren()) {
                        events.add(new Event(event));
                    }
                    setEvents(events);
                    showLoading(false, "Pulling real event data...");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Scout", databaseError.getMessage());
                }
            });
        } else {
            events = new ArrayList<>();
            setEvents(events);
        }
    }
}
