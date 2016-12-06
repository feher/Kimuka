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


public class CanBelayDialogFragment extends DialogFragment {

    public static final String INTERCATION_ITEM_SELECTED = CanBelayDialogFragment.class.getSimpleName() + ".INTERCATION_ITEM_SELECTED";
    public static final String DATA_ITEM_INDEX = CanBelayDialogFragment.class.getSimpleName() + ".DATA_ITEM_INDEX";

    public interface Listener {
        void onCanBelayItemSelected(int itemIndex);
    }

    public static CanBelayDialogFragment newInstance() {
        CanBelayDialogFragment fragment = new CanBelayDialogFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.can_belay_dialog_title)
                .setItems(R.array.can_belay_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Activity activity = getActivity();
                        if (activity instanceof FragmentInteractionListener) {
                            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                            Bundle data = new Bundle();
                            data.putInt(DATA_ITEM_INDEX, i);
                            listener.onFragmentAction(INTERCATION_ITEM_SELECTED, data);
                        }
                    }
                }).create();
    }
}
