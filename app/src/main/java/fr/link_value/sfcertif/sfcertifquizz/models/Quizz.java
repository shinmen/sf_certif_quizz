package fr.link_value.sfcertif.sfcertifquizz.models;

import java.util.List;

/**
 * Created by jbouffard on 16/02/2017.
 */

public class Quizz {
    private String question;
    private String more;
    private List<String> choices;
    private List<String> answers;
    private String subject;


    public Quizz(String question, String more, List<String> answer, String subject) {
        this.question = question;
        this.more = more;
        this.answers = answer;
        this.subject = subject;
    }

    public Quizz(String question, String more, List<String> choices, List<String> answers, String subject) {

        this.question = question;
        this.more = more;
        this.choices = choices;
        this.answers = answers;
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public String getMore() {
        return more;
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getSubject() {
        return subject;
    }
}
