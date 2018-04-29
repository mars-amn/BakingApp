package nanodegree.bakingapp.android.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static Retrofit mRetrofit = null;

    public static CakesClient getCakesClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(CakesClient.class);
    }
}
