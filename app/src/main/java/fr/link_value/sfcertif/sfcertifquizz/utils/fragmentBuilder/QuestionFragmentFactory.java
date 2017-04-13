package fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder;


import android.support.v4.app.Fragment;

import java.util.Hashtable;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;

/**
 * Created by jbouffard on 15/02/2017.
 */

public class QuestionFragmentFactory {
    private List<Quizz> quizzs;
    private Hashtable<String,QuestionFragmentBuilder> typeToFragmentBuilder;

    public QuestionFragmentFactory(List<Quizz> list) {
        quizzs = list;
        typeToFragmentBuilder = new Hashtable<>();
        typeToFragmentBuilder.put("radio" , new RadioQuestionFragmentBuilder());
        typeToFragmentBuilder.put("checkbox", new CheckboxQuestionFragmentBuilder());
        typeToFragmentBuilder.put("simple", new SimpleQuestionFragmentBuilder());
    }

    public Fragment getQuestionFragment(int position) {
        Quizz quizz = quizzs.get(position);
        QuestionFragmentBuilder fragmentBuilder = typeToFragmentBuilder.get(quizz.getQuestionType());

        return fragmentBuilder.getFragment(quizz);
    }
}
