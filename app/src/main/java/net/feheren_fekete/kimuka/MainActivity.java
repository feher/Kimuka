package net.feheren_fekete.kimuka;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.feheren_fekete.kimuka.availabilitylist.AvailabilityListFragment;
import net.feheren_fekete.kimuka.dialog.ActivityDialogFragment;
import net.feheren_fekete.kimuka.dialog.CanBelayDialogFragment;
import net.feheren_fekete.kimuka.dialog.DatePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.FilterDialogFragment;
import net.feheren_fekete.kimuka.dialog.GradeDialogFragment;
import net.feheren_fekete.kimuka.dialog.IfNoPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.NeedPartnerDialogFragment;
import net.feheren_fekete.kimuka.dialog.SharedEquimentDialogFragment;
import net.feheren_fekete.kimuka.dialog.TimePickerDialogFragment;
import net.feheren_fekete.kimuka.dialog.userpickerdialog.UserPickerDialogFragment;
import net.feheren_fekete.kimuka.model.Grading;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;

import java.util.Arrays;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements FragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AppPreferences mAppPreferences;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersTable;
    private @Nullable FirebaseUser mFirebaseUser;
    private @Nullable Query mUserQuery;
    private @Nullable User mUser;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private FloatingActionButton mFab;

    private SignInFragment mSignInFragment;
    private UserProfileFragment mUserProfileFragment;
    private PagerFragment mPagerFragment;
    private AvailabilityFragment mAvailabilityFragment;
    private RequestFragment mRequestFragment;
    private FilterFragment mFilterFragment;
    private Fragment mActiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppPreferences = new AppPreferences(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        mUsersTable = mDatabase.getReference(ModelUtils.TABLE_USER);
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
        if (mActiveFragment == mPagerFragment) {
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
        if (mActiveFragment == mAvailabilityFragment) {
            showPagerFragment();
        } else if (mActiveFragment == mUserProfileFragment) {
            showPagerFragment();
        } else if (mActiveFragment == mRequestFragment) {
            showPagerFragment();
        }
    }

    @Override
    public void onFragmentAction(String action, Bundle data) {
        if (UserProfileFragment.INTERACTION_DONE_TAPPED.equals(action)) {
            showPagerFragment();
        } else if (AvailabilityFragment.INTERACTION_DONE_TAPPED.equals(action)) {
            showPagerFragment();
        } else if (AvailabilityFragment.INTERACTION_SEND_REQUEST_TAPPED.equals(action)) {
            showRequestFragment(data.getString(AvailabilityFragment.DATA_AVAILABILITY_KEY), "");
        } else if (RequestFragment.INTERACTION_SEND_TAPPED.equals(action)) {
            showPagerFragment();
        } else if (AvailabilityListFragment.INTERACTION_ADD_AVAILABILITY_TAPPED.equals(action)) {
            showAvailabilityFragment("");
        } else if (AvailabilityListFragment.INTERACTION_AVAILABILITY_TAPPED.equals(action)) {
            showAvailabilityFragment(data.getString(AvailabilityListFragment.DATA_AVAILABILITY_KEY));
        } else if (FilterDialogFragment.INTERACTION_FILTER_SELECTED.equals(action)) {
            mAppPreferences.setActiveFilter(data.getString(FilterDialogFragment.DATA_FILTER_JSON));
            showPagerFragment();
        } else if (FilterDialogFragment.INTERACTION_NO_FILTER_SELECTED.equals(action)) {
            mAppPreferences.setActiveFilter("");
            showPagerFragment();
        } else if (FilterDialogFragment.INTERACTION_CREATE_FILTER_SELECTED.equals(action)) {
            showFilterFragment();
        } else if (FilterFragment.INTERACTION_DONE_TAPPED.equals(action)) {
            mAppPreferences.setActiveFilter(data.getString(FilterFragment.DATA_FILTER_JSON));
            showPagerFragment();
        } else if (UserPickerDialogFragment.INTERACTION_USER_SELECTED.equals(action)
                && mActiveFragment instanceof UserPickerDialogFragment.Listener) {
            UserPickerDialogFragment.Listener listener = (UserPickerDialogFragment.Listener) mActiveFragment;
            listener.onUserSelected(
                    data.getString(UserPickerDialogFragment.DATA_USER_KEY),
                    data.getString(UserPickerDialogFragment.DATA_USER_NAME));
        } else if (TimePickerDialogFragment.INTERACTION_TIME_PICKED.equals(action)
                && mActiveFragment instanceof TimePickerDialogFragment.Listener) {
            TimePickerDialogFragment.Listener listener = (TimePickerDialogFragment.Listener) mActiveFragment;
            listener.onTimeSet(
                    data.getInt(TimePickerDialogFragment.DATA_HOUR_OF_DAY),
                    data.getInt(TimePickerDialogFragment.DATA_MINUTE));
        } else if (DatePickerDialogFragment.INTERACTION_DATE_PICKED.equals(action)
                && mActiveFragment instanceof DatePickerDialogFragment.Listener) {
            DatePickerDialogFragment.Listener listener = (DatePickerDialogFragment.Listener) mActiveFragment;
            listener.onDateSet(
                    data.getInt(DatePickerDialogFragment.DATA_YEAR),
                    data.getInt(DatePickerDialogFragment.DATA_MONTH),
                    data.getInt(DatePickerDialogFragment.DATA_DAY_OF_MONTH));
        } else if (ActivityDialogFragment.INTERACTION_ACTIVITIES_SELECTED.equals(action)
                && mActiveFragment instanceof ActivityDialogFragment.Listener) {
            ActivityDialogFragment.Listener listener = (ActivityDialogFragment.Listener) mActiveFragment;
            listener.onActivitySelected(data.getIntegerArrayList(ActivityDialogFragment.DATA_ACTIVITIES));
        } else if (NeedPartnerDialogFragment.INTERACTION_ITEM_SELECTED.equals(action)
                && mActiveFragment instanceof NeedPartnerDialogFragment.Listener) {
            NeedPartnerDialogFragment.Listener listener = (NeedPartnerDialogFragment.Listener) mActiveFragment;
            listener.onNeedPartnerItemSelected(data.getInt(NeedPartnerDialogFragment.DATA_ITEM_INDEX));
        } else if (IfNoPartnerDialogFragment.INTERACTION_ITEM_SELECTED.equals(action)
                && mActiveFragment instanceof IfNoPartnerDialogFragment.Listener) {
            IfNoPartnerDialogFragment.Listener listener = (IfNoPartnerDialogFragment.Listener) mActiveFragment;
            listener.onIfNoPartnerItemSelected(data.getInt(IfNoPartnerDialogFragment.DATA_ITEM_INDEX));
        } else if (SharedEquimentDialogFragment.INTERACTION_EQUIPMENTS_SELECTED.equals(action)
                && mActiveFragment instanceof SharedEquimentDialogFragment.Listener) {
            SharedEquimentDialogFragment.Listener listener = (SharedEquimentDialogFragment.Listener) mActiveFragment;
            listener.onEquipmentSelected(data.getIntegerArrayList(SharedEquimentDialogFragment.DATA_EQUIPMENTS));
        } else if (GradeDialogFragment.INTERACTION_GRADE_SELECTED.equals(action)
                && mActiveFragment instanceof GradeDialogFragment.Listener) {
            GradeDialogFragment.Listener listener = (GradeDialogFragment.Listener) mActiveFragment;
            listener.onGradeSelected(data.getInt(GradeDialogFragment.DATA_GRADE));
        } else if (CanBelayDialogFragment.INTERACTION_ITEM_SELECTED.equals(action)
                && mActiveFragment instanceof CanBelayDialogFragment.Listener) {
            CanBelayDialogFragment.Listener listener = (CanBelayDialogFragment.Listener) mActiveFragment;
            listener.onCanBelayItemSelected(data.getInt(CanBelayDialogFragment.DATA_ITEM_INDEX));
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public FloatingActionButton getFab() {
        return mFab;
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
                if (mUser != null) {
                    showPagerFragment();
                }
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
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            showPagerFragment();
                        }
                    } else {
                        // TODO: Handle error.
                        throw new RuntimeException();
                    }
                } else {
                    // TODO: Handle error.
                    throw new RuntimeException();
                }
            } else if (mFirebaseUser != null) {
                createUser(mFirebaseUser.getUid(), mFirebaseUser.getDisplayName());
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // TODO: Handle error.
            throw new RuntimeException();
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
        mTabLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
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
        mTabLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        invalidateOptionsMenu();
        mActiveFragment = mUserProfileFragment;
    }

    private void showPagerFragment() {
        if (mPagerFragment != null) {
            mPagerFragment.setHasOptionsMenu(false);
        }
        mPagerFragment = PagerFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mPagerFragment, "PagerFragment")
                .commit();
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mTabLayout.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.GONE);
        invalidateOptionsMenu();
        mActiveFragment = mPagerFragment;
    }

    private void showAvailabilityFragment(String availabilityKey) {
        mAvailabilityFragment = AvailabilityFragment.newInstance(availabilityKey);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mAvailabilityFragment, "AvailabilityFragment")
                .commit();
        if (availabilityKey.isEmpty()) {
            getSupportActionBar().setTitle(R.string.availability_title_new);
        } else {
            getSupportActionBar().setTitle(R.string.availability_title_edit);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        invalidateOptionsMenu();
        mActiveFragment = mAvailabilityFragment;
    }

    private void showRequestFragment(String availabilityKey, String requestKey) {
        mRequestFragment = RequestFragment.newInstance(availabilityKey, "");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mRequestFragment, "RequestFragment")
                .commit();
        if (requestKey.isEmpty()) {
            getSupportActionBar().setTitle(R.string.request_title_new);
        } else {
            getSupportActionBar().setTitle(R.string.request_title_view);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        invalidateOptionsMenu();
        mActiveFragment = mRequestFragment;
    }

    private void showFilterFragment() {
        mFilterFragment = FilterFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mFilterFragment, "FilterFragment")
                .commit();
        getSupportActionBar().setTitle(R.string.filter_title_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        invalidateOptionsMenu();
        mActiveFragment = mFilterFragment;
    }

}
