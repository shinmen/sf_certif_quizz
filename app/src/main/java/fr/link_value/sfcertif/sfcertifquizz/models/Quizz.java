package fr.link_value.sfcertif.sfcertifquizz.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by jbouffard on 16/02/2017.
 */

public class Quizz extends RealmObject implements Parcelable {
    private String questionType;
    private String question;
    private List<Learn> lessons = new ArrayList<>();
    private List<Choice> choices = new ArrayList<>();;
    private List<Answer> answers = new ArrayList<>();;
    private String topic;

    public Quizz() {
    }

    public Quizz(String question, List<Learn> lessons, List<Answer> answer, String topic) {
        this.question = question;
        this.lessons = lessons;
        this.answers = answer;
        this.topic = topic;
    }

    public Quizz(String questionType, String question, List<Learn> lessons, List<Choice> choices, List<Answer> answers, String topic) {

        this.questionType = questionType;

        this.question = question;
        this.lessons = lessons;
        this.choices = choices;
        this.answers = answers;
        this.topic = topic;
    }

    protected Quizz(Parcel in) {
        questionType = in.readString();
        question = in.readString();
        in.readList(lessons, Learn.class.getClassLoader());
        in.readList(choices, Choice.class.getClassLoader());
        in.readList(answers, Answer.class.getClassLoader());
        topic = in.readString();
    }

    public static final Creator<Quizz> CREATOR = new Creator<Quizz>() {
        @Override
        public Quizz createFromParcel(Parcel in) {
            return new Quizz(in);
        }

        @Override
        public Quizz[] newArray(int size) {
            return new Quizz[size];
        }
    };

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public List<Learn> getLessons() {
        return lessons;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionType);
        dest.writeString(question);
        dest.writeString(topic);
        dest.writeList(lessons);
        dest.writeList(choices);
        dest.writeList(answers);
    }
}
