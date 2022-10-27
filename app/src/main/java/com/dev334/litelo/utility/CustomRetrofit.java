package com.dev334.litelo.utility;

import com.dev334.litelo.model.AuthResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CustomRetrofit {
    @POST("auth/signin")
    Call<AuthResponse> signIn(@Body JSONObject body);
}
