package kz.optimabank.optima24.fragment.service_point;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.ServicePointDetails;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.ATFGoogleMap;

public class ServicePointsMap extends ATFFragment {
    @BindView(R.id.mapView) MapView mMapView;

    @BindView(R.id.linDetails) LinearLayout linDetails;
    @BindView(R.id.linStatus) LinearLayout linStatus;
    @BindView(R.id.linMapButt) LinearLayout linMapButt;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.tvWorkingTime) TextView tvWorkingTime;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.imagTerm) ImageView imagTerm;
    @BindView(R.id.tvType) TextView tvType;
    @BindView(R.id.tvCash) TextView tvCash;
    @BindView(R.id.tvTextMap) TextView tvTextMap;
    @BindView(R.id.close) ImageView close;

    boolean isDetails;
    ArrayList<Terminal> branches = new ArrayList<>();
    ArrayList<Terminal> terminals = new ArrayList<>();
    ArrayList<Terminal> atms = new ArrayList<>();
    Terminal terminal;
    ATFGoogleMap gMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        getBundle();

        ServicePointList.M500 = true;
        ServicePointList.M1000 = true;
        ServicePointList.Mm1000 = true;

        linDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*linDetails.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        tvCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linDetails.getMaxLines() == 1) {
                    ObjectAnimator animation = ObjectAnimator.ofInt(
                            linDetails,
                            "LayoutParams",
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                    animation.setDuration(300);
                    animation.start();
                    tvCash.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
                } else {
                    ObjectAnimator animation = ObjectAnimator.ofInt(
                            linDetails,
                            "layout_height",
                            1);
                    animation.setDuration(300);
                    animation.start();
                    tvCash.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }
            }
        });*/
        final Animation down = AnimationUtils.loadAnimation(getActivity(), R.anim.map_down);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linDetails.setVisibility(View.GONE);
                linDetails.startAnimation(down);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isDetails){
            linDetails.setVisibility(View.VISIBLE);
            tvTitle.setText(terminal.getNote());
            tvAddress.setText(terminal.getAddress());
            tvWorkingTime.setText(terminal.getWorkTime());
            tvType.setText(terminal.getName());

            if (terminal.getPointType()!=0) {
                tvCash.setVisibility(View.VISIBLE);
                if (terminal.getIsCashIn()) {
                    tvCash.setText(R.string.cash_out_input);
                } else {
                    tvCash.setText(R.string.cash_out);
                }
            }else {
                tvCash.setVisibility(View.GONE);
            }

            if (terminal.getPointType() == 0) {
                linStatus.setVisibility(View.GONE);
            } else {
                linStatus.setVisibility(View.VISIBLE);
                switch (terminal.getStatus()) {
                    case 0:
                        tvStatus.setText(getString(R.string.t_online));
                        break;
                    case 1:
                        tvStatus.setText(getString(R.string.t_offline));
                        break;
                    case 2:
                        tvStatus.setText(getString(R.string.t_warning));
                        break;
                }
            }
            linMapButt.setVisibility(View.GONE);
        }else{
            linDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!isDetails && !GeneralManager.getInstance().getAtms().isEmpty()) {
            terminals = GeneralManager.getInstance().getTerminals();
            branches = GeneralManager.getInstance().getBranches();
            atms = GeneralManager.getInstance().getAtms();
        }
        getMap();
    }


    public void notifyDataChanged(){
        gMap.setVisibilityOfMarkers();
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
//            if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                if(!OptimaBank.getInstance().isGPSEnabled()) {
//                    buildAlertMessageNoGps();
//                } else {
//                    getMap();
//                }
//            }
//        }
//    }

    private void getBundle() {
        if(getArguments() != null) {
            isDetails = getArguments().getBoolean("isDetails");
            terminal = (Terminal) getArguments().getSerializable("terminal");
        }
    }

    private void getMap() {
        if(!isDetails) {
            GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Terminal terminal = GeneralManager.getInstance().getTerminalHashMap().get(marker.getTitle());
                        clickPointAction(terminal);
                        return true;
                    }
                };
            gMap = new ATFGoogleMap(getActivity(), atms, branches, terminals, markerClickListener);
            mMapView.getMapAsync(gMap);
        } else {
            GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    clickPointAction(terminal);
                    return true;
                }
            };
            mMapView.getMapAsync(new ATFGoogleMap(getActivity(), terminal, markerClickListener));
