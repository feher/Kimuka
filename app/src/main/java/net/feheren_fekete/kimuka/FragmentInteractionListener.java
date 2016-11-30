package net.feheren_fekete.kimuka;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface FragmentInteractionListener {
    void onFragmentAction(String action, @Nullable Bundle data);
}
