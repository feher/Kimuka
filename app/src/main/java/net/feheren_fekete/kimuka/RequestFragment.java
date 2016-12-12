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
                BaseFragment
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
    private static final String ARG_REQUEST_KEY = RequestFragment.class.getSimpleName() + ".ARG_REQUEST_KEY";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAvailabilityTable;
    private DatabaseReference mRequestTable;

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
    private boolean mIsNewRequest;

    public RequestFragment() {
    }

    public static RequestFragment newInstance(String availabilityKey, String requestKey) {
        RequestFragment fragment = new RequestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_AVAILABILITY_KEY, availabilityKey);
        bundle.putString(ARG_REQUEST_KEY, requestKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAvailabilityTable = mDatabase.getReference().child(ModelUtils.TABLE_AVAILABILITY);
        mRequestTable = mDatabase.getReference().child(ModelUtils.TABLE_REQUEST);
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
                mStartDateTextView.setError(null);
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
                mStartTimeTextView.setError(null);
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
                mEndDateTextView.setError(null);
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
                mEndTimeTextView.setError(null);
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
        String requestKey = getArguments().getString(ARG_REQUEST_KEY, "");
        if (!availabilityKey.isEmpty()) {
            mIsNewRequest = true;
            loadAvailabilityAndInitViews(availabilityKey);
        } else if (!requestKey.isEmpty()) {
            mIsNewRequest = false;
            loadRequestAndInitViews(requestKey);
        } else {
            // TODO: Handle error.
            throw new RuntimeException();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.request_menu, menu);
        if (mRequest != null) {
            if (mIsNewRequest) {
                menu.findItem(R.id.action_send_request).setVisible(true);
                menu.findItem(R.id.action_accept_request).setVisible(false);
                menu.findItem(R.id.action_reject_request).setVisible(false);
                menu.findItem(R.id.action_cancel_request).setVisible(false);
            } else {
                if (isUserSender()) {
                    menu.findItem(R.id.action_send_request).setVisible(false);
                    menu.findItem(R.id.action_accept_request).setVisible(false);
                    menu.findItem(R.id.action_reject_request).setVisible(false);
                    menu.findItem(R.id.action_cancel_request).setVisible(true);
                } else if (isUserReceiver()) {
                    menu.findItem(R.id.action_send_request).setVisible(false);
                    menu.findItem(R.id.action_accept_request).setVisible(true);
                    menu.findItem(R.id.action_reject_request).setVisible(true);
                    menu.findItem(R.id.action_cancel_request).setVisible(false);
                }
            }
        }
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
                    mRequest.setAvailabilityKey(mAvailability.getKey());
                    mRequest.setSenderKey(getUser().getKey());
                    mRequest.setSenderName(getUser().getName());
                    mRequest.setReceiverKey(mAvailability.getUserKey());
                    mRequest.setReceiverName(mAvailability.getUserName());
                    mRequest.setStartTime(mAvailability.getStartTime());
                    mRequest.setEndTime(mAvailability.getEndTime());
                    mRequest.setActivity(mAvailability.getActivity());
                    mRequest.setCanBelay(getUser().getCanBelay());
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

    private void loadRequestAndInitViews(String requestKey) {
        mRequestTable.child(requestKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mRequest = dataSnapshot.getValue(Request.class);
                    mRequest.setKey(dataSnapshot.getKey());
                    mAvailabilityTable.child(mRequest.getAvailabilityKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                mAvailability = dataSnapshot.getValue(Availability.class);
                                mAvailability.setKey(dataSnapshot.getKey());
                                updateViews();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // TODO: Handle error.
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Show toast, close fragment. Report error.
            }
        });
    }

    private boolean isUserSender() {
        return mRequest.getSenderKey().equals(getUser().getKey());
    }

    private boolean isUserReceiver() {
        return mRequest.getReceiverKey().equals(getUser().getKey());
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

        if (!mIsNewRequest) {
            mPartnerTextView.setEnabled(false);
            mLocationTextView.setEnabled(false);
            mStartDateTextView.setEnabled(false);
            mStartTimeTextView.setEnabled(false);
            mEndDateTextView.setEnabled(false);
            mEndTimeTextView.setEnabled(false);
            mActivityTextView.setEnabled(false);
            mCanBelayTextView.setEnabled(false);
            mSharedEquipmentTextView.setEnabled(false);
            mNoteEditText.setEnabled(false);
        }

        getActivity().invalidateOptionsMenu();
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

    private boolean isTimeOverlapping() {
        long startTimeA = mAvailability.getStartTime();
        long endTimeA = mAvailability.getEndTime();
        long startTimeR = mRequest.getStartTime();
        long endTimeR = mRequest.getEndTime();
        return (startTimeR < endTimeA) && (startTimeA < endTimeR);
    }

    private void sendRequest() {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            if (!isTimeOverlapping()) {
                Toast.makeText(activity, R.string.request_error_bad_time, Toast.LENGTH_SHORT).show();
                mStartTimeTextView.setError("");
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

            DatabaseReference requestRef = mRequestTable.push();
            requestRef.setValue(mRequest);

            activity.onFragmentAction(INTERACTION_SEND_TAPPED, new Bundle());
        }
    }

}
