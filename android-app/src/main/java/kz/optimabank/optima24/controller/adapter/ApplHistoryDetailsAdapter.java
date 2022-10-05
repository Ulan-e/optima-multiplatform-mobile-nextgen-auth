package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.fragment.requests.ApplicationParamsFragment;
import kz.optimabank.optima24.model.base.HistoryDetailsApplications;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

public class ApplHistoryDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ApplHistoryDetailsAdapter.class.getSimpleName();

    private static final int EDIT_TEXT_TYPE = 1;
    private static final int DATE_TYPE = 4;
    private static final int SPINNER_TYPE = 5;
    private static final int MULTISELECT_TYPE = 6;
    private static final int CHECKBOX_TYPE = 7;
    private static final int HIDDEN_TYPE = 8;
    private static final int CURRENT_DATE_TYPE = 9;
    private static final int COMPOUND_SPINNER_TYPE = 10;
    private static final int CLIENT_NAME_TYPE = 100;
    private static final int CLIENT_IDN_TYPE = 101;
    private static final int CLIENT_PHONE_TYPE = 102;
    private static final int CLIENT_SEX_TYPE = 104;
    private static final int CLIENT_DATE_OF_BIRTH_TYPE = 105;
    private static final int CLIENT_ACCOUNTS_TYPE = 106;


    private static final int ALL_TYPE = 111;
    private static final int RECORD_TYPE = 122;
    private static final int CANCEL_TYPE = 133;
    private static final int BAND_TYPE = 144;

    private HistoryDetailsApplications mHistoryDetailsApplications;
    private ArrayList<HistoryDetailsApplications.Types> mHistoryDetailsTypes;
    private ArrayList<HistoryDetailsApplications.Records> mHistoryDetailsRecords;
    private ApplicationParamsFragment fragment;
    private Context context;
    private OnItemClickListener onItemClickListener;
    String band = "BAND";

    public ApplHistoryDetailsAdapter(ApplicationParamsFragment fragment, HistoryDetailsApplications historyDetailsApplications, Context context, OnItemClickListener onItemClickListener) {
        if (historyDetailsApplications != null) {
            mHistoryDetailsApplications = historyDetailsApplications;
            mHistoryDetailsTypes = historyDetailsApplications.Types;
            mHistoryDetailsRecords = historyDetailsApplications.Records;
            this.context = context;
            this.onItemClickListener = onItemClickListener;

            this.fragment = fragment;
            if (mHistoryDetailsTypes != null)
                for (HistoryDetailsApplications.Types HistoryDetailsTypes : mHistoryDetailsTypes) {
                    if (HistoryDetailsTypes.Type == HIDDEN_TYPE) {
                        mHistoryDetailsTypes.remove(HistoryDetailsTypes);
                    }
                }
            mHistoryDetailsTypes.add(0, new HistoryDetailsApplications.Types(mHistoryDetailsApplications.Name,
                    String.valueOf(historyDetailsApplications.id)));
            mHistoryDetailsTypes.add(1, new HistoryDetailsApplications.Types(context.getString(R.string.application_number2),
                    String.valueOf(historyDetailsApplications.id)));
            mHistoryDetailsTypes.add(2, new HistoryDetailsApplications.Types(context.getString(R.string.application_date2),
                    historyDetailsApplications.Date.replace("T", " ")));
            mHistoryDetailsTypes.add(3, new HistoryDetailsApplications.Types(context.getString(R.string.status),
                    "TEST"));


            mHistoryDetailsTypes.add(4, new HistoryDetailsApplications.Types(band,band));


            mHistoryDetailsTypes.add(mHistoryDetailsTypes.size(), new HistoryDetailsApplications.Types(context.getString(R.string.payments_history),
                    String.valueOf(historyDetailsApplications.id)));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i(TAG, "onCreateViewHolder viewType = " + viewType);
        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
           /* case EDIT_TEXT_TYPE:
                view = inflater.inflate(R.layout.application_edit_text_item, viewGroup, false);
                return new EditTextViewHolder(view);
            case SPINNER_TYPE:
                view = inflater.inflate(R.layout.application_spinner_item, viewGroup, false);
                return new SpinnerViewHolder(view);
            case CHECKBOX_TYPE:
                view = inflater.inflate(R.layout.application_checkbox_item, viewGroup, false);
                return new CheckboxViewHolder(view);
            case CURRENT_DATE_TYPE:
                view = inflater.inflate(R.layout.application_current_date_item, viewGroup, false);
                return new CurrentDateViewHolder(view);
            case COMPOUND_SPINNER_TYPE:
                view = inflater.inflate(R.layout.application_compound_spinner_item, viewGroup, false);
                return new CompoundSpinnerViewHolder(view);
            case CLIENT_PHONE_TYPE:
                view = inflater.inflate(R.layout.application_client_phone_item, viewGroup, false);
                return new ClientPhoneViewHolder(view);
            case MULTISELECT_TYPE:
                view = inflater.inflate(R.layout.application_multiselect_item, viewGroup, false);
                return new MultiselectViewHolder(view);*/
            case ALL_TYPE:
                view = inflater.inflate(R.layout.application_history_item, viewGroup, false);
                return new AllViewHolder(view);
            case RECORD_TYPE:
                view = inflater.inflate(R.layout.application_history_record_item, viewGroup, false);
                return new RecordViewHolder(view);
            case CANCEL_TYPE:
                view = inflater.inflate(R.layout.application_cancel_button, viewGroup, false);
                return new CancelViewHolder(view);
            case BAND_TYPE:
                view = inflater.inflate(R.layout.application_history_band_item, viewGroup, false);
                return new BandViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        /*BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
        baseViewHolder.mHistoryDetalsType = HistoryDetailsTypes;
        if (baseViewHolder.paramName != null) {
            baseViewHolder.paramName.setText(HistoryDetailsTypes.Name);
        }
*/
        switch (getItemViewType(pos)) {
            case ALL_TYPE:
                HistoryDetailsApplications.Types HistoryDetailsTypes = mHistoryDetailsTypes.get(pos);
                AllViewHolder allViewHolder = (AllViewHolder) viewHolder;
                allViewHolder.textInfo.setText(HistoryDetailsTypes.Name);
                allViewHolder.text.setText(HistoryDetailsTypes.Value);
                //if (HistoryDetailsTypes.Value.contains("True")){
                if (allViewHolder.text.getText().toString().contains("True")){
                    allViewHolder.text.setText(context.getString(R.string._yes));
                } else if (allViewHolder.text.getText().toString().contains("False"))//HistoryDetailsTypes.Value.contains("False")
                    allViewHolder.text.setText(context.getString(R.string.Cancel));
                if (pos==0){
                    allViewHolder.text.setVisibility(View.GONE);
                    allViewHolder.textInfo.setTextSize(17);
                    allViewHolder.textInfo.setTextColor(ContextCompat.getColor(context,R.color.blue_0_93_186));
                    allViewHolder.linAll.setBackgroundColor(ContextCompat.getColor(context,R.color.white_240_240_240));//white_240_240_240
                }
                if (pos==mHistoryDetailsTypes.size()-1){
                    allViewHolder.text.setVisibility(View.GONE);
                    allViewHolder.textInfo.setTextSize(17);
                    allViewHolder.textInfo.setTextColor(ContextCompat.getColor(context,R.color.blue_0_93_186));
                    allViewHolder.linAll.setBackgroundColor(ContextCompat.getColor(context,R.color.white_240_240_240));//white_240_240_240
                }
                if (pos == 3){
                    if (!mHistoryDetailsApplications.Status.isEmpty()) {
                        allViewHolder.text.setText(mHistoryDetailsApplications.Status);
                        allViewHolder.text.setTextColor(Color.parseColor(mHistoryDetailsApplications.StatusClass));
                       /* switch (Integer.valueOf(mHistoryDetailsApplications.Status)) {
                            case 0:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.green_19_136_52));
                                break;
                            case 1:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.red_attention));
                                break;
                            case 2:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.blue_6_83_161));
                                break;
                            case 3:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.orange_246_121_37));
                                break;
                            case 4:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.black));
                                break;
                            case 5:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.gray_atf_));
                                break;
                            default:
                                allViewHolder.text.setTextColor(ContextCompat.getColor(context, R.color.green_19_136_52));
                                break;
                        }*/
                    }
                }
                break;

            case RECORD_TYPE:
                RecordViewHolder recordViewHolder = (RecordViewHolder) viewHolder;
                recordViewHolder.text.setText(mHistoryDetailsRecords.get(pos - mHistoryDetailsTypes.size()).Comment);
                recordViewHolder.textStatus.setText(mHistoryDetailsRecords.get(pos - mHistoryDetailsTypes.size()).Status);
                recordViewHolder.textTime.setText(mHistoryDetailsRecords.get(pos - mHistoryDetailsTypes.size()).Date);
                break;
            /*case EDIT_TEXT_TYPE:
                EditTextViewHolder editTextViewHolder = (EditTextViewHolder) viewHolder;
                editTextViewHolder.paramHint.setHint(HistoryDetailsTypes.Name);
                if (HistoryDetailsTypes.Value != null && !HistoryDetailsTypes.Value.isEmpty()) {
                    editTextViewHolder.paramField.setText(HistoryDetailsTypes.Value);
                }
                break;
            case CHECKBOX_TYPE:
                CheckboxViewHolder checkboxViewHolder = (CheckboxViewHolder) viewHolder;
                checkboxViewHolder.mCheckBox.setText(HistoryDetailsTypes.Name);
                try {
                checkboxViewHolder.mCheckBox.setChecked(Boolean.valueOf(HistoryDetailsTypes.Value));
                } catch (Exception ignored) {}
                break;
            case CLIENT_PHONE_TYPE:
                ClientPhoneViewHolder clientPhoneViewHolder = (ClientPhoneViewHolder) viewHolder;
                clientPhoneViewHolder.clientPhoneHint.setHint(HistoryDetailsTypes.Name);
                if (HistoryDetailsTypes.Value != null && !HistoryDetailsTypes.Value.isEmpty()) {
                    clientPhoneViewHolder.clientPhone.setText(HistoryDetailsTypes.Value);
                }
                break;
            case SPINNER_TYPE:
            case CLIENT_SEX_TYPE:
                SpinnerViewHolder spinnerViewHolder = (SpinnerViewHolder) viewHolder;
                spinnerViewHolder.paramHint.setHint(HistoryDetailsTypes.Name);
                if (HistoryDetailsTypes.Value != null && !HistoryDetailsTypes.Value.isEmpty()) {
                    spinnerViewHolder.selectedParam.setText(HistoryDetailsTypes.Value);
                }
                break;
            case COMPOUND_SPINNER_TYPE:
                CompoundSpinnerViewHolder compoundSpinnerViewHolder = (CompoundSpinnerViewHolder) viewHolder;
                compoundSpinnerViewHolder.parentBranchHint.setHint(HistoryDetailsTypes.Name);
                compoundSpinnerViewHolder.childBranchHint.setHint(HistoryDetailsTypes.Name);
//              TODO  split value
                break;
            case MULTISELECT_TYPE:
                MultiselectViewHolder multiselectViewHolder = (MultiselectViewHolder) viewHolder;
                if (multiselectViewHolder.recyclerView.getAdapter() == null) {
                    if (HistoryDetailsTypes.Value != null && !HistoryDetailsTypes.Value.isEmpty()) {
                        String[] values = HistoryDetailsTypes.Value.split("|");
                        List<ApplicationTypeDto.DropdownData> dropdownDatas = new ArrayList<>();
                        for (String value : values) {
                            dropdownDatas.add(new ApplicationTypeDto.DropdownData(value));
                        }
                        multiselectViewHolder.mMultiCheckBoxAdapter = new MultiCheckBoxAdapter(dropdownDatas, null, false);
                        multiselectViewHolder.recyclerView.setAdapter(multiselectViewHolder.mMultiCheckBoxAdapter);
                    }
                }
                break;
            case CURRENT_DATE_TYPE:
                CurrentDateViewHolder currentDateViewHolder = (CurrentDateViewHolder) viewHolder;
                if (HistoryDetailsTypes.Value != null && !HistoryDetailsTypes.Value.isEmpty()) {
                    currentDateViewHolder.tvCurrentDate.setText(HistoryDetailsTypes.Value);
                }
                break;*/
        }
    }

    @Override
    public int getItemCount() {
        Log.i("AHDA","mHistoryDetailsTypes.size() = "+ mHistoryDetailsTypes.size());
        Log.i("AHDA","mHistoryDetailsRecords.size() = "+ mHistoryDetailsRecords.size());
        if (!mHistoryDetailsApplications.Cancel)
            return mHistoryDetailsTypes.size() + mHistoryDetailsRecords.size();
        else
            return mHistoryDetailsTypes.size() + mHistoryDetailsRecords.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("AHDA","position = "+ position);
        /*switch (mHistoryDetailsTypes.get(position).Type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case CLIENT_NAME_TYPE:
            case CLIENT_IDN_TYPE:
            case CLIENT_DATE_OF_BIRTH_TYPE:
            case CLIENT_ACCOUNTS_TYPE:
                //return EDIT_TEXT_TYPE;
            case SPINNER_TYPE:
            case CLIENT_SEX_TYPE:
                //return SPINNER_TYPE;
            case MULTISELECT_TYPE:
                //return MULTISELECT_TYPE;
            case CHECKBOX_TYPE:
                //return CHECKBOX_TYPE;
            case CURRENT_DATE_TYPE:
            case DATE_TYPE:
                //return CURRENT_DATE_TYPE;
            case COMPOUND_SPINNER_TYPE:
                //return COMPOUND_SPINNER_TYPE;
            case CLIENT_PHONE_TYPE:
                //return CLIENT_PHONE_TYPE;
        }*/
        try {
            if (mHistoryDetailsTypes.size() >= position + 1)
                if (mHistoryDetailsTypes.get(position).Value.contains(band))
                    return BAND_TYPE;
        }catch (Exception ignored){}
        if (position < mHistoryDetailsTypes.size())
            return ALL_TYPE;
        else if (position == mHistoryDetailsTypes.size() + mHistoryDetailsRecords.size()) //for cancel button
            return CANCEL_TYPE;
        else
            return RECORD_TYPE;
    }



    /*abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.param_name)
        TextView paramName;

        HistoryDetailsApplications.Types mHistoryDetalsType;

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class EditTextViewHolder extends BaseViewHolder {

        @BindView(R.id.param_text_field)
        EditText paramField;
        @BindView(R.id.param_text_hint)
        TextInputLayout paramHint;

        public EditTextViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "EditTextViewHolder init");
            ButterKnife.bind(this, itemView);
            paramField.setKeyListener(null);
            paramField.setFocusable(false);
        }
    }

    class SpinnerViewHolder extends BaseViewHolder {

        @BindView(R.id.selected_param_hint)
        TextInputLayout paramHint;
        @BindView(R.id.selected_param)
        EditText selectedParam;

        public SpinnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            selectedParam.setKeyListener(null);
            selectedParam.setFocusable(false);
        }
    }

    class MultiselectViewHolder extends BaseViewHolder {

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        private MultiCheckBoxAdapter mMultiCheckBoxAdapter;

        public MultiselectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.setHasFixedSize(true);
        }
    }

    class CheckboxViewHolder extends BaseViewHolder {

        @BindView(R.id.checkbox)
        CheckBox mCheckBox;

        public CheckboxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCheckBox.setClickable(false);
        }
    }

    class ClientPhoneViewHolder extends BaseViewHolder {

        @BindView(R.id.client_phone)
        EditText clientPhone;
        @BindView(R.id.client_phone_hint)
        TextInputLayout clientPhoneHint;

        public ClientPhoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            MaskedTextChangedListener maskedTextChangedListener = new MaskedTextChangedListener(
                    "+[0] ([000]) [000]-[00]-[00]",
                    true,
                    clientPhone,
                    null,
                    new MaskedTextChangedListener.ValueListener() {
                        @Override
                        public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                            if (!extractedValue.isEmpty()) {
                                clientPhone.setError(null);
                            }
                        }
                    }
            );
            clientPhone.addTextChangedListener(maskedTextChangedListener);
            clientPhone.setKeyListener(null);
            clientPhone.setFocusable(false);
        }
    }

    class CurrentDateViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_current_date)
        TextView tvCurrentDate;

        public CurrentDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CompoundSpinnerViewHolder extends BaseViewHolder {

        @BindView(R.id.parent_branch_layout)
        View parentBranchLayout;
        @BindView(R.id.parent_branch_hint)
        TextInputLayout parentBranchHint;
        @BindView(R.id.parent_branch_field)
        EditText parentBranchField;
        @BindView(R.id.child_branch_layout)
        View childBranchLayout;
        @BindView(R.id.child_branch_hint)
        TextInputLayout childBranchHint;
        @BindView(R.id.child_branch_field)
        EditText childBranchField;

        public CompoundSpinnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentBranchField.setKeyListener(null);
            parentBranchField.setFocusable(false);
            childBranchField.setKeyListener(null);
            childBranchField.setFocusable(false);
        }
    }*/

    class AllViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textInfo) TextView textInfo;
        @BindView(R.id.text) TextView text;
        @BindView(R.id.linAll) LinearLayout linAll;

        public AllViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CancelViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linCan) LinearLayout linCan;

        public CancelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }}
            );
        }
    }

    class BandViewHolder extends RecyclerView.ViewHolder{

        public BandViewHolder(View itemView) {
            super(itemView);
        }
    }

    class RecordViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textTime) TextView textTime;
        @BindView(R.id.textStatus) TextView textStatus;
        @BindView(R.id.text) TextView text;

        public RecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
