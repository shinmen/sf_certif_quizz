package fr.link_value.sfcertif.sfcertifquizz.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by jbouffard on 16/02/2017.
 */
public class Quizz implements Parcelable{
    private String questionType;
    private String question;
    private List<String> lessons;
    private List<String> choices;
    private List<String> answers;
    private String topic;

    public Quizz() {
    }

    public Quizz(String question, List<String> lessons, List<String> answer, String topic) {
        this.question = question;
        this.lessons = lessons;
        this.answers = answer;
        this.topic = topic;
    }

    public Quizz(String questionType, String question, List<String> lessons, List<String> choices, List<String> answers, String topic) {
        this.questionType = questionType;
        this.question = question;
        this.lessons = lessons;
        this.choices = choices;
        this.answers = answers;
        this.topic = topic;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<String> getAnswers() {
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

    }
}
