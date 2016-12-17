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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterDialogFragment extends DialogFragment {

    public static final String INTERACTION_CREATE_FILTER_SELECTED = FilterDialogFragment.class.getSimpleName() + ".INTERACTION_CREATE_FILTER_SELECTED";
    public static final String INTERACTION_NO_FILTER_SELECTED = FilterDialogFragment.class.getSimpleName() + ".INTERACTION_NO_FILTER_SELECTED";
    public static final String INTERACTION_FILTER_SELECTED = FilterDialogFragment.class.getSimpleName() + ".INTERACTION_FILTER_SELECTED";
    public static final String DATA_FILTER_JSON = FilterDialogFragment.class.getSimpleName() + ".DATA_FILTER_JSON";

    private int mNoFilterIndex;
    private int mNewFilterIndex;

    public interface Listener {
        void onFilterSelected(String jsonFilter);
    }

    public static FilterDialogFragment newInstance() {
        FilterDialogFragment fragment = new FilterDialogFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> itemList = getFilterNames();
        final String[] items = new String[itemList.size()];
        itemList.toArray(items);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.filter_dialog_title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Activity activity = getActivity();
                        if (activity instanceof FragmentInteractionListener) {
                            FragmentInteractionListener listener = (FragmentInteractionListener) activity;
                            Bundle data = new Bundle();
                            if (i == mNewFilterIndex) {
                                listener.onFragmentAction(INTERACTION_CREATE_FILTER_SELECTED, data);
                            } else if (i == mNoFilterIndex) {
                                listener.onFragmentAction(INTERACTION_NO_FILTER_SELECTED, data);
                            } else {
                                String filterJson = loadFile(getFiltersDirPath() + File.separator + items[i]);
                                if (!filterJson.isEmpty()) {
                                    data.putString(DATA_FILTER_JSON, filterJson);
                                    listener.onFragmentAction(INTERACTION_FILTER_SELECTED, data);
                                } else {
                                    // TODO: Show toast: "Cannot load filter".
                                }
                            }
                        }
                    }
                }).create();
    }

    private String getFiltersDirPath() {
        File filtersDir = getContext().getExternalFilesDir("filters");
        return filtersDir.getAbsolutePath();
    }

    private List<String> getFilterNames() {
        List<String> result = new ArrayList<>();

        File filtersDir = new File(getFiltersDirPath());
        if (filtersDir.isDirectory() && filtersDir.exists()) {
            result.addAll(Arrays.asList(filtersDir.list()));
        }

        result.add(getResources().getString(R.string.filter_dialog_no_filter));
        mNoFilterIndex = result.size() - 1;
        result.add(getResources().getString(R.string.filter_dialog_new_filter));
        mNewFilterIndex = result.size() - 1;

        return result;
    }

    private String loadFile(String filePath) {
        String result = "";
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            result = sb.toString();
        } catch (IOException e) {
            // TODO: Report error?
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
        return result;
    }

}
