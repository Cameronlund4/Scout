package info.cameronlund.scout.layout;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.cameronlund.scout.R;
import info.cameronlund.scout.adapters.EventAdapter;
import info.cameronlund.scout.objects.DividerItemDecoration;
import info.cameronlund.scout.objects.Event;

public class EventListFragment extends Fragment {

    private EventAdapter adapter;
    private List<Event> events = new ArrayList<>();
    private RecyclerView recyclerView;

    public EventListFragment() {
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        adapter.setEvents(events);
    }

    public void showLoading(boolean loading, String message) {
        assert getActivity().findViewById(R.id.eventListLoading) != null;
        assert getActivity().findViewById(R.id.eventListRecycler) != null;
        getActivity().findViewById(R.id.eventListLoading).setVisibility(loading ? View.VISIBLE : View.GONE);
        getActivity().findViewById(R.id.eventListRecycler).setVisibility(!loading ? View.VISIBLE : View.GONE);
        if (loading)
            ((TextView) getActivity().findViewById(R.id.eventListLoadingSub)).setText(message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        recyclerView = (RecyclerView) view.findViewById(R.id.eventListRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        return view;
    }

    public EventAdapter getRecyclerAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
