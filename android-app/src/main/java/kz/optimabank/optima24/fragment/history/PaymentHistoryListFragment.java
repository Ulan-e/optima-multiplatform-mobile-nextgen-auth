package kz.optimabank.optima24.fragment.history;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class PaymentHistoryListFragment extends ATFFragment implements HistoryImpl.CallbackPaymentHistory {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tvNotData)
    TextView tvNotData;

    ArrayList<HistoryItem> historyItems = new ArrayList<>();
    History history;
    boolean isClickableRecyclerView;
    HistoryAdapter adapter;
    String fromDate, toDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isClickableRecyclerView = true;
        fetchPaymentHistory();
    }

    private void fetchPaymentHistory() {
        fromDate = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 7);
        toDate = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss").replaceAll("T00:00:00", "T23:59:59");
        if (GeneralManager.getInstance().getPaymentHistory().isEmpty() || GeneralManager.getInstance().isNeedToUpdateHistory()) {
            history = new HistoryImpl();
            history.registerPaymentCallBack(this);
            history.getPaymentHistory(getActivity(), fromDate, toDate);
        } else {
            setAdapter();
        }
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
        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            if (requestCode == SELECT_DATE) {

                Log.d(TAG, "fromDate = " + data.getStringExtra("fromDate"));
                Log.d(TAG, "toDate = " + data.getStringExtra("toDate"));
                fromDate = data.getStringExtra("fromDate");
                toDate = data.getStringExtra("toDate");

                if (history != null) {
                    progress.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(null);
                } else {
                    history = new HistoryImpl();
                    history.registerPaymentCallBack(this);
                }
                history.getPaymentHistory(getActivity(), fromDate, toDate.replaceAll("T00:00:00", "T23:59:59"));
            }
        }
    }

    @Override
    public void jsonPaymentHistoryResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            setAdapter();
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
                            if (historyItems.get(position).id != Constants.HEADER_ID && isClickableRecyclerView) {
                                isClickableRecyclerView = false;
                                intent.putExtra("isHistoryPayment", true);
                                intent.putExtra(HISTORY_KEY, historyItems.get(position));
                                startActivity(intent);
                            } else if (historyItems.get(position).id == Constants.HEADER_ID && position == 0) {
                                intent.putExtra("selectDate", true);
                                intent.putExtra(DATE_TAG, "PaymentHistory");
                                startActivityForResult(intent, SELECT_DATE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view, animatorListener);
            }
        };
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

    private SimpleDateFormat getSimpleDateFormat() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    public void setAdapter() {
        Log.i("TND2", "PHLF setAdapter ");
        if (isAdded()) {
            Log.i("TND2", "PHLF isAdded ");
            progress.setVisibility(View.GONE);
            ArrayList<HistoryItem> statement = getSortStatement();
            if (statement.isEmpty()) {
                Log.i("TND2", "PHLF statement.isEmpty ");

                Log.i("TND2", "PHLF fromDate = " + fromDate);
                Log.i("TND2", "PHLF toDate = " + toDate);
                if (fromDate != null) {
                    if (toDate != null) {
                        Log.i("TND2", "PHLF AlertDialog ");
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
                    }

                    /*statement.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data)+" "+getString(R.string.with)+" "+
                            fromDate+" "+getString(R.string.by)+" "+toDate));*/

                } else {
                    if (!progress.isShown()) {
                        tvNotData.setVisibility(View.VISIBLE);
                        Log.i("TND2", "PHLF VISIBLE ");
                    } else {
                        progress.setVisibility(View.GONE);
                        tvNotData.setVisibility(View.VISIBLE);
                        Log.i("TND2", "PHLF VISIBLE ");
                    }
                }


                //statement.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data) + " " +
                //        getString(R.string.during_period) + " "+getOperationDate(fromDate) + "-"+getOperationDate(toDate)));
            } else {
                tvNotData.setVisibility(View.GONE);
                Log.i("TND2", "PHLF GONE ");
            }
            adapter = new HistoryAdapter(getActivity(), statement, setOnClick());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public ArrayList<HistoryItem> getSortStatement() {
        if (!historyItems.isEmpty()) {
            historyItems.clear();
        }
        ArrayList<HistoryItem.PaymentHistoryItem> paymentHistory = GeneralManager.getInstance().getPaymentHistory();
        if (paymentHistory != null && !paymentHistory.isEmpty()) {
//            Collections.sort(paymentHistory, new Comparator<HistoryItem.PaymentHistoryItem>() {
//                @Override
//                public int compare(HistoryItem.PaymentHistoryItem o1, HistoryItem.PaymentHistoryItem o2) {
//                    return o2.createDate.compareTo(o1.createDate);
//                }
//            });

            String operationDate = "operation date";
            for (HistoryItem.PaymentHistoryItem paymentHistoryItem : paymentHistory) {
                if (operationDate != null && !operationDate.equals(paymentHistoryItem.getOperationDate(Constants.VIEW_DATE_FORMAT))) {
                    historyItems.add(new HistoryItem(Constants.HEADER_ID, Utilities.formatDateString(parentActivity, formatDate(false, paymentHistoryItem.createDate), false)));
                    historyItems.add(paymentHistoryItem);
                } else {
                    historyItems.add(paymentHistoryItem);
                }
                operationDate = paymentHistoryItem.getOperationDate(Constants.VIEW_DATE_FORMAT);
            }
        }
        if (historyItems.isEmpty()) {
            historyItems.add(new HistoryItem(Constants.HEADER_ID, getString(R.string.not_data_fmt,
                    formatDate(false, fromDate), formatDate(false, toDate))));
        }
        return historyItems;
    }
}
