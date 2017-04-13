package fr.link_value.sfcertif.sfcertifquizz.utils;

import android.util.Log;
import android.util.LruCache;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by jbouffard on 11/04/2017.
 */

public class MemoryCache {
    public static String QUIZZ_CACHE_KEY = "cache_quizz_key";
    private static int CACHE_SIZE = 4;
    private static LruCache<String, List<Quizz>> quizzCache;

    public static LruCache getCache() {
        if (quizzCache == null) {
            int cacheSize = CACHE_SIZE * 1024 * 1024; // 4MiB
            quizzCache = new LruCache<>(cacheSize);
        }
        return quizzCache;
    }

    public static Flowable getQuizzMemoryCache() {

        Flowable flowable =  Flowable.fromCallable(new Callable<List<Quizz>>() {
            @Override
            public List<Quizz> call() throws Exception {
                List<Quizz> quizzs = (List<Quizz>) getCache().get(QUIZZ_CACHE_KEY);
                if (quizzs == null) {
                    return new ArrayList<>();
                }

                return quizzs;
            }
        });

        return flowable;
    }

    public static Flowable<List<Quizz>> cacheQuizzInMemory(List<Quizz> quizzs) {
        return Flowable.just(quizzs)
                .flatMap(new Function<List<Quizz>, Publisher<List<Quizz>>>() {
                    @Override
                    public Publisher<List<Quizz>> apply(final List<Quizz> quizzs) throws Exception {
                        return Flowable.fromCallable(new Callable<List<Quizz>>() {
                            @Override
                            public List<Quizz> call() throws Exception {
                                getCache().put(QUIZZ_CACHE_KEY, quizzs);

                                return quizzs;
                            }
                        });
                    }
                });
    }
}
