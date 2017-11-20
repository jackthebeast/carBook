package jacopo.com.carbook;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, OnMapReadyCallback {

    private static final String ORIGIN_MARKER_TITLE = "Origin";
    private static final String DESTINATION_MARKER_TITLE = "Destination";
    private GoogleMap map;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        initializeMap();
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
