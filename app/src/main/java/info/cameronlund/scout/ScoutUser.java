package info.cameronlund.scout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.cameronlund.scout.objects.Event;

public class ScoutUser {


    private FirebaseUser user;
    private ArrayList<Event> events = new ArrayList<>();
    private ValueEventListener listener;
    private List<UserInfoListener> listeners = new ArrayList<>();

    public ScoutUser(FirebaseUser user, UserInfoListener completionListener) {
        this(user);
        addUserInfoListener(completionListener);
    }

    public ScoutUser(final FirebaseUser user) {
        this.user = user;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child(user.getUid());
        final ScoutUser userInstance = this;
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events = new ArrayList<>();
                for (DataSnapshot event : dataSnapshot.child("Nothing But Net").getChildren())
                    events.add(new Event(event));
                for (UserInfoListener listener : listeners)
                    listener.onUserInfoChanged(userInstance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(listener);
    }

    public void destroy() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child(user.getUid());
        ref.removeEventListener(listener);
    }

    public void addUserInfoListener(UserInfoListener listener) {
        listeners.add(listener);
    }

    public void removeUserInfoListener(UserInfoListener listener) {
        listeners.remove(listener);
    }

    public FirebaseUser getFirebaseUser() {
        return user;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
