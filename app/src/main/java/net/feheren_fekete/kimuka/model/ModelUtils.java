package net.feheren_fekete.kimuka.model;

import android.content.Context;

import net.feheren_fekete.kimuka.R;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    public static ArrayList<Integer> toIntList(String stringListOfInts) throws NumberFormatException {
        String[] parts = stringListOfInts.split(",");
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < parts.length; ++i) {
            result.add(Integer.valueOf(parts[i]));
        }
        return result;
    }

    public static String toCommaSeparatedString(List<Integer> args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.size() - 1; ++i) {
            stringBuilder.append(String.valueOf(args.get(i))).append(",");
        }
        stringBuilder.append(String.valueOf(args.get(args.size() - 1)));
        return stringBuilder.toString();
    }

    public static String createActivityNameList(Context context, List<Integer> activities) {
        String[] activityNames = context.getResources().getStringArray(R.array.activities);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < activities.size() - 1; ++i) {
            stringBuilder.append(activityNames[activities.get(i)]).append(", ");
        }
        stringBuilder.append(activityNames[activities.get(activities.size() - 1)]);
        return stringBuilder.toString();
    }

}
