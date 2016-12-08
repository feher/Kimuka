package net.feheren_fekete.kimuka.model;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grading {

    public static final int NAME_YDS = 0;
    public static final int NAME_FRENCH = 1;

    public static final int NAME_FONTENBLAU = 0;
    public static final int NAME_HUECO = 1;

    public static final int YDS_5_0 = 5000;
    public static final int YDS_5_1 = 5010;
    public static final int YDS_5_2 = 5020;
    public static final int YDS_5_3 = 5030;
    public static final int YDS_5_4 = 5040;
    public static final int YDS_5_5 = 5050;
    public static final int YDS_5_6 = 5060;
    public static final int YDS_5_7 = 5070;
    public static final int YDS_5_8 = 5080;
    public static final int YDS_5_9 = 5090;
    public static final int YDS_5_10_A = 5100;
    public static final int YDS_5_10_B = 5101;
    public static final int YDS_5_10_C = 5102;
    public static final int YDS_5_10_D = 5103;
    public static final int YDS_5_11_A = 5110;
    public static final int YDS_5_11_B = 5111;
    public static final int YDS_5_11_C = 5112;
    public static final int YDS_5_11_D = 5113;
    public static final int YDS_5_12_A = 5120;
    public static final int YDS_5_12_B = 5121;
    public static final int YDS_5_12_C = 5122;
    public static final int YDS_5_12_D = 5123;
    public static final int YDS_5_13_A = 5130;
    public static final int YDS_5_13_B = 5131;
    public static final int YDS_5_13_C = 5132;
    public static final int YDS_5_13_D = 5133;
    public static final int YDS_5_14_A = 5140;
    public static final int YDS_5_14_B = 5141;
    public static final int YDS_5_14_C = 5142;
    public static final int YDS_5_14_D = 5143;
    public static final int YDS_5_15_A = 5150;
    public static final int YDS_5_15_B = 5151;
    public static final int YDS_5_15_C = 5152;

    public static final int FRENCH_1 = YDS_5_0;
    public static final int FRENCH_2 = YDS_5_1;
    public static final int FRENCH_3 = YDS_5_3;
    public static final int FRENCH_4_A = YDS_5_4;
    public static final int FRENCH_4_B = YDS_5_5;
    public static final int FRENCH_4_C = YDS_5_6;
    public static final int FRENCH_5_A = YDS_5_7;
    public static final int FRENCH_5_B = YDS_5_8;
    public static final int FRENCH_5_C = YDS_5_9;
    public static final int FRENCH_6_A = YDS_5_10_A;
    public static final int FRENCH_6_A_PLUS = YDS_5_10_B;
    public static final int FRENCH_6_B = YDS_5_10_C;
    public static final int FRENCH_6_B_PLUS = YDS_5_10_D;
    public static final int FRENCH_6_C = YDS_5_11_A;
    public static final int FRENCH_6_C_PLUS = YDS_5_11_B;
    public static final int FRENCH_7_A = YDS_5_11_D;
    public static final int FRENCH_7_A_PLUS = YDS_5_12_A;
    public static final int FRENCH_7_B = YDS_5_12_B;
    public static final int FRENCH_7_B_PLUS = YDS_5_12_C;
    public static final int FRENCH_7_C = YDS_5_12_D;
    public static final int FRENCH_7_C_PLUS = YDS_5_13_A;
    public static final int FRENCH_8_A = YDS_5_13_B;
    public static final int FRENCH_8_A_PLUS = YDS_5_13_C;
    public static final int FRENCH_8_B = YDS_5_13_D;
    public static final int FRENCH_8_B_PLUS = YDS_5_14_A;
    public static final int FRENCH_8_C = YDS_5_14_B;
    public static final int FRENCH_8_C_PLUS = YDS_5_14_C;
    public static final int FRENCH_9_A = YDS_5_14_D;
    public static final int FRENCH_9_A_PLUS = YDS_5_15_A;
    public static final int FRENCH_9_B = YDS_5_15_B;
    public static final int FRENCH_9_B_PLUS = YDS_5_15_C;

    public static final int FONTENBLAU_3 = 3000;
    public static final int FONTENBLAU_4_MINUS = 4000;
    public static final int FONTENBLAU_4 = 4001;
    public static final int FONTENBLAU_4_PLUS = 4002;
    public static final int FONTENBLAU_5 = 5001;
    public static final int FONTENBLAU_5_PLUS = 5002;
    public static final int FONTENBLAU_6_A = 6010;
    public static final int FONTENBLAU_6_A_PLUS = 6011;
    public static final int FONTENBLAU_6_B = 6020;
    public static final int FONTENBLAU_6_B_PLUS = 6021;
    public static final int FONTENBLAU_6_C = 6030;
    public static final int FONTENBLAU_6_C_PLUS = 6031;
    public static final int FONTENBLAU_7_A = 7010;
    public static final int FONTENBLAU_7_A_PLUS = 7011;
    public static final int FONTENBLAU_7_B = 7020;
    public static final int FONTENBLAU_7_B_PLUS = 7021;
    public static final int FONTENBLAU_7_C = 7030;
    public static final int FONTENBLAU_7_C_PLUS = 7031;
    public static final int FONTENBLAU_8_A = 8010;
    public static final int FONTENBLAU_8_A_PLUS = 8011;
    public static final int FONTENBLAU_8_B = 8020;
    public static final int FONTENBLAU_8_B_PLUS = 8021;
    public static final int FONTENBLAU_8_C = 8030;
    public static final int FONTENBLAU_8_C_PLUS = 8031;
    public static final int FONTENBLAU_9_A = 9010;

    public static final int HUECO_V_B = FONTENBLAU_3;
    public static final int HUECO_V_0_MINUS = FONTENBLAU_4_MINUS;
    public static final int HUECO_V_0 = FONTENBLAU_4;
    public static final int HUECO_V_0_PLUS = FONTENBLAU_4_PLUS;
    public static final int HUECO_V_1 = FONTENBLAU_5;
    public static final int HUECO_V_2 = FONTENBLAU_5_PLUS;
    public static final int HUECO_V_3 = FONTENBLAU_6_A;
    public static final int HUECO_V_4 = FONTENBLAU_6_B;
    public static final int HUECO_V_5 = FONTENBLAU_6_C;
    public static final int HUECO_V_6 = FONTENBLAU_7_A;
    public static final int HUECO_V_7 = FONTENBLAU_7_A_PLUS;
    public static final int HUECO_V_8 = FONTENBLAU_7_B;
    public static final int HUECO_V_9 = FONTENBLAU_7_C;
    public static final int HUECO_V_10 = FONTENBLAU_7_C_PLUS;
    public static final int HUECO_V_11 = FONTENBLAU_8_A;
    public static final int HUECO_V_12 = FONTENBLAU_8_A_PLUS;
    public static final int HUECO_V_13 = FONTENBLAU_8_B;
    public static final int HUECO_V_14 = FONTENBLAU_8_B_PLUS;
    public static final int HUECO_V_15 = FONTENBLAU_8_C;
    public static final int HUECO_V_16 = FONTENBLAU_8_C_PLUS;
    public static final int HUECO_V_17 = FONTENBLAU_9_A;

    public static final List<Pair<Integer, ArrayList<String>>> sYdsNameMap;
    static {
        sYdsNameMap = new ArrayList<>();
        sYdsNameMap.add(new Pair<>(YDS_5_0, new ArrayList<>(Arrays.asList("YDS 5.0", "French 1"))));
        sYdsNameMap.add(new Pair<>(YDS_5_1, new ArrayList<>(Arrays.asList("YDS 5.1", "French 2"))));
        sYdsNameMap.add(new Pair<>(YDS_5_2, new ArrayList<>(Arrays.asList("YDS 5.2", "French 2"))));
        sYdsNameMap.add(new Pair<>(YDS_5_3, new ArrayList<>(Arrays.asList("YDS 5.3", "French 3"))));
        sYdsNameMap.add(new Pair<>(YDS_5_4, new ArrayList<>(Arrays.asList("YDS 5.4", "French 4a"))));
        sYdsNameMap.add(new Pair<>(YDS_5_5, new ArrayList<>(Arrays.asList("YDS 5.5", "French 4b"))));
        sYdsNameMap.add(new Pair<>(YDS_5_6, new ArrayList<>(Arrays.asList("YDS 5.6", "French 4c"))));
        sYdsNameMap.add(new Pair<>(YDS_5_7, new ArrayList<>(Arrays.asList("YDS 5.7", "French 5a"))));
        sYdsNameMap.add(new Pair<>(YDS_5_8, new ArrayList<>(Arrays.asList("YDS 5.8", "French 5b"))));
        sYdsNameMap.add(new Pair<>(YDS_5_9, new ArrayList<>(Arrays.asList("YDS 5.9", "French 5c"))));
        sYdsNameMap.add(new Pair<>(YDS_5_10_A, new ArrayList<>(Arrays.asList("YDS 5.10a", "French 6a"))));
        sYdsNameMap.add(new Pair<>(YDS_5_10_B, new ArrayList<>(Arrays.asList("YDS 5.10b", "French 6a+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_10_C, new ArrayList<>(Arrays.asList("YDS 5.10c", "French 6b"))));
        sYdsNameMap.add(new Pair<>(YDS_5_10_D, new ArrayList<>(Arrays.asList("YDS 5.10d", "French 6b+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_11_A, new ArrayList<>(Arrays.asList("YDS 5.11a", "French 6c"))));
        sYdsNameMap.add(new Pair<>(YDS_5_11_B, new ArrayList<>(Arrays.asList("YDS 5.11b", "French 6c+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_11_C, new ArrayList<>(Arrays.asList("YDS 5.11c", "French 6c+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_11_D, new ArrayList<>(Arrays.asList("YDS 5.11d", "French 7a"))));
        sYdsNameMap.add(new Pair<>(YDS_5_12_A, new ArrayList<>(Arrays.asList("YDS 5.12a", "French 7a+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_12_B, new ArrayList<>(Arrays.asList("YDS 5.12b", "French 7b"))));
        sYdsNameMap.add(new Pair<>(YDS_5_12_C, new ArrayList<>(Arrays.asList("YDS 5.12c", "French 7b+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_12_D, new ArrayList<>(Arrays.asList("YDS 5.12d", "French 7c"))));
        sYdsNameMap.add(new Pair<>(YDS_5_13_A, new ArrayList<>(Arrays.asList("YDS 5.13a", "French 7c+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_13_B, new ArrayList<>(Arrays.asList("YDS 5.13b", "French 8a"))));
        sYdsNameMap.add(new Pair<>(YDS_5_13_C, new ArrayList<>(Arrays.asList("YDS 5.13c", "French 8a+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_13_D, new ArrayList<>(Arrays.asList("YDS 5.13d", "French 8b"))));
        sYdsNameMap.add(new Pair<>(YDS_5_14_A, new ArrayList<>(Arrays.asList("YDS 5.14a", "French 8b+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_14_B, new ArrayList<>(Arrays.asList("YDS 5.14b", "French 8c"))));
        sYdsNameMap.add(new Pair<>(YDS_5_14_C, new ArrayList<>(Arrays.asList("YDS 5.14c", "French 8c+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_14_D, new ArrayList<>(Arrays.asList("YDS 5.14d", "French 9a"))));
        sYdsNameMap.add(new Pair<>(YDS_5_15_A, new ArrayList<>(Arrays.asList("YDS 5.15a", "French 9a+"))));
        sYdsNameMap.add(new Pair<>(YDS_5_15_B, new ArrayList<>(Arrays.asList("YDS 5.15b", "French 9b"))));
        sYdsNameMap.add(new Pair<>(YDS_5_15_C, new ArrayList<>(Arrays.asList("YDS 5.15c", "French 9b+"))));
    }

    public static final List<Pair<Integer, ArrayList<String>>> sFontenblauNameMap;
    static {
        sFontenblauNameMap = new ArrayList<>();
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_3, new ArrayList<>(Arrays.asList("Fontenblau 3", "HUECO VB"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_4_MINUS, new ArrayList<>(Arrays.asList("Fontenblau 4-", "HUECO V0-"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_4, new ArrayList<>(Arrays.asList("Fontenblau 4", "HUECO V0"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_4_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 4+", "HUECO V0+"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_5, new ArrayList<>(Arrays.asList("Fontenblau 5", "HUECO V1"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_5_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 5+", "HUECO V2"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_6_A, new ArrayList<>(Arrays.asList("Fontenblau 6A", "HUECO V3"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_6_A_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 6A+", "HUECO V3"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_6_B, new ArrayList<>(Arrays.asList("Fontenblau 6B", "HUECO V4"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_6_B_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 6B+", "HUECO V4"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_6_C, new ArrayList<>(Arrays.asList("Fontenblau 6C", "HUECO V5"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_6_C_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 6C+", "HUECO V5"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_7_A, new ArrayList<>(Arrays.asList("Fontenblau 7A", "HUECO V6"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_7_A_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 7A+", "HUECO V7"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_7_B, new ArrayList<>(Arrays.asList("Fontenblau 7B", "HUECO V8"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_7_B_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 7B+", "HUECO V8"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_7_C, new ArrayList<>(Arrays.asList("Fontenblau 7C", "HUECO V9"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_7_C_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 7C+", "HUECO V10"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_8_A, new ArrayList<>(Arrays.asList("Fontenblau 8A", "HUECO V11"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_8_A_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 8A+", "HUECO V12"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_8_B, new ArrayList<>(Arrays.asList("Fontenblau 8B", "HUECO V13"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_8_B_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 8B+", "HUECO V14"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_8_C, new ArrayList<>(Arrays.asList("Fontenblau 8C", "HUECO V15"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_8_C_PLUS, new ArrayList<>(Arrays.asList("Fontenblau 8C+", "HUECO V16"))));
        sFontenblauNameMap.add(new Pair<>(FONTENBLAU_9_A, new ArrayList<>(Arrays.asList("Fontenblau 9A", "HUECO V17"))));
    }

    public static String getNameForYdsGrade(int ydsGrade, int gradingSystem) {
        for (Pair<Integer, ArrayList<String>> gradeAndNames : sYdsNameMap) {
            if (gradeAndNames.first == ydsGrade) {
                return gradeAndNames.second.get(gradingSystem);
            }
        }
        // TODO: Throw exception.
        return "";
    }

    public static String[] getFreeClimbingGradeNames(int gradingSystem) {
        String[] result = new String[sYdsNameMap.size()];
        for (int i = 0; i < sYdsNameMap.size(); ++i) {
            Pair<Integer, ArrayList<String>> gradeAndNames = sYdsNameMap.get(i);
            result[i] = gradeAndNames.second.get(gradingSystem);
        }
        return result;
    }

    public static String getNameForFontenblauGrade(int fontGrade, int gradingSystem) {
        for (Pair<Integer, ArrayList<String>> gradeAndNames : sFontenblauNameMap) {
            if (gradeAndNames.first == fontGrade) {
                return gradeAndNames.second.get(gradingSystem);
            }
        }
        // TODO: Throw exception.
        return "";
    }

    public static String[] getBoulderingGradeNames(int gradingSystem) {
        String[] result = new String[sFontenblauNameMap.size()];
        for (int i = 0; i < sFontenblauNameMap.size(); ++i) {
            Pair<Integer, ArrayList<String>> gradeAndNames = sFontenblauNameMap.get(i);
            result[i] = gradeAndNames.second.get(gradingSystem);
        }
        return result;
    }

}
