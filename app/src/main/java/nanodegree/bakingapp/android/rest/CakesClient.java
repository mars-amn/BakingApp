package nanodegree.bakingapp.android.rest;

import java.util.List;

import nanodegree.bakingapp.android.models.Cake;
import retrofit2.Call;
import retrofit2.http.GET;


public interface CakesClient {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Cake>> getCakes();
}
