package fr.link_value.sfcertif.sfcertifquizz.utils;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.utils.Converter.QuestionConverter;
import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by jbouffard on 09/02/2017.
 */

public interface QuestionService {
    //mock api response => http://www.mocky.io/v2/
    @GET("58ac2aa0100000890c514a77")
    Single<List<QuestionConverter>> getListQuestions();
}
