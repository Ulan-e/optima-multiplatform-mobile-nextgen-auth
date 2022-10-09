package kz.optimabank.optima24.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;

import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.manager.GeneralManager;
/**
  Created by Timur on 27.03.2017.
 */

public class ATFGoogleMap extends MapView implements OnMapReadyCallback {
    Location location;
    CameraPosition cameraPosition;
    HashMap<String, Terminal> hashMap;
    ArrayList<Terminal> branches;
    ArrayList<Terminal> terminals;
    ArrayList<Terminal> atms;
    ArrayList<Marker> markerArrayList = new ArrayList<>();
    Context context;
    Terminal terminal;
    GoogleMap.OnMarkerClickListener markerClickListener;

    public ATFGoogleMap(Context context) {
        super(context);
        this.context = context;
    }

    public ATFGoogleMap(Context context, ArrayList<Terminal> atms, ArrayList<Terminal> branches, ArrayList<Terminal> terminals,
                        GoogleMap.OnMarkerClickListener markerClickListener) {
        super(context);
        this.atms = atms;
        this.branches = branches;
        this.terminals = terminals;
        this.context = context;
        this.markerClickListener = markerClickListener;
    }

    public ATFGoogleMap(Context context, Terminal terminal, GoogleMap.OnMarkerClickListener markerClickListener) {
        super(context);
        this.terminal = terminal;
        this.context = context;
        this.markerClickListener = markerClickListener;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        Log.d("TAG","getLocation() = " + getLocation());
        if(getLocation()!=null) {

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(42.8426948,
                            74.507752)).zoom(10).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        getMarker(googleMap);
        googleMap.setOnMarkerClickListener(markerClickListener);
    }

