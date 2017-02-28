package fr.link_value.sfcertif.sfcertifquizz.utils.converter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionConverter {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("choice")
    @Expose
    private List<String> choice = null;
    @SerializedName("answer")
    @Expose
    private List<String> answer = null;
    @SerializedName("more")
    @Expose
    private List<String> more = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getChoice() {
        return choice;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getMores() {
        return more;
    }

    public void setMores(List<String> more) {
        this.more = more;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}

