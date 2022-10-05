package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.CARD;
import static kz.optimabank.optima24.utility.Constants.CARD_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CREDIT_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CURRENT_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.DEPOSIT_ACCOUNT;

/**
  Created by Timur on 05.07.2017.
 */

public class AccountVisibilityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<UserAccounts> data;
    Context context;
    private final ArrayList<UserAccounts> dataChange = new ArrayList<>();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REGULAR = 1;

    public AccountVisibilityAdapter(Context context, ArrayList<UserAccounts> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new VHHeader(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_visibility_item, parent, false);
            viewHolder = new VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        UserAccounts account = null;
        if(data!=null&&!data.isEmpty()) {
            account = data.get(position);
        }
        if(account!=null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader)mHolder;
                holder.tvName.setText(account.name);
            } else {
                VHItem holder = (VHItem) mHolder;

                holder.tvName.setText(account.name);
                holder.tvNumber.setVisibility(View.VISIBLE);
                holder.tvNumber.setText(account.number);
                holder.linAccVisItem.setPadding(0,0,0,0);

                Log.i("account.isVisibility()","account.isVisibility() = "+account.isVisibility());
                if(account.isVisibility()) {
                    holder.imgVisibility.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_invision));
                } else {
                    holder.imgVisibility.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vision));
                }

                int imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
                int imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                holder.ivType.setLayoutParams(lp);

                if (account.accountType == CARD) {
                    UserAccounts.Cards card = (UserAccounts.Cards) data.get(position);
                    holder.linAccVisItem.setPadding(60,0,0,0);

                    if (card != null) {
                        holder.tvCardStatus.setVisibility(View.GONE);

                        if (card.isBlocked) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.card_is_blocked);
                        }
                        if (card.isClosed) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.card_is_closed);
                        }

                        int imageWidthForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources().getDisplayMetrics());
                        int imageHeightForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams lpc = new LinearLayout.LayoutParams(imageWidthForCard, imageHeightForCard);
                        holder.ivType.setLayoutParams(lpc);

                        byte[] min = card.getByteArrayMiniatureImg();
                        if (min!=null)
                            Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                        else
                            Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, BitmapFactory.decodeResource(context.getResources(),R.drawable.arrow_left));
                        //try {
                        //    Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, card.readObject(false));//card.miniatureIm
                        //} catch (IOException | ClassNotFoundException e) {
                        //    e.printStackTrace();
                        //    Utilities.setCardToImageView(card, holder.ivType, holder.tvMulti, card.miniatureIm);
                        //}
                    }

                } else if (account.accountType == CREDIT_ACCOUNT) {
                    holder.tvMulti.setVisibility(View.GONE);
                    holder.linAccVisItem.setPadding(0,0,0,0);
                    UserAccounts.CreditAccounts creditAccount = (UserAccounts.CreditAccounts) account;
                    holder.ivType.setImageResource(R.drawable.money);
                    holder.tvCardStatus.setVisibility(View.GONE);

                    String nextPaymentDate = creditAccount.getNextPaymentDate();
                    if(nextPaymentDate!=null) {
                        holder.tvNumber.setText(context.getResources()
                                .getString(R.string.next_payment)+" "+nextPaymentDate);
                    } else {
                        holder.tvNumber.setVisibility(View.GONE);
                    }

                    if (creditAccount.isClosed) {
                        holder.tvCardStatus.setVisibility(View.VISIBLE);
                        holder.tvCardStatus.setText(R.string.loan_is_closed);
                    }

                } else if (account.accountType == CURRENT_ACCOUNT) {
                    holder.tvMulti.setVisibility(View.GONE);
                    holder.linAccVisItem.setPadding(0,0,0,0);
                    UserAccounts.CheckingAccounts checkingAccounts = (UserAccounts.CheckingAccounts) account;
                    /*if (checkingAccounts.currency.equals("KZT")){
                        holder.ivType.setImageResource(R.drawable.tenge_2);
                    } else if (checkingAccounts.currency.equals("USD")){
                        holder.ivType.setImageResource(R.drawable.usd_2);
                    } else if (checkingAccounts.currency.equals("RUB")){
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

                } else if(account.accountType == CARD_ACCOUNT) {
                    holder.tvMulti.setVisibility(View.GONE);
                    holder.linAccVisItem.setPadding(0,0,0,0);
                    UserAccounts.CardAccounts cardAccounts = (UserAccounts.CardAccounts) account;
                    /*if (cardAccounts.currency.equals("KZT")){
                        holder.ivType.setImageResource(R.drawable.tenge_2);
                    } else if (cardAccounts.currency.equals("USD")){
                        holder.ivType.setImageResource(R.drawable.usd_2);
                    } else if (cardAccounts.currency.equals("RUB")){
                        holder.ivType.setImageResource(R.drawable.rub_2);
                    } else if (cardAccounts.currency.equals("GBP")){
                        holder.ivType.setImageResource(R.drawable.gbp_2);
                    } else if (cardAccounts.currency.equals("CNY")){
                        holder.ivType.setImageResource(R.drawable.cny_2);
                    } else if (cardAccounts.currency.equals("EUR")){
                        holder.ivType.setImageResource(R.drawable.eur_2);
                    } else {*/
                        holder.ivType.setImageResource(R.drawable.ic_image_dark_account_current);//bank_account
                    //}
                    holder.tvCardStatus.setVisibility(View.GONE);
                    if (cardAccounts.isClosed) {
                        holder.tvCardStatus.setVisibility(View.VISIBLE);
                        holder.tvCardStatus.setText(R.string.loan_is_closed);
                    }
                } else if (account.accountType == DEPOSIT_ACCOUNT) {
                    holder.tvMulti.setVisibility(View.GONE);
                    holder.linAccVisItem.setPadding(0,0,0,0);
                    UserAccounts.DepositAccounts depositAccount = (UserAccounts.DepositAccounts) account;
                    if(!depositAccount.isWish) {
                        holder.tvCardStatus.setVisibility(View.GONE);
                        if (depositAccount.isClosed) {
                            holder.tvCardStatus.setVisibility(View.VISIBLE);
                            holder.tvCardStatus.setText(R.string.loan_is_closed);
                        }
                            holder.ivType.setImageResource(R.drawable.ic_image_dark_account_depo);//ic_ico_dep
                    }
                }
                if(account instanceof UserAccounts.WishAccounts){
                    UserAccounts.WishAccounts wishDepositAccount = (UserAccounts.WishAccounts) account;
                    if(wishDepositAccount.isClosed) {
                        holder.tvCardStatus.setVisibility(View.VISIBLE);
                        holder.tvCardStatus.setText(R.string.loan_is_closed);
                    } else {
                        holder.tvCardStatus.setVisibility(View.GONE);
                    }
                    holder.ivType.setImageResource(R.drawable.ic_image_dark_account_goal);
                    holder.tvName.setText(wishDepositAccount.wishName);
//                    UserAccounts.CheckingAccounts c = (UserAccounts.CheckingAccounts) GeneralManager.getInstance().getAccountByCode(wishDepositAccount.accountCode);
                    holder.tvNumber.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data!=null && !data.isEmpty() && data.get(position).code == Constants.HEADER_ID) {
            return TYPE_HEADER;
        }
        return TYPE_REGULAR;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_account_name) TextView tvName;
        @BindView(R.id.textview_account_number) TextView tvNumber;
        @BindView(R.id.tv_card_status) TextView tvCardStatus;
        @BindView(R.id.iv_type) ImageView ivType;
        @BindView(R.id.imgVisibility) ImageView imgVisibility;
        @BindView(R.id.linAccVisItem) LinearLayout linAccVisItem;
        @BindView(R.id.tv_multi) TextView tvMulti;

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data!=null && !data.isEmpty()) {
                        UserAccounts accounts = data.get(getAdapterPosition());

                        if(accounts.isVisibility()) {
                            accounts.setVisibility(false);
                            imgVisibility.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vision));
                        } else {
                            accounts.setVisibility(true);
                            imgVisibility.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_invision));
                        }

                        if(accounts.isVisible != accounts.isVisibility()) {
                            dataChange.add(accounts);
                        } else {
                            deleteAccount(accounts);
                        }

                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }
    }

    private void deleteAccount(UserAccounts accounts) {
        Iterator<UserAccounts> it = dataChange.iterator();
        while (it.hasNext()) {
            UserAccounts userAccount = it.next();
            if(userAccount == accounts) {
                it.remove();
            }
        }
    }

    public ArrayList<UserAccounts> getDataChange() {
        Log.i("dataChange","dataChange = " + dataChange);
        return dataChange;
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header) TextView tvName;

        private VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
