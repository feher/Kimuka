package net.feheren_fekete.kimuka;

import android.app.Activity;
import android.support.v4.app.Fragment;

import net.feheren_fekete.kimuka.model.User;

public class BaseFragment extends Fragment {

    private User mUser;

    protected MainActivity getMainActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            return (MainActivity) activity;
        }
        return null;
    }

    protected User getUser() {
        if (mUser == null) {
            MainActivity activity = getMainActivity();
            if (activity != null) {
                User user = activity.getUser();
                if (user != null) {
                    mUser = user;
                } else {
                    // TODO: Handle error. Report exception. Close fragment.
                    throw new RuntimeException();
                }
            } else {
                // TODO: Handle error. Report exception. Close fragment.
                throw new RuntimeException();
            }
        }
        return mUser;
    }

}
