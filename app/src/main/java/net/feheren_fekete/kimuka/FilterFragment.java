package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.feheren_fekete.kimuka.dialog.ActivityDialogFragment;
import net.feheren_fekete.kimuka.dialog.CanBelayDialogFragment;
import net.feheren_fekete.kimuka.dialog.DatePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.NeedPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.SharedEquimentDialogFragment;
import net.feheren_fekete.kimuka.dialog.TimePickerDialogFragment;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.Filter;
import net.feheren_fekete.kimuka.model.ModelUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FilterFragment
        extends
                BaseFragment
        implements
                DatePickerDialogFragment.Listener,
                TimePickerDialogFragment.Listener,
                ActivityDialogFragment.Listener,
                SharedEquimentDialogFragment.Listener,
                NeedPartnerDialogFragment.Listener,
                CanBelayDialogFragment.Listener {

    private static final String TAG = FilterFragment.class.getSimpleName();

    public static final String INTERACTION_DONE_TAPPED = FilterFragment.class.getSimpleName() + ".INTERACTION_DONE_TAPPED";

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int ONE_HOUR_IN_MILLIS = 1000 * 60 * 60;
    private static final int TWO_HOURS_IN_MILLIS = ONE_HOUR_IN_MILLIS * 2;

    private TextView mLocationTextView;
    private TextView mStartDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndDateTextView;
    private TextView mEndTimeTextView;
    private boolean mIsAdjustingStartTime;
    private TextView mActivityTextView;
    private TextView mCanBelayTextView;
    private TextView mNeedPartnerTextView;
    private TextView mSharedEquipmentTextView;

    private Filter mFilter = new Filter();
    private boolean mIsNewFilter;

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        mLocationTextView = (TextView) view.findViewById(R.id.location_value);
        mLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationTextView.setError(null);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    // TODO: Handle exception.
                }
            }
        });

        mStartDateTextView = (TextView) view.findViewById(R.id.start_date_value);
        mStartDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateParts = mStartDateTextView.getText().toString().split("\\.");
                DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                        Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]) - 1, Integer.valueOf(dateParts[2]));
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
                mIsAdjustingStartTime = true;
            }
        });
        mStartTimeTextView = (TextView) view.findViewById(R.id.start_time_value);
        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] timeParts = mStartTimeTextView.getText().toString().split(":");
                DialogFragment newFragment = TimePickerDialogFragment.newInstance(
                        Integer.valueOf(timeParts[0]), Integer.valueOf(timeParts[1]));
                newFragment.show(getActivity().getSupportFragmentManager(), "TimePickerDialogFragment");
                mIsAdjustingStartTime = true;
            }
        });

        mEndDateTextView = (TextView) view.findViewById(R.id.end_date_value);
        mEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateParts = mEndDateTextView.getText().toString().split("\\.");
                DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                        Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]) - 1, Integer.valueOf(dateParts[2]));
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
                mIsAdjustingStartTime = false;
            }
        });
        mEndTimeTextView = (TextView) view.findViewById(R.id.end_time_value);
        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] timeParts = mEndTimeTextView.getText().toString().split(":");
                DialogFragment newFragment = TimePickerDialogFragment.newInstance(
                        Integer.valueOf(timeParts[0]), Integer.valueOf(timeParts[1]));
                newFragment.show(getActivity().getSupportFragmentManager(), "TimePickerDialogFragment");
                mIsAdjustingStartTime = false;
            }
        });

        mActivityTextView = (TextView) view.findViewById(R.id.activity_value);
        mActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityTextView.setError(null);
                DialogFragment newFragment = ActivityDialogFragment.newInstance(
                        ModelUtils.toIntList(mFilter.getActivity()));
                newFragment.show(getActivity().getSupportFragmentManager(), "ActivityDialogFragment");
            }
        });

        mCanBelayTextView = (TextView) view.findViewById(R.id.can_belay_value);
        mCanBelayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCanBelayTextView.setError(null);
                DialogFragment newFragment = CanBelayDialogFragment.newInstance();
                newFragment.show(getActivity().getSupportFragmentManager(), "CanBelayDialogFragment");
            }
        });

        mNeedPartnerTextView = (TextView) view.findViewById(R.id.need_partner_value);
        mNeedPartnerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNeedPartnerTextView.setError(null);
                DialogFragment newFragment = NeedPartnerDialogFragment.newInstance();
                newFragment.show(getActivity().getSupportFragmentManager(), "NeedPartnerDialogFragment");
            }
        });

        mSharedEquipmentTextView = (TextView) view.findViewById(R.id.shared_equipment_value);
        mSharedEquipmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = SharedEquimentDialogFragment.newInstance(
                        ModelUtils.toIntList(mFilter.getSharedEquipment()));
                newFragment.show(getActivity().getSupportFragmentManager(), "SharedEquipmentDialogFragment");
            }
        });

        mIsNewFilter = true;
        initNewAvailability();
        updateViews();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.availability_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_save_filter:
                addAvailability();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                mFilter.setLocationLatitude(place.getLatLng().latitude);
                mFilter.setLocationLongitude(place.getLatLng().longitude);
                mFilter.setLocationAddress(place.getAddress().toString());
                mFilter.setLocationName(place.getName().toString());
                updateViews();
            }
        }
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        if (mIsAdjustingStartTime) {
            calendar.setTimeInMillis(mFilter.getStartTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFilter.setStartTime(calendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mFilter.getEndTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFilter.setEndTime(calendar.getTimeInMillis());
        }
        adjustStartAndEndTime(mIsAdjustingStartTime);
        updateViews();
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mIsAdjustingStartTime) {
            calendar.setTimeInMillis(mFilter.getStartTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            mFilter.setStartTime(calendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mFilter.getEndTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            mFilter.setEndTime(calendar.getTimeInMillis());
        }
        adjustStartAndEndTime(mIsAdjustingStartTime);
        updateViews();
    }

    @Override
    public void onActivitySelected(List<Integer> activities) {
        mFilter.setActivity(ModelUtils.toCommaSeparatedString(activities));
        updateViews();
    }

    @Override
    public void onCanBelayItemSelected(int itemIndex) {
        mFilter.setCanBelay(itemIndex);
        updateViews();
    }

    @Override
    public void onNeedPartnerItemSelected(int itemIndex) {
        mFilter.setNeedPartner(itemIndex);
        updateViews();
    }

    @Override
    public void onEquipmentSelected(List<Integer> equipments) {
        mFilter.setSharedEquipment(ModelUtils.toCommaSeparatedString(equipments));
        updateViews();
    }

    private void initNewAvailability() {
        mFilter.setUserKey(getUser().getKey());
        mFilter.setUserName(getUser().getName());
        mFilter.setHostUser(true);
        mFilter.setCanBelay(getUser().getCanBelay());
        mFilter.setGrades(getUser().getGrades());
        mFilter.setLocationLatitude(Double.MAX_VALUE);
        mFilter.setLocationLongitude(Double.MAX_VALUE);
        long twoHoursAhead = (System.currentTimeMillis() + TWO_HOURS_IN_MILLIS);
        long twoHoursAheadRounded = (twoHoursAhead / ONE_HOUR_IN_MILLIS) * ONE_HOUR_IN_MILLIS;
        mFilter.setStartTime(twoHoursAheadRounded);
        long threeHoursAheadRounded = twoHoursAheadRounded + ONE_HOUR_IN_MILLIS;
        mFilter.setEndTime(threeHoursAheadRounded);
        mFilter.setLocationAddress("");
        mFilter.setLocationName("");
        mFilter.setActivity("");
        mFilter.setNeedPartner(Availability.NEED_PARTNER_UNDEFINED);
        mFilter.setIfNoPartner(Availability.IF_NO_PARTNER_UNDEFINED);
        mFilter.setSharedEquipment("");
    }

    private void adjustStartAndEndTime(boolean isTriggeredByStartTime) {
        long startTime = mFilter.getStartTime();
        long endTime = mFilter.getEndTime();
        if (isTriggeredByStartTime) {
            if (endTime <= startTime) {
                endTime = startTime + TWO_HOURS_IN_MILLIS;
            }
        } else {
            if (endTime <= startTime) {
                startTime = endTime - TWO_HOURS_IN_MILLIS;
            }
        }
        mFilter.setStartTime(startTime);
        mFilter.setEndTime(endTime);
    }

    private String formatDateForTextView(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return String.format(Locale.getDefault(), "%04d.%02d.%02d",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private String formatTimeForTextView(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return String.format(Locale.getDefault(), "%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private void updateViews() {
        if (mFilter.getLocationLatitude() != Double.MAX_VALUE) {
            mLocationTextView.setText(
                    mFilter.getLocationName() + ", "
                            + mFilter.getLocationAddress()
                            + " [" + mFilter.getLocationLatitude() + "," + mFilter.getLocationLongitude() + "]");
        } else {
            mLocationTextView.setText("");
        }

        mStartDateTextView.setText(formatDateForTextView(mFilter.getStartTime()));
        mStartTimeTextView.setText(formatTimeForTextView(mFilter.getStartTime()));

        mEndDateTextView.setText(formatDateForTextView(mFilter.getEndTime()));
        mEndTimeTextView.setText(formatTimeForTextView(mFilter.getEndTime()));

        mActivityTextView.setText(
                ModelUtils.createActivityNameList(
                        getContext(),
                        ModelUtils.toIntList(mFilter.getActivity())));

        mCanBelayTextView.setText(
                ModelUtils.createCanBelayText(getContext(), mFilter.getCanBelay()));

        int needPartner = mFilter.getNeedPartner();
        mNeedPartnerTextView.setText(
                (needPartner != Availability.NEED_PARTNER_UNDEFINED)
                ? ModelUtils.createNeedPartnerText(getContext(), needPartner)
                : "");

        mSharedEquipmentTextView.setText(
                ModelUtils.createEquipmentNameList(
                        getContext(),
                        ModelUtils.toIntList(mFilter.getSharedEquipment())));

        getActivity().invalidateOptionsMenu();
    }

    private void addAvailability() {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            activity.onFragmentAction(INTERACTION_DONE_TAPPED, new Bundle());
        }
    }

}
