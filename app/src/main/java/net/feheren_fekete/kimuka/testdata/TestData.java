package net.feheren_fekete.kimuka.testdata;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.ModelUtils;
import net.feheren_fekete.kimuka.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestData {

    public static void addTestData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference availabilityTable = database.getReference().child(ModelUtils.TABLE_AVAILABILITY);

        Availability testAvailability = new Availability();
        testAvailability.setKey("testAvailability1");
        testAvailability.setUserKey("testUser1");
        testAvailability.setUserName("Test User 1");
        testAvailability.setHostUser(true);
        testAvailability.setLocationLatitude(60.1657919);
        testAvailability.setLocationLongitude(24.9036832);
        testAvailability.setLocationName("Kiipeilyareena");
        testAvailability.setLocationAddress("Energiakatu 3, 00180 Helsinki, Suomi");
        testAvailability.setStartTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));
        testAvailability.setEndTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30) + TimeUnit.HOURS.toMillis(2));
        testAvailability.setActivity(ModelUtils.toCommaSeparatedString(Arrays.asList(
                Availability.ACTIVITY_LEAD)));
        testAvailability.setNeedPartner(Availability.NEED_PARTNER_YES);
        testAvailability.setIfNoPartner(Availability.IF_NO_PARTNER_NOT_DECIDED_YET);
        testAvailability.setSharedEquipment(ModelUtils.toCommaSeparatedString(Arrays.asList(
                Availability.EQUIPMENT_GRIGRI)));
        testAvailability.setCanBelay(User.CAN_BELAY_YES);
        testAvailability.setNote("");
        testAvailability.setJoinedAvailabilityKeys(new ArrayList<String>());
        DatabaseReference availabilityRef = availabilityTable.push();
        availabilityRef.setValue(testAvailability);
    }

}
