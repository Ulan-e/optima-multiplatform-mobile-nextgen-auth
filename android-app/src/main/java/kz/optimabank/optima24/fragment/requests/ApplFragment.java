package kz.optimabank.optima24.fragment.requests;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.HistoryApplicationsTypesAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;
import kz.optimabank.optima24.model.interfaces.HistoryApplicationsInterface;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.service.HistoryApllicationsImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Constants.SELECT_DATE;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

/**
 * Created by Max on 10.05.2018.
 */

public class ApplFragment extends ATFFragment implements HistoryApllicationsImpl.CallbackTypes{
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.tvNotData) TextView tvNotData;

    HistoryApplicationsTypesAdapter adapter;
    HistoryApplicationsInterface historyApplications;
    ArrayList<ApplicationTypeDto> applicationTypeDtoses = new ArrayList<>();
    ArrayList<ApplicationTypeDto> applicationTypeDtosesItog = new ArrayList<>();
    LinkedList<String> stringMass = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i("ApplFragment", "onStart");
        if (historyApplications == null)
            historyApplications = new HistoryApllicationsImpl();
        historyApplications.registerCallBackTypes(this);
        historyApplications.getTypesApplications(getActivity());
        super.onStart();
    }

    @Override
    public void jsonTypesApplicationsResponse(int statusCode, String errorMessage, ArrayList<ApplicationTypeDto> response) {
        if (statusCode == 200) {
            applicationTypeDtoses = response;
            setAdapter();
        }
    }

    @Override
    public void jsongetApplicationByIdResponse(int statusCode, String errorMessage, ApplicationTypeDto response) {
        if (statusCode == 200){
            ApplicationParamsFragment applicationParamsFragment = ApplicationParamsFragment.newInstance(response,R.layout.fragment_application_params);
            ((NavigationActivity) parentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, applicationParamsFragment).addToBackStack(null).commit();
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void setAdapter() {
        applicationTypeDtosesItog.clear();
        stringMass.clear();
        for (ApplicationTypeDto applicationTypeDto : applicationTypeDtoses){
            if (applicationTypeDto.CategoryName!=null)
                if (!stringMass.contains(applicationTypeDto.CategoryName)) {
                    stringMass.add(applicationTypeDto.CategoryName);
                }
        }

     /*   applicationTypeDtosesItog.add(new ApplicationTypeDto(Constants.HEADER_ID, applicationTypeDto.CategoryName, "-1"));
        applicationTypeDtosesItog.add(applicationTypeDto);
    } else {
        applicationTypeDtosesItog.add(applicationTypeDto);
    }*/

        for (String string : stringMass){
            applicationTypeDtosesItog.add(new ApplicationTypeDto(Constants.HEADER_ID, string, "-1"));
            for (ApplicationTypeDto applicationTypeDto : applicationTypeDtoses){
                if (applicationTypeDto.CategoryName != null && applicationTypeDto.CategoryName.contains(string))
                    applicationTypeDtosesItog.add(applicationTypeDto);
            }
        }

        adapter = new HistoryApplicationsTypesAdapter(getActivity(), applicationTypeDtosesItog, setOnClick());
        recyclerView.setAdapter(adapter);
    }


    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        try {
                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                            if(applicationTypeDtosesItog.get(position).id != Constants.HEADER_ID) {
                                if (historyApplications !=null){
                                    historyApplications.getApplicationById(getActivity(), applicationTypeDtosesItog.get(position).id);
                                }
                            } else if(applicationTypeDtosesItog.get(position).id == Constants.HEADER_ID && position==0) {
                                intent.putExtra("selectDate",true);
                                intent.putExtra(DATE_TAG,"PaymentHistory");
                                startActivityForResult(intent,SELECT_DATE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view,animatorListener);
            }
        };
    }
}
