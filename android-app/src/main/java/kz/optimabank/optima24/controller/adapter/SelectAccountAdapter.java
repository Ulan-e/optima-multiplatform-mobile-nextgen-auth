package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.utility.Constants.CARD;
import static kz.optimabank.optima24.utility.Constants.CARD_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CURRENT_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.DEPOSIT_ACCOUNT;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kg.optima.mobile.R;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

/**
 * Created by Timur on 18.04.2017.
 */

public class SelectAccountAdapter extends ArrayAdapter<UserAccounts> {
    private final LayoutInflater inflater;
    ArrayList<UserAccounts> data;
    private final Animation anim = new AlphaAnimation(0.2f, 1.0f);
    public String userAccFrom;
    public boolean depVos;
    Context context;

    public SelectAccountAdapter(Context context, ArrayList<UserAccounts> data) {
        super(context, 0, data);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;

    }

    public void setAcc(String accFrom) {// TODO: 09.02.2021 Получение бизнес карты
        this.userAccFrom = accFrom;
    }

    public void setDepVos(boolean depV) {// TODO: 09.02.2021 Получение текущей карты в деп и до вос из поля ввода(куда)
        this.depVos = depV;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (getItem(position).code == Constants.HEADER_ID) {
                return Constants.HEADER_ID;
            } else if (getItem(position).code == Constants.NEW_CARD_ID) {
                return Constants.NEW_CARD_ID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.ITEM_ID;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        if (getItemViewType(data.size() - 1) == Constants.HEADER_ID) {
            return data.size() - 1;
        }
        return data.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != Constants.HEADER_ID;
    }

    private View rowView(int position, View convertView, ViewGroup parent) {
        final int type = getItemViewType(position);
        UserAccounts account = getItem(position);
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            if (account != null && type == Constants.HEADER_ID) {
                convertView = inflater.inflate(R.layout.account_header, parent, false);
                holder.textviewName = convertView.findViewById(R.id.tv_header);

            } else if (account != null && type == Constants.NEW_CARD_ID) {
                convertView = inflater.inflate(R.layout.text_for_list, parent, false);
                holder.textviewName = convertView.findViewById(R.id.textForList);
            } else {
                convertView = inflater.inflate(R.layout.item_card_details, parent, false);
                holder.tv_validate_ending = convertView.findViewById(R.id.tv_validate_ending);
                holder.textviewName = convertView.findViewById(R.id.textview_account_name);
                holder.textviewBalance = convertView.findViewById(R.id.textview_account_balance);
                holder.textviewNumber = convertView.findViewById(R.id.textview_account_number);
                holder.ivType = convertView.findViewById(R.id.iv_type);
                holder.tvMulti = convertView.findViewById(R.id.tv_multi);
                holder.tvCardStatus = convertView.findViewById(R.id.tv_card_status);
                holder.account_amount_linear = convertView.findViewById(R.id.account_amount);
            }

            convertView.setTag(holder);
        }
        if (account != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (type == Constants.HEADER_ID) {
                holder.textviewName.setTextSize(16f);
                holder.textviewName.setText(account.name);
                return convertView;
            }

            if (type == Constants.NEW_CARD_ID) {
                holder.textviewName.setText(account.name);
                return convertView;
            }

            holder.tv_validate_ending.setVisibility(View.GONE);
            holder.tv_validate_ending.clearAnimation();

            holder.tvCardStatus.setTextColor(Color.BLACK);
            if (account instanceof UserAccounts.WishAccounts) {
                holder.textviewName.setText(((UserAccounts.WishAccounts) account).wishName);
                holder.textviewNumber.setText(((UserAccounts.WishAccounts) account).number);
            } else {
                    holder.textviewName.setText(account.name);
                    holder.textviewNumber.setText(account.number);
                }

            holder.textviewBalance.setText(account.getFormattedBalance(context));
            holder.tvCardStatus.setVisibility(View.GONE);
            holder.textviewBalance.setTextColor(context.getResources().getColor(R.color.gray_atf));

            int imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
            int imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            holder.ivType.setLayoutParams(lp);

            Log.i("accountType", "accountType = " + account.accountType);
            if (account.accountType == CARD) {
                UserAccounts.Cards cardAccount = (UserAccounts.Cards) getItem(position);

                if (cardAccount != null) {
                    Calendar calendar = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    try {
                        calendar.setTime(cardAccount.getCardExpireDate());
                        calendar.add(Calendar.MONTH, -1);

                        if (c.getTime().after(calendar.getTime()) && c.getTime().before(cardAccount.getCardExpireDate())) {
                            holder.tv_validate_ending.setVisibility(View.VISIBLE);
                            anim.setDuration(350);
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            holder.tv_validate_ending.startAnimation(anim);
                        }
                    } catch (Exception ignored) {
                        calendar.setTime(new Date(0000, 0, 0));
                        calendar.add(Calendar.MONTH, -1);

                        if (c.getTime().after(calendar.getTime()) && c.getTime().before(new Date(0000, 0, 0))) {
                            holder.tv_validate_ending.setVisibility(View.VISIBLE);
                            anim.setDuration(350);
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            holder.tv_validate_ending.startAnimation(anim);
                        }
                    }

                    int imageWidthc = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources().getDisplayMetrics());
                    int imageHeightc = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams lpc = new LinearLayout.LayoutParams(imageWidthc, imageHeightc);
                    holder.ivType.setLayoutParams(lpc);

                    byte[] min = cardAccount.getByteArrayMiniatureImg();
                    if (min != null)
                        Utilities.setCardToImageView(cardAccount, holder.ivType, holder.tvMulti, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                    else
                        Utilities.setCardToImageView(cardAccount, holder.ivType, holder.tvMulti, BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left));
                }

            } else if (account.accountType == DEPOSIT_ACCOUNT) {
                holder.tvMulti.setVisibility(View.GONE);
                holder.ivType.setImageResource(R.drawable.ic_image_dark_account_depo);
            } else if (account.accountType == CARD_ACCOUNT || account.accountType == CURRENT_ACCOUNT) {
                holder.tvMulti.setVisibility(View.GONE);
                holder.tvCardStatus.setVisibility(View.VISIBLE);
                if (account.accountType == 2) {
                    holder.tvCardStatus.setText(R.string.current_card_accounts);
                } else if (account.accountType == 1) {
                    holder.tvCardStatus.setText(R.string.current_account_type);
                }
                holder.ivType.setImageResource(R.drawable.ic_image_dark_account_current);

            }
        }

        return convertView;
    }

    private static class ViewHolder {
        public LinearLayout account_amount_linear;
        public TextView textviewName;
        public TextView textviewBalance;
        public TextView textviewNumber, tvCardStatus;
        public ImageView ivType;
        public TextView tvMulti;
        public TextView tv_validate_ending;
    }
}
