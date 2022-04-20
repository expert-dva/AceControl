package ru.alex.acecontrol;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroSrv {
    private static final String url = "http://192.168.88.10:8888/";
    private static Retrofit retrofit;
    private static RetroSrv instance;

    // Логирование запросов
/*    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();*/
    // конец куска для логирования

    private RetroSrv(){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
               // .client(client)// для логирования запросов
                .build();
    }

    public static RetroSrv getInstance(){
        if (instance == null){
            instance = new RetroSrv();
        }
        return instance;
    }
    public static AceAPI getAceApi() {
        return retrofit.create(AceAPI.class); }
}
