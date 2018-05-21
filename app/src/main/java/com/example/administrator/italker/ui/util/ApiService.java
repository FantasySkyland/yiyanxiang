package com.example.administrator.italker.ui.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ZDY
 * on 2018/5/20
 */

public interface ApiService {
    @GET("portal/index/getdata")
    Call<ResponseBody> sendData(@Query("code") int code, @Query("p") String p,
                                @Query("sh") String sh, @Query("v") String v
            , @Query("username") String username);
}
