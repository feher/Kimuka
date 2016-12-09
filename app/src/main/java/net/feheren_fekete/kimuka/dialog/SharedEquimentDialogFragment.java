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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SharedEquimentDialogFragment extends DialogFragment {

    public static final String INTERACTION_EQUIPMENTS_SELECTED = SharedEquimentDialogFragment.class.getSimpleName() + ".INTERACTION_EQUIPMENTS_SELECTED";
    public static final String DATA_EQUIPMENTS = SharedEquimentDialogFragment.class.getSimpleName() + ".DATA_EQUIPMENTS";

    private static final String ARG_SELECTED_ITEMS = "1";

    public interface Listener {
        void onEquipmentSelected(List<Integer> equipments);
    }

    public static SharedEquimentDialogFragment newInstance(ArrayList<Integer> selectedItems) {
        SharedEquimentDialogFragment fragment = new SharedEquimentDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putIntegerArrayList(ARG_SELECTED_ITEMS, selectedItems);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] items = getResources().getStringArray(R.array.shared_equipments);
        final ArrayList<Integer> selectedItems = getArguments().getIntegerArrayList(ARG_SELECTED_ITEMS);
        boolean[] selectedItemsMask = new boolean[items.length];
        for (Integer selectedItem : selectedItems) {
            selectedItemsMask[selectedItem] = true;
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.shared_equipment_dialog_title)
                .setMultiChoiceItems(items, selectedItemsMask, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemIndex, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(itemIndex);
                        } else if (selectedItems.contains(itemIndex)) {
                            selectedItems.remove(Integer.valueOf(itemIndex));
                        }
                    }
                }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Activity activity = getActivity();
                        if (activity instanceof FragmentInteractionListener) {
                            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                            Bundle data = new Bundle();
                            Collections.sort(selectedItems);
                            data.putIntegerArrayList(DATA_EQUIPMENTS, selectedItems);
                            listener.onFragmentAction(INTERACTION_EQUIPMENTS_SELECTED, data);
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
