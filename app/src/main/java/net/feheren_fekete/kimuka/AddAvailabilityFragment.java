package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                ActivityDialogFragment.Listener {

    private static final String TAG = AddAvailabilityFragment.class.getSimpleName();

    public static final String INTERACTION_DONE_TAPPED = DayFragment.class.getSimpleName() + ".INTERACTION_DONE_TAPPED";

    private static final int PLACE_PICKER_REQUEST = 1;

    @Nullable
    private FragmentInteractionListener mInteractionListener;

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

    private LatLng mLocationLatLng;
    private String mLocationAddress;
    private String mLocationName;
    private String mActivities;

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

        mLocationTextView = (TextView) view.findViewById(R.id.location_value);
        mLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    // TODO: Handle exception.
                }
            }
        });

        long twoHoursAhead = System.currentTimeMillis() + (2 * 1000 * 60 * 60);
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(twoHoursAhead);
        mStartDateTextView = (TextView) view.findViewById(R.id.start_date_value);
        mStartDateTextView.setText(String.format(Locale.getDefault(), "%04d.%02d.%02d",
                startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)));
        mStartDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateParts = mStartDateTextView.getText().toString().split("\\.");
                DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                        Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2]));
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
                mTargetDateTextView = mStartDateTextView;
            }
        });
        mStartTimeTextView = (TextView) view.findViewById(R.id.start_time_value);
        mStartTimeTextView.setText(String.format(Locale.getDefault(), "%02d:00", startCalendar.get(Calendar.HOUR)));
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

        long threeHoursAhead = twoHoursAhead + (1000 * 60 * 60);
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(threeHoursAhead);
        mEndDateTextView = (TextView) view.findViewById(R.id.end_date_value);
        mEndDateTextView.setText(String.format(Locale.getDefault(), "%04d.%02d.%02d",
                endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)));
        mEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateParts = mEndDateTextView.getText().toString().split("\\.");
                DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                        Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2]));
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
                mTargetDateTextView = mEndDateTextView;
            }
        });
        mEndTimeTextView = (TextView) view.findViewById(R.id.end_time_value);
        mEndTimeTextView.setText(String.format(Locale.getDefault(), "%02d:00", endCalendar.get(Calendar.HOUR)));
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
                DialogFragment newFragment = ActivityDialogFragment.newInstance(ModelUtils.toIntList(mActivityTextView.getText().toString()));
                newFragment.show(getActivity().getSupportFragmentManager(), "ActivityDialogFragment");
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mInteractionListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
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
                mLocationLatLng = place.getLatLng();
                mLocationAddress = place.getAddress().toString();
                mLocationName = place.getName().toString();
                mLocationTextView.setText(mLocationName + ", " + mLocationAddress
                        + " [" + mLocationLatLng.latitude + ","+ mLocationLatLng.longitude + "]");
            }
        }
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        mTargetDateTextView.setText(String.format(Locale.getDefault(),
                "%04d.%02d.%02d", year, month, dayOfMonth));
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        mTargetTimeTextView.setText(String.format(Locale.getDefault(),
                "%02d:%02d", hourOfDay, minute));
    }

    @Override
    public void onActivitySelected(List<Integer> activities) {
        mActivities = ModelUtils.toCommaSeparatedString(activities);
        mActivityTextView.setText(ModelUtils.createActivityNameList(getContext(), activities));
    }

    private void addAvailability() {
        Activity activity = getActivity();
        if (activity != null
                && (activity instanceof MainActivity)) {
            MainActivity mainActivity = (MainActivity) activity;
            User user = mainActivity.getUser();
            if (user != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());

                    String startDateString = mStartDateTextView.getText().toString();
                    String startTimeString = mStartTimeTextView.getText().toString();
                    String startDateTimeString = startDateString + " " + startTimeString;
                    Date date = dateFormat.parse(startDateTimeString);
                    long startTime = date.getTime();

                    String endDateString = mEndDateTextView.getText().toString();
                    String endTimeString = mEndTimeTextView.getText().toString();
                    String endDateTimeString = endDateString + " " + endTimeString;
                    date = dateFormat.parse(endDateTimeString);
                    long endTime = date.getTime();

                    Availability availability = new Availability();
                    availability.userKey = user.key;
                    availability.userName = user.name;
                    availability.locationLatitude = mLocationLatLng.latitude;
                    availability.locationLongitude = mLocationLatLng.longitude;
                    availability.locationAddress = mLocationAddress;
                    availability.locationName = mLocationName;
                    availability.startTime = startTime;
                    availability.endTime = endTime;
                    availability.activity = mActivities;
                    availability.ifNoPartner = ???;
                    availability.sharedEquipment = ???;
                    availability.canBelay = user.canBelay;
                    availability.grades = user.grades;
                    availability.note = ???;
                    // TODO: set Availability fields.

                    DatabaseReference availabilityRef = mAvailabilityTable.push();
                    availabilityRef.setValue(availability);
                    if (mInteractionListener != null) {
                        mInteractionListener.onFragmentAction(INTERACTION_DONE_TAPPED, null);
                    }
                } catch (ParseException e) {
                    // TODO: Report exception
                }
            }
        }
    }

}
