package com.dev334.litelo.utility;

import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.CoordinatorRequest;
import com.dev334.litelo.model.CoordinatorResponse;
import com.dev334.litelo.model.DepartmentResponse;
import com.dev334.litelo.model.EventCoordinatorResponse;
import com.dev334.litelo.model.EventRequest;
import com.dev334.litelo.model.EventResponse;
import com.dev334.litelo.model.LoginRequest;
import com.dev334.litelo.model.TeamInvitesResponse;
import com.dev334.litelo.model.UserResponse;
import com.dev334.litelo.model.member.TeamMemberResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomRetrofit {
    @POST("auth/signin")
    Call<AuthResponse> signIn(@Body LoginRequest body);

    @GET("dept-event")
    Call<DepartmentResponse> getDepartments();

    @POST("event")
    Call<EventResponse> getEvents(@Body EventRequest requestBody);

    @GET("user/coordie-event")
    Call<EventCoordinatorResponse> getEventsCoordinated(@Header("Authorization") String auth);

    @POST("auth/send-reset-mail")
    Call<AuthResponse> resetPassword(@Body LoginRequest request);

    @GET("user")
    Call<UserResponse> getUserData(@Header("Authorization") String auth);

    @POST("event-coordie")
    Call<CoordinatorResponse> getCoordinators(@Body CoordinatorRequest request);

    @GET("user/team-invite")
    Call<TeamInvitesResponse> getTeamInvites(@Header("Authorization") String auth);

    @GET("user/team/{id}")
    Call<TeamMemberResponse> getTeamMembers(@Path("id") String teamId);
}