//            clickPointAction(terminal);
        }
    }

    private void clickPointAction(final Terminal terminal) {
        if (!isDetails) {
            linDetails.setVisibility(View.VISIBLE);
            if (terminal != null) {
                Log.i("-------------","-----------");
                Log.i("PointType","PointType = "+terminal.getPointType());
                Log.i("Id","Id = "+terminal.getId());
                Log.i("Address","Address = "+terminal.getAddress());
                Log.i("BranchCode","BranchCode = "+terminal.getBranchCode());
                Log.i("City","City = "+terminal.getCity());
                Log.i("Note","Note = "+terminal.getNote());
                Log.i("Name","Name = "+terminal.getName());
                Log.i("Number","Number = "+terminal.getNumber());
                Log.i("WorkTime","WorkTime = "+terminal.getWorkTime());
                Log.i("Status","Status = "+terminal.getStatus());
                Log.i("Is24HAvailable","Is24HAvailable = "+terminal.getIs24HAvailable());
                Log.i("IsCashIn","IsCashIn = "+terminal.getIsCashIn());
                Log.i("IsInBank","IsInBank = "+terminal.getIsInBank());
                Log.i("-------------","-----------");
                tvTitle.setText(terminal.getNote());
                tvAddress.setText(terminal.getAddress());
                tvWorkingTime.setText(terminal.getWorkTime());
                tvType.setText(terminal.getName());

                if (terminal.getPointType()!=0) {
                    tvCash.setVisibility(View.VISIBLE);
                    if (terminal.getIsCashIn()) {
                        tvCash.setText(R.string.cash_out_input);
                    } else {
                        tvCash.setText(R.string.cash_out);
                    }
                }else {
                    tvCash.setVisibility(View.GONE);
                }
                if(terminal.getPointType() == 0) {
                    switch (terminal.getStatus()){
                        case 0 :
                            imagTerm.setImageResource(R.drawable.ic_branch_green);
                            break;
                        case 1 :
                            imagTerm.setImageResource(R.drawable.ic_branch_red);
                            break;
                        case 2 :
                            imagTerm.setImageResource(R.drawable.ic_branch_yellow);
                            break;
                    }
                }else if(terminal.getPointType() == 2)
                    switch (terminal.getStatus()){
                        case 0 :
                            imagTerm.setImageResource(R.drawable.ic_atm_green);
                            break;
                        case 1 :
                            imagTerm.setImageResource(R.drawable.ic_atm_red);
                            break;
                        case 2 :
                            imagTerm.setImageResource(R.drawable.ic_atm_yellow);
                            break;
                    }
                else if(terminal.getPointType() == 3)
                    switch (terminal.getStatus()){
                        case 0 :
                            imagTerm.setImageResource(R.drawable.ic_cashin_green);
                            break;
                        case 1 :
                            imagTerm.setImageResource(R.drawable.ic_cashin_red);
                            break;
                        case 2 :
                            imagTerm.setImageResource(R.drawable.ic_cashin_yellow);
                            break;
                    }

                imagTerm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Terminal terminal1;
                        terminal1 = terminal;
                        Intent intent = new Intent(getActivity(), ServicePointDetails.class);
                        intent.putExtra("isDetails", true);
                        intent.putExtra("terminal", terminal1);
                        getActivity().startActivity(intent);
                    }
                });
                tvTextMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Terminal terminal1;
                        terminal1 = terminal;
                        Intent intent = new Intent(getActivity(), ServicePointDetails.class);
                        intent.putExtra("isDetails", true);
                        intent.putExtra("terminal", terminal1);
                        getActivity().startActivity(intent);
                    }
                });
                if (terminal.getPointType() == 0) {
                    linStatus.setVisibility(View.GONE);
                } else {
                    linStatus.setVisibility(View.VISIBLE);
                    switch (terminal.getStatus()) {
                        case 0:
                            tvStatus.setText(getString(R.string.t_online));
                            break;
                        case 1:
                            tvStatus.setText(getString(R.string.t_offline));
                            break;
                        case 2:
                            tvStatus.setText(getString(R.string.t_warning));
                            break;
                    }
                }
            }
        }
    }
}