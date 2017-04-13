package fr.link_value.sfcertif.sfcertifquizz.utils;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import io.reactivex.Flowable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jbouffard on 13/04/2017.
 */

public class QuizzLoader {

    public Disposable getQuizzs(final Predicate filter, DisposableSingleObserver subscriber) {
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return subscriber;
    }
}
