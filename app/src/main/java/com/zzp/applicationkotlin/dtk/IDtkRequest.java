package com.zzp.applicationkotlin.dtk;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by samzhang on 2021/9/2.
 */
public interface IDtkRequest {

    @GET("etc/search/list-hot-words")
    Call<String> queryHotWord();
}
