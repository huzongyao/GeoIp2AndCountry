package com.hzy.geoip2;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzy.geoip2.api.HttpBinService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    EditText mIpInput;
    Button mBtnGetCountry;
    TextView mGeoIpCountry;
    SwipeRefreshLayout mRefresh;
    DatabaseReader mReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIpInput = (EditText) findViewById(R.id.text_ip_input);
        mBtnGetCountry = (Button) findViewById(R.id.button_get_country);
        mGeoIpCountry = (TextView) findViewById(R.id.text_groip_country);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.ip_local_refresh);
        getLocalIpAddress();
        prepareGeoIpDat();
        setUpEvents();
    }

    /**
     * set up some enent of the controls
     */
    private void setUpEvents() {
        mBtnGetCountry.setOnClickListener(l -> getCountryFromGeoIpDat());
        mRefresh.setOnRefreshListener(this::getLocalIpAddress);
    }

    /**
     * get country name and code from LookupService
     */
    private void getCountryFromGeoIpDat() {
        if (mReader != null) {
            Observable.just(mIpInput.getText().toString()).map(s -> {
                CountryResponse countryRespond = null;
                try {
                    InetAddress address = InetAddress.getByName(s);
                    countryRespond = mReader.country(address);
                } catch (GeoIp2Exception | IOException e) {
                    e.printStackTrace();
                }
                return countryRespond;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(countryRespond -> {
                        Country country = countryRespond.getCountry();
                        Continent continent = countryRespond.getContinent();
                        String couns = country.getNames().toString() + " " + country.getIsoCode();
                        String conts = continent.getNames().toString() + " " + continent.getCode();
                        mGeoIpCountry.setText(couns + "\n\n" + conts);
                    });
        }
    }

    /**
     * make sure the LookupService is available
     */
    private void prepareGeoIpDat() {
        Observable.just("GeoLite2-Country.mmdb").map(s -> {
            try {
                copyAssets(s);
                File datFile = new File(getFilesDir(), s);
                mReader = new DatabaseReader.Builder(datFile).build();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    mGeoIpCountry.setText("prepare dat file " + (aBoolean ? "ok" : "failed"));
                });
    }

    /**
     * get local IP address from http://httpbin.org/
     */
    private void getLocalIpAddress() {
        HttpBinService.create().getOriginIp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ipObject -> {
                    mIpInput.setText(ipObject.getOrigin());
                    mRefresh.setRefreshing(false);
                });
    }

    /**
     * copy assets file to file dir
     *
     * @param assetName
     * @throws IOException
     */
    private void copyAssets(String assetName) throws IOException {
        File of = new File(getFilesDir(), assetName);
        InputStream is = this.getAssets().open(assetName);
        if (of.exists() && of.length() == is.available()) {
            return;
        }
        OutputStream os = new FileOutputStream(of);
        byte[] buffer = new byte[1024 * 1024];
        int length = is.read(buffer);
        while (length > 0) {
            os.write(buffer, 0, length);
            length = is.read(buffer);
        }
        os.flush();
        is.close();
        os.close();
    }
}
