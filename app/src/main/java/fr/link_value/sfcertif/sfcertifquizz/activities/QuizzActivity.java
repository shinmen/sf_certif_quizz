package fr.link_value.sfcertif.sfcertifquizz.activities;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.adapter.QuestionPagerAdapter;
import fr.link_value.sfcertif.sfcertifquizz.fragments.OnFragmentResponseListener;
import fr.link_value.sfcertif.sfcertifquizz.utils.Converter.QuestionConverter;
import fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder.QuestionFragmentFactory;
import fr.link_value.sfcertif.sfcertifquizz.utils.QuestionService;
import fr.link_value.sfcertif.sfcertifquizz.utils.RestClient;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class QuizzActivity extends AppCompatActivity implements OnFragmentResponseListener {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        loadQuestionList()
                //.cache()
  /*              .flatMapIterable(new Function<List<QuestionConverter>, Iterable<QuestionConverter>>() {
                    @Override
                    public Iterable<QuestionConverter> apply(List<QuestionConverter> questions) throws Exception {
                        return questions;
                    }
                })*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<QuestionConverter>>() {
                    @Override
                    public void onSuccess(List<QuestionConverter> questionConverters) {
                        QuestionFragmentFactory fragmentFactory = new QuestionFragmentFactory(questionConverters);
                        // Instantiate a ViewPager and a PagerAdapter.
                        mPager = (ViewPager) findViewById(R.id.pager_question);
                        mPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragmentFactory, questionConverters.size());
                        mPager.setAdapter(mPagerAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("err", e.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onFragmentResponse(String uri) {

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
