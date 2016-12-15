package net.feheren_fekete.kimuka.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserPickerDialogFragment extends DialogFragment {

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
        mDatabase = FirebaseDatabase.getInstance();
        mUserTable = mDatabase.getReference().child(ModelUtils.TABLE_USER);
        mFilteredAvailabilityTable = createFilteredQuery();
        mFilteredAvailabilityTable.addChildEventListener(mChildEventListener);
        mAdapter = new UserListAdapter(getContext(), this);

        View view = inflater.inflate(R.layout.user_picker_dialog, container, false);
        mUserNameEditText = (EditText) view.findViewById(R.id.partner_name_value);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private Query createFilteredQuery() {
        String userName = mUserNameEditText.getText().toString().trim();
        if (!userName.isEmpty()) {
            return mUserTable.orderByChild("userName").startAt(userName);
        } else {
            return mUserTable.orderByChild("userName");
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

    public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

        public interface Listener {
            void onItemClicked(User user);
        }

        private Context mContext;
        private Listener mListener;
        private List<User> mUsers = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout mRootLayout;
            private TextView mUserName;
            public ViewHolder(View itemView) {
                super(itemView);
                mRootLayout = (RelativeLayout) itemView.findViewById(R.id.root_layout);
                mUserName = (TextView) itemView.findViewById(R.id.user_name);
            }
        }

        public UserListAdapter(Context context, Listener listener) {
            mContext = context;
            mListener = listener;
        }

        public void setAvailabilities(List<User> availabilities) {
            mUsers = availabilities;
            notifyDataSetChanged();
        }

        public void addItem(User newUser) {
            addItem(newUser, -1);
        }

        public void updateItem(User updatedUser) {
            final int count = mUsers.size();
            User user;
            for (int i = 0; i < count; ++i) {
                user = mUsers.get(i);
                if (user.getKey().equals(updatedUser.getKey())) {
                    if (user.getStartTime() == updatedUser.getStartTime()) {
                        mUsers.set(i, updatedUser);
                        notifyItemChanged(i);
                    } else {
                        mUsers.remove(i);
                        addItem(updatedUser, i);
                    }
                    break;
                }
            }
        }

        public void removeItem(User user) {
            final int count = mUsers.size();
            for (int i = 0; i < count; ++i) {
                if (mUsers.get(i).getKey().equals(user.getKey())) {
                    mUsers.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
        }

        @Override
        public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserListAdapter.ViewHolder holder, final int position) {
            final User user = mUsers.get(position);

            long startTime = user.getStartTime();
            mStartTimeCalendar.clear();
            mStartTimeCalendar.setTimeInMillis(startTime);

            long endTime = user.getEndTime();
            mEndTimeCalendar.clear();
            mEndTimeCalendar.setTimeInMillis(endTime);

            String period;
            if (mStartTimeCalendar.get(Calendar.YEAR) == mEndTimeCalendar.get(Calendar.YEAR)
                    && mStartTimeCalendar.get(Calendar.MONTH) == mEndTimeCalendar.get(Calendar.MONTH)
                    && mStartTimeCalendar.get(Calendar.DAY_OF_MONTH) == mEndTimeCalendar.get(Calendar.DAY_OF_MONTH)) {
                // Same day
                mDate.setTime(startTime);
                period = mDateFormat.format(mDate) + ", " + mTimeFormat.format(mDate);
                mDate.setTime(endTime);
                period += " - " + mTimeFormat.format(mDate);
            } else {
                mDate.setTime(startTime);
                period = mDateFormat.format(mDate) + ", " + mTimeFormat.format(mDate);
                mDate.setTime(endTime);
                period += " - " + mDateFormat.format(mDate) + ", " + mTimeFormat.format(mDate);
            }
            holder.mTextView1.setText(period);

            String activity = ModelUtils.createActivityNameList(mContext, ModelUtils.toIntList(user.getActivity()));

            String sharedEquipment = "";
            if (!user.getSharedEquipment().isEmpty()) {
                sharedEquipment = " ("
                        + ModelUtils.createEquipmentNameList(mContext, ModelUtils.toIntList(user.getSharedEquipment()))
                        + ")";
            }

            String who = ", " + user.getUserName();
            List<String> joinedUserKeys = user.getJoinedUserKeys();
            if (!joinedUserKeys.isEmpty()) {
                for (String userKey : joinedUserKeys) {
                    User joinedUser = getUser(userKey);
                    if (joinedUser != null) {
                        who += ", " + joinedUser.getUserName();
                    } else {
                        // TODO: Report error.
                    }
                }
            }

            holder.mTextView2.setText(activity + sharedEquipment + who);

            holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(user);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        private void addItem(User newUser, int fromPosition) {
            final int count = mUsers.size();
            User user;
            int newPosition = 0;
            for (; newPosition < count; ++newPosition) {
                user = mUsers.get(newPosition);
                if (newUser.getStartTime() < user.getStartTime()) {
                    break;
                }
                if (user.getKey().equals(newUser.getKey())) {
                    return;
                }
            }
            mUsers.add(newPosition, newUser);
            if (fromPosition != -1) {
                notifyItemMoved(fromPosition, newPosition);
            } else {
                notifyItemInserted(newPosition);
                Log.d(TAG, "ADDED ITEM AT " + newPosition + " , new size " + mUsers.size());
            }
        }

        @Nullable
        private User getUser(String userKey) {
            final int count = mUsers.size();
            User user;
            for (int i = 0; i < count; ++i) {
                user = mUsers.get(i);
                if (user.getKey().equals(userKey)) {
                    return user;
                }
            }
            return null;
        }

    }


}
