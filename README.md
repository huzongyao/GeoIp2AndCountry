# GeoIp2AndCountry
This project demonstrate how to roughly query the country by IP Address with GeoIP2 on Android

#### Detail
* First, we get the local client origin IP by request some server such as: http://httpbin.org/ip
* with the IP, we locally query the GeOIP database to get some country name and code  
* the dat file: http://geolite.maxmind.com/download/geoip/database/GeoLite2-Country.mmdb.gz
* and the algorithm java code: http://maxmind.github.io/GeoIP2-java/

* Gradle
```
dependencies {
    compile 'com.maxmind.geoip2:geoip2:2.8.0'
}
```


#### for more information
* GeoIp2: http://dev.maxmind.com/geoip/geoip2/web-services/
* retrofit: https://github.com/square/retrofit
* RxJava: https://github.com/ReactiveX/RxJava
* RxAndroid: https://github.com/ReactiveX/RxAndroid
* gradle-retrolambda: https://github.com/evant/gradle-retrolambda


#### screenshot
![screenshot](https://github.com/huzongyao/GeoIp2AndCountry/blob/master/misc/screen.gif?raw=true)