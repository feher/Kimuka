package net.feheren_fekete.kimuka;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import net.feheren_fekete.kimuka.model.User;


public class UserProfileFragment extends Fragment {

    private EditText mName;
    private EditText mFreeclimbingGrade;
    private EditText mBoulderingGrade;
    private Switch mCanBelay;
    private EditText mNote;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mName = (EditText) view.findViewById(R.id.name_value);
        mFreeclimbingGrade = (EditText) view.findViewById(R.id.free_climbing_grade_value);
        mBoulderingGrade = (EditText) view.findViewById(R.id.bouldering_grade_value);
        mCanBelay = (Switch) view.findViewById(R.id.can_belay_switch);
        mNote = (EditText) view.findViewById(R.id.note_value);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            User user = mainActivity.getUser();
             if (user != null) {
                 mName.setText(user.name);
                 mFreeclimbingGrade.setText(user.grades.get(User.GRADE_FREE_CLIMBING));
                 mBoulderingGrade.setText(user.grades.get(User.GRADE_BOULDERING));
                 mCanBelay.setChecked(user.canBelay);
                 mNote.setText(user.note);
             }
        } else {
            throw new RuntimeException(context.toString() + " must implement MainActivity");
        }
    }

}
