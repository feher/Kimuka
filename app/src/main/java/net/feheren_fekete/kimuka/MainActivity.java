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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.feheren_fekete.kimuka.availabilities.AvailabilitiesFragment;
import net.feheren_fekete.kimuka.dialog.ActivityDialogFragment;
import net.feheren_fekete.kimuka.dialog.CanBelayDialogFragment;
import net.feheren_fekete.kimuka.dialog.DatePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.GradeDialogFragment;
import net.feheren_fekete.kimuka.dialog.IfNoPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.NeedPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.SharedEquimentDialogFragment;
import net.feheren_fekete.kimuka.dialog.TimePickerDialogFragment;
import net.feheren_fekete.kimuka.model.Grading;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;

import java.util.Arrays;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements FragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersTable;
    private @Nullable FirebaseUser mFirebaseUser;
    private @Nullable Query mUserQuery;
    private @Nullable User mUser;

    private SignInFragment mSignInFragment;
    private UserProfileFragment mUserProfileFragment;
    private AvailabilitiesFragment mAvailabilitiesFragment;
    private AddAvailabilityFragment mAddAvailabilityFragment;
    private Fragment mActiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        mUsersTable = mDatabase.getReference(ModelUtils.TABLE_USERS);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterUserListener();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        if (mActiveFragment == mAvailabilitiesFragment) {
            menu.findItem(R.id.action_user_profile).setVisible(true);
            menu.findItem(R.id.action_sign_out).setVisible(true);
        } else {
            menu.findItem(R.id.action_user_profile).setVisible(false);
            menu.findItem(R.id.action_sign_out).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user_profile:
                showUserProfileFragment();
                return true;
            case R.id.action_sign_out:
                mAuth.signOut();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mActiveFragment == mAddAvailabilityFragment) {
            showAvailabilitiesFragment();
        } else if (mActiveFragment == mUserProfileFragment) {
            showAvailabilitiesFragment();
        }
    }

    @Override
    public void onFragmentAction(String action, @Nullable Bundle data) {
        if (UserProfileFragment.INTERACTION_DONE_TAPPED.equals(action)) {
            showAvailabilitiesFragment();
        } else if (AddAvailabilityFragment.INTERACTION_DONE_TAPPED.equals(action)) {
            showAvailabilitiesFragment();
        } else if (AvailabilitiesFragment.INTERACTION_ADD_AVAILABILITY_TAPPED.equals(action)) {
            showAddAvailabilityFragment("");
        } else if (AvailabilitiesFragment.INTERACTION_AVAILABILITY_TAPPED.equals(action)
                && data != null) {
            showAddAvailabilityFragment(data.getString(AvailabilitiesFragment.DATA_AVAILABILITY_KEY));
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
        } else if (ActivityDialogFragment.INTERCATION_ACTIVITIES_SELECTED.equals(action)
                && data != null
                && mActiveFragment instanceof ActivityDialogFragment.Listener) {
            ActivityDialogFragment.Listener listener = (ActivityDialogFragment.Listener) mActiveFragment;
            listener.onActivitySelected(data.getIntegerArrayList(ActivityDialogFragment.DATA_ACTIVITIES));
        } else if (NeedPartnerDialogFragment.INTERCATION_ITEM_SELECTED.equals(action)
                && data != null
                && mActiveFragment instanceof NeedPartnerDialogFragment.Listener) {
            NeedPartnerDialogFragment.Listener listener = (NeedPartnerDialogFragment.Listener) mActiveFragment;
            listener.onNeedPartnerItemSelected(data.getInt(NeedPartnerDialogFragment.DATA_ITEM_INDEX));
        } else if (IfNoPartnerDialogFragment.INTERCATION_ITEM_SELECTED.equals(action)
                && data != null
                && mActiveFragment instanceof IfNoPartnerDialogFragment.Listener) {
            IfNoPartnerDialogFragment.Listener listener = (IfNoPartnerDialogFragment.Listener) mActiveFragment;
            listener.onIfNoPartnerItemSelected(data.getInt(IfNoPartnerDialogFragment.DATA_ITEM_INDEX));
        } else if (SharedEquimentDialogFragment.INTERCATION_EQUIPMENTS_SELECTED.equals(action)
                && data != null
                && mActiveFragment instanceof SharedEquimentDialogFragment.Listener) {
            SharedEquimentDialogFragment.Listener listener = (SharedEquimentDialogFragment.Listener) mActiveFragment;
            listener.onEquipmentSelected(data.getIntegerArrayList(SharedEquimentDialogFragment.DATA_EQUIPMENTS));
        } else if (GradeDialogFragment.INTERCATION_GRADE_SELECTED.equals(action)
                && data != null
                && mActiveFragment instanceof GradeDialogFragment.Listener) {
            GradeDialogFragment.Listener listener = (GradeDialogFragment.Listener) mActiveFragment;
            listener.onGradeSelected(data.getInt(GradeDialogFragment.DATA_GRADE));
        } else if (CanBelayDialogFragment.INTERCATION_ITEM_SELECTED.equals(action)
                && data != null
                && mActiveFragment instanceof CanBelayDialogFragment.Listener) {
            CanBelayDialogFragment.Listener listener = (CanBelayDialogFragment.Listener) mActiveFragment;
            listener.onCanBelayItemSelected(data.getInt(CanBelayDialogFragment.DATA_ITEM_INDEX));
        }
    }

    @Nullable
    public User getUser() {
        return mUser;
    }

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            mFirebaseUser = firebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
                reRegisterUserListener(mFirebaseUser.getUid());
                showAvailabilitiesFragment();
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
                mUser = null;
                unregisterUserListener();
                showSignInFragment();
            }
        }
    };

    private void reRegisterUserListener(String uid) {
        unregisterUserListener();
        registerUserListener(uid);
    }

    private void registerUserListener(String uid) {
        mUserQuery = mUsersTable.orderByChild("uid").equalTo(uid);
        mUserQuery.addValueEventListener(mUserListener);
    }

    private void unregisterUserListener() {
        if (mUserQuery != null) {
            mUserQuery.removeEventListener(mUserListener);
            mUserQuery = null;
        }
    }

    private ValueEventListener mUserListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                if (iterator.hasNext()) {
                    DataSnapshot user = iterator.next();
                    mUser = user.getValue(User.class);
                    if (mUser != null) {
                        mUser.setKey(user.getKey());
                    } else {
                        // TODO: Handle error.
                    }
                } else {
                    // TODO: Handle error.
                }
            } else if (mFirebaseUser != null) {
                createUser(mFirebaseUser.getUid(), mFirebaseUser.getDisplayName());
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // TODO: Handle error.
        }
    };

    private void createUser(String uid, String name) {
        User user = new User();
        user.setUid(uid);
        user.setName(name);
        user.setCanBelay(User.CAN_BELAY_NO);
        user.setGrades(ModelUtils.toCommaSeparatedString(Arrays.asList(
                Grading.YDS_5_0, Grading.FONTENBLAU_3)));
        user.setNote("");
        DatabaseReference userRef = mUsersTable.push();
        userRef.setValue(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO: Handle error.
            }
        });
    }

    private void showSignInFragment() {
        if (mSignInFragment == null) {
            mSignInFragment = SignInFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mSignInFragment, "SignInFragment")
                .commit();
        getSupportActionBar().setTitle(R.string.sign_in_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        invalidateOptionsMenu();
        mActiveFragment = mSignInFragment;
    }

    private void showUserProfileFragment() {
        if (mUserProfileFragment == null) {
            mUserProfileFragment = UserProfileFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mUserProfileFragment, "UserProfileFragment")
                .commit();
        getSupportActionBar().setTitle(R.string.user_profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();
        mActiveFragment = mUserProfileFragment;
    }

    private void showAvailabilitiesFragment() {
        if (mAvailabilitiesFragment == null) {
            mAvailabilitiesFragment = AvailabilitiesFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mAvailabilitiesFragment, "AvailabilitiesFragment")
                .commit();
        getSupportActionBar().setTitle(R.string.availabilities_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        invalidateOptionsMenu();
        mActiveFragment = mAvailabilitiesFragment;
    }

    private void showAddAvailabilityFragment(String availabilityKey) {
        if (mAddAvailabilityFragment == null) {
            mAddAvailabilityFragment = AddAvailabilityFragment.newInstance(availabilityKey);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mAddAvailabilityFragment, "AddAvailabilityFragment")
                .commit();
        if (availabilityKey.isEmpty()) {
            getSupportActionBar().setTitle(R.string.add_availability_title_new);
        } else {
            getSupportActionBar().setTitle(R.string.add_availability_title_edit);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();
        mActiveFragment = mAddAvailabilityFragment;
    }

}
