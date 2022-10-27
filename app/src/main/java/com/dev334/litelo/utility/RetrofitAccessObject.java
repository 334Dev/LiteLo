package com.dev334.litelo.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAccessObject {
    private static CustomRetrofit customRetrofit;
    private static final String BASE_URL = "https://api.divyansh.rocks/api/";

    private RetrofitAccessObject() {
    }

    /**
     * Creates retrofit object (if not already created)
     * Returns the retrofit object
     */
    public static CustomRetrofit getRetrofitAccessObject() {
        if (customRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            customRetrofit = retrofit.create(CustomRetrofit.class);
        }
        return customRetrofit;
    }
}
