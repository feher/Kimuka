package net.feheren_fekete.kimuka.model;

import android.content.Context;

import net.feheren_fekete.kimuka.R;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    public static final String TABLE_AVAILABILITIES = "availability";
    public static final String TABLE_USERS = "users";

    public static ArrayList<Integer> toIntList(String stringListOfInts) throws NumberFormatException {
        String[] parts = stringListOfInts.split(",");
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < parts.length; ++i) {
            String part = parts[i].trim();
            if (!part.isEmpty()) {
                result.add(Integer.valueOf(part));
            }
        }
        return result;
    }

    public static String toCommaSeparatedString(List<Integer> args) {
        if (args.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.size() - 1; ++i) {
            stringBuilder.append(String.valueOf(args.get(i))).append(",");
        }
        stringBuilder.append(String.valueOf(args.get(args.size() - 1)));
        return stringBuilder.toString();
    }

    public static String createActivityNameList(Context context, List<Integer> activities) {
        if (activities.isEmpty()) {
            return "";
        }
        String[] activityNames = context.getResources().getStringArray(R.array.activities);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < activities.size() - 1; ++i) {
            stringBuilder.append(activityNames[activities.get(i)]).append(", ");
        }
        stringBuilder.append(activityNames[activities.get(activities.size() - 1)]);
        return stringBuilder.toString();
    }

    public static String createNeedPartnerText(Context context, int itemIndex) {
        String[] items = context.getResources().getStringArray(R.array.need_partner_options);
        return items[itemIndex];
    }

    public static String createIfNoPartnerText(Context context, int itemIndex) {
        String[] items = context.getResources().getStringArray(R.array.no_partner_options);
        return items[itemIndex];
    }

    public static String createEquipmentNameList(Context context, List<Integer> equipments) {
        if (equipments.isEmpty()) {
            return "";
        }
        String[] equipmentNames = context.getResources().getStringArray(R.array.shared_equipments);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < equipments.size() - 1; ++i) {
            stringBuilder.append(equipmentNames[equipments.get(i)]).append(", ");
        }
        stringBuilder.append(equipmentNames[equipments.get(equipments.size() - 1)]);
        return stringBuilder.toString();
    }

    public static String createCanBelayText(Context context, int itemIndex) {
        String[] items = context.getResources().getStringArray(R.array.can_belay_options);
        return items[itemIndex];
    }

}
