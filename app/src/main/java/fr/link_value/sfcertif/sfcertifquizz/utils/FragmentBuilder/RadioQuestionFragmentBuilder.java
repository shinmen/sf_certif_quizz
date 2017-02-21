package fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder;

import android.support.v4.app.Fragment;

import fr.link_value.sfcertif.sfcertifquizz.fragments.RadioQuestionFragment;
import fr.link_value.sfcertif.sfcertifquizz.utils.Converter.QuestionConverter;

/**
 * Created by jbouffard on 15/02/2017.
 */

public class RadioQuestionFragmentBuilder implements QuestionFragmentBuilder {
    @Override
    public Fragment getFragment(QuestionConverter questionConverter) {

        return RadioQuestionFragment.newInstance(questionConverter);
    }
}
