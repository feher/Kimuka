package net.feheren_fekete.kimuka.model;

import java.util.List;

public class ModelUtils {

    public static int[] toIntArray(String stringListOfInts) throws NumberFormatException {
        String[] parts = stringListOfInts.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            result[i] = Integer.parseInt(parts[i]);
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

}
