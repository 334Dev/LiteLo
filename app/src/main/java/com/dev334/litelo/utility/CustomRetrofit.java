package com.dev334.litelo.utility;

import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.LoginData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CustomRetrofit {
    @POST("auth/signin")
    Call<AuthResponse> signIn(@Body LoginData body);
}
