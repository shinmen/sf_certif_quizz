package fr.link_value.sfcertif.sfcertifquizz.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by jbouffard on 07/04/2017.
 */

public class Choice extends RealmObject implements Parcelable{
    private String text;

    public Choice(String text) {
        this.text = text;
    }

    public Choice() {
    }

    public String getText() {
        return text;
    }

    protected Choice(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Choice> CREATOR = new Creator<Choice>() {
        @Override
        public Choice createFromParcel(Parcel in) {
            return new Choice(in);
        }

        @Override
        public Choice[] newArray(int size) {
            return new Choice[size];
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
