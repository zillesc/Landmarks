package ninja.zilles.landmarks;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zilles on 11/1/16.
 */

public class Landmark implements Comparable<Landmark>,Parcelable {

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
        return landmark_name.compareTo(another.landmark_name);

//        if (another instanceof Landmark) {
//            return landmark_name.compareTo(((Landmark)another).landmark_name);
//        }
//        return 0;
    }

    public static class Location implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.latitude);
            dest.writeString(this.longitude);
            dest.writeString(this.human_address);
            dest.writeByte(this.needs_recording ? (byte) 1 : (byte) 0);
        }

        public Location() {
        }

        protected Location(Parcel in) {
            this.latitude = in.readString();
            this.longitude = in.readString();
            this.human_address = in.readString();
            this.needs_recording = in.readByte() != 0;
        }

        public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel source) {
                return new Location(source);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type_of_landmark);
        dest.writeString(this.address);
        dest.writeString(this.notes);
        dest.writeString(this.ownership);
        dest.writeString(this.date_designated);
        dest.writeString(this.landmark_name);
        dest.writeParcelable(this.location, flags);
    }

    public Landmark() {
    }

    protected Landmark(Parcel in) {
        this.type_of_landmark = in.readString();
        this.address = in.readString();
        this.notes = in.readString();
        this.ownership = in.readString();
        this.date_designated = in.readString();
        this.landmark_name = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Parcelable.Creator<Landmark> CREATOR = new Parcelable.Creator<Landmark>() {
        @Override
        public Landmark createFromParcel(Parcel source) {
            return new Landmark(source);
        }

        @Override
        public Landmark[] newArray(int size) {
            return new Landmark[size];
        }
    };
}
