package fr.link_value.sfcertif.sfcertifquizz.utils;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.models.Answer;
import fr.link_value.sfcertif.sfcertifquizz.models.Choice;
import fr.link_value.sfcertif.sfcertifquizz.models.Learn;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by jbouffard on 07/04/2017.
 */

public class QuizzRequest {

    public static Flowable<List<Quizz>> getQuestionList(final Realm realm) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!*/
        Retrofit client = RestClient
                .buildClient("http://www.mocky.io/v2/", httpClient);

        QuestionService quizzService = client.create(QuestionService.class);

        return quizzService.getListQuestions()
                .flatMap(new Function<List<QuestionConverter>, Publisher<List<Quizz>>>() {
                    @Override
                    public Publisher<List<Quizz>> apply(List<QuestionConverter> questionConverters) throws Exception {
                        return Flowable.fromIterable(questionConverters)
                                .map(new Function<QuestionConverter, Quizz>() {
                                    @Override
                                    public Quizz apply(QuestionConverter questionConverter) throws Exception {
                                        String type = questionConverter.getType();
                                        String question = questionConverter.getQuestion();
                                        String topic = questionConverter.getSubject();
                                        List<Learn> mores = new ArrayList<Learn>();
                                        for (String more:questionConverter.getMores()) {
                                            mores.add(new Learn(more));
                                        }
                                        List<Choice> choices = new ArrayList<Choice>();
                                        for (String choice:questionConverter.getChoices()) {
                                            choices.add(new Choice(choice));
                                        }
                                        List<Answer> answers = new ArrayList<Answer>();
                                        for (String answer:questionConverter.getChoices()) {
                                            answers.add(new Answer(answer));
                                        }
                                        return new Quizz(type, question, mores, choices, answers, topic);
                                    }
                                })
                                .toList()
                                .toFlowable();
                    }
                }).flatMap(new Function<List<Quizz>, Flowable<List<Quizz>>>() {
                    @Override
                    public Flowable<List<Quizz>> apply(List<Quizz> quizzs) throws Exception {
                        return DbQuizzStorage.cacheQuizzes(Flowable.just(quizzs), DbQuizzStorage.getRealm(realm));
                    }
                });

    }
}
