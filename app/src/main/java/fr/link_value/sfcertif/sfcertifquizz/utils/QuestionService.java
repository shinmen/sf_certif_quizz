package fr.link_value.sfcertif.sfcertifquizz.utils;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;
import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by jbouffard on 09/02/2017.
 */

public interface QuestionService {
    //mock api response => http://www.mocky.io/v2/
    @GET("58b571741000007711ea56a6")
    Single<List<QuestionConverter>> getListQuestions();
}
