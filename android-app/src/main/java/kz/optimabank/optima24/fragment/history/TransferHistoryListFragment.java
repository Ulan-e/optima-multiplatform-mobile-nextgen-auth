package kz.optimabank.optima24.fragment.history;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.HistoryAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.HistoryItem;
import kz.optimabank.optima24.model.interfaces.History;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.HistoryImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Constants.HISTORY_KEY;
import static kz.optimabank.optima24.utility.Constants.SELECT_DATE;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.formatDate;

public class TransferHistoryListFragment extends ATFFragment implements HistoryImpl.CallbackTransferHistory {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.tvNotData) TextView tvNotData;

    ArrayList<HistoryItem> historyItems = new ArrayList<>();
    History history;
    boolean isClickableRecyclerView, first = true;
    HistoryAdapter adapter;
    String fromDate,toDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fromDate = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 7);
        toDate = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss").replaceAll("T00:00:00","T23:59:59");
    }

    @Override
    public void onResume() {
        super.onResume();
        isClickableRecyclerView = true;
        fetchTransferHistory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.closeBD();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS && data!=null) {
            if (requestCode == SELECT_DATE) {
                fromDate = data.getStringExtra("fromDate");
                toDate = data.getStringExtra("toDate");
                if(history!=null) {
                    progress.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(null);
                } else {
                    history = new HistoryImpl();
                    history.registerTransferCallBack(this);
                }
                history.getTransferHistory(getActivity(), fromDate, toDate.replaceAll("T00:00:00","T23:59:59"));
            }
        }
    }

    private void fetchTransferHistory(){
        if(GeneralManager.getInstance().getTransferHistory().isEmpty() || GeneralManager.getInstance().isNeedToUpdateHistory()) {
            history = new HistoryImpl();
            history.registerTransferCallBack(this);
            history.getTransferHistory(getActivity(), fromDate, toDate);
        } else {
            setAdapter(false);
        }
    }

    private SimpleDateFormat getSimpleDateFormat(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    public String getOperationDate(String date) {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = Constants.API_DAY_MONTH_FORMAT.format(sdf.parse(date));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    @Override
    public void jsonTransferHistoryResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {


            setAdapter(true);
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            if (!progress.isShown()) {
                tvNotData.setVisibility(View.VISIBLE);
                Log.i("TND2", "PHLF VISIBLE ");
            } else {
                progress.setVisibility(View.GONE);
                tvNotData.setVisibility(View.VISIBLE);
                Log.i("TND2", "PHLF VISIBLE ");
            }
            onError(errorMessage);
        }
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
                            if(historyItems.get(position).id != Constants.HEADER_ID && isClickableRecyclerView) {
                                intent.putExtra("isHistoryTransfer", true);
                                intent.putExtra(HISTORY_KEY, historyItems.get(position));
                                startActivity(intent);
                            } else if(historyItems.get(position).id == Constants.HEADER_ID && position==0) {
                                intent.putExtra("selectDate",true);
                                intent.putExtra(DATE_TAG,"TransferHistory");
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

    public void setAdapter(boolean isTime) {
        if(isAdded()) {
            progress.setVisibility(View.GONE);
            ArrayList<HistoryItem> statement =  getSortStatement();
            if(statement.isEmpty()) {

                if (fromDate!=null) {
                    Log.i("TND2","THLF fromDate = "+fromDate);
                    Log.i("TND2","THLF toDate = "+toDate);
                    if (toDate!=null) {
                        if (!first) {
                            Log.i("TND2", "THLF AlertDialog ");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            //builder.setTitle(getString(R.string.not_data));
                            builder.setMessage(getString(R.string.not_data_fmt, formatDate(false, fromDate), formatDate(false, toDate)));
                            builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                    //intent.putExtra("selectDate", true);
                                    //intent.putExtra(DATE_TAG, "PaymentHistory");
                                    //startActivityForResult(intent, SELECT_DATE);
                                }
                            });
                            builder.create();
                            builder.show();
                        }else{
                            if(!progress.isShown()) {
                                Log.i("TND2","THLF VISIBLE ");
                                tvNotData.setVisibility(View.VISIBLE);
                            }else{
                                progress.setVisibility(View.GONE);
                                Log.i("TND2","THLF VISIBLE ");
                                tvNotData.setVisibility(View.VISIBLE);
                            }
                            first = false;
                        }
                    }

                    /*statement.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data)+" "+getString(R.string.with)+" "+
                            fromDate+" "+getString(R.string.by)+" "+toDate));*/

                    if(!progress.isShown()) {
                        Log.i("TND2","THLF VISIBLE ");
                        tvNotData.setVisibility(View.VISIBLE);
                    }else{
                        progress.setVisibility(View.GONE);
                        Log.i("TND2","THLF VISIBLE ");
                        tvNotData.setVisibility(View.VISIBLE);
                    }
                }

                //statement.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data)+" "+getString(R.string.with)+" "+
                //        fromDate+" "+getString(R.string.by)+" "+toDate));
            }else{
                tvNotData.setVisibility(View.GONE);
                Log.i("TND2","THLF GONE ");

                /*Log.i("isTime","isTime = "+isTime);
                if (isTime){
                    statement.add(0,new HistoryItem(Constants.HEADER_ID,getOperationDate(fromDate)+" - "+getOperationDate(toDate)));
                }*/
            }
            adapter = new HistoryAdapter(getActivity(), statement, setOnClick());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public ArrayList<HistoryItem> getSortStatement() {
        if(!historyItems.isEmpty()) {
            historyItems.clear();
        }
        ArrayList<HistoryItem.TransferHistoryItem> transferHistory = GeneralManager.getInstance().getTransferHistory();
        if(transferHistory!=null && !transferHistory.isEmpty()) {
//            Collections.sort(transferHistory, new Comparator<HistoryItem.TransferHistoryItem>() {
//                @Override
//                public int compare(HistoryItem.TransferHistoryItem o1, HistoryItem.TransferHistoryItem o2) {
//                    return o2.createDate.compareTo(o1.createDate);
//                }
//            });

            String operationDate = "operation date";
            for (HistoryItem.TransferHistoryItem transferHistoryItem : transferHistory) {
                if (operationDate != null && !operationDate.equals(transferHistoryItem.getOperationDate(Constants.VIEW_DATE_FORMAT))) {
                    historyItems.add(new HistoryItem(Constants.HEADER_ID, Utilities.formatDateString(parentActivity, formatDate(false, transferHistoryItem.createDate), false)));
                    historyItems.add(transferHistoryItem);
                } else {
                    historyItems.add(transferHistoryItem);
                }
                operationDate = transferHistoryItem.getOperationDate(Constants.VIEW_DATE_FORMAT);
            }
        }
        if (historyItems.isEmpty()){
            historyItems.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data_fmt,
                    formatDate(false, fromDate), formatDate(false, toDate))));
        }
        return historyItems;
    }
}
