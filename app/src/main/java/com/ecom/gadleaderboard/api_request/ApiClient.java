package com.ecom.gadleaderboard.api_request;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BASE_URL = "https://gadsapi.herokuapp.com/";
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static Retrofit liveAdapter() {


        LoggingInterceptor interceptor = new LoggingInterceptor();
        //HttpLoggingInterceptor interceptors = new HttpLoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static void LOG(String message) {
        Log.d("HTTP", message);
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {


            Request request = chain.request().newBuilder()
                    .build();

            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers());

            try {
                if (request.method().equalsIgnoreCase("post") || request.method().equalsIgnoreCase("put")) {
                    requestLog = "\n" + requestLog + "\n" + bodyToString(request);
                }
            } catch (Exception e) {
                LOG("Couldnt log api " + e.getMessage());
            }


            LOG("request" + "\n" + requestLog);

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            String responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());

            String bodyString = response.body().string();

            LOG("response" + "\n" + responseLog + "\n" + bodyString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
            // return response;
        }
    }


}