    public Location getLocation() {
        if (GeneralManager.getInstance().getUserLocation() == null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        Log.i("MAPGMS", "LOCATION = " + loc);
                        if (loc != null) {
                            location = loc;
                        }
                    }
                });
            }
        } else {
            location = GeneralManager.getInstance().getUserLocation();
        }
        return location;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getMarker(GoogleMap googleMap) {
        hashMap = new HashMap<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        googleMap.setOnMapLoadedCallback(() -> {
            Log.d("TAG","onMapLoaded() = ");
            if(terminal == null){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 13));
            }
        });

        if(atms!=null&&!atms.isEmpty()) {
            for (int i = 0; i < atms.size(); i++) {
                Terminal item = atms.get(i);
                Log.i("atms","isCash ="+item.getIsCashIn());
                String latitude = item.getLatitude();
                String longitude = item.getLongitude();
                LatLng coordinates = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                builder.include(coordinates);
                MarkerOptions options;
                switch (item.getStatus()) {
                    case 0:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_green))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_online));
                        break;
                    case 1:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_red))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_offline));
                        break;
                    case 2:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_yellow))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_warning));
                        break;
                    default:
                        Log.i("isCash","isCash ="+terminal.getIsCashIn());
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_green))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime());
                        break;
                }
                Marker m = googleMap.addMarker(options);
                m.setTag("atm");
                markerArrayList.add(m);
                hashMap.put(options.getTitle(), item);
            }
            //cameraPosition = new CameraPosition.Builder()
            //        .target(new LatLng(Double.parseDouble(atms.get(0).getLatitude()),
            //                Double.parseDouble(atms.get(0).getLongitude()))).zoom(13).build();
            //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        if(branches!=null && !branches.isEmpty()) {
            for (int i = 0; i < branches.size(); i++) {
                Terminal item = branches.get(i);
                Log.i("branches","isCash ="+item.getIsCashIn());
                String lat = item.getLatitude();
                String lng = item.getLongitude();
                LatLng coordinates = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                builder.include(coordinates);
                MarkerOptions options;
                switch (item.getStatus()) {
                    case 0:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_green))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_online));
                        break;
                    case 1:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_red))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_offline));
                        break;
                    case 2:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_yellow))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_warning));
                        break;
                    default:
                        Log.i("isCash","isCash ="+terminal.getIsCashIn());
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_green))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime());
                        break;
                }

                Marker m = googleMap.addMarker(options);
                m.setTag("branch");
                markerArrayList.add(m);
                hashMap.put(options.getTitle(), item);
            }
        }

        if(terminals!=null && !terminals.isEmpty()) {
            for (int i = 0; i < terminals.size(); i++) {
                Terminal item = terminals.get(i);
                Log.i("terminals","isCash ="+item.getIsCashIn());
                String lat = item.getLatitude();
                String lng = item.getLongitude();
                LatLng coordinates = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                builder.include(coordinates);
                MarkerOptions options;
                switch (item.getStatus()) {
                    case 0:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_green))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_online));
                        break;
                    case 1:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_red))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_offline));
                        break;
                    case 2:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_yellow))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime() + "\n" +
                                        context.getString(R.string.status_warning));
                        break;
                    default:
                        Log.i("isCash","isCash ="+terminal.getIsCashIn());
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_green))
                                .title(item.getAddress() + "\n" + context.getString(R.string.working_time) + " " + item.getWorkTime());
                        break;
                }

                Marker m = googleMap.addMarker(options);
                m.setTag("terminal");
                markerArrayList.add(m);
                hashMap.put(options.getTitle(), item);
            }
        }

        if(terminal!=null) {
            String lat = terminal.getLatitude();
            String lng = terminal.getLongitude();
            LatLng coordinates = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            builder.include(coordinates);
            MarkerOptions options;
            if(terminal.getPointType() == 0) {
                switch (terminal.getStatus()) {
                    case 0:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_green))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_online));
                        break;
                    case 1:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_red))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_offline));
                        break;
                    case 2:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_yellow))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_warning));
                        break;
                    default:
                        Log.i("isCash","isCash ="+terminal.getIsCashIn());
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_branch_green))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime());
                        break;
                }
            } else if(terminal.getPointType() == 2){
                switch (terminal.getStatus()) {
                    case 0:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_green))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_online));
                        break;
                    case 1:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_red))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_offline));
                        break;
                    case 2:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_yellow))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_warning));
                        break;
                    default:
                        Log.i("isCash","isCash ="+terminal.getIsCashIn());
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_atm_green))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime());
                        break;
                }
            } else if(terminal.getPointType() == 3) {
                switch (terminal.getStatus()) {
                    case 0:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_green))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_online));
                        break;
                    case 1:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_red))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_offline));
                        break;
                    case 2:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_yellow))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime() + "\n" +
                                        context.getString(R.string.status_warning));
                        break;
                    default:
                        options = new MarkerOptions().position(coordinates).icon(bitmapDescriptorFromVector(context, R.drawable.ic_cashin_green))
                                .title(terminal.getAddress() + "\n" + context.getString(R.string.working_time) + " " + terminal.getWorkTime());
                        break;
                }
            } else
                options = null;
            googleMap.addMarker(options);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));
        } else {
            GeneralManager.getInstance().setTerminalHashMap(hashMap);
        }
        setVisibilityOfMarkers();
    }

    public void setVisibilityOfMarkers(){
        boolean isTerminalChecked = GeneralManager.isTerminalChecked();
        boolean isBranchesChecked =  GeneralManager.isBranchesChecked();
        boolean isAtmChecked = GeneralManager.isAtmChecked();

        for(Marker m: markerArrayList){
            if(m.getTag() != null && m.getTag().toString().equals("terminal"))
                m.setVisible(isTerminalChecked);
        }
        for(Marker m: markerArrayList){
            if(m.getTag() != null && m.getTag().toString().equals("branch"))
                m.setVisible(isBranchesChecked);
        }
        for(Marker m: markerArrayList){
            if(m.getTag() != null && m.getTag().toString().equals("atm"))
                m.setVisible(isAtmChecked);
        }
    }
}
