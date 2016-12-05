package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.feheren_fekete.kimuka.dialog.ActivityDialogFragment;
import net.feheren_fekete.kimuka.dialog.DatePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.IfNoPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.NeedPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.SharedEquimentDialogFragment;
import net.feheren_fekete.kimuka.dialog.TimePickerDialogFragment;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddAvailabilityFragment
        extends
                Fragment
        implements
                DatePickerDialogFragment.Listener,
                TimePickerDialogFragment.Listener,
                ActivityDialogFragment.Listener,
                IfNoPartnerDialogFragment.Listener,
                SharedEquimentDialogFragment.Listener,
                NeedPartnerDialogFragment.Listener {

    private static final String TAG = AddAvailabilityFragment.class.getSimpleName();

    public static final String INTERACTION_DONE_TAPPED = AddAvailabilityFragment.class.getSimpleName() + ".INTERACTION_DONE_TAPPED";

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int ONE_HOUR_IN_MILLIS = 1000 * 60 * 60;
    private static final int TWO_HOURS_IN_MILLIS = ONE_HOUR_IN_MILLIS * 2;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAvailabilityTable;

    private TextView mLocationTextView;
    private TextView mStartDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndDateTextView;
    private TextView mEndTimeTextView;
    private TextView mTargetTimeTextView;
    private TextView mTargetDateTextView;
    private TextView mActivityTextView;
    private TextView mNeedPartnerTextView;
    private TextView mIfNoPartnerTextView;
    private TextView mSharedEquipmentTextView;
    private EditText mNoteEditText;

    private double mLocationLatitude = Double.MAX_VALUE;
    private double mLocationLongitude = Double.MAX_VALUE;
    private long mStartTime;
    private long mEndTime;
    private String mLocationAddress = "";
    private String mLocationName = "";
    private String mActivities = "";
    private int mNeedPartner = Availability.NEED_PARTNER_UNDEFINED;
    private int mIfNoPartner = Availability.IF_NO_PARTNER_UNDEFINED;
    private String mSharedEquipment = "";

    public AddAvailabilityFragment() {
    }

    public static AddAvailabilityFragment newInstance() {
        AddAvailabilityFragment fragment = new AddAvailabilityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAvailabilityTable = mDatabase.getReference().child("availability");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_availability, container, false);

        mLocationLatitude = Double.MAX_VALUE;
        mLocationLongitude = Double.MAX_VALUE;
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

        long twoHoursAhead = System.currentTimeMillis() + TWO_HOURS_IN_MILLIS;
        mStartTime = twoHoursAhead;
        mStartDateTextView = (TextView) view.findViewById(R.id.start_date_value);
        mStartDateTextView.setText(formatDateForTextView(mStartTime));
        mStartDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateParts = mStartDateTextView.getText().toString().split("\\.");
                DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                        Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]) - 1, Integer.valueOf(dateParts[2]));
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
                mTargetDateTextView = mStartDateTextView;
            }
        });
        mStartTimeTextView = (TextView) view.findViewById(R.id.start_time_value);
        mStartTimeTextView.setText(formatTimeForTextView(mStartTime));
        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] timeParts = mStartTimeTextView.getText().toString().split(":");
                DialogFragment newFragment = TimePickerDialogFragment.newInstance(
                        Integer.valueOf(timeParts[0]), Integer.valueOf(timeParts[1]));
                newFragment.show(getActivity().getSupportFragmentManager(), "TimePickerDialogFragment");
                mTargetTimeTextView = mStartTimeTextView;
            }
        });

        long threeHoursAhead = twoHoursAhead + ONE_HOUR_IN_MILLIS;
        mEndTime = threeHoursAhead;
        mEndDateTextView = (TextView) view.findViewById(R.id.end_date_value);
        mEndDateTextView.setText(formatDateForTextView(mEndTime));
        mEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateParts = mEndDateTextView.getText().toString().split("\\.");
                DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                        Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]) - 1, Integer.valueOf(dateParts[2]));
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
                mTargetDateTextView = mEndDateTextView;
            }
        });
        mEndTimeTextView = (TextView) view.findViewById(R.id.end_time_value);
        mEndTimeTextView.setText(formatTimeForTextView(mEndTime));
        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] timeParts = mEndTimeTextView.getText().toString().split(":");
                DialogFragment newFragment = TimePickerDialogFragment.newInstance(
                        Integer.valueOf(timeParts[0]), Integer.valueOf(timeParts[1]));
                newFragment.show(getActivity().getSupportFragmentManager(), "TimePickerDialogFragment");
                mTargetTimeTextView = mEndTimeTextView;
            }
        });

        mActivityTextView = (TextView) view.findViewById(R.id.activity_value);
        mActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityTextView.setError(null);
                DialogFragment newFragment = ActivityDialogFragment.newInstance(
                        ModelUtils.toIntList(mActivities));
                newFragment.show(getActivity().getSupportFragmentManager(), "ActivityDialogFragment");
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

        mIfNoPartnerTextView = (TextView) view.findViewById(R.id.if_no_partner_value);
        mIfNoPartnerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIfNoPartnerTextView.setError(null);
                DialogFragment newFragment = IfNoPartnerDialogFragment.newInstance();
                newFragment.show(getActivity().getSupportFragmentManager(), "IfNoPartnerDialogFragment");
            }
        });

        mSharedEquipmentTextView = (TextView) view.findViewById(R.id.shared_equipment_value);
        mSharedEquipmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = SharedEquimentDialogFragment.newInstance(
                        ModelUtils.toIntList(mSharedEquipment));
                newFragment.show(getActivity().getSupportFragmentManager(), "SharedEquipmentDialogFragment");
            }
        });

        mNoteEditText = (EditText) view.findViewById(R.id.note_value);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_availability_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_done:
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
                mLocationLatitude = place.getLatLng().latitude;
                mLocationLongitude = place.getLatLng().longitude;
                mLocationAddress = place.getAddress().toString();
                mLocationName = place.getName().toString();
                mLocationTextView.setText(mLocationName + ", " + mLocationAddress
                        + " [" + mLocationLatitude + ","+ mLocationLongitude + "]");
            }
        }
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        mTargetDateTextView.setText(String.format(Locale.getDefault(),
                "%04d.%02d.%02d", year, month + 1, dayOfMonth));
        adjustStartAndEndTime(mTargetDateTextView == mStartDateTextView);
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        mTargetTimeTextView.setText(String.format(Locale.getDefault(),
                "%02d:%02d", hourOfDay, minute));
        adjustStartAndEndTime(mTargetTimeTextView == mStartTimeTextView);
    }

    @Override
    public void onActivitySelected(List<Integer> activities) {
        mActivities = ModelUtils.toCommaSeparatedString(activities);
        mActivityTextView.setText(ModelUtils.createActivityNameList(getContext(), activities));
    }

    @Override
    public void onNeedPartnerItemSelected(int itemIndex) {
        mNeedPartner = itemIndex;
        mNeedPartnerTextView.setText(ModelUtils.createNeedPartnerText(getContext(), itemIndex));
    }

    @Override
    public void onIfNoPartnerItemSelected(int itemIndex) {
        mIfNoPartner = itemIndex;
        mIfNoPartnerTextView.setText(ModelUtils.createIfNoPartnerText(getContext(), itemIndex));
    }

    @Override
    public void onEquipmentSelected(List<Integer> equipments) {
        mSharedEquipment = ModelUtils.toCommaSeparatedString(equipments);
        mSharedEquipmentTextView.setText(ModelUtils.createEquipmentNameList(getContext(), equipments));
    }

    private void adjustStartAndEndTime(boolean isTriggeredByStartTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
            String startDateString = mStartDateTextView.getText().toString();
            String startTimeString = mStartTimeTextView.getText().toString();
            String startDateTimeString = startDateString + " " + startTimeString;
            Date date = dateFormat.parse(startDateTimeString);
            mStartTime = date.getTime();

            String endDateString = mEndDateTextView.getText().toString();
            String endTimeString = mEndTimeTextView.getText().toString();
            String endDateTimeString = endDateString + " " + endTimeString;
            date = dateFormat.parse(endDateTimeString);
            mEndTime = date.getTime();

            if (isTriggeredByStartTime) {
                if (mEndTime <= mStartTime) {
                    mEndTime = mStartTime + TWO_HOURS_IN_MILLIS;
                    mEndDateTextView.setText(formatDateForTextView(mEndTime));
                    mEndTimeTextView.setText(formatTimeForTextView(mEndTime));
                }
            } else {
                if (mEndTime <= mStartTime) {
                    mStartTime = mEndTime - TWO_HOURS_IN_MILLIS;
                    mStartDateTextView.setText(formatDateForTextView(mStartTime));
                    mStartTimeTextView.setText(formatTimeForTextView(mStartTime));
                }
            }
        } catch (ParseException e) {
            // TODO: Handle error.
        }
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

    private void addAvailability() {
        Activity activity = getActivity();
        if (activity != null
                && (activity instanceof MainActivity)) {
            MainActivity mainActivity = (MainActivity) activity;
            User user = mainActivity.getUser();
            if (user != null) {
                if (mLocationLatitude == Double.MAX_VALUE || mLocationLongitude == Double.MAX_VALUE) {
                    Toast.makeText(activity, R.string.add_availability_missing_location, Toast.LENGTH_SHORT).show();
                    mLocationTextView.setError("");
                    return;
                }
                if (mActivities.isEmpty()) {
                    Toast.makeText(activity, R.string.add_availability_missing_activity, Toast.LENGTH_SHORT).show();
                    mActivityTextView.setError("");
                    return;
                }
                if (mNeedPartner == Availability.NEED_PARTNER_UNDEFINED) {
                    Toast.makeText(activity, R.string.add_availability_missing_need_partner, Toast.LENGTH_SHORT).show();
                    mNeedPartnerTextView.setError("");
                    return;
                }

                Availability availability = new Availability();
                availability.setUserKey(user.getKey());
                availability.setUserName(user.getName());
                availability.setLocationLatitude(mLocationLatitude);
                availability.setLocationLongitude(mLocationLongitude);
                availability.setLocationName(mLocationName);
                availability.setLocationAddress(mLocationAddress);
                availability.setStartTime(mStartTime);
                availability.setEndTime(mEndTime);
                availability.setActivity(mActivities);
                availability.setNeedPartner(mNeedPartner);
                if (availability.getNeedPartner() == Availability.NEED_PARTNER_YES) {
                    if (mIfNoPartner == Availability.IF_NO_PARTNER_UNDEFINED) {
                        Toast.makeText(activity, R.string.add_availability_missing_no_partner, Toast.LENGTH_SHORT).show();
                        mIfNoPartnerTextView.setError("");
                        return;
                    }
                    availability.setIfNoPartner(mIfNoPartner);
                } else {
                    availability.setIfNoPartner(
                            (mIfNoPartner == Availability.IF_NO_PARTNER_UNDEFINED)
                            ? Availability.IF_NO_PARTNER_NOT_DECIDED_YET
                            : mIfNoPartner);
                }
                availability.setSharedEquipment(mSharedEquipment);
                availability.setCanBelay(user.getCanBelay());
                availability.setGrades(user.getGrades());
                availability.setNote(mNoteEditText.getText().toString().trim().replace('\n', ' '));

                DatabaseReference availabilityRef = mAvailabilityTable.push();
                availabilityRef.setValue(availability);

                mainActivity.onFragmentAction(INTERACTION_DONE_TAPPED, null);
            }
        }
    }

}
