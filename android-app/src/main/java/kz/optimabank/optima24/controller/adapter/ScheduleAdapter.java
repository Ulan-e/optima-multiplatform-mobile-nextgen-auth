package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.activity.TransfersActivity.USER_ACCOUNT;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.utility.Constants;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CREDIT_HEADER = 0;
    private static final int TYPE_REGULAR = 1;
    private static final int TYPE_HEADER = 2;

    Context context;
    ArrayList<LoanScheduleResponse> data;
    private final UserAccounts account;

    public ScheduleAdapter(Context context, ArrayList<LoanScheduleResponse> data, UserAccounts account) {
        this.context = context;
        this.data = data;
        this.account = account;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == TYPE_CREDIT_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_header, parent, false);
            viewHolder = new VHCreditHeader(view);
        } else if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new VHHeader(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_schedule, parent, false);
            viewHolder = new VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        LoanScheduleResponse loanSchedule = null;
        if(data!=null && !data.isEmpty()) {
            loanSchedule = data.get(position);
        }
        if(loanSchedule!=null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader)mHolder;
                holder.tvName.setText(loanSchedule.name);
            } else if(mHolder instanceof VHCreditHeader) {
                VHCreditHeader holder = (VHCreditHeader) mHolder;
                if(account!=null) {
                    Log.i("DAc","account.isClosed = "+account.isClosed);
                    if (account.isClosed){
                        holder.linStatus.setVisibility(View.VISIBLE);
                    }else{
                        holder.linStatus.setVisibility(View.GONE);
                    }
                    UserAccounts.CreditAccounts creditAccount = (UserAccounts.CreditAccounts) account;
                    Log.i("agreementNumber","agreementNumber " + creditAccount.agreementNumber); //номер договора
                    Log.i("agreementAmount","agreementAmount " + creditAccount.agreementAmount);//сумма займа
                    Log.i("agreementDate","agreementDate " + creditAccount.agreementDate);//Дата заключения
                    Log.i("agreementEndDate","agreementEndDate " + creditAccount.agreementEndDate);//дата закрытия
                    Log.i("interestRate","interestRate " + creditAccount.interestRate); //ставка
                    Log.i("nextPaymentDate","nextPaymentDate " + creditAccount.nextPaymentDate);//оплатить до ??
                    Log.i("nextPaymentAmount","nextPaymentAmount " + creditAccount.nextPaymentAmount);//ежемесячный платеж
                    Log.i("creditAccounts","balance " + creditAccount.balance);//ежемесячный платеж
                    holder.tvPayTo.setText(context.getString(R.string.receipt_expire_date_));
                    holder.tvMonthlyPayment.setText(context.getString(R.string.monthly_payment));
                    holder.tvPaid.setText(context.getString(R.string.paid));

                    holder.tvPayToInfo.setText(creditAccount.getNextPaymentDay());
                    holder.tvMonthlyPaymentInfo.setText(getFormattedBalance(creditAccount.nextPaymentAmount, creditAccount.currency));
                    holder.tvPaidInfo.setText(creditAccount.getFormattedBalance(context));
                    holder.tvInterestRate.setText(R.string.deposit_rate);
                    holder.tvInterestRateInfo.setText((Double.valueOf(creditAccount.interestRate)) + " %");//Math.round
                    holder.tvAgreementNumber.setText(R.string.contract_number);
                    holder.tvAgreementNumberInfo.setText(creditAccount.agreementNumber);
                    holder.tvAgreementDate.setText(R.string.conclusion_date);
                    holder.tvAgreementDateInfo.setText(creditAccount.getAgreementDate());
                    holder.linNextPayment.setVisibility(View.VISIBLE);
                    holder.tvNextPayment.setText(R.string.loan_next_date);
                    holder.tvNextPaymentInfo.setText(creditAccount.getNextPaymentDate());
                    holder.linStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(R.string.loan_next_pay_sum);
                    holder.tvStatusInfo.setText(creditAccount.getFormattedNextPaymentAmount());
                    holder.tvStatusInfo.setTextColor(context.getResources().getColor(R.color.gray_black_56_56_56));
                }
            } else {
                VHItem holder = (VHItem) mHolder;
                holder.tvDate.setText(loanSchedule.getDate());
                holder.tvMainDebt.setText(getFormattedBalance(loanSchedule.getMainDebt(), account.currency));
                holder.tvRemuneration.setText(getFormattedBalance(loanSchedule.getInterest(), account.currency));
                holder.tvFee.setText(getFormattedBalance(loanSchedule.getCommission(), account.currency));
                holder.tvAmount.setText(getFormattedBalance(loanSchedule.getAmount(), account.currency));
                holder.tvRemainDebtSum.setText(getFormattedBalance(loanSchedule.getDebt(), account.currency));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data!=null ? data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(data!=null && !data.isEmpty()) {
            if (data.get(position).id == Constants.HEADER_ID) {
                return TYPE_HEADER;
            } else if (data.get(position).id == Constants.CREDIT_HEADER) {
                return TYPE_CREDIT_HEADER;
            }
        }
        return TYPE_REGULAR;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.tvMainDebt) TextView tvMainDebt;
        @BindView(R.id.tvRemuneration) TextView tvRemuneration;
        @BindView(R.id.tvFee) TextView tvFee;
        @BindView(R.id.tvAmount) TextView tvAmount;
        @BindView(R.id.tvRemainDebtSum) TextView tvRemainDebtSum;

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header) TextView tvName;

        private VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHCreditHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPayTo) TextView tvPayTo;
        @BindView(R.id.tvPayToInfo) TextView tvPayToInfo;
        @BindView(R.id.tvInterestRate) TextView tvInterestRate;
        @BindView(R.id.tvInterestRateInfo) TextView tvInterestRateInfo;
        @BindView(R.id.tvStatusInfo) TextView tvStatusInfo;
        @BindView(R.id.tvStatus) TextView tvStatus;
        @BindView(R.id.linStatus) LinearLayout linStatus;
        @BindView(R.id.tvAgreementNumber) TextView tvAgreementNumber;
        @BindView(R.id.tvAgreementNumberInfo) TextView tvAgreementNumberInfo;
        @BindView(R.id.tvAgreementDate) TextView tvAgreementDate;
        @BindView(R.id.tvAgreementDateInfo) TextView tvAgreementDateInfo;
        @BindView(R.id.tvMonthlyPayment) TextView tvMonthlyPayment;
        @BindView(R.id.tvMonthlyPaymentInfo) TextView tvMonthlyPaymentInfo;
        @BindView(R.id.tvPaid) TextView tvPaid;
        @BindView(R.id.tvPaidInfo) TextView tvPaidInfo;
        @BindView(R.id.linPayTo) LinearLayout linPayTo;
        @BindView(R.id.linMonthlyPayment) LinearLayout linMonthlyPayment;
        @BindView(R.id.linPaid) LinearLayout linPaid;
        @BindView(R.id.linNextPayment) LinearLayout linNextPayment;
        @BindView(R.id.tvNextPayment) TextView tvNextPayment;
        @BindView(R.id.tvNextPaymentInfo) TextView tvNextPaymentInfo;
        @BindView(R.id.linConvertCurrency) LinearLayout linConvertCurrency;
        @BindView(R.id.btnConvertCurrency) Button btnConvertCurrency;

        private VHCreditHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btnConvertCurrency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,TransfersActivity.class);
                    intent.putExtra(TRANSFER_NAME,context.getString(R.string.transfer_multi_card));
                    Log.d("TAG","account = " + account);
                    intent.putExtra(USER_ACCOUNT,account);
                    context.startActivity(intent);
                }
            });
        }
    }
}
