package com.jungho.reddit.api.retrofit;

import android.util.Log;

import com.jungho.reddit.api.model.Data;
import com.jungho.reddit.api.model.RedditData;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RedditRetrofitWebService {

    private RedditRetrofitAPI retrofitAPI;

    public RedditRetrofitWebService(Retrofit retrofit) {
        retrofitAPI = retrofit.create(RedditRetrofitAPI.class);
    }

    public Single<Data> fetchRedditList(String after) {
        return retrofitAPI.getRedditData(after)
                .observeOn(Schedulers.computation())
                .map(new Function<RedditData, Data>() {
                    @Override
                    public Data apply(@NonNull RedditData redditData) throws Exception {
                        return redditData.getData();
                    }
                })
                .onErrorReturn(new Function<Throwable, Data>() {
                    @Override
                    public Data apply(@NonNull Throwable throwable) throws Exception {
                        // error message log
                        Log.i(RedditRetrofitWebService.class.getSimpleName(), throwable.toString());
                        return new Data();
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
