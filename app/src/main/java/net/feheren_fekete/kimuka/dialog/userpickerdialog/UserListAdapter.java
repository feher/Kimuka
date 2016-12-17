package net.feheren_fekete.kimuka.dialog.userpickerdialog;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private static final String TAG = UserListAdapter.class.getSimpleName();

    public interface Listener {
        void onItemClicked(User user);
    }

    private Listener mListener;
    private List<User> mUsers = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRootLayout;
        private TextView mUserName;
        public ViewHolder(View itemView) {
            super(itemView);
            mRootLayout = (RelativeLayout) itemView.findViewById(R.id.root_layout);
            mUserName = (TextView) itemView.findViewById(R.id.user_name_value);
        }
    }

    public UserListAdapter(Listener listener) {
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
                if (user.getName().equals(updatedUser.getName())) {
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

    public void removeItems() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_picker_dialog_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, final int position) {
        final User user = mUsers.get(position);
        holder.mUserName.setText(user.getName());
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
            if (newUser.getName().compareTo(user.getName()) < 0) {
                break;
            }
            if (newUser.getKey().equals(user.getKey())) {
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
