package net.feheren_fekete.kimuka.dialog.userpickerdialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.feheren_fekete.kimuka.FragmentInteractionListener;
import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;


public class UserPickerDialogFragment extends DialogFragment implements UserListAdapter.Listener {

    private static final String TAG = UserPickerDialogFragment.class.getSimpleName();

    public static final String INTERACTION_USER_SELECTED = UserPickerDialogFragment.class.getSimpleName() + ".INTERACTION_USER_SELECTED";
    public static final String DATA_USER_NAME = UserPickerDialogFragment.class.getSimpleName() + ".DATA_USER_NAME";
    public static final String DATA_USER_KEY = UserPickerDialogFragment.class.getSimpleName() + ".DATA_USER_KEY";

    private EditText mUserNameEditText;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserTable;
    private Query mFilteredAvailabilityTable;
    private UserListAdapter mAdapter;

    public interface Listener {
        void onUserSelected(String userKey, String userName);
    }

    public static UserPickerDialogFragment newInstance() {
        UserPickerDialogFragment fragment = new UserPickerDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdapter = new UserListAdapter(this);

        View view = inflater.inflate(R.layout.user_picker_dialog, container, false);

        mUserNameEditText = (EditText) view.findViewById(R.id.partner_name_value);
        mUserNameEditText.addTextChangedListener(mTextWatcher);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mUserTable = mDatabase.getReference().child(ModelUtils.TABLE_USER);
        mFilteredAvailabilityTable = createFilteredQuery();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFilteredAvailabilityTable.addChildEventListener(mChildEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFilteredAvailabilityTable.removeEventListener(mChildEventListener);
    }

    @Override
    public void onItemClicked(User user) {
        Activity activity = getActivity();
        if (activity instanceof FragmentInteractionListener) {
            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
            Bundle data = new Bundle();
            data.putString(DATA_USER_KEY, user.getKey());
            data.putString(DATA_USER_NAME, user.getName());
            listener.onFragmentAction(INTERACTION_USER_SELECTED, data);
            dismiss();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mFilteredAvailabilityTable.removeEventListener(mChildEventListener);
            mAdapter.removeItems();
            mFilteredAvailabilityTable = createFilteredQuery();
            mFilteredAvailabilityTable.addChildEventListener(mChildEventListener);
        }
    };

    private Query createFilteredQuery() {
        String userName = mUserNameEditText.getText().toString().trim();
        if (!userName.isEmpty()) {
            return mUserTable.orderByChild(User.FIELD_NAME).startAt(userName);
        } else {
            return mUserTable.orderByChild(User.FIELD_NAME);
        }
    }

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                User user = dataSnapshot.getValue(User.class);
                user.setKey(dataSnapshot.getKey());
                mAdapter.addItem(user);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                User user = dataSnapshot.getValue(User.class);
                user.setKey(dataSnapshot.getKey());
                mAdapter.updateItem(user);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                User user = dataSnapshot.getValue(User.class);
                user.setKey(dataSnapshot.getKey());
                mAdapter.removeItem(user);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {
                User user = dataSnapshot.getValue(User.class);
                user.setKey(dataSnapshot.getKey());
                mAdapter.updateItem(user);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



}
