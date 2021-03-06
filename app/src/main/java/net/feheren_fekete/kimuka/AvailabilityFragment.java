package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.feheren_fekete.kimuka.dialog.ActivityDialogFragment;
import net.feheren_fekete.kimuka.dialog.CanBelayDialogFragment;
import net.feheren_fekete.kimuka.dialog.DatePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.IfNoPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.NeedPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.SharedEquimentDialogFragment;
import net.feheren_fekete.kimuka.dialog.TimePickerDialogFragment;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AvailabilityFragment
        extends
                BaseFragment
        implements
                DatePickerDialogFragment.Listener,
                TimePickerDialogFragment.Listener,
                ActivityDialogFragment.Listener,
                IfNoPartnerDialogFragment.Listener,
                SharedEquimentDialogFragment.Listener,
                NeedPartnerDialogFragment.Listener,
                CanBelayDialogFragment.Listener {

    private static final String TAG = AvailabilityFragment.class.getSimpleName();

    public static final String INTERACTION_DONE_TAPPED = AvailabilityFragment.class.getSimpleName() + ".INTERACTION_DONE_TAPPED";
    public static final String INTERACTION_SEND_REQUEST_TAPPED = AvailabilityFragment.class.getSimpleName() + ".INTERACTION_SEND_REQUEST_TAPPED";

    public static final String DATA_AVAILABILITY_KEY = AvailabilityFragment.class.getSimpleName() + ".DATA_AVAILABILITY_KEY";

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int ONE_HOUR_IN_MILLIS = 1000 * 60 * 60;
    private static final int TWO_HOURS_IN_MILLIS = ONE_HOUR_IN_MILLIS * 2;

    private static final String ARG_AVAILABILITY_KEY = AvailabilityFragment.class.getSimpleName() + ".ARG_AVAILABILITY_KEY";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAvailabilityTable;

    private TextView mPartnerTitleTextView;
    private TextView mPartnerNameTextView;
    private TextView mLocationTextView;
    private TextView mStartDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndDateTextView;
    private TextView mEndTimeTextView;
    private boolean mIsAdjustingStartTime;
    private TextView mActivityTextView;
    private TextView mCanBelayTextView;
    private TextView mNeedPartnerTextView;
    private TextView mIfNoPartnerTextView;
    private TextView mSharedEquipmentTextView;
    private EditText mNoteEditText;

    private Availability mAvailability = new Availability();
    private boolean mIsNewAvailability;

    public AvailabilityFragment() {
    }

    public static AvailabilityFragment newInstance(String availabilityKey) {
        AvailabilityFragment fragment = new AvailabilityFragment();
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

        View view = inflater.inflate(R.layout.fragment_availability, container, false);

        mPartnerTitleTextView = (TextView) view.findViewById(R.id.partner_title);
        mPartnerNameTextView = (TextView) view.findViewById(R.id.partner_value);
        mPartnerNameTextView.setEnabled(false);

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
                mStartTimeTextView.setError(null);
                mEndTimeTextView.setError(null);
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
                mEndTimeTextView.setError(null);
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
                mStartTimeTextView.setError(null);
                mEndTimeTextView.setError(null);
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
                mStartTimeTextView.setError(null);
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
                        ModelUtils.toIntList(mAvailability.getActivity()));
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
                        ModelUtils.toIntList(mAvailability.getSharedEquipment()));
                newFragment.show(getActivity().getSupportFragmentManager(), "SharedEquipmentDialogFragment");
            }
        });

        mNoteEditText = (EditText) view.findViewById(R.id.note_value);
        mNoteEditText.addTextChangedListener(mNoteTextWatcher);

        String availabilityKey = getArguments().getString(ARG_AVAILABILITY_KEY, "");
        if (!availabilityKey.isEmpty()) {
            mIsNewAvailability = false;
            loadAvailabilityAndInitViews(availabilityKey);
        } else {
            mIsNewAvailability = true;
            initNewAvailability();
            updateViews();
        }

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

        if (mAvailability != null) {
            boolean canEditAvailability = mIsNewAvailability || (isUserHosting() && nobodyJoinedYet());
            menu.findItem(R.id.action_save_availability).setVisible(canEditAvailability);

            boolean canSendRequest = (!mIsNewAvailability && !isUserHosting() && nobodyJoinedYet());
            menu.findItem(R.id.action_send_request).setVisible(canSendRequest);

            boolean canCancel = (!mIsNewAvailability && isUserOwner());
            menu.findItem(R.id.action_cancel_availability).setVisible(canCancel);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_send_request:
                sendRequest();
                break;
            case R.id.action_save_availability:
                addOrUpdateAvailability();
                break;
            case R.id.action_cancel_availability:
                // TODO: cancelAvailability();
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
                mAvailability.setLocationLatitude(place.getLatLng().latitude);
                mAvailability.setLocationLongitude(place.getLatLng().longitude);
                mAvailability.setLocationAddress(place.getAddress().toString());
                mAvailability.setLocationName(place.getName().toString());
                updateViews();
            }
        }
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        if (mIsAdjustingStartTime) {
            calendar.setTimeInMillis(mAvailability.getStartTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mAvailability.setStartTime(calendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mAvailability.getEndTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mAvailability.setEndTime(calendar.getTimeInMillis());
        }
        adjustStartAndEndTime(mIsAdjustingStartTime);
        updateViews();
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mIsAdjustingStartTime) {
            calendar.setTimeInMillis(mAvailability.getStartTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            mAvailability.setStartTime(calendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mAvailability.getEndTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            mAvailability.setEndTime(calendar.getTimeInMillis());
        }
        adjustStartAndEndTime(mIsAdjustingStartTime);
        updateViews();
    }

    @Override
    public void onActivitySelected(List<Integer> activities) {
        mAvailability.setActivity(ModelUtils.toCommaSeparatedString(activities));
        updateViews();
    }

    @Override
    public void onCanBelayItemSelected(int itemIndex) {
        mAvailability.setCanBelay(itemIndex);
        updateViews();
    }

    @Override
    public void onNeedPartnerItemSelected(int itemIndex) {
        mAvailability.setNeedPartner(itemIndex);
        updateViews();
    }

    @Override
    public void onIfNoPartnerItemSelected(int itemIndex) {
        mAvailability.setIfNoPartner(itemIndex);
        updateViews();
    }

    @Override
    public void onEquipmentSelected(List<Integer> equipments) {
        mAvailability.setSharedEquipment(ModelUtils.toCommaSeparatedString(equipments));
        updateViews();
    }

    private void initNewAvailability() {
        mAvailability.setUserKey(getUser().getKey());
        mAvailability.setUserName(getUser().getName());
        mAvailability.setHostUser(true);
        mAvailability.setCanBelay(getUser().getCanBelay());
        mAvailability.setGrades(getUser().getGrades());
        mAvailability.setLocationLatitude(Double.MAX_VALUE);
        mAvailability.setLocationLongitude(Double.MAX_VALUE);
        long twoHoursAhead = (System.currentTimeMillis() + TWO_HOURS_IN_MILLIS);
        long twoHoursAheadRounded = (twoHoursAhead / ONE_HOUR_IN_MILLIS) * ONE_HOUR_IN_MILLIS;
        mAvailability.setStartTime(twoHoursAheadRounded);
        long threeHoursAheadRounded = twoHoursAheadRounded + ONE_HOUR_IN_MILLIS;
        mAvailability.setEndTime(threeHoursAheadRounded);
        mAvailability.setLocationAddress("");
        mAvailability.setLocationName("");
        mAvailability.setActivity("");
        mAvailability.setNeedPartner(Availability.NEED_PARTNER_UNDEFINED);
        mAvailability.setIfNoPartner(Availability.IF_NO_PARTNER_UNDEFINED);
        mAvailability.setSharedEquipment("");
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
            mAvailability.setNote(editable.toString().trim().replace('\n', ' '));
        }
    };

    private void adjustStartAndEndTime(boolean isTriggeredByStartTime) {
//        long startTime = mAvailability.getStartTime();
//        long endTime = mAvailability.getEndTime();
//        if (isTriggeredByStartTime) {
//            if (endTime <= startTime) {
//                endTime = startTime + TWO_HOURS_IN_MILLIS;
//            }
//        } else {
//            if (endTime <= startTime) {
//                startTime = endTime - TWO_HOURS_IN_MILLIS;
//            }
//        }
//        mAvailability.setStartTime(startTime);
//        mAvailability.setEndTime(endTime);
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
                    updateViews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Show toast, close fragment. Report error.
            }
        });
    }

    private boolean isUserHosting() {
        return getUser().getKey().equals(mAvailability.getUserKey())
                && mAvailability.isHostUser();
    }

    private boolean isUserOwner() {
        return getUser().getKey().equals(mAvailability.getUserKey());
    }

    private boolean nobodyJoinedYet() {
        return mAvailability.getJoinedAvailabilityKeys().isEmpty();
    }

    private void updateViews() {
        if (getContext() == null || getMainActivity() == null) {
            return;
        }

        mPartnerNameTextView.setText(mAvailability.getUserName());
        if (isUserOwner()) {
            mPartnerTitleTextView.setVisibility(View.GONE);
            mPartnerNameTextView.setVisibility(View.GONE);
        } else {
            mPartnerTitleTextView.setVisibility(View.VISIBLE);
            mPartnerNameTextView.setVisibility(View.VISIBLE);
        }

        if (mAvailability.getLocationLatitude() != Double.MAX_VALUE) {
            mLocationTextView.setText(
                    mAvailability.getLocationName() + ", "
                            + mAvailability.getLocationAddress()
                            + " [" + mAvailability.getLocationLatitude() + "," + mAvailability.getLocationLongitude() + "]");
        } else {
            mLocationTextView.setText("");
        }

        mStartDateTextView.setText(formatDateForTextView(mAvailability.getStartTime()));
        mStartTimeTextView.setText(formatTimeForTextView(mAvailability.getStartTime()));

        mEndDateTextView.setText(formatDateForTextView(mAvailability.getEndTime()));
        mEndTimeTextView.setText(formatTimeForTextView(mAvailability.getEndTime()));

        mActivityTextView.setText(
                ModelUtils.createActivityNameList(
                        getContext(),
                        ModelUtils.toIntList(mAvailability.getActivity())));

        mCanBelayTextView.setText(
                ModelUtils.createCanBelayText(getContext(), mAvailability.getCanBelay()));

        int needPartner = mAvailability.getNeedPartner();
        mNeedPartnerTextView.setText(
                (needPartner != Availability.NEED_PARTNER_UNDEFINED)
                ? ModelUtils.createNeedPartnerText(getContext(), needPartner)
                : "");

        int ifNoPartner = mAvailability.getIfNoPartner();
        mIfNoPartnerTextView.setText(
                (ifNoPartner != Availability.IF_NO_PARTNER_UNDEFINED)
                ? ModelUtils.createIfNoPartnerText(getContext(), ifNoPartner)
                : "");

        mSharedEquipmentTextView.setText(
                ModelUtils.createEquipmentNameList(
                        getContext(),
                        ModelUtils.toIntList(mAvailability.getSharedEquipment())));

        mNoteEditText.setText(mAvailability.getNote());

        boolean isEditable = mIsNewAvailability || (isUserHosting() && nobodyJoinedYet());
        if (!isEditable) {
            mLocationTextView.setEnabled(false);
            mStartDateTextView.setEnabled(false);
            mStartTimeTextView.setEnabled(false);
            mEndDateTextView.setEnabled(false);
            mEndTimeTextView.setEnabled(false);
            mActivityTextView.setEnabled(false);
            mCanBelayTextView.setEnabled(false);
            mNeedPartnerTextView.setEnabled(false);
            mIfNoPartnerTextView.setEnabled(false);
            mSharedEquipmentTextView.setEnabled(false);
            mNoteEditText.setEnabled(false);
        }

        if (mIsNewAvailability) {
            getMainActivity().getSupportActionBar().setTitle(R.string.availability_title_new);
        } else if (isUserHosting() && nobodyJoinedYet()) {
            getMainActivity().getSupportActionBar().setTitle(R.string.availability_title_edit);
        } else {
            getMainActivity().getSupportActionBar().setTitle(R.string.availability_title_view);
        }

        getActivity().invalidateOptionsMenu();
    }

    private void addOrUpdateAvailability() {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            if (mAvailability.getLocationLatitude() == Double.MAX_VALUE
                    || mAvailability.getLocationLongitude() == Double.MAX_VALUE) {
                Toast.makeText(activity, R.string.availability_error_missing_location, Toast.LENGTH_SHORT).show();
                mLocationTextView.setError("");
                return;
            }
            if (mAvailability.getStartTime() >= mAvailability.getEndTime()) {
                Toast.makeText(activity, R.string.availability_error_bad_time, Toast.LENGTH_SHORT).show();
                mStartTimeTextView.setError("");
                mEndTimeTextView.setError("");
                return;
            }
            if (mAvailability.getActivity().isEmpty()) {
                Toast.makeText(activity, R.string.availability_error_missing_activity, Toast.LENGTH_SHORT).show();
                mActivityTextView.setError("");
                return;
            }
            if (mAvailability.getNeedPartner() == Availability.NEED_PARTNER_UNDEFINED) {
                Toast.makeText(activity, R.string.availability_error_missing_need_partner, Toast.LENGTH_SHORT).show();
                mNeedPartnerTextView.setError("");
                return;
            }

            if (mAvailability.getNeedPartner() == Availability.NEED_PARTNER_YES) {
                if (mAvailability.getIfNoPartner() == Availability.IF_NO_PARTNER_UNDEFINED) {
                    Toast.makeText(activity, R.string.availability_error_missing_no_partner, Toast.LENGTH_SHORT).show();
                    mIfNoPartnerTextView.setError("");
                    return;
                }
            } else {
                mAvailability.setIfNoPartner(
                        (mAvailability.getIfNoPartner() == Availability.IF_NO_PARTNER_UNDEFINED)
                        ? Availability.IF_NO_PARTNER_NOT_DECIDED_YET
                        : mAvailability.getIfNoPartner());
            }

            DatabaseReference availabilityRef;
            if (mIsNewAvailability) {
                availabilityRef = mAvailabilityTable.push();
            } else {
                availabilityRef = mAvailabilityTable.child(mAvailability.getKey());
            }
            availabilityRef.setValue(mAvailability);

            activity.onFragmentAction(INTERACTION_DONE_TAPPED, new Bundle());
        }
    }

    private void sendRequest() {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            Bundle data = new Bundle();
            data.putString(DATA_AVAILABILITY_KEY, mAvailability.getKey());
            activity.onFragmentAction(INTERACTION_SEND_REQUEST_TAPPED, data);
        }
    }

}
