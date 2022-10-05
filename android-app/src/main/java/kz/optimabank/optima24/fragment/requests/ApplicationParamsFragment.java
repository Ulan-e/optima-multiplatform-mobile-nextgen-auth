package kz.optimabank.optima24.fragment.requests;


import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.ApplCreateAdapter;
import kz.optimabank.optima24.controller.adapter.ApplHistoryDetailsAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;
import kz.optimabank.optima24.model.base.CancelApplicationModel;
import kz.optimabank.optima24.model.base.HistoryDetailsApplications;
import kz.optimabank.optima24.model.interfaces.CancelApplicationsInterface;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.service.CancelApllicationsImpl;
import kz.optimabank.optima24.model.service.HistoryApllicationsImpl;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.fragment.CustomListFragment.SELECTED_ITEM_EXTRA;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

public class ApplicationParamsFragment extends ATFFragment implements HistoryApllicationsImpl.CallbackCreateApplication, CancelApllicationsImpl.Callback {
    public static final int SELECT_ITEM_REQUEST_CODE = 10;

    private static final String APPLICATION_EXTRA = "application_extra";
    private static final String HISTORY_EXTRA = "history_extra";
    private static final int COMMENT = 123;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.succesLin) LinearLayout succesLin;
    @BindView(R.id.textInfo) TextView textInfo;
    @BindView(R.id.forClick) View forClick;

    private RecyclerView.Adapter adapter;
    private static int resource;
    private CancelApplicationsInterface cancelApplications;
    private HistoryDetailsApplications historyDetailsApplications;
    private CancelApplicationModel cancelApplicationModel;
    private String comment;

    public static ApplicationParamsFragment newInstance(ApplicationTypeDto applicationTypeDto, int resource) {
        ApplicationParamsFragment applicationParamsFragment = new ApplicationParamsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(APPLICATION_EXTRA, applicationTypeDto);
        bundle.putInt("resource",resource);
        applicationParamsFragment.setArguments(bundle);
        return applicationParamsFragment;
    }

    public static ApplicationParamsFragment newInstance(HistoryDetailsApplications historyDetailsApplications, int resource) {
        ApplicationParamsFragment applicationParamsFragment = new ApplicationParamsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HISTORY_EXTRA, historyDetailsApplications);
        bundle.putInt("resource",resource);
        applicationParamsFragment.setArguments(bundle);
        return applicationParamsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ApplicationTypeDto applicationTypeDto = (ApplicationTypeDto) getArguments().getSerializable(APPLICATION_EXTRA);
            historyDetailsApplications = (HistoryDetailsApplications) getArguments().getSerializable(HISTORY_EXTRA);
            resource = getArguments().getInt("resource");

            HistoryApllicationsImpl historyApllicationsImpl = new HistoryApllicationsImpl();
            historyApllicationsImpl.registerCallbackCreateApplication(this);
            if (applicationTypeDto != null)
                adapter = new ApplCreateAdapter(this, applicationTypeDto, historyApllicationsImpl);
            else
                adapter = new ApplHistoryDetailsAdapter(this, historyDetailsApplications, getActivity(), setOnCLick());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(resource, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        if (adapter instanceof ApplHistoryDetailsAdapter){
            forClick.setVisibility(View.VISIBLE);
            forClick.setEnabled(false);
            forClick.setClickable(false);
            forClick.setOnTouchListener(null);
        } else {
            forClick.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        if (!(adapter instanceof ApplHistoryDetailsAdapter))
            mRecyclerView.addItemDecoration(new DividerItemDecoration(parentActivity, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(adapter.getItemCount());
        if (cancelApplications == null){
            cancelApplications = new CancelApllicationsImpl();
        }
        cancelApplications.registerCancelCallBack(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS && data!=null) {
            if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                ((ApplCreateAdapter) adapter).handleActivityResult(data.getSerializableExtra(ACCOUNT_KEY));
            } else if (requestCode == SELECT_ITEM_REQUEST_CODE) {
                ((ApplCreateAdapter) adapter).handleActivityResult(data.getSerializableExtra(SELECTED_ITEM_EXTRA));
            } else if (requestCode == COMMENT) {
                cancelApplicationModel.id = String.valueOf(historyDetailsApplications.id);
                cancelApplicationModel.comment = data.getStringExtra("COMMENT");
                cancelApplications.cancelApplicationSecond(getActivity(), getFieldNamesAndValues(cancelApplicationModel));
            }
        }
    }

    private OnItemClickListener setOnCLick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (cancelApplications != null){
                            cancelApplications.cancelApplicationFirst(getActivity(), historyDetailsApplications.id);
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

    private void initToolbar() {
        ((NavigationActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((NavigationActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void jsonCreateApplication(int statusCode, String errorMessage, ResponseBody response) {
        if (statusCode == 200) {
//            Intent intent = new Intent(parentActivity, NavigationActivity.class);
//            intent.putExtra("isApplication", true);
//            startActivity(intent);
//            getActivity().getSupportFragmentManager().beginTransaction().replace(this).commit();
            //getActivity().getSupportFragmentManager().popBackStack();

            /*Bundle bundle = new Bundle();
            bundle.putBoolean("isApplication", true);
            SuccessOperation successOperation = new SuccessOperation();
            successOperation.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_content, successOperation).commit();*/

            forClick.setVisibility(View.GONE);
            succesLin.setVisibility(View.VISIBLE);
            try {
                textInfo.setText(getString(R.string.thx_request, response.string()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*try {
                intent.putExtra("customTextS", getString(R.string.thx_request)+" "+response.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivity(intent);*/
            /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            try {
                builder.setMessage(getString(R.string.thx_request)+" "+response.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().onBackPressed();
                }
            });
            builder.create();
            builder.show();*/
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("AppParamsFrag", "onDestroy");
    }

    @Override
    public void jsonCancelAppFirst(int statusCode, String errorMessage, CancelApplicationModel response) {
        if (statusCode == 200){
            cancelApplicationModel = response;
            Intent intent = new Intent(getActivity(), NavigationActivity.class);
            intent.putExtra("isComment", true);
            startActivityForResult(intent, COMMENT);
        }
    }

    @Override
    public void jsonCancelAppSecond(int statusCode, String errorMessage, ResponseBody response) {
        //if (statusCode == 200){
        if (getActivity() != null && isAdded()) {
            getActivity().onBackPressed();
        }
        //}
    }
}
