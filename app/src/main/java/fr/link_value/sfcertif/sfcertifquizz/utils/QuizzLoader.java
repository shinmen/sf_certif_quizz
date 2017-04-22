package fr.link_value.sfcertif.sfcertifquizz.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import io.reactivex.Flowable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.LongConsumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jbouffard on 13/04/2017.
 */

public class QuizzLoader {

    public Disposable getQuizzs(final Predicate filter, DisposableSingleObserver subscriber, final LinearLayout progressBar) {
        Flowable.concat(MemoryCache.getQuizzMemoryCache(), QuizzRequest.getQuestionList())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .skipWhile(new Predicate<List<Quizz>>() {
                    @Override
                    public boolean test(List<Quizz> o) throws Exception {
                        return o.size() == 0;
                    }
                })
                .first(new ArrayList<Quizz>())
                .flatMap(new Function<List<Quizz>, SingleSource<List<Quizz>>>() {
                    @Override
                    public SingleSource<List<Quizz>> apply(List<Quizz> quizzs) throws Exception {
                        return Flowable.fromIterable(quizzs)
                                .filter(filter)
                                .toList();
                    }
                })
                .map(new Function<List<Quizz>, List<Quizz>>() {
                    @Override
                    public List<Quizz> apply(List<Quizz> quizzs) throws Exception {
                        long seed = System.nanoTime();
                        Collections.shuffle(quizzs, new Random(seed));
                        return quizzs;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .doOnSuccess(new Consumer<List<Quizz>>() {
                    @Override
                    public void accept(List<Quizz> quizzs) throws Exception {
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .subscribe(subscriber);

        return subscriber;
    }
}
