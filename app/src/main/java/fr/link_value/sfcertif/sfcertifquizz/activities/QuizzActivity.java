package fr.link_value.sfcertif.sfcertifquizz.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.adapter.QuestionPagerAdapter;
import fr.link_value.sfcertif.sfcertifquizz.fragments.FilterTopicFragment;
import fr.link_value.sfcertif.sfcertifquizz.utils.QuestionService;
import fr.link_value.sfcertif.sfcertifquizz.utils.RestClient;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;
import fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder.QuestionFragmentFactory;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class QuizzActivity extends AppCompatActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPagerQuizz;

    private ViewPager mPagerFilter;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private CompositeDisposable mComposite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        mComposite = new CompositeDisposable();
        mComposite.add(loadQuestionList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSingleObserver<List<QuestionConverter>>() {
                    @Override
                    public void onSuccess(List<QuestionConverter> questionConverters) {
                        QuestionFragmentFactory fragmentFactory = new QuestionFragmentFactory(questionConverters);
                        // Instantiate a ViewPager and a PagerAdapter.
                        mPagerQuizz = (ViewPager) findViewById(R.id.pager_question);
                        mPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragmentFactory, questionConverters.size());
                        mPagerQuizz.setAdapter(mPagerAdapter);

                        /*mPagerFilter = (ViewPager) findViewById(R.id.pager_filter);
                        mFilterTopicAdapter = new FilterTopicPagerAdapter(getSupportFragmentManager(), loadFilterTopics());
                        mPagerFilter.setAdapter(mFilterTopicAdapter);*/
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("err", e.getMessage());
                    }
                }));

    }

    @Override
    public void onBackPressed() {
        if (mPagerQuizz.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPagerQuizz.setCurrentItem(mPagerQuizz.getCurrentItem() - 1);
        }
    }

    private List<Fragment> loadFilterTopics() {
        List<Fragment> list = new ArrayList<>();
        list.add(FilterTopicFragment.newInstance("HTTP"));
        list.add(FilterTopicFragment.newInstance("PHP et Sécurité Web"));
        list.add(FilterTopicFragment.newInstance("Routing"));

        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComposite.dispose();
    }

    private Single<List<QuestionConverter>> loadQuestionList(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!*/
        Retrofit client = RestClient
                .buildClient("http://www.mocky.io/v2/", httpClient);

        QuestionService eventService = client.create(QuestionService.class);

        return eventService.getListQuestions();
    }
}
