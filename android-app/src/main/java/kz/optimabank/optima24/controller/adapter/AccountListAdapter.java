package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.CARD;
import static kz.optimabank.optima24.utility.Constants.CARD_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CREDIT_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CURRENT_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.DEPOSIT_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.IS_SHOW_TIP_DEF_CARD;

public class AccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REGULAR = 1;
    private static final int HEADER_SPACING = 2;
    private static final int FOOTER_REQUEST = -3;
    private static final int ACC_VISI = -4;
    private Animation anim = new AlphaAnimation(0.2f, 1.0f);
    ArrayList<UserAccounts> data;
    Context context;
    private OnItemClickListener mOnItemClickListener;
    private AnchorViewListener anchorViewListener;

    public AccountListAdapter(Context context, ArrayList<UserAccounts> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new VHHeader(view);
        } else if (viewType == Constants.FOOTER_ID) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_footer, parent, false);
            viewHolder = new VHFooter(view);
        } else if (viewType == HEADER_SPACING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_spacing, parent, false);
            viewHolder = new VHSpacing(view);
        } else if (viewType == FOOTER_REQUEST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_request, parent, false);
            viewHolder = new VHFooterRequest(view);
        } else if (viewType == ACC_VISI) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acc_visi_item, parent, false);
            viewHolder = new VHAccVisi(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_account_item, parent, false);
            viewHolder = new VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        UserAccounts account = null;
        if (data != null && !data.isEmpty()) {
            account = data.get(position);
        }
        if (account != null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader) mHolder;
                holder.tvName.setText(account.name);
            } else if (mHolder instanceof VHFooter) {
                VHFooter holder = (VHFooter) mHolder;
                holder.btnFooter.setText(data.get(position).name);
                if (data.get(position - 1) instanceof UserAccounts.Cards) {
                    holder.btnFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(context, NavigationActivity.class);
                            intent.putExtra("UserAccountsB", true);
                            intent.putExtra("UserAccounts", "Cards");
                            context.startActivity(intent);
                        }
                    });
                } else if (data.get(position - 1) instanceof UserAccounts.WishAccounts) {
                    holder.btnFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(context, NavigationActivity.class);
                            intent.putExtra("UserAccountsB", true);
                            intent.putExtra("UserAccounts", "WishAccounts");
                            context.startActivity(intent);
                        }
                    });
                } else if (data.get(position - 1) instanceof UserAccounts.CheckingAccounts) {
                    holder.btnFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context,"CheckingAccounts",Toast.LENGTH_SHORT).show();
                            Intent intent;
                            intent = new Intent(context, NavigationActivity.class);
                            intent.putExtra("UserAccountsB", true);
                            intent.putExtra("UserAccounts", "CheckingAccounts");
                            context.startActivity(intent);
                        }
                    });
                } else if (data.get(position - 1) instanceof UserAccounts.DepositAccounts) {
                    holder.btnFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context,"DepositAccounts",Toast.LENGTH_SHORT).show();
                            Intent intent;
                            intent = new Intent(context, NavigationActivity.class);
                            intent.putExtra("UserAccountsB", true);
                            intent.putExtra("UserAccounts", "DepositAccounts");
                            context.startActivity(intent);
                        }
                    });
                } else if (data.get(position - 1) instanceof UserAccounts.CreditAccounts) {
                    holder.btnFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context,"CreditAccounts",Toast.LENGTH_SHORT).show();
                            Intent intent;
                            intent = new Intent(context, NavigationActivity.class);
                            intent.putExtra("UserAccountsB", true);
                            intent.putExtra("UserAccounts", "CreditAccounts");
                            context.startActivity(intent);
                        }
                    });
                } else if (data.get(position - 1) instanceof UserAccounts.CardAccounts) {
                    holder.btnFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context,"CreditAccounts",Toast.LENGTH_SHORT).show();
                            Intent intent;
                            intent = new Intent(context, NavigationActivity.class);
                            intent.putExtra("UserAccountsB", true);
                            intent.putExtra("UserAccounts", "CardAccounts");
                            context.startActivity(intent);
                        }
                    });
                }
            } else if (mHolder instanceof VHItem) {
                VHItem holder = (VHItem) mHolder;
                holder.tv_validate_ending.setVisibility(View.GONE);
                holder.tvProgress.setVisibility(View.GONE);
                holder.progress.setVisibility(View.GONE);
                holder.tvExtinguishedSumForCredit.setVisibility(View.GONE);
                holder.tv_validate_ending.clearAnimation();

                holder.textviewName.setText(account.name);
                holder.textviewNumber.setVisibility(View.VISIBLE);
                holder.textviewNumber.setText(account.number);

                if (account instanceof UserAccounts.CreditAccounts)
                    if (account.isClosed)
                        holder.textviewBalance.setText("0.00");
                    else {
                        UserAccounts.CreditAccounts credit = (UserAccounts.CreditAccounts) account;
                        Log.i("WhatAboutBalance", "balance = " + credit.balance);
                        Log.i("WhatAboutBalance", "balance = " + credit.agreementAmount);
                        holder.textviewBalance.setText(Utilities.getFormattedBalance(credit.balance, credit.currency));
                    }
                else
                    holder.textviewBalance.setText(account.getFormattedBalance(context));

                if (account.balance > 0)
                    holder.textviewBalance.setTextColor(context.getResources().getColor(R.color.gray_atf));//Color.parseColor("#005dba")
                else
                    holder.textviewBalance.setTextColor(context.getResources().getColor(R.color.blue_atf));

                int imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
                int imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(imageWidth, imageHeight);
                holder.ivType.setLayoutParams(lp);

                if (account.accountType == CARD) {
                    UserAccounts.Cards card = (UserAccounts.Cards) data.get(position);

                    if (card != null) {
                        holder.tvCardStatus.setVisibility(View.GONE);

                        // короче я поменял
                        if(card.isMultiBalance){
                            holder.textviewBalance.setVisibility(View.VISIBLE);
                            holder.textviewBalance.setText(Utilities.getFormattedBalance(card.balance, card.currency));
                            holder.tvHideBalance.setVisibility(View.VISIBLE);
                        }

                        if (card.isBlocked) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.card_is_blocked);
                            holder.textviewBalance.setTextColor(context.getResources().getColor(R.color.blue_atf));//Color.parseColor("#FF4000")
                        } else if (card.isClosed) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.card_is_closed);
                            holder.textviewBalance.setTextColor(context.getResources().getColor(R.color.blue_atf));//Color.parseColor("#FF4000")
                        }

                        if (card.getDefaultStatus() > 0) {
                            holder.imageViewDefaultCard.setVisibility(View.VISIBLE);

                            SharedPreferences sharedPreferences = Utilities.getPreferences(context);
                            boolean isShowTip = sharedPreferences.getBoolean(IS_SHOW_TIP_DEF_CARD, true);
                            if(isShowTip) {
                                if(anchorViewListener != null) {
                                    releaseTipShow();
                                    showTip(holder);
                                }
                            }
                        } else {
                            holder.imageViewDefaultCard.setVisibility(View.GONE);
                        }

                        Date expireDate = card.getCardExpireDate();
                        if (expireDate != null) {
                            Calendar calendar = Calendar.getInstance();
                            Calendar c = Calendar.getInstance();
                            calendar.setTime(expireDate);
                            calendar.add(Calendar.MONTH, -1);


                            if (c.getTime().after(calendar.getTime()) && c.getTime().before(expireDate)) {
                                holder.tv_validate_ending.setVisibility(View.VISIBLE);
                                anim.setDuration(350);
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                holder.tv_validate_ending.startAnimation(anim);
                            }
                        }

                        int imageWidthForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources().getDisplayMetrics());
                        int imageHeightForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
                        ConstraintLayout.LayoutParams lpc = new ConstraintLayout.LayoutParams(imageWidthForCard, imageHeightForCard);
                        holder.ivType.setLayoutParams(lpc);
                        //try {
                        byte[] min = card.getByteArrayMiniatureImg();
                        if (min != null)
                            Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                        else
                            Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left));
                        //else {
                        //    Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, card.miniatureIm);
                        //}
                        //Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, card.readObject(false));//card.miniatureIm
                        //} catch (IOException | ClassNotFoundException e) {
                        //    Log.i("setCustomCard","error");
                        //    e.printStackTrace();
                        //    Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, card.miniatureIm);
                        //}

                    }

                } else if (account.accountType == CREDIT_ACCOUNT) {
                    UserAccounts.CreditAccounts creditAccount = (UserAccounts.CreditAccounts) account;
                    holder.ivType.setImageResource(R.drawable.ic_image_dark_account_loan);//money
                    holder.progress.setVisibility(View.VISIBLE);
                    if (creditAccount.isClosed) {
                        holder.tvExtinguishedSumForCredit.setVisibility(View.GONE);
                    } else {
                        holder.tvExtinguishedSumForCredit.setVisibility(View.VISIBLE);
                    }

                    holder.tvExtinguishedSumForCredit.setText(creditAccount.getFormattedBalance(context));
                    holder.tvCardStatus.setVisibility(View.GONE);
                    holder.progress.setMax((float) creditAccount.agreementAmount);
                    if (!creditAccount.isClosed)
                        holder.progress.setProgress(Float.valueOf(creditAccount.getFormattedBalance(context)
                                .substring(0, creditAccount.getFormattedBalance(context).length() - 4).replaceAll(" ", "").replaceAll(",", ".")));
                    else
                        holder.progress.setProgress(0);

                    String nextPaymentDate = creditAccount.getNextPaymentDate();
                    if (nextPaymentDate != null) {
                        holder.textviewNumber.setText(context.getResources()
                                .getString(R.string.next_payment) + " " + nextPaymentDate);
                    } else {
                        holder.textviewNumber.setVisibility(View.GONE);
                    }

                    if (creditAccount.isClosed) {
                        holder.tvCardStatus.setVisibility(View.VISIBLE);
                        holder.tvCardStatus.setText(R.string.loan_is_closed);
                    }
                    holder.tvMulti.setVisibility(View.GONE);

                } else if (account.accountType == CARD_ACCOUNT || account.accountType == CURRENT_ACCOUNT) {
                    UserAccounts checkingAccounts = new UserAccounts();
                    if (account instanceof UserAccounts.CheckingAccounts) {
                        checkingAccounts = (UserAccounts.CheckingAccounts) account;
                    } else if (account instanceof UserAccounts.CardAccounts) {
                        checkingAccounts = (UserAccounts.CardAccounts) account;
                    }
                    Log.i("ALA", "currency = " + checkingAccounts.currency);
                    /*if (checkingAccounts.currency.equals("KZT")){
                        holder.ivType.setImageResource(R.drawable.tenge_2);
                    } else if (checkingAccounts.currency.equals("USD")){
                        holder.ivType.setImageResource(R.drawable.usd_2);
                    } else if (checkingAccounts.currency.equals("RUB")||checkingAccounts.currency.equals("RUR")){
                        holder.ivType.setImageResource(R.drawable.rub_2);
                    } else if (checkingAccounts.currency.equals("GBP")){
                        holder.ivType.setImageResource(R.drawable.gbp_2);
                    } else if (checkingAccounts.currency.equals("CNY")){
                        holder.ivType.setImageResource(R.drawable.cny_2);
                    } else if (checkingAccounts.currency.equals("EUR")){
                        holder.ivType.setImageResource(R.drawable.eur_2);
                    } else {*/
                    holder.ivType.setImageResource(R.drawable.ic_image_dark_account_current);//bank_account
                    //}
                    holder.tvCardStatus.setVisibility(View.GONE);
                    if (checkingAccounts.isClosed) {
                        holder.tvCardStatus.setVisibility(View.VISIBLE);
                        holder.tvCardStatus.setText(R.string.loan_is_closed);
                    }
                    holder.tvMulti.setVisibility(View.GONE);

                } else if (account.accountType == DEPOSIT_ACCOUNT) {
                    UserAccounts.DepositAccounts depositAccount = (UserAccounts.DepositAccounts) account;
                    if (!depositAccount.isWish) {
                        holder.tvCardStatus.setVisibility(View.GONE);
                        if (depositAccount.isClosed) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.loan_is_closed);
                        }
                        /*if (depositAccount.currency.equals("KZT")){
                            holder.ivType.setImageResource(R.drawable.tenge_1);
                        } else if (depositAccount.currency.equals("USD")){
                            holder.ivType.setImageResource(R.drawable.usd_1);
                        } else if (depositAccount.currency.equals("EUR")){
                            holder.ivType.setImageResource(R.drawable.eur_1);
                        } else {*/
                        holder.ivType.setImageResource(R.drawable.ic_image_dark_account_depo);//ic_ico_dep
                        //}
                    } else {
                        UserAccounts.WishAccounts wishDepositAccount = (UserAccounts.WishAccounts) account;
                        if (wishDepositAccount.isClosed) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.loan_is_closed);
                        } else {
                            holder.tvCardStatus.setVisibility(View.GONE);
                        }

                        holder.progress.setVisibility(View.VISIBLE);
                        holder.tvProgress.setVisibility(View.VISIBLE);
                        holder.ivType.setImageResource(R.drawable.ic_image_dark_account_goal);//flower
                        double residualAmount = (wishDepositAccount.totalAmount - wishDepositAccount.balance);
                        holder.tvProgress.setText(Utilities.getFormattedInteger(residualAmount)
                                + " / " + Utilities.getFormattedInteger(wishDepositAccount.totalAmount));
                        holder.progress.setMax((float) wishDepositAccount.totalAmount);
                        holder.progress.setProgress((float) wishDepositAccount.balance);

                        holder.textviewName.setText(wishDepositAccount.wishName);
                        holder.textviewNumber.setText(context.getResources().getString(R.string.wish_text));
                    }
                    holder.tvMulti.setVisibility(View.GONE);
                }
            }
        }
    }

    private void releaseTipShow(){
        SharedPreferences.Editor editor = Utilities.getPreferences(context).edit();
        editor.putBoolean(IS_SHOW_TIP_DEF_CARD, false);
        editor.commit();
    }

    private void showTip(VHItem holder) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            anchorViewListener.onPassAnchorView(holder.imageViewDefaultCard);
        }, 500);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && !data.isEmpty()) {
            if (data.get(position).code == Constants.HEADER_ID) {
                return TYPE_HEADER;
            } else if (data.get(position).code == Constants.HEADER_SPACING) {
                return HEADER_SPACING;
            } else if (data.get(position).code == Constants.FOOTER_ID) {
                return Constants.FOOTER_ID;
            } else if (data.get(position).code == FOOTER_REQUEST) {
                return FOOTER_REQUEST;
            } else if (data.get(position).code == ACC_VISI) {
                return ACC_VISI;
            }
        }
        return TYPE_REGULAR;
    }

    public void setAnchorViewListener(AnchorViewListener listener){
        this.anchorViewListener = listener;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_account_name)
        TextView textviewName;
        @BindView(R.id.tv_validate_ending)
        TextView tv_validate_ending;
        @BindView(R.id.textview_account_balance)
        TextView textviewBalance;
        @BindView(R.id.textview_account_number)
        TextView textviewNumber;
        @BindView(R.id.tv_card_status)
        TextView tvCardStatus;
        @BindView(R.id.tvProgress)
        TextView tvProgress;
        @BindView(R.id.iv_type)
        ImageView ivType;
        @BindView(R.id.progress)
        RoundCornerProgressBar progress;
        @BindView(R.id.tv_multi)
        TextView tvMulti;
        @BindView(R.id.tv_extinguished_sum)
        TextView tvExtinguishedSumForCredit;
        @BindView(R.id.text_view_hide_balance)
        TextView tvHideBalance;
        @BindView(R.id.image_default_card)
        ImageView imageViewDefaultCard;
        @BindView(R.id.account_amount)
        ConstraintLayout cardConstraintLayout;

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                    Log.d("proverka ejje", "clicked__________________");
                }
            });
        }
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header)
        TextView tvName;

        private VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }    }

    public class VHFooter extends RecyclerView.ViewHolder {
        @BindView(R.id.btnFooter)
        Button btnFooter;

        private VHFooter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHFooterRequest extends RecyclerView.ViewHolder {
        private VHFooterRequest(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public class VHAccVisi extends RecyclerView.ViewHolder {
        private VHAccVisi(View itemView) {
            super(itemView);
        }
    }

    public class VHSpacing extends RecyclerView.ViewHolder {
        @BindView(R.id.view)
        View view;

        private VHSpacing(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) context.getResources().getDimension(R.dimen.app_bar_height);
            params.width = RecyclerView.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
        }
    }

    public interface AnchorViewListener{
        void onPassAnchorView(ImageView imageView);
    }
}