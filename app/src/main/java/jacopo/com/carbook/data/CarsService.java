package jacopo.com.carbook.data;

import java.util.List;

import jacopo.com.carbook.model.SearchRequest;
import jacopo.com.carbook.model.Car;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jacop on 19/11/2017.
 */

public interface CarsService {
    @POST("estimate")
    Call<List<Car>> searchCars(@Body SearchRequest request);
}
