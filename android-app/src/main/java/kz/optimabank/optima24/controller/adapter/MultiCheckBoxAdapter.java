package kz.optimabank.optima24.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;


public class MultiCheckBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ApplicationTypeDto.DropdownData> objects;
    private final List<ObserverCheckBox> mObserverCheckBoxes;
    private final ApplCreateAdapter.MultiselectViewHolder parentViewHolder;
    private final boolean checkable;

    public MultiCheckBoxAdapter(@NonNull List<ApplicationTypeDto.DropdownData> objects, ApplCreateAdapter.MultiselectViewHolder parentViewHolder, boolean checkable) {
        this.objects = objects;
        this.parentViewHolder = parentViewHolder;
        mObserverCheckBoxes = new ArrayList<>();
        this.checkable = checkable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.application_multiselect_checkbox_item, parent, false);
        return new CheckBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CheckBoxViewHolder mHolder = (CheckBoxViewHolder) holder;
        mHolder.mDropdownData = objects.get(position);
        mHolder.checkbox.setText(objects.get(position).Value);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public String getResult() {
        String res = "";
        for (ObserverCheckBox observerCheckBox : mObserverCheckBoxes) {
            if (observerCheckBox.getChecked() != null) {
                if (res.isEmpty()){
                    res+= observerCheckBox.getChecked();
                }else {
                    res += "|" + observerCheckBox.getChecked();
                }
            }
        }
        return res;
    }

    class CheckBoxViewHolder extends RecyclerView.ViewHolder implements ObserverCheckBox{

        @BindView(R.id.checkbox) CheckBox checkbox;

        private ApplicationTypeDto.DropdownData mDropdownData;

        CheckBoxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (checkable) {
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            MultiCheckBoxAdapter.this.parentViewHolder.onCheckboxChecked();
                        }
                    }
                });
                mObserverCheckBoxes.add(this);
            } else {
                checkbox.setClickable(false);
            }
        }

        @Override
        public String getChecked() {
            if (checkbox.isChecked()) {
                return mDropdownData.Value;
            }
            return null;
        }
    }

    interface ObserverCheckBox {
        String getChecked();
    }
}
