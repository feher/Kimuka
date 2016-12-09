package net.feheren_fekete.kimuka;


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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.feheren_fekete.kimuka.dialog.CanBelayDialogFragment;
import net.feheren_fekete.kimuka.dialog.GradeDialogFragment;
import net.feheren_fekete.kimuka.model.Grading;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;


public class UserProfileFragment
        extends
                Fragment
        implements
                GradeDialogFragment.Listener,
                CanBelayDialogFragment.Listener {

    public static final String INTERACTION_DONE_TAPPED = UserProfileFragment.class.getSimpleName() + ".INTERACTION_DONE_TAPPED";

    private EditText mNameTextView;
    private TextView mFreeClimbingGradeTextView;
    private TextView mBoulderingGradeTextView;
    private TextView mCanBelayTextView;
    private TextView mTargetGradeTextView;
    private EditText mNoteTextView;

    private int mFreeClimbingGrade;
    private int mBoulderingGrade;
    private int mCanBelay;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mNameTextView = (EditText) view.findViewById(R.id.name_value);

        mFreeClimbingGradeTextView = (TextView) view.findViewById(R.id.free_climbing_grade_value);
        mFreeClimbingGradeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = GradeDialogFragment.newInstance(GradeDialogFragment.TYPE_FREE_CLIMBING);
                fragment.show(getActivity().getSupportFragmentManager(), "GradeDialogFragment");
                mTargetGradeTextView = mFreeClimbingGradeTextView;
            }
        });

        mBoulderingGradeTextView = (TextView) view.findViewById(R.id.bouldering_grade_value);
        mBoulderingGradeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = GradeDialogFragment.newInstance(GradeDialogFragment.TYPE_BOULDERING);
                fragment.show(getActivity().getSupportFragmentManager(), "GradeDialogFragment");
                mTargetGradeTextView = mBoulderingGradeTextView;
            }
        });

        mCanBelayTextView = (TextView) view.findViewById(R.id.can_belay_value);
        mCanBelayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = CanBelayDialogFragment.newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), "CanBelayDialogFragment");
            }
        });

        mNoteTextView = (EditText) view.findViewById(R.id.note_value);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            User user = mainActivity.getUser();
            if (user != null) {
                mNameTextView.setText(user.getName());
                mFreeClimbingGradeTextView.setText(Grading.getNameForYdsGrade(
                        user.getFreeClimbingGrade(), Grading.NAME_FRENCH));
                mBoulderingGradeTextView.setText(Grading.getNameForFontenblauGrade(
                        user.getBoulderingGrade(), Grading.NAME_FONTENBLAU));
                mCanBelayTextView.setText(ModelUtils.createCanBelayText(getContext(), user.getCanBelay()));
                mNoteTextView.setText(user.getNote());

                mFreeClimbingGrade = user.getFreeClimbingGrade();
                mBoulderingGrade = user.getBoulderingGrade();
                mCanBelay = user.getCanBelay();
            }
        } else {
            throw new RuntimeException("Activity must implement MainActivity");
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_save_availability:
                saveUserProfile();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGradeSelected(int grade) {
        if (mTargetGradeTextView == mFreeClimbingGradeTextView) {
            mFreeClimbingGrade = grade;
            mFreeClimbingGradeTextView.setText(Grading.getNameForYdsGrade(grade, Grading.NAME_FRENCH));
        } else {
            mBoulderingGrade = grade;
            mBoulderingGradeTextView.setText(Grading.getNameForFontenblauGrade(grade, Grading.NAME_FONTENBLAU));
        }
    }

    @Override
    public void onCanBelayItemSelected(int itemIndex) {
        mCanBelay = itemIndex;
        mCanBelayTextView.setText(ModelUtils.createCanBelayText(getContext(), itemIndex));
    }

    private void saveUserProfile() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            User user = mainActivity.getUser();
            if (user != null) {
                user.setName(mNameTextView.getText().toString().trim());
                user.setGrades(mFreeClimbingGrade + "," + mBoulderingGrade);
                user.setCanBelay(mCanBelay);
                user.setNote(mNoteTextView.getText().toString().trim());

                DatabaseReference userRef = FirebaseDatabase.getInstance()
                        .getReference(ModelUtils.TABLE_USER)
                        .child(user.getKey());
                userRef.setValue(user);

                mainActivity.onFragmentAction(INTERACTION_DONE_TAPPED, new Bundle());
            }
        }
    }

}
