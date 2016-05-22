package info.cameronlund.scout.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import info.cameronlund.scout.MainActivity;
import info.cameronlund.scout.R;
import info.cameronlund.scout.objects.Event;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events = new ArrayList<>();
    private DateFormat dateFormat = DateFormat.getDateInstance();

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d("Schoolbelt", "Ran ClassAdapter's onCreateViewHolder");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_event, viewGroup, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Log.d("Schoolbelt", "Ran ClassAdapter's onBindViewHolder");
        eventViewHolder.name.setText(events.get(i).getName());
        eventViewHolder.location.setText(events.get(i).getLocation().getVenue());
        eventViewHolder.date.setText(dateFormat.format(events.get(i).getDate()));
        eventViewHolder.level.setText(events.get(i).getLevel());
        eventViewHolder.itemView.setTag(events.get(i));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Log.d("Schoolbelt", "Ran ClassAdapter's onAttachedToRecyclerView");
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setEvents(List<Event> events) {
        Log.d("Schoolbelt", "Ran ClassAdapter's setClasses");
        this.events = events;
        notifyDataSetChanged();
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView location;
        TextView date;
        TextView level;
        EventViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.eventName);
            location = (TextView) itemView.findViewById(R.id.eventLocation);
            date = (TextView) itemView.findViewById(R.id.eventDate);
            level = (TextView) itemView.findViewById(R.id.eventLevel);
        }
    }
}