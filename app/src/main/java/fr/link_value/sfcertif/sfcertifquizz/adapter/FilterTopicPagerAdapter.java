package fr.link_value.sfcertif.sfcertifquizz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.fragments.FilterTopicFragment;

/**
 * Created by jbouffard on 31/03/2017.
 */

public class FilterTopicPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mTopics;

    public FilterTopicPagerAdapter(FragmentManager fm, List<Fragment> topics) {
        super(fm);
        mTopics = topics;
    }

    @Override
    public Fragment getItem(int position) {
        return mTopics.get(position);
    }

    @Override
    public int getCount() {
        return mTopics.size();
    }
}
