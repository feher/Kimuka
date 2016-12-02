package net.feheren_fekete.kimuka.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import net.feheren_fekete.kimuka.FragmentInteractionListener;


public class TimePickerDialogFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String INTERCATION_TIME_PICKED = TimePickerDialogFragment.class.getSimpleName() + ".INTERCATION_TIME_PICKED";
    public static final String DATA_HOUR_OF_DAY = TimePickerDialogFragment.class.getSimpleName() + ".DATA_HOUR_OF_DAY";
    public static final String DATA_MINUTE = TimePickerDialogFragment.class.getSimpleName() + ".DATA_MINUTE";

    private int mDefaultHourOfDay;
    private int mDefaultMinute;

    public interface Listener {
        void onTimeSet(int hourOfDay, int minute);
    }

    public static TimePickerDialogFragment newInstance(int hourOfDay, int minute) {
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.mDefaultHourOfDay = hourOfDay;
        fragment.mDefaultMinute = minute;
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(
                getActivity(), this, mDefaultHourOfDay, mDefaultMinute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Activity activity = getActivity();
        if (activity instanceof FragmentInteractionListener) {
            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
            Bundle data = new Bundle();
            data.putInt(DATA_HOUR_OF_DAY, hourOfDay);
            data.putInt(DATA_MINUTE, minute);
            listener.onFragmentAction(INTERCATION_TIME_PICKED, data);
        }
    }

}
