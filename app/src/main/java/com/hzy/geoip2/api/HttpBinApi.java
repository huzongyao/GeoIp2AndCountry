package com.hzy.geoip2.api;

import com.hzy.geoip2.bean.IPObject;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by huzongrao on 16-9-22.
 */
public interface HttpBinApi {

    @GET("ip")
    Observable<IPObject> getOriginIp();
}
