package fr.link_value.sfcertif.sfcertifquizz.utils;

import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import retrofit2.http.GET;

/**
 * Created by jbouffard on 09/02/2017.
 */

public interface QuestionService {
    //mock api response => http://www.mocky.io/v2/
    // test 58bd81de0f00009c1e5c67e1
    @GET("58beab2f2600001f19f07c0e")
    Flowable<List<QuestionConverter>> getListQuestions();
}
