package com.mksens.devicespecs.rest.api;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import com.mksens.devicespecs.model.DeviceSpecs;
import com.mksens.devicespecs.model.Statistic;
import com.mksens.devicespecs.rest.Response;

public interface ApiService {
    @POST("/specs")
    Call<Response> postSpecs(@Body DeviceSpecs info);

    @GET("/stat/{mac}/{sensor}")
    Call<Statistic> getStat(@Path("mac") String mac, @Path("sensor") String sensorName);
}
