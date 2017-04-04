package fr.link_value.sfcertif.sfcertifquizz.utils;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.Callable;

import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jbouffard on 03/04/2017.
 */

public class DbQuizzStorage {

    public static Flowable getRealm(Realm realm) {
        return Flowable.just(realm);
    }

    public static Flowable<List<Quizz>> cacheQuizzes(Flowable<List<Quizz>> quizzes, Flowable<Realm> realm) {
        return Flowable
                .zip(quizzes, realm, new BiFunction<List<Quizz>, Realm, List<Quizz>>() {
                    @Override
                    public List<Quizz> apply(List<Quizz> quizzes, Realm realm) throws Exception {
                        realm.beginTransaction();
                        realm.copyToRealm(quizzes);
                        realm.commitTransaction();
                        realm.close();

                        return quizzes;
                    }
                });
    }

    public static Flowable<List<Quizz>> getCachedQuizzes(final Realm realm) {
        return Flowable.fromCallable(new Callable<List<Quizz>>() {
            @Override
            public List<Quizz> call() throws Exception {
                RealmResults<Quizz> result = realm.where(Quizz.class).findAll();
                realm.close();

                return realm.copyFromRealm(result);
            }
        });
    }
}
