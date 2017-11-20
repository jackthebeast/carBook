package jacopo.com.carbook;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import jacopo.com.carbook.model.Car;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, OnMapReadyCallback {

    private static final String ORIGIN_MARKER_TITLE = "Origin";
    private static final String DESTINATION_MARKER_TITLE = "Destination";
    private GoogleMap map;
    private MainViewModel viewModel;
    private RecyclerView carsList;
    private ProgressBar progress;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        initializeMap();

        viewModel.setErrorVisibility(View.GONE);
        viewModel.setCarsListVisibility(View.GONE);
        viewModel.setCarsProgressVisibility(View.GONE);

        carsList = (RecyclerView) findViewById(R.id.cars_list);
        carsList.setLayoutManager(new LinearLayoutManager(this));

        progress = (ProgressBar) findViewById(R.id.loading_cars_list);
        error = (TextView) findViewById(R.id.list_error); 

        viewModel.getCarsList().observeForever(new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                CarsAdapter adapter = new CarsAdapter();
                adapter.setCars(cars);
                carsList.setAdapter(adapter);
                carsList.getAdapter().notifyDataSetChanged();
            }
        });

        viewModel.getCarsProgressVisibility().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer visibility) {
                progress.setVisibility(visibility);
            }
        });

        viewModel.getCarsListVisibility().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer visibility) {
                carsList.setVisibility(visibility);
            }
        });

        viewModel.getErrorVisibility().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer visibility) {
                error.setVisibility(visibility);
            }
        });

        viewModel.getError().observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String text) {
                error.setText(text);
            }
        });
    }


    protected synchronized void initializeMap() {
        if (map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void displayMarkers(){
        map.clear();

        map.addMarker(new MarkerOptions()
                .draggable(true)
                .position(viewModel.getOrigin())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(ORIGIN_MARKER_TITLE));

        if(viewModel.getDestination() != null){
            map.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(viewModel.getDestination())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(DESTINATION_MARKER_TITLE));

            //TODO Calculate journey?
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //move camera to Madrid just for faster access
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(40.416907, -3.703138), 13);
        map.animateCamera(cu);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (viewModel.getOrigin() == null) {
                    viewModel.setOrigin(point);
                    Log.d("CARS", "added origin marker: "+ point.toString());
                } else {
                    Log.d("CARS", "added destination marker: "+ point.toString());
                    viewModel.setDestination(point);
                }
                displayMarkers();
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //Log.d("CARS", "dragStart "+ marker.getTitle() + " new position: " + marker.getPosition().toString());
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //Log.d("CARS", "drag "+ marker.getTitle() + " new position: " + marker.getPosition().toString());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("CARS", "dragEnd "+ marker.getTitle() + " new position: " + marker.getPosition().toString());
                if(marker.getTitle().equals(ORIGIN_MARKER_TITLE))
                    viewModel.setOrigin(marker.getPosition());
                if(marker.getTitle().equals(DESTINATION_MARKER_TITLE))
                    viewModel.setDestination(marker.getPosition());
            }
        });
    }

}
