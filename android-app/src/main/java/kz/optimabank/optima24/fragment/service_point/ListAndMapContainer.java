package kz.optimabank.optima24.fragment.service_point;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.UnauthorizedTabActivity;
import kz.optimabank.optima24.app.NonSwipeableViewPager;
import kz.optimabank.optima24.controller.adapter.ATMTabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.ServicePointsImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class ListAndMapContainer extends ATFFragment implements ServicePointsImpl.Callback, LocationListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nonSwipeableViewPager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.mapLoader)
    ProgressBar mapLoader;

    ATMTabAdapter atmTabAdapter;
    boolean isMainPage = true, isVisibleToUser;
    ServicePointsImpl servicePoints;
    Location location;
    public static float zoom = 12f;

    ServicePointsMap pointsMap = new ServicePointsMap();
    ServicePointList pointsList = new ServicePointList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_and_map_container, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();
        if (getUserVisibleHint()){
            checkPermissionMap();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isMainPage) {
            if (isVisibleToUser) {
                if (!GeneralManager.getInstance().getAtms().isEmpty()) {
                    setViewPagerAdapter(0);
                } else {
                    requestPoints();
                }
            }
        } else {
            if (!GeneralManager.getInstance().getAtms().isEmpty()) {
                setViewPagerAdapter(0);
            } else {
                requestPoints();
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==REQUEST_ID_MULTIPLE_PERMISSIONS_MAP) {
//            Map<String, Integer> perms = new HashMap<>();
//            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
//            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
//
//            // Fill with actual results from user
//            if (grantResults.length > 0) {
//                for (int i = 0; i < permissions.length; i++)
//                    perms.put(permissions[i], grantResults[i]);
//            }
//        }
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (isAdded()) {
                checkPermissionMap();
                if (!GeneralManager.getInstance().getAtms().isEmpty()) {
                    setViewPagerAdapter(0);
                } else {
                    requestPoints();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.points_settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        getUserLocation();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View v = View.inflate(getActivity(), R.layout.service_point_settings_alert, null);
            final CheckBox cbTerminals = v.findViewById(R.id.cbTerminals);
            final CheckBox cbAtm = v.findViewById(R.id.cbAtm);
            final CheckBox cbBranches = v.findViewById(R.id.cbBranches);
            cbTerminals.setChecked(GeneralManager.isTerminalChecked());
            cbBranches.setChecked(GeneralManager.isBranchesChecked());
            cbAtm.setChecked(GeneralManager.isAtmChecked());
            builder.setView(v);
            builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GeneralManager.setTerminalChecked(cbTerminals.isChecked());
                    GeneralManager.setBranchesChecked(cbBranches.isChecked());
                    GeneralManager.setAtmChecked(cbAtm.isChecked());
                    getActivity().setResult(CommonStatusCodes.SUCCESS);

                    if (!GeneralManager.getInstance().getAtms().isEmpty()) {
                            notifyDatatSetChanged();
                    } else {
                        requestPoints();
                    }
                }
            });
            builder.setNegativeButton(getString(R.string._no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
            //builder.setView(v);

        }
        return super.onOptionsItemSelected(item);
    }

    private void notifyDatatSetChanged(){
        pointsMap.notifyDataChanged();
        pointsList.notifyDataChanged();
    }

    @Override
    public void jsonAllServiceResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            setViewPagerAdapter(viewPager.getCurrentItem());
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void requestPoints() {
        servicePoints = new ServicePointsImpl();
        servicePoints.registerCallBack(this);
        servicePoints.getAllServicePoints(getActivity());
    }

    private void getUserLocation() {
        if (getActivity() != null) {
            if (GeneralManager.getInstance().getUserLocation() == null) {
                if (ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(parentActivity);
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(parentActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("MAPGMS", "LOCATION = " + location);
                            if (location != null) {
                                GeneralManager.getInstance().setUserLocation(location);
                            }
                        }
                    });
                }
            }
        }
    }

    private void initToolbar() {
        if(!isMainPage) {
            viewPager.setPadding(0,0,0,0);
            ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((NavigationActivity)getActivity()).getSupportActionBar();
            if(actionBar!=null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            toolbar.setTitle(null);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_left));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            ActionBar actionBar;
            try {
                actionBar = ((UnauthorizedTabActivity) getActivity()).getSupportActionBar();
            }catch (Exception e){
                actionBar = ((MenuActivity) getActivity()).getSupportActionBar();
            }
            if(actionBar!=null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            try {
                ((UnauthorizedTabActivity) getActivity()).setSupportActionBar(toolbar);
            }catch (Exception e){
                ((MenuActivity) getActivity()).setSupportActionBar(toolbar);
            }
            toolbar.setTitle(null);
        }
    }

    private void getBundle() {
        if(getArguments()!=null) {
            isMainPage = getArguments().getBoolean("isMainPage",true);
        }
    }

    private ArrayList<ATFFragment> getFragments() {
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        fragments.add(pointsMap);
        fragments.add(pointsList);
        return fragments;
    }

    private ArrayList<String> getFragmentsTitle() {
        ArrayList<String> fragmentsTitle = new ArrayList<>();
        fragmentsTitle.add(getString(R.string.t_on_map));
        fragmentsTitle.add(getString(R.string.t_in_list));
        return fragmentsTitle;
    }

    private void setViewPagerAdapter(int pageNumber) {
        if(isAdded()) {
            ServicePointList.M500 = true;
            ServicePointList.Mm1000 = true;
            ServicePointList.M1000 = true;
            atmTabAdapter = new ATMTabAdapter(getChildFragmentManager(), getFragments(), getFragmentsTitle());
            viewPager.setAdapter(atmTabAdapter);
            viewPager.setCurrentItem(pageNumber);
            tabLayout.setupWithViewPager(viewPager);
            mapLoader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("MAP", "location LC = " + location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("MAP", "provider = " + provider+" status = " + status+" extras = " + extras);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("MAP", "provider PE = " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("MAP", "provider PD = " + provider);
    }
}
