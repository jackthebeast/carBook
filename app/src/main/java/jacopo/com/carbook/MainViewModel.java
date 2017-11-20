package jacopo.com.carbook;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import jacopo.com.carbook.data.CarsFactory;
import jacopo.com.carbook.data.CarsService;
import jacopo.com.carbook.model.Car;
import jacopo.com.carbook.model.SearchRequest;
import jacopo.com.carbook.model.Stop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by jacop on 18/11/2017.
 */

public class MainViewModel extends ViewModel implements Callback<List<Car>> {

    private LatLng origin;
    private LatLng destination;
    private MutableLiveData<List<Car>> carsList;
    private MutableLiveData<Integer> carsProgressVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> carsListVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> errorVisibility = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public void fetchAvailableCars(){
        carsProgressVisibility.postValue(View.VISIBLE);
        carsListVisibility.postValue(View.GONE);
        errorVisibility.postValue(View.GONE);

        SearchRequest request = new SearchRequest();
        Stop originStop = new Stop();
        Stop destinationStop = new Stop();

        originStop.setLoc(origin);
        destinationStop.setLoc(destination);

        request.setStops(originStop, destinationStop);

        Retrofit retrofit = CarsFactory.create();

        CarsService service = retrofit.create(CarsService.class);

        Call<List<Car>> call = service.searchCars(request);

        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
        setCarsList(response.body());
        carsProgressVisibility.postValue(View.GONE);
        if(response.body() == null || response.body().size() == 0){
            carsListVisibility.postValue(View.GONE);
            errorVisibility.postValue(View.VISIBLE);
            error.postValue("No car available for the selected route");
        }else
            carsListVisibility.postValue(View.VISIBLE);
    }

    @Override
    public void onFailure(Call<List<Car>> call, Throwable t) {
        carsProgressVisibility.postValue(View.GONE);
        carsListVisibility.postValue(View.GONE);
        errorVisibility.postValue(View.VISIBLE);
        error.postValue(t.getMessage());
    }


    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
        if(destination != null)
            fetchAvailableCars();
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
        fetchAvailableCars();
    }

    public MutableLiveData<List<Car>> getCarsList() {
        if(carsList == null)
            carsList = new MutableLiveData<>();
        return carsList;
    }

    public void setCarsList(List<Car> carList) {
        this.carsList.postValue(carList);
    }

    public MutableLiveData<Integer> getCarsProgressVisibility() {
        return carsProgressVisibility;
    }

    public void setCarsProgressVisibility(Integer carsProgressVisibility) {
        this.carsProgressVisibility.postValue(carsProgressVisibility);
    }

    public MutableLiveData<Integer> getCarsListVisibility() {
        return carsListVisibility;
    }

    public void setCarsListVisibility(Integer carsListVisibility) {
        this.carsListVisibility.postValue(carsListVisibility);
    }

    public MutableLiveData<Integer> getErrorVisibility() {
        return errorVisibility;
    }

    public void setErrorVisibility(Integer errorVisibility) {
        this.errorVisibility.postValue(errorVisibility);
    }

    public MutableLiveData<String> getError() {
        return error;
    }

}
