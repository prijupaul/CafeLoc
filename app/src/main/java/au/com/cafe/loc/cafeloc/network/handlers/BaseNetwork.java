
package au.com.cafe.loc.cafeloc.network.handlers;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by priju.jacobpaul on 20/12/16.
 */
public class BaseNetwork {

    private String baseUrl;
    private Retrofit retrofit;
    protected static Call<Object> mCurrentRetrofitCall;

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected void init() {

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        })
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getBaseUrl()).client(httpClient).build();
    }


    protected Retrofit getRetrofit() {
        return retrofit;
    }

    protected static Call<Object> getCurrentRetrofitCall() {
        return mCurrentRetrofitCall;
    }
}
