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


public class NeedPartnerDialogFragment extends DialogFragment {

    public static final String INTERCATION_ITEM_SELECTED = NeedPartnerDialogFragment.class.getSimpleName() + ".INTERCATION_ITEM_SELECTED";
    public static final String DATA_ITEM_INDEX = NeedPartnerDialogFragment.class.getSimpleName() + ".DATA_ITEM_INDEX";

    public interface Listener {
        void onNeedPartnerItemSelected(int itemIndex);
    }

    public static NeedPartnerDialogFragment newInstance() {
        NeedPartnerDialogFragment fragment = new NeedPartnerDialogFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] items = getResources().getStringArray(R.array.no_partner_options);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.need_partner_dialog_title)
                .setItems(R.array.need_partner_options, new DialogInterface.OnClickListener() {
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
