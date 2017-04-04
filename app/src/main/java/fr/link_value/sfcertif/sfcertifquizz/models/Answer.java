package fr.link_value.sfcertif.sfcertifquizz.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by jbouffard on 07/04/2017.
 */

public class Answer extends RealmObject implements Parcelable{
    private String text;

    public Answer(String text) {
        this.text = text;
    }

    public Answer() {
    }

    public String getText() {
        return text;
    }

    protected Answer(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
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

    @Override
    public String toString() {
        return text;
    }
}
