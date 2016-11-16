package edu.training.droidbountyhunter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Eden on 18/08/2016.
 */
public class MapActivity extends FragmentActivity{
    private LatLng posicion;
    private GoogleMap map;
    Bundle oExt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapa);

        oExt = this.getIntent().getExtras();
        this.setTitle("Mapa" + oExt.getString("nom"));
        Double lat = oExt.getDouble("lat");
        Double lon = oExt.getDouble("lon");

        if(lat == 0.0 && lon == 0.0){
            lat = -33.4716;
            lon = -70.6428;
        }

        posicion = new LatLng(lat, lon);

        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapa)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.addMarker(new MarkerOptions()
                                        .position(posicion).title(oExt.getString("nom", "Fugitivo"))
                                        .snippet("Atrapenlo!!").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 9));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        });
    }
}
