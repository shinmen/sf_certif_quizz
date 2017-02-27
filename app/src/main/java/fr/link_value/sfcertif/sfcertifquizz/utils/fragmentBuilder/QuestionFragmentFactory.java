package fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder;


import android.support.v4.app.Fragment;

import java.util.Hashtable;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;

/**
 * Created by jbouffard on 15/02/2017.
 */

public class QuestionFragmentFactory {
    private List<QuestionConverter> questionConverters;
    private Hashtable<String,QuestionFragmentBuilder> typeToFragmentBuilder;

    public QuestionFragmentFactory(List<QuestionConverter> list) {
        questionConverters = list;
        typeToFragmentBuilder = new Hashtable<>();
        typeToFragmentBuilder.put("radio" , new RadioQuestionFragmentBuilder());
        typeToFragmentBuilder.put("checkbox", new CheckboxQuestionFragmentBuilder());
        typeToFragmentBuilder.put("simple", new SimpleQuestionFragmentBuilder());
    }

    public Fragment getQuestionFragment(int position) {
        QuestionConverter questionConverter = questionConverters.get(position);
        QuestionFragmentBuilder fragmentBuilder = typeToFragmentBuilder.get(questionConverter.getType());

        return fragmentBuilder.getFragment(questionConverter);
    }
}
