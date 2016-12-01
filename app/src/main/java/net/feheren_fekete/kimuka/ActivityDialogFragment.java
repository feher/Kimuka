package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;


public class ActivityDialogFragment extends DialogFragment {

    public static final String INTERCATION_ACTIVITIES_SELECTED = DatePickerDialogFragment.class.getSimpleName() + ".INTERCATION_ACTIVITIES_SELECTED";
    public static final String DATA_ACTIVITIES = DatePickerDialogFragment.class.getSimpleName() + ".DATA_ACTIVITIES";

    public interface Listener {
        void onActivityDialogOk(List<Integer> activities);
    }

    public static ActivityDialogFragment newInstance() {
        ActivityDialogFragment fragment = new ActivityDialogFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] activityNames = getResources().getStringArray(R.array.activities);
        final ArrayList<Integer> seletedItems = new ArrayList();
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.activity_dialog_title)
                .setMultiChoiceItems(activityNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemIndex, boolean isChecked) {
                        if (isChecked) {
                            seletedItems.add(itemIndex);
                        } else if (seletedItems.contains(itemIndex)) {
                            seletedItems.remove(Integer.valueOf(itemIndex));
                        }
                    }
                }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Activity activity = getActivity();
                        if (activity instanceof FragmentInteractionListener) {
                            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                            Bundle data = new Bundle();
                            data.putIntegerArrayList(DATA_ACTIVITIES, seletedItems);
                            listener.onFragmentAction(INTERCATION_ACTIVITIES_SELECTED, data);
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
    }
}
