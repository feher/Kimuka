package net.feheren_fekete.kimuka.requestlist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.Request;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {

    private static final String TAG = RequestListAdapter.class.getSimpleName();

    public interface Listener {
        void onItemClicked(Request request);
    }

    private Context mContext;
    private Listener mListener;
    private List<Request> mRequests = new ArrayList<>();
    private Calendar mStartTimeCalendar = Calendar.getInstance();
    private Calendar mEndTimeCalendar = Calendar.getInstance();
    private Date mDate = new Date();
    private DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRootLayout;
        private TextView mTextView1;
        private TextView mTextView2;
        public ViewHolder(View itemView) {
            super(itemView);
            mRootLayout = (RelativeLayout) itemView.findViewById(R.id.root_layout);
            mTextView1 = (TextView) itemView.findViewById(R.id.text_view_1);
            mTextView2 = (TextView) itemView.findViewById(R.id.text_view_2);
        }
    }

    public RequestListAdapter(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setRequests(List<Request> requests) {
        mRequests = requests;
        notifyDataSetChanged();
    }

    public void addItem(Request newRequest) {
        addItem(newRequest, -1);
    }

    public void updateItem(Request updatedRequest) {
        final int count = mRequests.size();
        Request request;
        for (int i = 0; i < count; ++i) {
            request = mRequests.get(i);
            if (request.getKey().equals(updatedRequest.getKey())) {
                if (request.getStartTime() == updatedRequest.getStartTime()) {
                    mRequests.set(i, updatedRequest);
                    notifyItemChanged(i);
                } else {
                    mRequests.remove(i);
                    addItem(updatedRequest, i);
                }
                break;
            }
        }
    }

    public void removeItem(Request request) {
        final int count = mRequests.size();
        for (int i = 0; i < count; ++i) {
            if (mRequests.get(i).getKey().equals(request.getKey())) {
                mRequests.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public RequestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestListAdapter.ViewHolder holder, final int position) {
        final Request request = mRequests.get(position);

        long startTime = request.getStartTime();
        mStartTimeCalendar.clear();
        mStartTimeCalendar.setTimeInMillis(startTime);

        long endTime = request.getEndTime();
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

        String activity = ModelUtils.createActivityNameList(mContext, ModelUtils.toIntList(request.getActivity()));

        String sharedEquipment = "";
        if (!request.getSharedEquipment().isEmpty()) {
            sharedEquipment = " ("
                    + ModelUtils.createEquipmentNameList(mContext, ModelUtils.toIntList(request.getSharedEquipment()))
                    + ")";
        }

        String who = ", " + request.getReceiverName() + ", " + request.getSenderName();

        holder.mTextView2.setText(activity + sharedEquipment + who);

        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    private void addItem(Request newRequest, int fromPosition) {
        final int count = mRequests.size();
        Request request;
        int newPosition = 0;
        for (; newPosition < count; ++newPosition) {
            request = mRequests.get(newPosition);
            if (newRequest.getStartTime() > request.getStartTime()) {
                break;
            }
            if (request.getKey().equals(newRequest.getKey())) {
                return;
            }
        }
        mRequests.add(newPosition, newRequest);
        if (fromPosition != -1) {
            notifyItemMoved(fromPosition, newPosition);
        } else {
            notifyItemInserted(newPosition);
            Log.d(TAG, "ADDED ITEM AT " + newPosition + " , new size " + mRequests.size());
        }
    }

    @Nullable
    private Request getRequest(String requestKey) {
        final int count = mRequests.size();
        Request request;
        for (int i = 0; i < count; ++i) {
            request = mRequests.get(i);
            if (request.getKey().equals(requestKey)) {
                return request;
            }
        }
        return null;
    }

}
