package net.feheren_fekete.kimuka;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements FragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private SignInFragment mSignInFragment;
    private DayFragment mDayFragment;
    private AddAvailabilityFragment mAddAvailabilityFragment;
    private Fragment mActiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentAction(String action, @Nullable Bundle data) {
        if (AddAvailabilityFragment.INTERACTION_DONE_TAPPED.equals(action)) {
            showDayFragment();
        } else if (DayFragment.INTERACTION_ADD_AVAILABILITY_TAPPED.equals(action)) {
            showAddAvailabilityFragment();
        } else if (TimePickerDialogFragment.INTERCATION_TIME_PICKED.equals(action)
                && data != null
                && mActiveFragment instanceof TimePickerDialogFragment.Listener) {
            TimePickerDialogFragment.Listener listener = (TimePickerDialogFragment.Listener) mActiveFragment;
            listener.onTimeSet(
                    data.getInt(TimePickerDialogFragment.DATA_HOUR_OF_DAY),
                    data.getInt(TimePickerDialogFragment.DATA_MINUTE));
        } else if (DatePickerDialogFragment.INTERCATION_DATE_PICKED.equals(action)
                && data != null
                && mActiveFragment instanceof DatePickerDialogFragment.Listener) {
            DatePickerDialogFragment.Listener listener = (DatePickerDialogFragment.Listener) mActiveFragment;
            listener.onDateSet(
                    data.getInt(DatePickerDialogFragment.DATA_YEAR),
                    data.getInt(DatePickerDialogFragment.DATA_MONTH),
                    data.getInt(DatePickerDialogFragment.DATA_DAY_OF_MONTH));
        }
    }

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                showDayFragment();
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
                showSignInFragment();
            }
        }
    };

    private void showSignInFragment() {
        if (mSignInFragment == null) {
            mSignInFragment = SignInFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mSignInFragment, "SignInFragment")
                .commit();
        mActiveFragment = mSignInFragment;
    }

    private void showDayFragment() {
        if (mDayFragment == null) {
            mDayFragment = DayFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mDayFragment, "DayFragment")
                .commit();
        mActiveFragment = mDayFragment;
    }

    private void showAddAvailabilityFragment() {
        if (mAddAvailabilityFragment == null) {
            mAddAvailabilityFragment = AddAvailabilityFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mAddAvailabilityFragment, "AddAvailabilityFragment")
                .commit();
        mActiveFragment = mAddAvailabilityFragment;
    }

}
