package com.ecom.gadleaderboard.api_request;

import com.ecom.gadleaderboard.models.LearningLeader;
import com.ecom.gadleaderboard.models.SkillsLeaders;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("api/hours")
    Call<List<LearningLeader>> fetchLearningLeaders();

    @GET("api/skilliq")
    Call<List<SkillsLeaders>> fetchIQSkillsLeaders();
}
