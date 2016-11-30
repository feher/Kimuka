package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String INTERCATION_DATE_PICKED = DatePickerDialogFragment.class.getSimpleName() + ".INTERCATION_DATE_PICKED";
    public static final String DATA_YEAR = DatePickerDialogFragment.class.getSimpleName() + ".DATA_YEAR";
    public static final String DATA_MONTH = DatePickerDialogFragment.class.getSimpleName() + ".DATA_MONTH";
    public static final String DATA_DAY_OF_MONTH = DatePickerDialogFragment.class.getSimpleName() + ".DATA_DAY_OF_MONTH";

    private int mDefaultYear;
    private int mDefaultMonth;
    private int mDefaultDayOfMonth;

    public interface Listener {
        void onDateSet(int year, int month, int dayOfMonth);
    }

    public static DatePickerDialogFragment newInstance(int year, int month, int dayOfMonth) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.mDefaultYear = year;
        fragment.mDefaultMonth = month;
        fragment.mDefaultDayOfMonth = dayOfMonth;
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this, mDefaultYear, mDefaultMonth, mDefaultDayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Activity activity = getActivity();
        if (activity instanceof FragmentInteractionListener) {
            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
            Bundle data = new Bundle();
            data.putInt(DATA_YEAR, year);
            data.putInt(DATA_MONTH, month);
            data.putInt(DATA_DAY_OF_MONTH, dayOfMonth);
            listener.onFragmentAction(INTERCATION_DATE_PICKED, data);
        }
    }

}
