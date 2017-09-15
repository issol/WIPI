package com.example.isolatorv.wipi.login;

/**
 * Created by isolatorv on 2017. 9. 10..
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("http://13.229.34.115/login-register/index.php")
    Call<ServerResponse> operation(@Body ServerRequest request);

}