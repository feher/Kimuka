package net.feheren_fekete.kimuka;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DayFragment extends Fragment {

    public static final String INTERACTION_ADD_AVAILABILITY_TAPPED = DayFragment.class.getSimpleName() + ".INTERACTION_ADD_AVAILABILITY_TAPPED";

    @Nullable
    private FragmentInteractionListener mInteractionListener;

    public DayFragment() {
    }

    public static DayFragment newInstance() {
        DayFragment fragment = new DayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInteractionListener != null) {
                    mInteractionListener.onFragmentAction(INTERACTION_ADD_AVAILABILITY_TAPPED, null);
                }
            }
        });
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

}
