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
import net.feheren_fekete.kimuka.model.User;

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
        mLocationTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mLocationTextView.setText("");
                mFilter.setLocationLatitude(null);
                mFilter.setLocationLongitude(null);
                mFilter.setLocationName(null);
                mFilter.setLocationAddress(null);
                return true;
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
        View.OnLongClickListener startDateTimeLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mStartDateTextView.setText("");
                mStartDateTextView.setText("");
                mFilter.setStartTime(null);
                return true;
            }
        };
        mStartDateTextView.setOnLongClickListener(startDateTimeLongClickListener);
        mStartTimeTextView.setOnLongClickListener(startDateTimeLongClickListener);

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
        View.OnLongClickListener endDateTimeLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mEndDateTextView.setText("");
                mEndTimeTextView.setText("");
                mFilter.setEndTime(null);
                return true;
            }
        };
        mEndDateTextView.setOnLongClickListener(endDateTimeLongClickListener);
        mEndTimeTextView.setOnLongClickListener(endDateTimeLongClickListener);

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
        mActivityTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mActivityTextView.setText("");
                mFilter.setActivity(null);
                return true;
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
        mCanBelayTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCanBelayTextView.setText("");
                mFilter.setCanBelay(null);
                return true;
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
        mNeedPartnerTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mNeedPartnerTextView.setText("");
                mFilter.setNeedPartner(null);
                return true;
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
        mSharedEquipmentTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mSharedEquipmentTextView.setText("");
                mFilter.setSharedEquipment(null);
                return true;
            }
        });

        mIsNewFilter = true;
        initNewFilter();
        updateViews();

        return view;
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
            case R.id.action_reset_filter:
                resetFilter();
            case R.id.action_save_filter:
                saveFilter();
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
        if (!activities.isEmpty()) {
            mFilter.setActivity(ModelUtils.toCommaSeparatedString(activities));
        } else {
            mFilter.setActivity(null);
        }
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
        if (!equipments.isEmpty()) {
            mFilter.setSharedEquipment(ModelUtils.toCommaSeparatedString(equipments));
        } else {
            mFilter.setSharedEquipment(null);
        }
        updateViews();
    }

    private void initNewFilter() {
        mFilter = new Filter();
        mFilter.setCanBelay(User.CAN_BELAY_YES);
        long twoHoursAhead = (System.currentTimeMillis() + TWO_HOURS_IN_MILLIS);
        long twoHoursAheadRounded = (twoHoursAhead / ONE_HOUR_IN_MILLIS) * ONE_HOUR_IN_MILLIS;
        mFilter.setStartTime(twoHoursAheadRounded);
        long threeHoursAheadRounded = twoHoursAheadRounded + ONE_HOUR_IN_MILLIS;
        mFilter.setEndTime(threeHoursAheadRounded);
        mFilter.setNeedPartner(Availability.NEED_PARTNER_YES);
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
        if (mFilter.getLocationLatitude() != null
                && mFilter.getLocationLatitude() != Double.MAX_VALUE) {
            mLocationTextView.setText(
                    mFilter.getLocationName() + ", "
                            + mFilter.getLocationAddress()
                            + " [" + mFilter.getLocationLatitude() + "," + mFilter.getLocationLongitude() + "]");
        } else {
            mLocationTextView.setText("");
        }

        if (mFilter.getStartTime() != null) {
            mStartDateTextView.setText(formatDateForTextView(mFilter.getStartTime()));
            mStartTimeTextView.setText(formatTimeForTextView(mFilter.getStartTime()));
        } else {
            mStartDateTextView.setText("");
            mStartTimeTextView.setText("");
        }

        if (mFilter.getEndTime() != null) {
            mEndDateTextView.setText(formatDateForTextView(mFilter.getEndTime()));
            mEndTimeTextView.setText(formatTimeForTextView(mFilter.getEndTime()));
        } else {
            mEndDateTextView.setText("");
            mEndTimeTextView.setText("");
        }

        if (mFilter.getActivity() != null) {
            mActivityTextView.setText(
                    ModelUtils.createActivityNameList(
                            getContext(),
                            ModelUtils.toIntList(mFilter.getActivity())));
        } else {
            mActivityTextView.setText("");
        }

        if (mFilter.getCanBelay() != null) {
            mCanBelayTextView.setText(
                    ModelUtils.createCanBelayText(getContext(), mFilter.getCanBelay()));
        } else {
            mCanBelayTextView.setText("");
        }

        if (mFilter.getNeedPartner() != null) {
            int needPartner = mFilter.getNeedPartner();
            mNeedPartnerTextView.setText(
                    (needPartner != Availability.NEED_PARTNER_UNDEFINED)
                            ? ModelUtils.createNeedPartnerText(getContext(), needPartner)
                            : "");
        } else {
            mNeedPartnerTextView.setText("");
        }

        if (mFilter.getSharedEquipment() != null) {
            mSharedEquipmentTextView.setText(
                    ModelUtils.createEquipmentNameList(
                            getContext(),
                            ModelUtils.toIntList(mFilter.getSharedEquipment())));
        } else {
            mSharedEquipmentTextView.setText("");
        }

        getActivity().invalidateOptionsMenu();
    }

    private void resetFilter() {
        initNewFilter();
        updateViews();
    }

    private void saveFilter() {
        Activity activity = getActivity();
        if (activity instanceof FragmentInteractionListener) {
            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
            listener.onFragmentAction(INTERACTION_DONE_TAPPED, new Bundle());
        }
    }

}
