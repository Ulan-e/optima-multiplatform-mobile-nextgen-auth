package kz.optimabank.optima24.controller.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.model.gson.response.Data;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> { //Адаптер для Карт
    ArrayList<Data> interfaceViewProgramLoyalty;
    CardClickListener listener;

    public CardsAdapter(CardClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loyalty, parent, false);
        return new CardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        holder.onBind(interfaceViewProgramLoyalty.get(position));
    }

    @Override
    public int getItemCount() {
        return interfaceViewProgramLoyalty.size();
    }

    public void setData(ArrayList<Data> listCards) { // getData from AccountListFragment
        this.interfaceViewProgramLoyalty = listCards;
        notifyDataSetChanged();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {
        TextView bonuses_bank_tv, numbers_tv, bank_amount_tv, scores_amount_tv;
        ImageView card_image;

        public CardsViewHolder(@NonNull View itemView) { // init views
            super(itemView);
            bonuses_bank_tv = itemView.findViewById(R.id.bonuses_bank_tv);
            numbers_tv = itemView.findViewById(R.id.numbers_tv);
            bank_amount_tv = itemView.findViewById(R.id.bonus_amount_tv);
            scores_amount_tv = itemView.findViewById(R.id.scores_amount_tv);
            card_image = itemView.findViewById(R.id.card_image);
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Data cardModel) { // заполнение данные с модельки
            bonuses_bank_tv.setText(cardModel.getName());
            numbers_tv.setText(cardModel.getInterfaceView().get(0).getTitle());
            bank_amount_tv.setText(cardModel.getInterfaceView().get(0).getInterfaceFormsData().get(0).getMask());
            String []image = {"https://previews.123rf.com/images/stalkerstudent/stalkerstudent1512/stalkerstudent151200070/48801441-bank-icon-in-flat-style-with-the-building-facade-with-three-pillars-illustration.jpg","https://icons-for-free.com/iconfiles/png/512/finance+mango+piggy+bank-131979031755696834.png"};
            for (int i = 0; i <image.length ; i++) {
                Picasso.with(card_image.getContext()).load(Uri.parse(image[i])).into(card_image);
            }
            scores_amount_tv.setText(cardModel.getInterfaceView().get(0).getInterfaceFormsData().get(0).getMaxLength().toString());
            itemView.setOnClickListener(new View.OnClickListener() {  //init listener
                @Override
                public void onClick(View v) {
                    listener.onCardClick(cardModel);
                }
            });
        }
    }

    public interface CardClickListener { // Слушатель Клика на item и служить для передачи объекта по клику
        void onCardClick(Data cardModel);
    }
}

