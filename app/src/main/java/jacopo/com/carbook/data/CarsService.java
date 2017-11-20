package jacopo.com.carbook.data;

import java.util.List;
import io.reactivex.Observable;

import jacopo.com.carbook.model.SearchRequest;
import jacopo.com.carbook.model.Vehicle;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by jacop on 19/11/2017.
 */

public interface CarsService {
    @POST("estimate")
    Call<List<Vehicle>> searchCars(@Body SearchRequest request);
}
