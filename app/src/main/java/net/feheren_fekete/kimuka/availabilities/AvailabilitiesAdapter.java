package net.feheren_fekete.kimuka.availabilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AvailabilitiesAdapter extends RecyclerView.Adapter<AvailabilitiesAdapter.ViewHolder> {

    private Context mContext;
    private List<Availability> mAvailabilities = new ArrayList<>();
    private Calendar mStartTimeCalendar = Calendar.getInstance();
    private Calendar mEndTimeCalendar = Calendar.getInstance();
    private Date mDate = new Date();
    private DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView1;
        private TextView mTextView2;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.text_view_1);
            mTextView2 = (TextView) itemView.findViewById(R.id.text_view_2);
        }
    }

    public AvailabilitiesAdapter(Context context) {
        mContext = context;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        mAvailabilities = availabilities;
        notifyDataSetChanged();
    }

    public void addItem(Availability newAvailability) {
        addItem(newAvailability, -1);
    }

    private void addItem(Availability newAvailability, int fromPosition) {
        final int count = mAvailabilities.size();
        Availability availability;
        int newPosition = 0;
        for (; newPosition < count; ++newPosition) {
            availability = mAvailabilities.get(newPosition);
            if (newAvailability.getStartTime() > availability.getStartTime()) {
                break;
            }
            if (availability.getKey().equals(newAvailability.getKey())) {
                return;
            }
        }
        mAvailabilities.add(newPosition, newAvailability);
        if (fromPosition != -1) {
            notifyItemMoved(fromPosition, newPosition);
        } else {
            notifyItemInserted(newPosition);
        }
    }

    public void updateItem(Availability updatedAvailability) {
        final int count = mAvailabilities.size();
        Availability availability;
        for (int i = 0; i < count; ++i) {
            availability = mAvailabilities.get(i);
            if (availability.getKey().equals(updatedAvailability.getKey())) {
                if (availability.getStartTime() == updatedAvailability.getStartTime()) {
                    mAvailabilities.set(i, updatedAvailability);
                    notifyItemChanged(i);
                } else {
                    mAvailabilities.remove(i);
                    addItem(updatedAvailability, i);
                }
                break;
            }
        }
    }

    public void removeItem(Availability availability) {
        final int count = mAvailabilities.size();
        for (int i = 0; i < count; ++i) {
            if (mAvailabilities.get(i).getKey().equals(availability.getKey())) {
                mAvailabilities.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public AvailabilitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.availability_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AvailabilitiesAdapter.ViewHolder holder, int position) {
        Availability availability = mAvailabilities.get(position);

        long startTime = availability.getStartTime();
        mStartTimeCalendar.clear();
        mStartTimeCalendar.setTimeInMillis(startTime);

        long endTime = availability.getEndTime();
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

        String whatAndWho = ModelUtils.createActivityNameList(mContext, ModelUtils.toIntList(availability.getActivity()));
        if (!availability.getSharedEquipment().isEmpty()) {
            whatAndWho += " ("
                    + ModelUtils.createEquipmentNameList(mContext, ModelUtils.toIntList(availability.getSharedEquipment()))
                    + ")";
        }
        whatAndWho += ", " + availability.getUserName();
        holder.mTextView2.setText(whatAndWho);
    }

    @Override
    public int getItemCount() {
        return mAvailabilities.size();
    }

}