package net.feheren_fekete.kimuka.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import net.feheren_fekete.kimuka.FragmentInteractionListener;
import net.feheren_fekete.kimuka.R;

import java.text.DateFormat;
import java.util.Calendar;


public class DateTimePickerDialogFragment extends DialogFragment {

    public static final String INTERACTION_DATE_TIME_PICKED = DateTimePickerDialogFragment.class.getSimpleName() + ".INTERACTION_DATE_TIME_PICKED";
    public static final String DATA_YEAR = DateTimePickerDialogFragment.class.getSimpleName() + ".DATA_YEAR";
    public static final String DATA_MONTH = DateTimePickerDialogFragment.class.getSimpleName() + ".DATA_MONTH";
    public static final String DATA_DAY_OF_MONTH = DateTimePickerDialogFragment.class.getSimpleName() + ".DATA_DAY_OF_MONTH";
    public static final String DATA_HOUR_OF_DAY = DateTimePickerDialogFragment.class.getSimpleName() + ".DATA_HOUR_OF_DAY";
    public static final String DATA_MINUTE = DateTimePickerDialogFragment.class.getSimpleName() + ".DATA_MINUTE";

    private static final String ARG_YEAR = DateTimePickerDialogFragment.class.getSimpleName() + ".ARG_YEAR";
    private static final String ARG_MONTH = DateTimePickerDialogFragment.class.getSimpleName() + ".ARG_MONTH";
    private static final String ARG_DAY_OF_MONTH = DateTimePickerDialogFragment.class.getSimpleName() + ".ARG_DAY_OF_MONTH";
    private static final String ARG_HOUR_OF_DAY = DateTimePickerDialogFragment.class.getSimpleName() + ".ARG_HOUR_OF_DAY";
    private static final String ARG_MINUTE = DateTimePickerDialogFragment.class.getSimpleName() + ".ARG_MINUTE";

    private ViewFlipper mViewFlipper;
    private TextView mDateTimeTextView;
    private Calendar mCalendar = Calendar.getInstance();
    private DateFormat mDateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mHourOfDay;
    private int mMinute;

    public interface Listener {
        void onDateTimeSet(int year, int month, int dayOfMonth, int hourOfDay, int minute);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() > e2.getX()) {
                mViewFlipper.showNext();
            }
            if (e1.getX() < e2.getX()) {
                mViewFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public static DateTimePickerDialogFragment newInstance(int year, int month, int dayOfMonth,
                                                           int hourOfDay, int minute) {
        DateTimePickerDialogFragment fragment = new DateTimePickerDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_YEAR, year);
        arguments.putInt(ARG_MONTH, month);
        arguments.putInt(ARG_DAY_OF_MONTH, dayOfMonth);
        arguments.putInt(ARG_HOUR_OF_DAY, hourOfDay);
        arguments.putInt(ARG_MINUTE, minute);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mYear = getArguments().getInt(ARG_YEAR);
        mMonth = getArguments().getInt(ARG_MONTH);
        mDayOfMonth = getArguments().getInt(ARG_DAY_OF_MONTH);
        mHourOfDay = getArguments().getInt(ARG_HOUR_OF_DAY);
        mMinute = getArguments().getInt(ARG_MINUTE);

        View view = inflater.inflate(R.layout.date_time_picker, container, false);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        datePicker.init(mYear, mMonth, mDayOfMonth, mOnDateChangedListener);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setCurrentHour(mHourOfDay);
        timePicker.setCurrentMinute(mMinute);
        timePicker.setOnTimeChangedListener(mOnTimeChangedListener);

        mDateTimeTextView = (TextView) view.findViewById(R.id.datetime_text_value);
        updateDateTimeText();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

        SwipeGestureListener swipeGestureListener = new SwipeGestureListener();
        final GestureDetector gestureDetector = new GestureDetector(getContext(), swipeGestureListener);
        mViewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        view.findViewById(R.id.ok_button).setOnClickListener(mOnOkClicked);

        return view;
    }

    private void updateDateTimeText() {
        mCalendar.clear();
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDayOfMonth);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        mCalendar.set(Calendar.MINUTE, mMinute);
        String dateTimeString = mDateTimeFormat.format(mCalendar.getTime());
        mDateTimeTextView.setText(dateTimeString);
    }

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    mViewFlipper.showNext();
                    break;
                case 1:
                    mViewFlipper.showPrevious();
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
            mYear = i;
            mMonth = i1;
            mDayOfMonth = i2;
            updateDateTimeText();
        }
    };

    private TimePicker.OnTimeChangedListener mOnTimeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int i, int i1) {
            mHourOfDay = i;
            mMinute = i1;
            updateDateTimeText();
        }
    };

    private View.OnClickListener mOnOkClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Activity activity = getActivity();
            if (activity instanceof FragmentInteractionListener) {
                FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                Bundle data = new Bundle();
                data.putInt(DATA_YEAR, mYear);
                data.putInt(DATA_MONTH, mMonth);
                data.putInt(DATA_DAY_OF_MONTH, mDayOfMonth);
                data.putInt(DATA_HOUR_OF_DAY, mHourOfDay);
                data.putInt(DATA_MINUTE, mMinute);
                listener.onFragmentAction(INTERACTION_DATE_TIME_PICKED, data);
            }
        }
    };

}
