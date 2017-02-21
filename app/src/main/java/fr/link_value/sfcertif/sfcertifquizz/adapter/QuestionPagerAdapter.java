package fr.link_value.sfcertif.sfcertifquizz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder.QuestionFragmentFactory;

/**
 * Created by jbouffard on 08/02/2017.
 */

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private QuestionFragmentFactory fragmentFactory;

    private int countQuestions;

    public QuestionPagerAdapter(FragmentManager fm, QuestionFragmentFactory questionFragmentFactory, int questionsSize) {
        super(fm);
        fragmentFactory = questionFragmentFactory;
        countQuestions = questionsSize;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentFactory.getQuestionFragment(position);
    }

    @Override
    public int getCount() {
        return countQuestions;
    }
}
