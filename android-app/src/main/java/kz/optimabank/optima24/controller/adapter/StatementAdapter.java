package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.activity.TransfersActivity.USER_ACCOUNT;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.pxInDp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.Category;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

public class StatementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REGULAR = 1;
    private static final int TYPE_CREDIT_HEADER = 2;
    private static final int TYPE_CARD_HEADER = 3;
    private static final int TYPE_STATUS_HEADER = 4;
    private static final int TYPE_BALANCE = 5;
    private final Context context;
    private UserAccounts account;
    ArrayList<ATFStatement> data;
    private final boolean isMainPage;
    private boolean isDefaultCard= false;
    private final ArrayList<Category> categories = GeneralManager.getInstance().getCategories();

    private DefaultCardChangeListener defaultCardChangeListener;

    public void setDefaultCardChangeListener(DefaultCardChangeListener listener){
        this.defaultCardChangeListener = listener;
    }

    public StatementAdapter(Context context, ArrayList<ATFStatement> data, boolean isMainPage) {
        this.data = data;
        this.context = context;
        this.isMainPage = isMainPage;
    }

    public StatementAdapter(Context context, ArrayList<ATFStatement> data, UserAccounts account, boolean isMainPage, boolean isDefaultCard) {
        this.data = data;
        this.context = context;
        this.isMainPage = isMainPage;
        this.account = account;
        this.isDefaultCard = isDefaultCard;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        Log.i("viewType", "viewType = " + viewType);
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new StatementAdapter.VHHeader(view);
        } else if (viewType == TYPE_CREDIT_HEADER || viewType == TYPE_CARD_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_header, parent, false);
            viewHolder = new StatementAdapter.VHCreditHeader(view);
        } else if (viewType == TYPE_STATUS_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_text_view_item, parent, false);
            viewHolder = new StatementAdapter.VHStatus(view);
        } else if (viewType == TYPE_BALANCE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_footer_item, parent, false);
            viewHolder = new StatementAdapter.VHBalance(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_statement_item, parent, false);
            viewHolder = new StatementAdapter.VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        ATFStatement statement = null;
        if (data != null && !data.isEmpty()) {
            statement = data.get(position);
        }
        if (statement != null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader) mHolder;
                holder.tvName.setText(statement.name);
            } else if (mHolder instanceof VHStatus) {
                VHStatus holder = (VHStatus) mHolder;
                if (account != null)
                    if (account.isBlocked) {
                        holder.tvStatus.setText(R.string.card_is_blocked);
                    } else if (account.isClosed) {
                        holder.tvStatus.setText(R.string.card_is_closed);
                    } else {
                        holder.linStatus.setVisibility(View.GONE);
                    }
            } else if (mHolder instanceof VHCreditHeader) {
                VHCreditHeader holder = (VHCreditHeader) mHolder;
                holder.linInterestRate.setVisibility(View.GONE);
                holder.linAgreementNumber.setVisibility(View.GONE);
                holder.linAgreementDate.setVisibility(View.GONE);
                if (account != null) {
                    if (account instanceof UserAccounts.CreditAccounts) {
                        UserAccounts.CreditAccounts creditAccount = (UserAccounts.CreditAccounts) account;
                        holder.tvPayTo.setText(context.getString(R.string.receipt_expire_date_));
                        holder.tvMonthlyPayment.setText(context.getString(R.string.monthly_payment));
                        holder.tvPaid.setText(context.getString(R.string.paid));

                        holder.tvPayToInfo.setText(creditAccount.getNextPaymentDay());
                        holder.tvMonthlyPaymentInfo.setText(getFormattedBalance(creditAccount.nextPaymentAmount, creditAccount.currency));
                        holder.tvPaidInfo.setText(creditAccount.getFormattedBalance(context));
                    } else if (account instanceof UserAccounts.Cards) {
                        UserAccounts.Cards card = (UserAccounts.Cards) account;

                        String multiBalanceInfo = null;
                        if (!card.encryptPossibility) {
                            holder.linMain.setPadding(pxInDp(context, 15), pxInDp(context, 20), pxInDp(context, 15), pxInDp(context, 20));
                        } else {
                            holder.linMain.setPadding(pxInDp(context, 15), pxInDp(context, 30), pxInDp(context, 15), pxInDp(context, 30));
                        }
                        if (card.multiBalanceList != null && card.multiBalanceList.isEmpty()) {
                            multiBalanceInfo = getFormattedBalance(card.balance, card.currency);
                        } else {
                            if (card.multiBalanceList != null) {
                                for (UserAccounts.Cards.MultiBalanceList multiBalanceItem : card.multiBalanceList) {
                                    Log.d("TAG", "multiBalanceItem.amount = " + multiBalanceItem.amount);
                                    if (multiBalanceInfo == null) {
                                        multiBalanceInfo = getFormattedBalance(multiBalanceItem.amount,
                                                multiBalanceItem.currency);
                                    } else {
                                        multiBalanceInfo = multiBalanceInfo + "\n" + getFormattedBalance(/*context,*/ multiBalanceItem.amount,
                                                multiBalanceItem.currency);
                                    }
                                }

                                holder.linLimitPayment.setVisibility(View.VISIBLE);
                                holder.contentAvailableBalance.setVisibility(View.VISIBLE);


                                List<UserAccounts.CardAccounts> cardAccounts = GeneralManager.getInstance().getCardsAccounts();
                                if(cardAccounts != null) {
                                    for (UserAccounts.CardAccounts cardAccount : cardAccounts) {
                                        if (cardAccount != null) {
                                            if (((UserAccounts.Cards) account).cardAccountCode == cardAccount.code) {
                                                double amount = cardAccount.blockedAmount * -1;
                                                String blockedBalance = getFormattedBalance(amount, "KGS");
                                                holder.tvLimitPaymentInfo.setText(blockedBalance);
                                            }
                                        }
                                    }
                                }


                                holder.textAvailableBalance.setText(getFormattedBalance(card.balance, card.currency));
                            }
                        }
                        holder.linPaid.setVisibility(View.GONE);

                        Utilities.setRobotoTypeFaceToTextView(context, holder.tvPayToInfo);
                        holder.tvPayTo.setTextColor(context.getResources().getColor(R.color.gray_middle));
                        holder.tvPayToInfo.setText(multiBalanceInfo);


                        if (card.isBlocked) {
                            holder.constraintLayout.setVisibility(View.GONE);
                        } else {
                            holder.constraintLayout.setVisibility(View.VISIBLE);
                        }

                        if (card.getDefaultStatus() > 0) {
                            holder.btnDefaultCard.setActivated(true);
                            holder.btnDefaultCard.setEnabled(false);
                            holder.btnMainCardInfo.setOnClickListener(v ->
                                    showMainCardDescriptionDescription(context));
                        } else {
                            holder.btnDefaultCard.setActivated(false);
                            holder.btnDefaultCard.setEnabled(true);
                            holder.btnMainCardInfo.setOnClickListener(v ->
                                    showMainCardDescriptionDescription(context));
                        }

                        holder.btnDefaultCard.setOnClickListener(view -> {
                            if (defaultCardChangeListener != null) {
                                defaultCardChangeListener.makeDefaultStatusCard();
                            }
                        });

                        if (!isDefaultCard) {
                            holder.btnDefaultCard.setActivated(true);
                            holder.btnDefaultCard.setEnabled(false);
                        }


                        Log.i("creditLimitAmount", "creditLimitAmount = " + card.creditLimitAmount);
                        if (card.creditLimitAmount > 0) {
                            holder.linInterestRate.setVisibility(View.VISIBLE);
                            holder.linAgreementNumber.setVisibility(View.VISIBLE);
                            holder.linAgreementDate.setVisibility(View.VISIBLE);
                            holder.linMonthlyPayment.setVisibility(View.VISIBLE);

                            holder.tvInterestRate.setText(context.getString(R.string.blocked_amount));
                            holder.tvInterestRate.setTextSize(15);
                            holder.tvInterestRateInfo.setTextSize(16);
                            holder.tvInterestRate.setTextColor(context.getResources().getColor(R.color.gray_180_180_180));
                            holder.tvInterestRateInfo.setText(card.getFormattedSum(context, card.blockedAmount));

                            holder.tvAgreementNumber.setText(context.getString(R.string.card_limit));
                            holder.tvAgreementNumber.setTextSize(15);
                            holder.tvAgreementNumberInfo.setTextSize(16);
                            holder.tvAgreementNumber.setTextColor(context.getResources().getColor(R.color.gray_180_180_180));
                            holder.tvAgreementNumberInfo.setText(card.getFormattedSum(context, card.creditLimitAmount));

                            holder.tvAgreementDate.setText(context.getString(R.string.card_debt));
                            holder.tvAgreementDate.setTextSize(15);
                            holder.tvAgreementDateInfo.setTextSize(16);
                            holder.tvAgreementDate.setTextColor(context.getResources().getColor(R.color.gray_180_180_180));
                            holder.tvAgreementDateInfo.setText(card.getFormattedSum(context, card.debtAmount));

                            holder.tvMonthlyPayment.setText(context.getString(R.string.min_payment));
                            holder.tvMonthlyPayment.setTextSize(15);
                            holder.tvMonthlyPaymentInfo.setTextSize(16);
                            holder.tvMonthlyPayment.setTextColor(context.getResources().getColor(R.color.gray_180_180_180));
                            holder.tvMonthlyPaymentInfo.setText(card.getFormattedSum(context, card.minPayAmount));
                        } else {
                            holder.linInterestRate.setVisibility(View.GONE);
                            holder.linAgreementNumber.setVisibility(View.GONE);
                            holder.linAgreementDate.setVisibility(View.GONE);
                            holder.linMonthlyPayment.setVisibility(View.GONE);
                        }
                        if (card.isMultiBalance) {
                            holder.linConvertCurrency.setVisibility(View.VISIBLE);
                            holder.textAvailableBalanceDesc.setVisibility(View.VISIBLE);
                            holder.tvPayTo.setText(context.getString(R.string.text_amount_by_currency));
                        } else {
                            holder.linConvertCurrency.setVisibility(View.GONE);
                            holder.textAvailableBalanceDesc.setVisibility(View.GONE);
                            holder.tvPayTo.setText(context.getString(R.string.loan_card));
                        }

                    } else if (account instanceof UserAccounts.DepositAccounts) {
                        UserAccounts.DepositAccounts depositAccounts = (UserAccounts.DepositAccounts) account;
                        if (depositAccounts instanceof UserAccounts.WishAccounts) {
                            UserAccounts.WishAccounts wishAccounts = (UserAccounts.WishAccounts) account;
                            holder.linPayTo.setVisibility(View.GONE);
                            holder.linPaid.setVisibility(View.GONE);
                            holder.tvMonthlyPayment.setText(context.getString(R.string.monthly_payment));
                            holder.tvMonthlyPaymentInfo.setText(getFormattedBalance(wishAccounts.mountlyPayment, wishAccounts.currency));
                        }
                    }
                }
            } else if (mHolder instanceof VHBalance) {
                VHBalance holder = (VHBalance) mHolder;
                holder.tvName.setText(statement.name);
                holder.tvCurrency.setText(statement.currency);
                holder.tvAmount.setText(String.valueOf(statement.amount));
            } else {
                VHItem holder = (VHItem) mHolder;
                if (statement.contragentNumberInfo != null && !statement.contragentNumberInfo.isEmpty())
                    holder.linReference.setVisibility(View.VISIBLE);
                holder.tvInfoReference.setText(statement.contragentNumberInfo);
                holder.tvInfoReference.setTextColor(context.getResources().getColor(R.color.gray_180_180_180));
                if (statement.name != null) {
                    if (statement.operationDate.equals("minStat") || statement.operationDate.equals("feeStat")) {
                        holder.tvBalance.setVisibility(View.VISIBLE);
                        holder.tvDate.setVisibility(View.GONE);
                        holder.ivType.setVisibility(View.GONE);
                        if (position == data.size() - 1) {
                            if (statement.name.isEmpty()) {
                                holder.separatorTop.setVisibility(View.GONE);
                                holder.linPadd.setPadding(15, 15, 15, 10);
                            } else {
                                holder.separatorTop.setVisibility(View.VISIBLE);
                                holder.linPadd.setPadding(15, 15, 15, 10);
                            }
                        } else {
                            try {
                                if (statement.name.isEmpty()) {
                                    holder.separatorTop.setVisibility(View.GONE);
                                    holder.linPadd.setPadding(15, 15, 15, 10);
                                } else {
                                    holder.separatorTop.setVisibility(View.VISIBLE);
                                    holder.linPadd.setPadding(15, 15, 15, 10);
                                }
                            } catch (Exception e) {
                                holder.separatorTop.setVisibility(View.GONE);
                                holder.linPadd.setPadding(15, 15, 15, 10);
                            }
                        }
                        holder.linFee.setVisibility(View.GONE);
                        holder.separator.setVisibility(View.GONE);
                        if (statement.amount != 0) {
                            holder.tvName.setText(statement.name);
                            holder.tvBalance.setText(statement.currency);

                            holder.tvName.setTextColor(Color.BLACK);
                            holder.tvBalance.setTextColor(Color.BLACK);
                            holder.tvBalance.setTypeface(holder.tvBalance.getTypeface(), Typeface.BOLD);// TODO: 03.02.2021

                        } else {
                            holder.allLine.setVisibility(View.GONE);
                        }
                    }
                    Log.i("LimitViewHolder", "currency ==== " + statement.currency);
                    Log.i("LimitViewHolder", "currency ==== " + statement.name);
                } else {
                    holder.tvBalance.setVisibility(View.VISIBLE);
                    holder.tvDate.setVisibility(View.VISIBLE);
                    holder.ivType.setVisibility(View.VISIBLE);
                    holder.linPadd.setPadding(15, 20, 15, 15);
                    holder.ivType.setPadding(15, 15, 15, 15);
                    holder.tvName.setTextColor(context.getResources().getColor(R.color.gray_black_56_56_56));
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.gray_black_56_56_56));
                    holder.tvBalance.setTypeface(holder.tvBalance.getTypeface(), Typeface.NORMAL);
                    //Log.i("STATID","id = "+statement.id);
                    //Log.i("STATID","categoryId = "+statement.categoryId);
                    if (categories != null)
                        for (Category category : categories) {
                            if (String.valueOf(statement.categoryId).equals(String.valueOf(Double.valueOf(category.ID)))) {
                                if (category.ParentId == 0) {
                                    Log.i("STATID", "category.id= " + category.ID);
                                    setImageRad(category.ID, holder.ivType);
                                } else {
                                    for (Category categ : categories) {
                                        if (categ.ID == category.ParentId) {
                                            Log.i("STATID", "categ.id= " + categ.ID);
                                            setImageRad(categ.ID, holder.ivType);
                                        }
                                    }
                                }
                            }
                        }
                    holder.linFee.setVisibility(View.VISIBLE);
                    if (statement.fee != 0) {
                        holder.linFee.setVisibility(View.VISIBLE);
                    } else {
                        holder.linFee.setVisibility(View.GONE);
                    }
                    if (statement.description != null) {
                        holder.tvName.setText(statement.description.trim());
                        Log.i("statement.description", "statement.description = " + statement.description);
                    }
                    if (String.valueOf(statement.amount).equals(String.valueOf(statement.tAmount)) && statement.currency.equals(statement.tCurrency)) {
                        holder.tvBalance.setText(statement.getFormattedTAmount(context));
                    } else {
                        if (statement.amount != 0 && statement.tAmount != 0) {
                            holder.tvBalance.setText(statement.getFormattedTAmount(context) + "\n" + statement.getFormattedAmount(context));
                        }
                        if (statement.amount != 0 && statement.tAmount == 0) {
                            holder.tvBalance.setText(statement.getFormattedAmount(context));
                        }
                        if (statement.amount == 0 && statement.tAmount != 0) {
                            holder.tvBalance.setText(statement.getFormattedTAmount(context));
                        }
                    }
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.gray_black_56_56_56));
                    holder.tvComm.setText(String.valueOf(statement.getFormattedFee(context)));
                    if (isMainPage) {
                        holder.tvDate.setText(statement.getOperationDate() + " " +
                                context.getString(R.string.in) + " " + statement.getOperationTime());
                    } else {
                        holder.tvDate.setText(statement.getOperationTime());
                    }
                }
            }
        }
    }

    private void showMainCardDescriptionDescription(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(context.getResources().getString(R.string.text_default_incoming_card));
        builder.setPositiveButton(context.getResources().getString(R.string.status_ok), (dialog, which) ->
                dialog.dismiss()
        );
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setImageRad(int ID, ImageView imageView) {
        switch (ID) {
            case 1:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_car));
                imageView.setImageResource(R.drawable.ic_image_white_category_1);
                break;
            case 2:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_bank_operations));
                imageView.setImageResource(R.drawable.ic_image_white_category_2);
                break;
            case 3:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_home));
                imageView.setImageResource(R.drawable.ic_image_white_category_3);
                break;
            case 4:
            case 26:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_animals));
                imageView.setImageResource(R.drawable.ic_image_white_category_4);
                break;
            case 5:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_health));
                imageView.setImageResource(R.drawable.ic_image_white_category_5);
                break;
            case 6:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_restaurants));
                imageView.setImageResource(R.drawable.ic_image_white_category_6);
                break;
            case 7:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_shop));
                imageView.setImageResource(R.drawable.ic_image_white_category_7);
                break;
            case 8:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_edu));
                imageView.setImageResource(R.drawable.ic_image_white_category_8);
                break;
            case 9:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_clothes));
                imageView.setImageResource(R.drawable.ic_image_white_category_9);
                break;
            case 10:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_transfer_from_card));
                imageView.setImageResource(R.drawable.ic_card);
                break;
            case 11:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_air));
                imageView.setImageResource(R.drawable.ic_image_white_category_11);
                break;
            case 12:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_tv));
                imageView.setImageResource(R.drawable.ic_image_white_category_12);
                break;
            case 13:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_tv));
                imageView.setImageResource(R.drawable.ic_image_white_category_13);
                break;
            case 14:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_portf));
                imageView.setImageResource(R.drawable.ic_image_white_category_14);
                break;
            case 15:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_communal));
                imageView.setImageResource(R.drawable.ic_image_white_category_15);
                break;
            case 16:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_boots));
                imageView.setImageResource(R.drawable.ic_image_white_category_16);
                break;
            case 17:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_members));
                imageView.setImageResource(R.drawable.ic_image_white_category_17);
                break;
            case 22:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_decoration));
                imageView.setImageResource(R.drawable.ic_image_white_category_22);
                break;

            default:
                imageView.setBackground(context.getResources().getDrawable(R.drawable.radius_star));
                imageView.setImageResource(R.drawable.ic_stars2);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && !data.isEmpty()) {
            if (data.get(position).id == Constants.HEADER_ID) {
                return TYPE_HEADER;
            } else if (data.get(position).id == Constants.CREDIT_HEADER) {
                return TYPE_CREDIT_HEADER;
            } else if (data.get(position).id == Constants.CARD_HEADER) {
                return TYPE_CARD_HEADER;
            } else if (data.get(position).id == -5) {
                return TYPE_STATUS_HEADER;
            } else if (data.get(position).id == -6) {
                return TYPE_BALANCE;
            }
        }
        return TYPE_REGULAR;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvBalance)
        TextView tvBalance;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.iv_type)
        ImageView ivType;
        @BindView(R.id.tvInfoReference)
        TextView tvInfoReference;
        @BindView(R.id.tvComm)
        TextView tvComm;
        @BindView(R.id.linReference)
        LinearLayout linReference;
        @BindView(R.id.fee_linear_wrapper)
        LinearLayout linFee;
        @BindView(R.id.allLine)
        LinearLayout allLine;
        @BindView(R.id.linPadd)
        LinearLayout linPadd;
        @BindView(R.id.separator)
        View separator;
        @BindView(R.id.separatorTop)
        View separatorTop;

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header)
        TextView tvName;

        private VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHStatus extends RecyclerView.ViewHolder {
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.linStatus)
        LinearLayout linStatus;

        private VHStatus(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHBalance extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;

        private VHBalance(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHCreditHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPayTo)
        TextView tvPayTo;
        @BindView(R.id.tvPayToInfo)
        TextView tvPayToInfo;
        @BindView(R.id.tvMonthlyPayment)
        TextView tvMonthlyPayment;
        @BindView(R.id.tvAgreementDate)
        TextView tvAgreementDate;
        @BindView(R.id.tvAgreementDateInfo)
        TextView tvAgreementDateInfo;
        @BindView(R.id.tvMonthlyPaymentInfo)
        TextView tvMonthlyPaymentInfo;
        @BindView(R.id.tvLimitPayment)
        TextView tvLimitPayment;
        @BindView(R.id.tvLimitPaymentInfo)
        TextView tvLimitPaymentInfo;
        @BindView(R.id.tvPaid)
        TextView tvPaid;
        @BindView(R.id.tvPaidInfo)
        TextView tvPaidInfo;
        @BindView(R.id.linPayTo)
        LinearLayout linPayTo;
        @BindView(R.id.linMonthlyPayment)
        LinearLayout linMonthlyPayment;
        @BindView(R.id.linMain)
        LinearLayout linMain;
        @BindView(R.id.linLimitPayment)
        LinearLayout linLimitPayment;
        @BindView(R.id.linInterestRate)
        LinearLayout linInterestRate;
        @BindView(R.id.tvInterestRate)
        TextView tvInterestRate;
        @BindView(R.id.tvInterestRateInfo)
        TextView tvInterestRateInfo;
        @BindView(R.id.linAgreementNumber)
        LinearLayout linAgreementNumber;
        @BindView(R.id.linAgreementDate)
        LinearLayout linAgreementDate;
        @BindView(R.id.tvAgreementNumber)
        TextView tvAgreementNumber;
        @BindView(R.id.tvAgreementNumberInfo)
        TextView tvAgreementNumberInfo;
        @BindView(R.id.linPaid)
        LinearLayout linPaid;
        @BindView(R.id.linConvertCurrency)
        LinearLayout linConvertCurrency;
        @BindView(R.id.btnConvertCurrency)
        Button btnConvertCurrency;
        @BindView(R.id.content_available_balance)
        LinearLayout contentAvailableBalance;
        @BindView(R.id.text_available_balance)
        TextView textAvailableBalance;
        @BindView(R.id.text_view_available_balance_desc)
        TextView textAvailableBalanceDesc;
        @BindView(R.id.btn_default_card)
        AppCompatImageButton btnDefaultCard;
        @BindView(R.id.btn_main_card_info)
        ImageButton btnMainCardInfo;
        @BindView(R.id.content_default_card)
        ConstraintLayout constraintLayout;


        private VHCreditHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btnConvertCurrency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TransfersActivity.class);
                    intent.putExtra(TRANSFER_NAME, context.getString(R.string.transfer_multi_card));
                    Log.d("TAG", "account = " + account);
                    intent.putExtra(USER_ACCOUNT, account);
                    context.startActivity(intent);
                }
            });
        }
    }

    public interface DefaultCardChangeListener{
        void makeDefaultStatusCard();
    }
}