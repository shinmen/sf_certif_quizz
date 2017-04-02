package fr.link_value.sfcertif.sfcertifquizz.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.adapter.QuestionPagerAdapter;
import fr.link_value.sfcertif.sfcertifquizz.fragments.FilterTopicFragment;
import fr.link_value.sfcertif.sfcertifquizz.utils.QuestionService;
import fr.link_value.sfcertif.sfcertifquizz.utils.RestClient;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;
import fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder.QuestionFragmentFactory;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class QuizzyActivity extends AppCompatActivity
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

    private void load(final Predicate<QuestionConverter> filter) {
        mComposite = new CompositeDisposable();
        mComposite.add(loadQuestionList()
                .cache()
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
        );
    }

    private ResourceSingleObserver getNewSubscriber() {
        return new ResourceSingleObserver<List<QuestionConverter>>() {
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
        };
    }
}
