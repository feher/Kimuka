package net.feheren_fekete.kimuka.availabilitylist;


import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.feheren_fekete.kimuka.BaseFragment;
import net.feheren_fekete.kimuka.FragmentInteractionListener;
import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;


public class AvailabilityListFragment extends BaseFragment implements AvailabilityListAdapter.Listener {

    private static final String TAG = AvailabilityListFragment.class.getSimpleName();

    public static final String INTERACTION_ADD_AVAILABILITY_TAPPED = AvailabilityListFragment.class.getSimpleName() + ".INTERACTION_ADD_AVAILABILITY_TAPPED";
    public static final String INTERACTION_AVAILABILITY_TAPPED = AvailabilityListFragment.class.getSimpleName() + ".INTERACTION_AVAILABILITY_TAPPED";

    public static final String DATA_AVAILABILITY_KEY = AvailabilityListFragment.class.getSimpleName() + ".DATA_AVAILABILITY_KEY";

    private static final String ARG_USER_KEY = AvailabilityListFragment.class.getSimpleName() + ".ARG_USER_KEY";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAvailabilityTable;
    private Query mSortedAvailabilityTable;
    private RecyclerView mRecyclerView;
    private AvailabilityListAdapter mAdapter;

    public AvailabilityListFragment() {
    }

    public static AvailabilityListFragment newInstance(@Nullable Availability filter) {
        AvailabilityListFragment fragment = new AvailabilityListFragment();
        if (filter != null) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_USER_KEY, filter.getUserKey());
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAvailabilityTable = mDatabase.getReference().child(ModelUtils.TABLE_AVAILABILITY);
        mSortedAvailabilityTable = createFilteredQuery();
        mSortedAvailabilityTable.addChildEventListener(mChildEventListener);
        mAdapter = new AvailabilityListAdapter(getContext(), this);
    }

    private Query createFilteredQuery() {
        Query result;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String userKey = arguments.getString(ARG_USER_KEY, "");
            if (!userKey.isEmpty()) {
                result = mAvailabilityTable
                        .orderByChild("userKey").equalTo(userKey)
                        .limitToFirst(100);
            } else {
                result = mAvailabilityTable
                        .orderByChild("startTime")
//                        .startAt(System.currentTimeMillis())
                        .limitToFirst(100);
            }
        } else {
            result = mAvailabilityTable
                    .orderByChild("startTime")
//                    .startAt(System.currentTimeMillis())
                    .limitToFirst(100);
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_availability_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton floatingActionButton = getMainActivity().getFab();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity instanceof FragmentInteractionListener) {
                    FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                    listener.onFragmentAction(INTERACTION_ADD_AVAILABILITY_TAPPED, new Bundle());
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClicked(Availability availability) {
        Activity activity = getActivity();
        if (activity instanceof FragmentInteractionListener) {
            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
            Bundle data = new Bundle();
            data.putString(DATA_AVAILABILITY_KEY, availability.getKey());
            listener.onFragmentAction(INTERACTION_AVAILABILITY_TAPPED, data);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mAdapter.notifyDataSetChanged();
//        Log.d(TAG, "ON RESUME: ITEMS " + mAdapter.getItemCount() + " " + this);
//    }

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
