package net.feheren_fekete.kimuka.requestlist;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import net.feheren_fekete.kimuka.BaseFragment;
import net.feheren_fekete.kimuka.FragmentInteractionListener;
import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.Request;


public class RequestListFragment extends BaseFragment implements RequestListAdapter.Listener {

    private static final String TAG = RequestListFragment.class.getSimpleName();

    public static final String INTERACTION_REQUEST_TAPPED = RequestListFragment.class.getSimpleName() + ".INTERACTION_REQUEST_TAPPED";

    public static final String DATA_REQUEST_KEY = RequestListFragment.class.getSimpleName() + ".DATA_REQUEST_KEY";

    private static final String ARG_RECEIVER_KEY = RequestListFragment.class.getSimpleName() + ".ARG_RECEIVER_KEY";
    private static final String ARG_SENDER_KEY = RequestListFragment.class.getSimpleName() + ".ARG_SENDER_KEY";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRequestTable;
    private Query mSortedRequestTable;
    private RecyclerView mRecyclerView;
    private RequestListAdapter mAdapter;

    public RequestListFragment() {
    }

    public static RequestListFragment newInstance(@Nullable Request filter) {
        RequestListFragment fragment = new RequestListFragment();
        if (filter != null) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_RECEIVER_KEY, filter.getReceiverKey());
            arguments.putString(ARG_SENDER_KEY, filter.getSenderKey());
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRequestTable = mDatabase.getReference().child(ModelUtils.TABLE_REQUEST);
        mSortedRequestTable = createFilteredQuery();
        mSortedRequestTable.addChildEventListener(mChildEventListener);
        mAdapter = new RequestListAdapter(getContext(), this);
    }

    private Query createFilteredQuery() {
        Query result;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String senderKey = arguments.getString(ARG_SENDER_KEY, "");
            String receiverKey = arguments.getString(ARG_RECEIVER_KEY, "");
            if (!senderKey.isEmpty()) {
                result = mRequestTable
                        .orderByChild("senderKey").equalTo(senderKey)
                        .limitToFirst(100);
            } else if (!receiverKey.isEmpty()) {
                result = mRequestTable
                        .orderByChild("receiverKey").equalTo(receiverKey)
                        .limitToFirst(100);
            } else {
                result = mRequestTable
                        .orderByChild("startTime")
//                        .startAt(System.currentTimeMillis())
                        .limitToFirst(100);
            }
        } else {
            result = mRequestTable
                    .orderByChild("startTime")
//                        .startAt(System.currentTimeMillis())
                    .limitToFirst(100);
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onItemClicked(Request request) {
        Activity activity = getActivity();
        if (activity instanceof FragmentInteractionListener) {
            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
            Bundle data = new Bundle();
            data.putString(DATA_REQUEST_KEY, request.getKey());
            listener.onFragmentAction(INTERACTION_REQUEST_TAPPED, data);
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
                Request request = dataSnapshot.getValue(Request.class);
                request.setKey(dataSnapshot.getKey());
                mAdapter.addItem(request);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                Request request = dataSnapshot.getValue(Request.class);
                request.setKey(dataSnapshot.getKey());
                mAdapter.updateItem(request);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Request request = dataSnapshot.getValue(Request.class);
                request.setKey(dataSnapshot.getKey());
                mAdapter.removeItem(request);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                Request request = dataSnapshot.getValue(Request.class);
                request.setKey(dataSnapshot.getKey());
                mAdapter.updateItem(request);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
