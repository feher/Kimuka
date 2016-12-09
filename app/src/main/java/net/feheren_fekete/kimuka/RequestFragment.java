package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.feheren_fekete.kimuka.dialog.ActivityDialogFragment;
import net.feheren_fekete.kimuka.dialog.CanBelayDialogFragment;
import net.feheren_fekete.kimuka.dialog.DatePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.SharedEquimentDialogFragment;
import net.feheren_fekete.kimuka.dialog.TimePickerDialogFragment;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.Request;
import net.feheren_fekete.kimuka.model.User;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class RequestFragment
        extends
                Fragment
        implements
                DatePickerDialogFragment.Listener,
                TimePickerDialogFragment.Listener,
                ActivityDialogFragment.Listener,
                SharedEquimentDialogFragment.Listener,
                CanBelayDialogFragment.Listener {

    private static final String TAG = RequestFragment.class.getSimpleName();

    public static final String INTERACTION_SEND_TAPPED = RequestFragment.class.getSimpleName() + ".INTERACTION_SEND_TAPPED";

    private static final int ONE_HOUR_IN_MILLIS = 1000 * 60 * 60;
    private static final int TWO_HOURS_IN_MILLIS = ONE_HOUR_IN_MILLIS * 2;

    private static final String ARG_AVAILABILITY_KEY = RequestFragment.class.getSimpleName() + ".ARG_AVAILABILITY_KEY";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAvailabilityTable;

    private TextView mPartnerTextView;
    private TextView mLocationTextView;
    private TextView mStartDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndDateTextView;
    private TextView mEndTimeTextView;
    private boolean mIsAdjustingStartTime;
    private TextView mActivityTextView;
    private TextView mCanBelayTextView;
    private TextView mSharedEquipmentTextView;
    private EditText mNoteEditText;

    private User mUser;
    private Availability mAvailability = new Availability();
    private Request mRequest = new Request();

    public RequestFragment() {
    }

    public static RequestFragment newInstance(String availabilityKey) {
        RequestFragment fragment = new RequestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_AVAILABILITY_KEY, availabilityKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAvailabilityTable = mDatabase.getReference().child(ModelUtils.TABLE_AVAILABILITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_request, container, false);

        mPartnerTextView = (TextView) view.findViewById(R.id.partner_value);
        mLocationTextView = (TextView) view.findViewById(R.id.location_value);

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
                        ModelUtils.toIntList(mRequest.getActivity()));
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

        mSharedEquipmentTextView = (TextView) view.findViewById(R.id.shared_equipment_value);
        mSharedEquipmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = SharedEquimentDialogFragment.newInstance(
                        ModelUtils.toIntList(mRequest.getSharedEquipment()));
                newFragment.show(getActivity().getSupportFragmentManager(), "SharedEquipmentDialogFragment");
            }
        });

        mNoteEditText = (EditText) view.findViewById(R.id.note_value);
        mNoteEditText.addTextChangedListener(mNoteTextWatcher);

        String availabilityKey = getArguments().getString(ARG_AVAILABILITY_KEY, "");
        if (!availabilityKey.isEmpty()) {
            loadAvailabilityAndInitViews(availabilityKey);
        } else {
            // TODO: Handle error.
            throw new RuntimeException();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null
                && (activity instanceof MainActivity)) {
            MainActivity mainActivity = (MainActivity) activity;
            User user = mainActivity.getUser();
            if (user != null) {
                mUser = user;
            } else {
                // TODO: Handle error. Report exception. Close fragment.
                throw new RuntimeException();
            }
        } else {
            // TODO: Handle error. Report exception. Close fragment.
            throw new RuntimeException();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.request_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_send_request:
                sendRequest();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        if (mIsAdjustingStartTime) {
            calendar.setTimeInMillis(mRequest.getStartTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mRequest.setStartTime(calendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mRequest.getEndTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mRequest.setEndTime(calendar.getTimeInMillis());
        }
        adjustStartAndEndTime(mIsAdjustingStartTime);
        updateViews();
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mIsAdjustingStartTime) {
            calendar.setTimeInMillis(mRequest.getStartTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            mRequest.setStartTime(calendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mRequest.getEndTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            mRequest.setEndTime(calendar.getTimeInMillis());
        }
        adjustStartAndEndTime(mIsAdjustingStartTime);
        updateViews();
    }

    @Override
    public void onActivitySelected(List<Integer> activities) {
        mRequest.setActivity(ModelUtils.toCommaSeparatedString(activities));
        updateViews();
    }

    @Override
    public void onCanBelayItemSelected(int itemIndex) {
        mRequest.setCanBelay(itemIndex);
        updateViews();
    }

    @Override
    public void onEquipmentSelected(List<Integer> equipments) {
        mRequest.setSharedEquipment(ModelUtils.toCommaSeparatedString(equipments));
        updateViews();
    }

    private TextWatcher mNoteTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mRequest.setNote(editable.toString().trim().replace('\n', ' '));
        }
    };

    private void adjustStartAndEndTime(boolean isTriggeredByStartTime) {
        long startTime = mRequest.getStartTime();
        long endTime = mRequest.getEndTime();
        if (isTriggeredByStartTime) {
            if (endTime <= startTime) {
                endTime = startTime + TWO_HOURS_IN_MILLIS;
            }
        } else {
            if (endTime <= startTime) {
                startTime = endTime - TWO_HOURS_IN_MILLIS;
            }
        }
        mRequest.setStartTime(startTime);
        mRequest.setEndTime(endTime);
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

    private void loadAvailabilityAndInitViews(String availabilityKey) {
        mAvailabilityTable.child(availabilityKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mAvailability = dataSnapshot.getValue(Availability.class);
                    mAvailability.setKey(dataSnapshot.getKey());
                    mRequest.setSenderKey(mUser.getKey());
                    mRequest.setSenderName(mUser.getName());
                    mRequest.setReceiverKey(mAvailability.getUserKey());
                    mRequest.setReceiverName(mAvailability.getUserName());
                    mRequest.setStartTime(mAvailability.getStartTime());
                    mRequest.setEndTime(mAvailability.getEndTime());
                    mRequest.setActivity(mAvailability.getActivity());
                    mRequest.setCanBelay(mUser.getCanBelay());
                    mRequest.setSharedEquipment("");
                    mRequest.setNote("");
                    updateViews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Show toast, close fragment. Report error.
            }
        });
    }

    private void updateViews() {
        mPartnerTextView.setText(mRequest.getReceiverName());

        if (mAvailability.getLocationLatitude() != Double.MAX_VALUE) {
            mLocationTextView.setText(
                    mAvailability.getLocationName() + ", "
                            + mAvailability.getLocationAddress()
                            + " [" + mAvailability.getLocationLatitude() + "," + mAvailability.getLocationLongitude() + "]");
        } else {
            // TODO: Handle error.
            throw new RuntimeException();
        }

        mStartDateTextView.setText(formatDateForTextView(mRequest.getStartTime()));
        mStartTimeTextView.setText(formatTimeForTextView(mRequest.getStartTime()));

        mEndDateTextView.setText(formatDateForTextView(mRequest.getEndTime()));
        mEndTimeTextView.setText(formatTimeForTextView(mRequest.getEndTime()));

        mActivityTextView.setText(
                ModelUtils.createActivityNameList(
                        getContext(),
                        ModelUtils.toIntList(mRequest.getActivity())));

        mCanBelayTextView.setText(
                ModelUtils.createCanBelayText(getContext(), mRequest.getCanBelay()));

        mSharedEquipmentTextView.setText(
                ModelUtils.createEquipmentNameList(
                        getContext(),
                        ModelUtils.toIntList(mRequest.getSharedEquipment())));

        mNoteEditText.setText(mRequest.getNote());
    }

    private boolean isActivityOverlapping() {
        List<Integer> availableActivities = ModelUtils.toIntList(mAvailability.getActivity());
        List<Integer> requestedActivities = ModelUtils.toIntList(mRequest.getActivity());
        for (Integer requestedActivity : requestedActivities) {
            if (availableActivities.contains(requestedActivity)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeBetween(long minTime, long maxTime, long time) {
        return minTime <= time && time <= maxTime;
    }

    private void sendRequest() {
        Activity activity = getActivity();
        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;
            if (!isTimeBetween(mAvailability.getStartTime(), mAvailability.getEndTime(), mRequest.getStartTime())) {
                Toast.makeText(activity, R.string.request_error_bad_time, Toast.LENGTH_SHORT).show();
                mStartDateTextView.setError("");
                mStartTimeTextView.setError("");
                return;
            }
            if (!isTimeBetween(mAvailability.getStartTime(), mAvailability.getEndTime(), mRequest.getEndTime())) {
                Toast.makeText(activity, R.string.request_error_bad_time, Toast.LENGTH_SHORT).show();
                mEndDateTextView.setError("");
                mEndTimeTextView.setError("");
                return;
            }
            if (mRequest.getActivity().isEmpty()) {
                Toast.makeText(activity, R.string.request_error_missing_activity, Toast.LENGTH_SHORT).show();
                mActivityTextView.setError("");
                return;
            }
            if (!isActivityOverlapping()) {
                Toast.makeText(activity, R.string.request_error_missing_common_activity, Toast.LENGTH_SHORT).show();
                mActivityTextView.setError("");
                return;
            }

            DatabaseReference requestTable = mDatabase.getReference().child(ModelUtils.TABLE_REQUEST);
            DatabaseReference requestRef = requestTable.push();
            requestRef.setValue(mRequest);

            mainActivity.onFragmentAction(INTERACTION_SEND_TAPPED, new Bundle());
        }
    }

}
