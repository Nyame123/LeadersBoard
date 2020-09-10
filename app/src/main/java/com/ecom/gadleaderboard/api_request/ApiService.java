package com.ecom.gadleaderboard.api_request;

import com.ecom.gadleaderboard.models.LearningLeader;
import com.ecom.gadleaderboard.models.SkillsLeaders;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("api/hours")
    Call<List<LearningLeader>> fetchLearningLeaders();

    @GET("api/skilliq")
    Call<List<SkillsLeaders>> fetchIQSkillsLeaders();

    @POST("1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse")
    @FormUrlEncoded
    Call<Void> postResults(@Field("entry.1824927963") String email, @Field("entry.1877115667") String name
            , @Field("entry.2006916086") String last, @Field("entry.284483984") String linkToProject);
}
