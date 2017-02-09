package fr.link_value.sfcertif.sfcertifquizz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fr.link_value.sfcertif.sfcertifquizz.fragments.QuestionFragment;
import fr.link_value.sfcertif.sfcertifquizz.fragments.RadioQuestionFragment;

/**
 * Created by jbouffard on 08/02/2017.
 */

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    public QuestionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return RadioQuestionFragment.newInstance("", "");
    }

    @Override
    public int getCount() {
        return 2;
    }
}
