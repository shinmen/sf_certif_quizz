package fr.link_value.sfcertif.sfcertifquizz.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by jbouffard on 07/04/2017.
 */

public class Learn extends RealmObject implements Parcelable{
    private String text;

    public Learn(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Learn() {
    }

    protected Learn(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Learn> CREATOR = new Creator<Learn>() {
        @Override
        public Learn createFromParcel(Parcel in) {
            return new Learn(in);
        }

        @Override
        public Learn[] newArray(int size) {
            return new Learn[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
}
