package com.example.tims_project.model.notification;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAACSIX8q4:APA91bFzYN0QMKTiwXzrJSKCNwK5nIawXRIKoZ52ZVa1_yxccKHj0zSfo9VuG0GN2aO9flRnw364hYZoWsqaTIf3NzoBF5D5fivbJv_vUilijl5eh6Kk1DnoLpPsAHXe_fRKYR8-WmAV"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
