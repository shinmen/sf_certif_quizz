package fr.link_value.sfcertif.sfcertifquizz.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.adapter.QuestionPagerAdapter;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.CacheDisk;
import fr.link_value.sfcertif.sfcertifquizz.utils.QuestionService;
import fr.link_value.sfcertif.sfcertifquizz.utils.QuizzRequest;
import fr.link_value.sfcertif.sfcertifquizz.utils.RestClient;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;
import fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder.QuestionFragmentFactory;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class QuizzActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPagerQuizz;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private CompositeDisposable mComposite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzy);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Predicate<QuestionConverter> filter = new Predicate<QuestionConverter>() {
            @Override
            public boolean test(QuestionConverter questionConverter) throws Exception {
                return true;
            }
        };

        load(filter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mPagerQuizz.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPagerQuizz.setCurrentItem(mPagerQuizz.getCurrentItem() - 1);
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Predicate<QuestionConverter> filter = new Predicate<QuestionConverter>() {
            @Override
            public boolean test(QuestionConverter questionConverter) throws Exception {
                return questionConverter.getSubject().equalsIgnoreCase(item.getTitle().toString());
            }
        };
        load(filter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComposite.dispose();
        CacheDisk.getCache().remove(CacheDisk.QUIZZ_JSON);
    }

    private Publisher<List<QuestionConverter>> loadQuestionList(){
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

    private void load(final Predicate<QuestionConverter> filter) {
        mComposite = new CompositeDisposable();
        /*QuizzRequest.getQuestionList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ResourceSubscriber<List<Quizz>>() {
            @Override
            public void onNext(List<Quizz> quizzs) {
                Log.d("request", String.valueOf(quizzs.size()));
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                CacheDisk.getQuizzCacheDisk()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResourceSubscriber<List<Quizz>>() {
                            @Override
                            public void onNext(List<Quizz> quizzs) {
                                Log.d("from cache", quizzs.get(1).getQuestion());
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });*/

        Flowable.concat(CacheDisk.getQuizzCacheDisk(), QuizzRequest.getQuestionList())
                .skipWhile(new Predicate<List<Quizz>>() {
                    @Override
                    public boolean test(List<Quizz> o) throws Exception {
                        return o.size() == 0;
                    }
                })
                .first(new ArrayList<Quizz>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Quizz>>() {
                    @Override
                    public void onSuccess(List<Quizz> quizzs) {
                        Log.d("concat", String.valueOf(quizzs.size()));
                        QuestionFragmentFactory fragmentFactory = new QuestionFragmentFactory(quizzs);
                        // Instantiate a ViewPager and a PagerAdapter.
                        mPagerQuizz = (ViewPager) findViewById(R.id.pager_question);
                        mPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragmentFactory, quizzs.size());
                        mPagerQuizz.setAdapter(mPagerAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.getMessage());
                        e.printStackTrace();
                    }
                })
                ;

        /*Flowable.concat(QuizzRequest.getQuestionList(), CacheDisk.getQuizzCacheDisk())
                .first(new ArrayList<Quizz>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Quizz>>() {
                    @Override
                    public void onSuccess(List<Quizz> quizzs) {
                        Log.d("concat", String.valueOf(quizzs.size()));
                        QuestionFragmentFactory fragmentFactory = new QuestionFragmentFactory(quizzs);
                        // Instantiate a ViewPager and a PagerAdapter.
                        mPagerQuizz = (ViewPager) findViewById(R.id.pager_question);
                        mPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragmentFactory, quizzs.size());
                        mPagerQuizz.setAdapter(mPagerAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.getMessage());
                        e.printStackTrace();
                    }
                })
        ;*/


        /*mComposite.add(Flowable.concat(loadQuestionList().fromItera)
                //.first()
                .flatMap(new Function<List<QuestionConverter>, SingleSource<List<QuestionConverter>>>() {
                    @Override
                    public SingleSource<List<QuestionConverter>> apply(List<QuestionConverter> questionConverters) throws Exception {
                        return Flowable.fromIterable(questionConverters)
                                .filter(filter)
                                .toList();
                    }
                })
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("err", e.getMessage());
                    }
                })
        );*/
    }
}
