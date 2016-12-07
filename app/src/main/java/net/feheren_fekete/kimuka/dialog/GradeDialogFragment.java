package net.feheren_fekete.kimuka.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.feheren_fekete.kimuka.FragmentInteractionListener;
import net.feheren_fekete.kimuka.R;
import net.feheren_fekete.kimuka.model.Grading;


public class GradeDialogFragment extends DialogFragment {

    public static final int TYPE_FREE_CLIMBING = 0;
    public static final int TYPE_BOULDERING = 1;

    public static final String INTERCATION_GRADE_SELECTED = GradeDialogFragment.class.getSimpleName() + ".INTERCATION_GRADE_SELECTED";
    public static final String DATA_GRADE = GradeDialogFragment.class.getSimpleName() + ".DATA_GRADE";

    private static final String ARG_TYPE = GradeDialogFragment.class.getSimpleName() + ".ARG_TYPE";

    public interface Listener {
        void onGradeSelected(int ydsGrade);
    }

    public static GradeDialogFragment newInstance(int type) {
        GradeDialogFragment fragment = new GradeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int type = getArguments().getInt(ARG_TYPE, TYPE_FREE_CLIMBING);
        String[] items = (type == TYPE_FREE_CLIMBING)
                ? Grading.getFreeClimbingGradeNames(Grading.NAME_FRENCH)
                : Grading.getBoulderingGradeNames(Grading.NAME_FONTENBLAU);
        return new AlertDialog.Builder(getActivity())
                .setTitle(type == TYPE_FREE_CLIMBING
                        ? R.string.free_climbing_grade_dialog_title
                        : R.string.bouldering_grade_dialog_title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Activity activity = getActivity();
                        if (activity instanceof FragmentInteractionListener) {
                            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                            Bundle data = new Bundle();
                            data.putInt(
                                    DATA_GRADE,
                                    (type == TYPE_FREE_CLIMBING)
                                            ? Grading.sYdsNameMap.get(i).first
                                            : Grading.sFontenblauNameMap.get(i).first);
                            listener.onFragmentAction(INTERCATION_GRADE_SELECTED, data);
                        }
                    }
                }).create();
    }
}
