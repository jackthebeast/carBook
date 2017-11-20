package jacopo.com.carbook;

import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import jacopo.com.carbook.data.CarsFactory;
import jacopo.com.carbook.data.CarsService;
import jacopo.com.carbook.model.SearchRequest;
import jacopo.com.carbook.model.Stop;
import jacopo.com.carbook.model.Vehicle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by jacop on 18/11/2017.
 */

public class MainViewModel extends ViewModel implements Callback<List<Vehicle>> {

    private LatLng origin;
    private LatLng destination;
    private List<Vehicle> vehicleList;

    public void fetchAvailableCars(){

        SearchRequest request = new SearchRequest();
        Stop originStop = new Stop();
        Stop destinationStop = new Stop();

        originStop.setLoc(origin);
        destinationStop.setLoc(destination);

        request.setStops(originStop, destinationStop);

        Retrofit retrofit = CarsFactory.create();

        CarsService service = retrofit.create(CarsService.class);

        Call<List<Vehicle>> call = service.searchCars(request);

        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
        return;
    }

    @Override
    public void onFailure(Call<List<Vehicle>> call, Throwable t) {
        return;
    }


    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
        fetchAvailableCars();
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }


}
