package com.jungho.reddit;

import android.app.Application;

import com.jungho.reddit.api.retrofit.RedditRetrofitWebService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditApplication extends Application {

    private static final String BASE_URL = "https://www.reddit.com/";

    private RedditRetrofitWebService redditRetrofitWebService;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        redditRetrofitWebService = new RedditRetrofitWebService(retrofit);
    }

    public RedditRetrofitWebService getRedditRetrofitWebService() {
        return redditRetrofitWebService;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
