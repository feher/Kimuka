package net.feheren_fekete.kimuka.availabilities;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.feheren_fekete.kimuka.FragmentInteractionListener;
import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.Availability;


public class AvailabilitiesFragment extends Fragment {

    public static final String INTERACTION_ADD_AVAILABILITY_TAPPED = AvailabilitiesFragment.class.getSimpleName() + ".INTERACTION_ADD_AVAILABILITY_TAPPED";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAvailabilityTable;
    private Query mSortedAvailabilityTable;
    private RecyclerView mRecyclerView;
    private AvailabilitiesAdapter mAdapter;

    public AvailabilitiesFragment() {
    }

    public static AvailabilitiesFragment newInstance() {
        AvailabilitiesFragment fragment = new AvailabilitiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAvailabilityTable = mDatabase.getReference().child("availability");
        mSortedAvailabilityTable = mAvailabilityTable.orderByChild("startTime").limitToLast(100);
        mSortedAvailabilityTable.addChildEventListener(mChildEventListener);
        mAdapter = new AvailabilitiesAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_availabilities, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity instanceof FragmentInteractionListener) {
                    FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                    listener.onFragmentAction(INTERACTION_ADD_AVAILABILITY_TAPPED, null);
                }
            }
        });

        return view;
    }

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                Availability availability = dataSnapshot.getValue(Availability.class);
                availability.setKey(dataSnapshot.getKey());
                mAdapter.addItem(availability);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                Availability availability = dataSnapshot.getValue(Availability.class);
                availability.setKey(dataSnapshot.getKey());
                mAdapter.updateItem(availability);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Availability availability = dataSnapshot.getValue(Availability.class);
                availability.setKey(dataSnapshot.getKey());
                mAdapter.removeItem(availability);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                Availability availability = dataSnapshot.getValue(Availability.class);
                availability.setKey(dataSnapshot.getKey());
                mAdapter.updateItem(availability);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
