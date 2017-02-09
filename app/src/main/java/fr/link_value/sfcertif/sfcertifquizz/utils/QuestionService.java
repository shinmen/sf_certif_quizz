package fr.link_value.sfcertif.sfcertifquizz.utils;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.utils.Converter.Question;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jbouffard on 09/02/2017.
 */

public interface QuestionService {
    //mock api response => http://www.mocky.io/v2/
    @GET("589c64a3250000a2078055c7")
    Observable<List<Question>> getListQuestions();
}
