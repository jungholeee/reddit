package com.jungho.reddit.api.retrofit;

import com.jungho.reddit.api.model.RedditData;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * retrofit interface for reddit listiing
 */
public interface RedditRetrofitAPI {

    @GET("r/all/new.json")
    Single<RedditData> getRedditData(@Query("after") @Nullable String after);
}