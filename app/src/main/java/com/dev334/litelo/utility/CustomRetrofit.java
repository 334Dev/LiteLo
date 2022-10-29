package com.dev334.litelo.utility;

import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.CoordinatorRequest;
import com.dev334.litelo.model.DepartmentResponse;
import com.dev334.litelo.model.EventCoordinatorResponse;
import com.dev334.litelo.model.EventRequest;
import com.dev334.litelo.model.EventResponse;
import com.dev334.litelo.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CustomRetrofit {
    @POST("auth/signin")
    Call<AuthResponse> signIn(@Body LoginRequest body);

    @GET("dept-event")
    Call<DepartmentResponse> getDepartments();

    @POST("event")
    Call<EventResponse> getEvents(@Body EventRequest requestBody);

    @POST("event-coordie")
    Call<EventCoordinatorResponse> getEventCoordinator(@Body CoordinatorRequest requestBody);
}
