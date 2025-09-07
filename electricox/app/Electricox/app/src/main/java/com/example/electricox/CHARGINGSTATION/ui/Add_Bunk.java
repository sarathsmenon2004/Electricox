package com.example.electricox.CHARGINGSTATION.ui;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.COMMON.GPSTracker;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.example.electricox.databinding.FragmentAddBunkBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class  Add_Bunk extends Fragment {


    ImageView locationMarker;
    String ADDRESS = "";

    ImageView imageView3, imgcam, imggal;
    Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;
    String bal = "null";
    GPSTracker gps;
    double latitude;
    double longitude;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    TextView LAT, LONG, mapaddress;

    private FragmentAddBunkBinding binding;
    private EditText businessHoursEditText;
    private EditText bunkNameEditText;
    private EditText chargingRateEditText;
    private EditText emergencyContactEditText;
    private Spinner onSiteSupportSpinner;


    private Button saveBunkButton , btn;


    private String businessHours;
    private String bunkName;
    private String chargingRate;
    private String emergencyContact;
    private String onSiteSupport;


    Spinner chargerTypeSpinner;

    CheckBox type1CheckBox, type2CheckBox, ccsCheckBox, chademoCheckBox, gbtCheckBox;
    EditText  address;

    private CheckBox wifiCheckBox;
    private CheckBox restroomCheckBox;
    private CheckBox waitingAreaCheckBox;
    private CheckBox nearbyAmenitiesCheckBox;

    // Declare global variables for storing CheckBox values
    private boolean isWifiAvailable;
    private boolean hasRestroomFacilities;
    private boolean hasWaitingArea;
    private boolean hasNearbyAmenities;

    String IsWifiAvailable,HasRestroomFacilities,HasWaitingArea,HasNearbyAmenities;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBunkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        businessHoursEditText = root.findViewById(R.id.business_hours);
        bunkNameEditText = root.findViewById(R.id.bunk_name);
        emergencyContactEditText = root.findViewById(R.id.emergency_contact);
        onSiteSupportSpinner = root.findViewById(R.id.on_site_support_spinner);
        wifiCheckBox = root.findViewById(R.id.checkbox_wifi);
        restroomCheckBox = root.findViewById(R.id.checkbox_restroom);
        waitingAreaCheckBox = root.findViewById(R.id.checkbox_waiting_area);
        nearbyAmenitiesCheckBox = root.findViewById(R.id.checkbox_nearby_amenities);
        saveBunkButton=root.findViewById(R.id.saveButton);
        locationMarker=root.findViewById(R.id.location_icon);
        imageView3 = root.findViewById(R.id.imageView3);
        imgcam = root.findViewById(R.id.property_camera);
        imggal = root.findViewById(R.id.property_gal);
        address=root.findViewById(R.id.input_address);
        LAT=root.findViewById(R.id.latitude);
        LONG=root.findViewById(R.id.longitude);
        chargingRateEditText=root.findViewById(R.id.charging_rate);
        type1CheckBox = root.findViewById(R.id.checkbox_type1);
        type2CheckBox = root.findViewById(R.id.checkbox_type2);
        ccsCheckBox = root.findViewById(R.id.checkbox_ccs);
        chademoCheckBox = root.findViewById(R.id.checkbox_chademo);
        gbtCheckBox = root.findViewById(R.id.checkbox_gbt);



        saveBunkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ValidateBUNKDeatails();


            }
        });


        locationMarker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                PopMap(LAT, LONG, address, "address");
                return false;
            }
        });

        imgcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "new-photo-name.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
                imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, PICK_Camera_IMAGE);
            }
        });

        imggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent gintent = new Intent();
                    gintent.setType("image/*");
                    gintent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gintent, "Select Picture"), PICK_IMAGE);

                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }
        });



        return root;
    }

    private void ValidateBUNKDeatails()
    {

        businessHours = businessHoursEditText.getText().toString();
        bunkName = bunkNameEditText.getText().toString();
        emergencyContact = emergencyContactEditText.getText().toString();
        onSiteSupport = onSiteSupportSpinner.getSelectedItem().toString();
        isWifiAvailable = wifiCheckBox.isChecked();
        hasRestroomFacilities = restroomCheckBox.isChecked();
        hasWaitingArea = waitingAreaCheckBox.isChecked();
        hasNearbyAmenities = nearbyAmenitiesCheckBox.isChecked();
        String chargingRate = chargingRateEditText.getText().toString();

        StringBuilder plugTypes = new StringBuilder();
        if (type1CheckBox.isChecked()) plugTypes.append("Type 1, ");
        if (type2CheckBox.isChecked()) plugTypes.append("Type 2, ");
        if (ccsCheckBox.isChecked()) plugTypes.append("CCS, ");
        if (chademoCheckBox.isChecked()) plugTypes.append("CHAdeMO, ");
        if (gbtCheckBox.isChecked()) plugTypes.append("GB/T, ");

        String selectedPlugTypes = plugTypes.toString().trim();
        if (selectedPlugTypes.endsWith(",")) {
            selectedPlugTypes = selectedPlugTypes.substring(0, selectedPlugTypes.length() - 1);
        }

        if(isWifiAvailable==true)
        {
            IsWifiAvailable="yes";
        }
        else
        {
            IsWifiAvailable="no";
        }
        if(hasRestroomFacilities==true)
        {
            HasRestroomFacilities="yes";
        }
        else
        {
            HasRestroomFacilities="no";
        }

        if(hasWaitingArea==true)
        {
            HasWaitingArea="yes";
        }
        else
        {
            HasWaitingArea="no";
        }
        if(hasNearbyAmenities==true)
        {
            HasNearbyAmenities="yes";
        }
        else
        {
            HasNearbyAmenities="no";
        }

        if (businessHours.isEmpty() || bunkName.isEmpty() || emergencyContact.isEmpty()
                || onSiteSupport.isEmpty() || chargingRate.isEmpty()
                || selectedPlugTypes.isEmpty()
                || bal.equals("null") || address.getText().toString().isEmpty()
                || LAT.getText().toString().isEmpty() || LONG.getText().toString().isEmpty()
                || !isLocationValid()) {

            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();

        } else {
            // ✅ Everything is valid — pass it all to addPackage()
            addPackage(
                    chargingRate, selectedPlugTypes);
        }
    }

    private void addPackage(String chargingRate, String selectedPlugTypes)
    {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Already Exist")) {

                    Toast.makeText(getContext(), " Already Exists", Toast.LENGTH_SHORT).show();

                } else if (!response.trim().equals("failed")) {

                    Toast.makeText(getContext(), " Bunk Added Success", Toast.LENGTH_SHORT).show();
                    getActivity().recreate();
                    businessHoursEditText.setText("");
                    bunkNameEditText.setText("");
                    emergencyContactEditText.setText("");
                    onSiteSupportSpinner.setSelection(0); // Set to the default selection
                    wifiCheckBox.setChecked(false);
                    restroomCheckBox.setChecked(false);
                    waitingAreaCheckBox.setChecked(false);
                    nearbyAmenitiesCheckBox.setChecked(false);
                    imageView3.setVisibility(View.GONE);
                    bal = "null";
                    address.getText().clear();
                    LAT.setText("");
                    LONG.setText("");
                    ADDRESS = "";

                } else {
                    Toast.makeText(getContext(), " Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                map.put("key", "addBunk");
                map.put("id", uid);
                map.put("businessHours", businessHours);
                map.put("bunkName", bunkName);
                map.put("emergencyContact", emergencyContact);
                map.put("onSiteSupport", onSiteSupport);
                map.put("IsWifiAvailable", IsWifiAvailable);
                map.put("HasRestroomFacilities", HasRestroomFacilities);
                map.put("image", bal);
                map.put("lat", String.valueOf(latitude));
                map.put("long", String.valueOf(longitude));
                map.put("HasWaitingArea", HasWaitingArea);
                map.put("HasNearbyAmenities", HasNearbyAmenities);
                map.put("ADDRESS", ADDRESS);
                map.put("chargingRate", chargingRate);
                map.put("selectedPlugTypes", selectedPlugTypes);

                System.out.println(chargingRate+ selectedPlugTypes +"                   aaaaaaaaaaaaaaaaaaaaaaa                                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

                return map;
            }
        };
        queue.add(request);
    }

    private boolean isLocationValid() {
        // You can add more specific validation for latitude and longitude if needed
        return !LAT.getText().toString().equals("0.0") && !LONG.getText().toString().equals("0.0");
    }

    //////////////image code//////////////////
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = imageUri;
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                String filemanagerstring = selectedImageUri.getPath();
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public void decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        imageView3.setVisibility(View.VISIBLE);
        imageView3.setImageBitmap(bitmap);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        bal = android.util.Base64.encodeToString(ba, android.util.Base64.DEFAULT);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    ////////////////////////////////location code///////////////////////////////////////////////
    void PopMap(final TextView lat, final TextView longg, final EditText address, final String ADDTYPE) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /////make map clear
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.custom_map_lattlongg);////your custom content



        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);

        mapaddress = dialog.findViewById(R.id.custom_map_address);
        btn = dialog.findViewById(R.id.custom_map_btnadd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ADDTYPE.equals("address")) {
                    if (ADDRESS.isEmpty()) {
                        Toast.makeText(getContext(), "Please Select location", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Something gone wrong", Toast.LENGTH_SHORT).show();

                }


            }
        });

        MapsInitializer.initialize(getContext());

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {


                LatLng posisiabsen = new LatLng(latitude, longitude); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Yout"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16.5f));


                //                ....
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("New Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;

                        lat.setText("" + latitude);
                        longg.setText("" + longitude);

                        //getAddress strat
                        try {

                            Geocoder geocoder;
                            List<Address> yourAddresses;
                            geocoder = new Geocoder(getContext(), Locale.getDefault());
                            yourAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            System.out.println(" CURRENT ADDRES :"+yourAddresses);
                            if (yourAddresses.size() > 0) {
                                String yourAddress = yourAddresses.get(0).getAddressLine(0);
                                String yourCity = yourAddresses.get(0).getAddressLine(1);
                                String yourCountry = yourAddresses.get(0).getAddressLine(2);
                                Log.d("***", yourAddress);
                                if (ADDTYPE.equals("address")) {
                                    ADDRESS = yourAddress;
                                }
                                ADDRESS = yourAddress;
                                address.setText(yourAddress);
                                mapaddress.setText(yourAddress);

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });
        dialog.show();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            double lat = location.getLatitude();
                            double longi = location.getLongitude();
                            latitude = lat;
                            longitude = longi;
                            Toast.makeText(getContext(), "Location Updated", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double lat = mLastLocation.getLatitude();
            double longi = mLastLocation.getLongitude();
            latitude = lat;
            longitude = longi;
            Toast.makeText(getContext(), "Location Updated", Toast.LENGTH_SHORT).show();
            //latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


}