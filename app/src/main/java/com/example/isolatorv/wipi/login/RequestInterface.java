package com.example.isolatorv.wipi.login;

/**
 * Created by isolatorv on 2017. 9. 10..
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("http://13.228.91.43/login-register/index.php")
    Call<ServerResponse> operation(@Body ServerRequest request);

}