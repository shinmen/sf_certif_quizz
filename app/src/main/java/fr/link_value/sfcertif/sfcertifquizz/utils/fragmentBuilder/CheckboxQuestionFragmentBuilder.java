package fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder;

import android.support.v4.app.Fragment;

import fr.link_value.sfcertif.sfcertifquizz.fragments.CheckboxQuestionFragment;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;

/**
 * Created by jbouffard on 15/02/2017.
 */

public class CheckboxQuestionFragmentBuilder implements QuestionFragmentBuilder {
    @Override
    public Fragment getFragment(QuestionConverter questionConverter) {
        return CheckboxQuestionFragment.newInstance(questionConverter);
    }
}
