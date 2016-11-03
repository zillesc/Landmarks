package ninja.zilles.landmarks;

/**
 * Created by zilles on 11/1/16.
 */

public class Landmark implements Comparable<Landmark> {
    public static final String example = "{\n"+
            "        \"type_of_landmark\": \"Building\",\n"+
            "        \"address\": \"346-352 N Neil St\",\n"+
            "        \"notes\": \"also on National Register Historic Places\",\n"+
            "        \"ownership\": \"Museum Group\",\n"+
            "        \"date_designated\": \"1998-03-17T00:00:00\",\n"+
            "        \"landmark_name\": \"Orpheum Theater\",\n"+
            "        \"location\": {\n"+
            "            \"latitude\": \"40.1194838838\",\n"+
            "            \"human_address\": \"{\\\"address\\\":\\\"346-352 N Neil St\\\",\\\"city\\\":\\\"\\\",\\\"state\\\":\\\"\\\",\\\"zip\\\":\\\"\\\"}\",\n"+
            "            \"needs_recoding\": false,\n"+
            "            \"longitude\": \"-88.2425214665\"\n"+
            "        }\n"+
            "    }";

    private String type_of_landmark;
    private String address;
    private String notes;
    private String ownership;
    private String date_designated;
    private String landmark_name;
    private Location location;

    public String getTypeOfLandmark() {
        return type_of_landmark;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }

    public String getOwnership() {
        return ownership;
    }

    public String getDateDesignated() {
        return date_designated;
    }

    public String getLandmarkName() {
        return landmark_name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int compareTo(Landmark another) {
        if (another instanceof Landmark) {
            return landmark_name.compareTo(((Landmark)another).landmark_name);
        }
        return 0;
    }

    public class Location {
        private String latitude;
        private String longitude;
        private String human_address;
        private boolean needs_recording;

        public double getLatitude() {
            return Double.parseDouble(latitude);
        }

        public double getLongitude() {
            return Double.parseDouble(longitude);
        }

        public String getHumanAddress() {
            return human_address;
        }

        public boolean needsRecording() {
            return needs_recording;
        }
    }

}
