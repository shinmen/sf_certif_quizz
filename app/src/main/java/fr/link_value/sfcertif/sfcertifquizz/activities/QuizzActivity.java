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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.adapter.QuestionPagerAdapter;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.MemoryCache;
import fr.link_value.sfcertif.sfcertifquizz.utils.QuizzLoader;
import fr.link_value.sfcertif.sfcertifquizz.utils.fragmentBuilder.QuestionFragmentFactory;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableSingleObserver;

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

    private LinearLayout mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mProgressBar = (LinearLayout) findViewById(R.id.loader_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mComposite = new CompositeDisposable();
        loadQuizzOnFirstOpen();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mPagerQuizz == null || mPagerQuizz.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPagerQuizz.setCurrentItem(mPagerQuizz.getCurrentItem() - 1);
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
        String topic = item.getTitle().toString();
        loadQuizzOnFilter(topic);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComposite.dispose();
        MemoryCache.getCache().remove(MemoryCache.QUIZZ_CACHE_KEY);
    }

    private void loadQuizzOnFirstOpen() {
        QuizzLoader loader = new QuizzLoader();
        DisposableSingleObserver<List<Quizz>> subscriber = new DisposableSingleObserver<List<Quizz>>() {
            @Override
            public void onSuccess(List<Quizz> quizzs) {
                QuestionFragmentFactory fragmentFactory = new QuestionFragmentFactory(quizzs);
                // Instantiate a ViewPager and a PagerAdapter.
                mPagerQuizz = (ViewPager) findViewById(R.id.pager_question);
                mPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragmentFactory, quizzs.size());
                mPagerQuizz.setAdapter(mPagerAdapter);
                dispose();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("error", e.getMessage());
                Toast.makeText(QuizzActivity.this, new String("Une erreur est survenue lors du chargement de votre quizz."), Toast.LENGTH_LONG).show();
            }
        };

        Predicate<Quizz> filter = new Predicate<Quizz>() {
            @Override
            public boolean test(Quizz quizz) throws Exception {
                return true;
            }
        };
        Disposable disposable = loader.getQuizzs(filter, subscriber, mProgressBar);
        mComposite.add(disposable);
    }

    private void loadQuizzOnFilter(final String topic) {
        QuizzLoader loader = new QuizzLoader();

        DisposableSingleObserver<List<Quizz>> subscriber = new DisposableSingleObserver<List<Quizz>>() {
            @Override
            public void onSuccess(List<Quizz> quizzs) {
                QuestionFragmentFactory fragmentFactory = new QuestionFragmentFactory(quizzs);
                // Instantiate a ViewPager and a PagerAdapter.
                mPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragmentFactory, quizzs.size());
                mPagerQuizz.setAdapter(mPagerAdapter);
                dispose();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("error", e.getMessage());
                Toast.makeText(QuizzActivity.this, new String("Une erreur est survenue lors du chargement de votre quizz."), Toast.LENGTH_LONG).show();
            }
        };

        Predicate<Quizz> filter = new Predicate<Quizz>() {
            @Override
            public boolean test(Quizz quizz) throws Exception {
                if (topic.equalsIgnoreCase(getString(R.string.topic_all))) {
                    return true;
                }
                return quizz.getTopic().equalsIgnoreCase(topic);
            }
        };

        Disposable disposable = loader.getQuizzs(filter, subscriber, mProgressBar);
        mComposite.add(disposable);
    }
}
