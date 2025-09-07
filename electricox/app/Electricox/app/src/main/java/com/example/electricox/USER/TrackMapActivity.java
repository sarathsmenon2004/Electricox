package com.example.electricox.USER;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.electricox.R;
import com.example.electricox.COMMON.ApiService;
import com.example.electricox.COMMON.RetrofitClient;
import com.example.electricox.COMMON.RouteResponse;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedClient;
    private LatLng userLocation;
    private Polyline routePolyline;

    private String destLat, destLng;
    private ApiService googleApiService;

    private final String apiKey = "AIzaSyA5XF3Im3BnyQK653GSVPV1GoQXPQ1ZUQw";

    private TextView infoCard;
    private ImageButton navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_map);

        destLat = getIntent().getStringExtra("lat");
        destLng = getIntent().getStringExtra("lng");

        googleApiService = RetrofitClient.getInstance().getGoogleApi();
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        infoCard = findViewById(R.id.infoCard);
        navButton = findViewById(R.id.btnStartNav);

        navButton.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destLat + "," + destLng + "&mode=driving");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Uri webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + destLat + "," + destLng + "&travelmode=driving");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(webIntent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        enableLocationAndTrack();
    }

    private void enableLocationAndTrack() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000);

        fusedClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                for (Location location : result.getLocations()) {
                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    LatLng dest = new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLng));

                    if (routePolyline != null) routePolyline.remove();

                    fetchAndDrawRoute(userLocation, dest);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
                }
            }
        }, Looper.getMainLooper());
    }

    private void fetchAndDrawRoute(LatLng origin, LatLng destination) {
        String originStr = origin.latitude + "," + origin.longitude;
        String destStr = destination.latitude + "," + destination.longitude;

        googleApiService.getRoute(originStr, destStr, apiKey).enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getRoutes().isEmpty()) {
                    List<LatLng> path = PolyUtil.decode(response.body().getRoutes().get(0).getOverviewPolyline().getPoints());
                    routePolyline = mMap.addPolyline(new PolylineOptions().addAll(path).color(Color.BLUE).width(10f));

                    try {
                        // Parse distance + duration manually from response object
                        RouteResponse.Route route = response.body().getRoutes().get(0);
                        if (route.getLegs() != null && !route.getLegs().isEmpty()) {
                            RouteResponse.Leg leg = route.getLegs().get(0);

                            String distance = leg.getDistance().getText();
                            String duration = leg.getDuration().getText();

                            Log.d("ROUTE_DATA", "ETA: " + duration + " | Distance: " + distance);

                            infoCard.setText("ETA: " + duration + " | Distance: " + distance);
                            infoCard.setVisibility(View.VISIBLE);
                            navButton.setVisibility(View.VISIBLE);
                        } else {
                            infoCard.setText("ETA: - | Distance: -");
                            infoCard.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e("TrackMap", "Parse error: " + e.getMessage());
                        infoCard.setText("ETA: - | Distance: -");
                        infoCard.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(TrackMapActivity.this, "No route found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Toast.makeText(TrackMapActivity.this, "Route error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TrackMap", "Route fetch failed: " + t.getMessage());
            }
        });
    }

}
