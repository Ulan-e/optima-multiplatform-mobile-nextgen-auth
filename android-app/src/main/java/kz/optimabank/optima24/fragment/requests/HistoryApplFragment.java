package kz.optimabank.optima24.fragment.requests;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.HistoryApplicationsAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;
import kz.optimabank.optima24.model.base.HistoryApplications;
import kz.optimabank.optima24.model.base.HistoryDetailsApplications;
import kz.optimabank.optima24.model.interfaces.HistoryApplicationsInterface;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.service.HistoryApllicationsImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.DATE_FORMAT_FOR_REQEST;
import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Constants.DAY_MONTH_YEAR_FORMAT;
import static kz.optimabank.optima24.utility.Constants.SELECT_DATE;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.formatDate;

/**
 * Created by Max on 10.05.2018.
 */

public class HistoryApplFragment extends ATFFragment implements HistoryApllicationsImpl.Callback, TextWatcher{
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.tvNotData) TextView tvNotData;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.clearSearch) ImageView clearSearch;

    HistoryApplicationsAdapter adapter;

    ArrayList<HistoryApplications> historyApplicationses = new ArrayList<>();
    ArrayList<HistoryApplications> historyApplicationsesItog = new ArrayList<>();
    ArrayList<HistoryApplications> items = new ArrayList<>();
    HistoryApplicationsInterface historyApplications;

    String fromDate,toDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_with_search, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if (historyApplications == null)
            historyApplications = new HistoryApllicationsImpl();
        historyApplications.registerCallBack(this);
//        historyApplications.getHistoryApplications(getActivity());

        fromDate = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 30);
        toDate = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss").replaceAll("T00:00:00","T23:59:59");
        historyApplications.getHistoryApplicationsByDate(getActivity(), fromDate, toDate);

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
        edSearch.addTextChangedListener(this);
        edSearch.setCursorVisible(true);
        edSearch.clearFocus();
        super.onActivityCreated(savedInstanceState);
    }

    void filter(String text) {
        items.clear();
        for(HistoryApplications history : historyApplicationses) {
            if((history.DisplayName + history.StatusDisplayName + history.id).toLowerCase().contains(text.toLowerCase())) {
                items.add(history);
            }
        }
        if (adapter != null) {
            adapter.updateList(configureHistoryList(items));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS && data != null){
            if (requestCode == SELECT_DATE){
                Log.d(TAG,"fromDate = " + data.getStringExtra("fromDate"));
                Log.d(TAG,"toDate = " + data.getStringExtra("toDate"));
                fromDate = data.getStringExtra("fromDate");
                toDate = data.getStringExtra("toDate");
                Log.i("OnDateSetToDate", "toDate = "+ toDate);
                Log.i("OnDateSetToDate", "toDateSS = "+ toDate.substring(0,11));
                Log.i("OnDateSetToDate", "toDate++ = "+ toDate.substring(0,11)+"23:59:59");
                toDate = toDate.substring(0,11)+"23:59:59";
                historyApplications.getHistoryApplicationsByDate(getActivity(), fromDate, toDate);
            }
        }
    }

    @Override
    public void jsonHistoryAppResponse(int statusCode, String errorMessage, ArrayList<HistoryApplications> response) {
        if (statusCode == 200){
            Log.i("HistorAppl"," response = "+response);
            historyApplicationses = response;
            setAdapter(response);
        }
    }

    @Override
    public void jsonApplicationDetailsById(int statusCode, String errorMessage, ApplicationTypeDto response) {

    }

    @Override
    public void jsonHistoryDetailsById(int statusCode, String errorMessage, HistoryDetailsApplications response) {
        if (statusCode == 200){
            ApplicationParamsFragment applicationParamsFragment = ApplicationParamsFragment.newInstance(response, R.layout.fragment_hist_application_params);
            ((NavigationActivity) parentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, applicationParamsFragment).addToBackStack(null).commit();
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void setAdapter(ArrayList<HistoryApplications> list) {
        adapter = new HistoryApplicationsAdapter(getActivity(), configureHistoryList(list),setOnClick());
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    public ArrayList<HistoryApplications> configureHistoryList(ArrayList<HistoryApplications> historyItems) {
        historyApplicationsesItog.clear();
        Log.i("HistorAppl","clear");
        Log.i("HistorAppl","historyItems.isEmpty() = " + historyItems.isEmpty());

        if(historyItems!=null && !historyItems.isEmpty()) {
            Collections.sort(historyItems, new Comparator<HistoryApplications>() {
                @Override
                public int compare(HistoryApplications o1, HistoryApplications o2) {
                    return o2.Date.compareTo(o1.Date);
                }
            });

            String operationDate = "operation date";
            for (HistoryApplications historyApplications : historyItems) {
                if (operationDate != null && !operationDate.equals(historyApplications.getOperationDate(Constants.VIEW_DATE_FORMAT))) {
                    try {
                        historyApplicationsesItog.add(new HistoryApplications(Constants.HEADER_ID, DAY_MONTH_YEAR_FORMAT.format(DATE_FORMAT_FOR_REQEST.parse(historyApplications.Date))));
                    } catch (ParseException e) {
                        Log.e(TAG, "Exception when parse date", e);
                    }
                    historyApplicationsesItog.add(historyApplications);
                } else {
                    historyApplicationsesItog.add(historyApplications);
                }
                operationDate = historyApplications.getOperationDate(Constants.VIEW_DATE_FORMAT);
            }
        } else if(fromDate!=null && !(fromDate.isEmpty()) && toDate!=null && !(toDate.isEmpty()) ) {
            historyApplicationsesItog.add(new HistoryApplications(Constants.HEADER_ID, getString(R.string.not_data_fmt,
                    formatDate(false, fromDate), formatDate(false, toDate) )));
        }
        if (historyApplicationsesItog.isEmpty()){
            /*historyItems.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data_fmt,
                    formatDate(false, fromDate), formatDate(false, toDate))));*/
            tvNotData.setVisibility(View.VISIBLE);
        }
        return historyApplicationsesItog;
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
                            /*HistoryApplications historyApplication = null;
                            if(items!=null && !items.isEmpty()) {
                                historyApplication = items.get(position);
                            } else if(historyApplicationsesItog!=null && !historyApplicationsesItog.isEmpty()) {
                                historyApplication = historyApplicationsesItog.get(position);
                            }
                            if (historyApplication != null) {*/
                                if (historyApplicationsesItog.get(position).id != Constants.HEADER_ID) {
                                    //TODO waiting for the opening of the story
                                    historyApplications.getHistoryDetailsById(getActivity(), historyApplicationsesItog.get(position).id);
                                } else if (historyApplicationsesItog.get(position).id == Constants.HEADER_ID && position == 0) {
                                    intent.putExtra("selectDate", true);
                                    intent.putExtra(DATE_TAG, "PaymentHistory");
                                    startActivityForResult(intent, SELECT_DATE);
                                }
                            //}
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());
    }
}
