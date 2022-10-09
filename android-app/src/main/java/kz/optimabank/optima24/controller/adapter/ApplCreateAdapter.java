package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.VIEW_DATE_FORMAT;
import static kz.optimabank.optima24.utility.Utilities.getLayoutParamsForImageSize;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.fragment.CustomListFragment;
import kz.optimabank.optima24.fragment.requests.ApplicationParamsFragment;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.service.HistoryApllicationsImpl;
import kz.optimabank.optima24.utility.Utilities;

public class ApplCreateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ApplCreateAdapter.class.getSimpleName();

    private static final int HEADER_TYPE = 0;
    private static final int DESCRIPTION_TYPE = 11; //type == 1
    private static final int EDIT_TEXT_TYPE = 1;
    private static final int DATE_TYPE = 4;
    private static final int SPINNER_TYPE = 5;
    private static final int MULTISELECT_TYPE = 6;
    private static final int CHECKBOX_TYPE = 7;
    private static final int HIDDEN_TYPE = 8;
    private static final int CURRENT_DATE_TYPE = 9;
    private static final int COMPOUND_SPINNER_TYPE = 10;
    private static final int CLIENT_NAME_TYPE = 100;            //not editable
    private static final int CLIENT_IDN_TYPE = 101;             //not editable
    private static final int CLIENT_PHONE_TYPE = 102;           //not editable
    private static final int CLIENT_SEX_TYPE = 104;
    private static final int CLIENT_DATE_OF_BIRTH_TYPE = 105;   //not editable
    private static final int CLIENT_ACCOUNTS_TYPE = 106;
    private static final int FOOTER_TYPE = 200;
    private static final int CLIENT = 111;

    private ApplicationTypeDto mApplicationTypeDto;
    private List<ApplicationTypeDto.ApplicationParamModel> mApplicationParams;
    private ArrayList<ApplicationTypeDto.ApplicationParamModel> filledApplicationParams;
    private ApplicationParamsFragment fragment;
    private List<ButtonClickObservable> mObservables;
    private ActivityResultCallback mActivityResultCallback;
    private HistoryApllicationsImpl mHistoryApllicationsImpl;

    public ApplCreateAdapter(ApplicationParamsFragment fragment, ApplicationTypeDto applicationTypeDto, HistoryApllicationsImpl historyApllicationsImpl) {
        if (applicationTypeDto != null) {
            mApplicationTypeDto = applicationTypeDto;
            mApplicationParams = applicationTypeDto.Params;
            filledApplicationParams = new ArrayList<>(applicationTypeDto.Params);
            mHistoryApllicationsImpl = historyApllicationsImpl;
            this.fragment = fragment;
            mObservables = new ArrayList<>();
            for (ApplicationTypeDto.ApplicationParamModel applicationParamModel : filledApplicationParams) {
                if (applicationParamModel.Type == HIDDEN_TYPE) {
                    mApplicationParams.remove(applicationParamModel);
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i(TAG, "onCreateViewHolder viewType = " + viewType);
        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case HEADER_TYPE:
                view = inflater.inflate(R.layout.application_account_header, viewGroup, false);
                return new HeaderViewHolder(view);
            case CLIENT:
                view = inflater.inflate(R.layout.application_edit_text_item, viewGroup, false);
                return new EditTextViewHolder(view,true);
            case DESCRIPTION_TYPE:
                view = inflater.inflate(R.layout.application_edit_text_item, viewGroup, false);
                return new EditTextViewHolder(view,true);
            case EDIT_TEXT_TYPE:
                view = inflater.inflate(R.layout.application_edit_text_item, viewGroup, false);
                return new EditTextViewHolder(view,false);
            case DATE_TYPE:
                view = inflater.inflate(R.layout.application_date_item, viewGroup, false);
                return null;
                        //new SelectDateViewHolder(view);
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
            case CLIENT_ACCOUNTS_TYPE:
                view = inflater.inflate(R.layout.application_client_account_item, viewGroup, false);
                return new ClientAccountViewHolder(view);
            case MULTISELECT_TYPE:
                view = inflater.inflate(R.layout.application_multiselect_item, viewGroup, false);
                return new MultiselectViewHolder(view);
            case CLIENT_SEX_TYPE:
                view = inflater.inflate(R.layout.application_spinner_item, viewGroup, false);
                return new ClientSexViewHolder(view);
            case FOOTER_TYPE:
                view = inflater.inflate(R.layout.application_footer, viewGroup, false);
                return new FooterViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        if (getItemViewType(pos) == FOOTER_TYPE) {
            return;
        }
        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
        ApplicationTypeDto.ApplicationParamModel applicationParam = mApplicationParams.get(pos);
        baseViewHolder.mApplicationParam = applicationParam;
        if (baseViewHolder.paramName != null) {
            baseViewHolder.paramName.setText(applicationParam.Name);
        }
        if (baseViewHolder.tvHint != null) {
            if (applicationParam.Comment != null && !applicationParam.Comment.isEmpty() && !applicationParam.Name.contains(applicationParam.Comment)) {
                baseViewHolder.tvHint.setVisibility(View.VISIBLE);
                baseViewHolder.tvHint.setText(applicationParam.Comment);
            } else {
                baseViewHolder.tvHint.setVisibility(View.GONE);
            }
        }

        switch (getItemViewType(pos)) {
            case HEADER_TYPE:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                if (applicationParam.Value != null && !applicationParam.Value.isEmpty()) {
                    headerViewHolder.tvHeader.setText(applicationParam.Value);
                }
                break;
            case EDIT_TEXT_TYPE:
                EditTextViewHolder editTextViewHolder = (EditTextViewHolder) viewHolder;
                editTextViewHolder.paramHint.setHint(applicationParam.Name);
                if (applicationParam.Value != null && !applicationParam.Value.isEmpty()) {
                    editTextViewHolder.paramField.setText(applicationParam.Value);
                }
                break;
            case DESCRIPTION_TYPE:
                EditTextViewHolder editTextViewHolderDesc = (EditTextViewHolder) viewHolder;
                editTextViewHolderDesc.paramHint.setHint(applicationParam.Name);
                if (applicationParam.Value != null && !applicationParam.Value.isEmpty()) {
                    editTextViewHolderDesc.paramField.setText(applicationParam.Value);
                }
                break;
            case CLIENT:
                EditTextViewHolder editTextViewHolderClient = (EditTextViewHolder) viewHolder;
                editTextViewHolderClient.paramHint.setHint(applicationParam.Name);
                if (applicationParam.Value != null && !applicationParam.Value.isEmpty()) {
                    editTextViewHolderClient.paramField.setText(applicationParam.Value);
                }
                break;
            case CHECKBOX_TYPE:
                CheckboxViewHolder checkboxViewHolder = (CheckboxViewHolder) viewHolder;
                checkboxViewHolder.mCheckBox.setText(applicationParam.Name);
                if (mApplicationParams.get(pos).Checked)
                    checkboxViewHolder.mCheckBox.setChecked(mApplicationParams.get(pos).Checked);
                break;
            case CLIENT_PHONE_TYPE:
                ClientPhoneViewHolder clientPhoneViewHolder = (ClientPhoneViewHolder) viewHolder;
                clientPhoneViewHolder.clientPhoneHint.setHint(applicationParam.Name);
                if (applicationParam.Value != null && !applicationParam.Value.isEmpty()) {
                    clientPhoneViewHolder.clientPhone.setText(applicationParam.Value);
                }
                break;
            case SPINNER_TYPE:
            case CLIENT_SEX_TYPE:
                SpinnerViewHolder spinnerViewHolder = (SpinnerViewHolder) viewHolder;
                spinnerViewHolder.paramHint.setHint(applicationParam.Name);
                if (applicationParam.Value != null && !applicationParam.Value.isEmpty()) {
                    spinnerViewHolder.selectedParam.setText(applicationParam.Value);
                }
                break;
            case COMPOUND_SPINNER_TYPE:
                CompoundSpinnerViewHolder compoundSpinnerViewHolder = (CompoundSpinnerViewHolder) viewHolder;
                compoundSpinnerViewHolder.parentBranchHint.setHint(applicationParam.Name);
                compoundSpinnerViewHolder.childBranchHint.setHint(applicationParam.Name);
                break;
            case MULTISELECT_TYPE:
                MultiselectViewHolder multiselectViewHolder = (MultiselectViewHolder) viewHolder;
                if (multiselectViewHolder.recyclerView.getAdapter() == null) {
                    multiselectViewHolder.mMultiCheckBoxAdapter = new MultiCheckBoxAdapter(applicationParam.DropDownList, multiselectViewHolder, true);
                    multiselectViewHolder.recyclerView.setAdapter(multiselectViewHolder.mMultiCheckBoxAdapter);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mApplicationParams.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mApplicationParams.size()) {
            return FOOTER_TYPE;
        }
        switch (mApplicationParams.get(position).Type) {
            case 0:
                return HEADER_TYPE;
            case 1:
                return DESCRIPTION_TYPE;
            case 2:
            case 3:
                return EDIT_TEXT_TYPE;
            case CLIENT_DATE_OF_BIRTH_TYPE:
            case CLIENT_IDN_TYPE:
            case CLIENT_NAME_TYPE:
                return CLIENT;
            case DATE_TYPE:
                return DATE_TYPE;
            case SPINNER_TYPE:
                return SPINNER_TYPE;
            case MULTISELECT_TYPE:
                return MULTISELECT_TYPE;
            case CHECKBOX_TYPE:
                return CHECKBOX_TYPE;
            case CURRENT_DATE_TYPE:
                return CURRENT_DATE_TYPE;
            case COMPOUND_SPINNER_TYPE:
                return COMPOUND_SPINNER_TYPE;
            case CLIENT_PHONE_TYPE:
                return CLIENT_PHONE_TYPE;
            case CLIENT_ACCOUNTS_TYPE:
                return CLIENT_ACCOUNTS_TYPE;
            case CLIENT_SEX_TYPE:
                return CLIENT_SEX_TYPE;
        }
        return super.getItemViewType(position);
    }

    public void handleActivityResult(Serializable data) {
        mActivityResultCallback.itemSelected(data);
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder implements ButtonClickObservable {
        @Nullable
        @BindView(R.id.tv_hint)
        TextView tvHint;
        @Nullable
        @BindView(R.id.param_name)
        TextView paramName;

        ApplicationTypeDto.ApplicationParamModel mApplicationParam;

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mObservables.add(this);
        }
    }


    class EditTextViewHolder extends BaseViewHolder {

        @BindView(R.id.param_text_field)
        EditText paramField;
        @BindView(R.id.param_text_hint)
        TextInputLayout paramHint;

        public EditTextViewHolder(View itemView, boolean isNotEditable) {
            super(itemView);
            Log.i(TAG, "EditTextViewHolder init");
            ButterKnife.bind(this, itemView);
            paramField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        paramField.setError(null);
                    }
                }
            });
            if (isNotEditable){
                paramField.setKeyListener(null);
                paramField.setFocusable(false);
            }
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (paramField.getText().toString().isEmpty()) {
                    paramField.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = paramField.getText().toString();
                }
            } else {
                mApplicationParam.Value = paramField.getText().toString();
            }
            return valid;
        }
    }

    class HeaderViewHolder extends BaseViewHolder {

        @BindView(R.id.tvHeader)
        TextView tvHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "EditTextViewHolder init");
            ButterKnife.bind(this, itemView);
            tvHeader.setKeyListener(null);
            tvHeader.setFocusable(false);
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            mApplicationParam.Value = tvHeader.getText().toString();
            return valid;
        }
    }

    class ClientAccountViewHolder extends BaseViewHolder implements ActivityResultCallback<UserAccounts> {

        @BindView(R.id.select_account_view)
        View selectAccountView;
        @BindView(R.id.tv_account_hint)
        TextView tvSpinnerFrom;
        @BindView(R.id.linFromAccountInfo)
        LinearLayout linFromAccountInfo;
        @BindView(R.id.tvStatusFrom)
        TextView tvStatusFrom;
        @BindView(R.id.tvFromAccountName)
        TextView tvFromAccountName;
        @BindView(R.id.tvFromAccountBalance)
        TextView tvFromAccountBalance;
        @BindView(R.id.tvFromAccountNumber)
        TextView tvFromAccountNumber;
        @BindView(R.id.imgTypeFrom)
        ImageView imgTypeFrom;
        @BindView(R.id.tv_multi_from)
        TextView tvMultiFrom;

        private UserAccounts mUserAccounts;

        public ClientAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            selectAccountView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivityResultCallback = ClientAccountViewHolder.this;
                    Intent intent = new Intent(fragment.getContext(), SelectAccountActivity.class);
                    intent.putExtra("debitAccounts", true);
                    fragment.startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                }
            });
        }

        @Override
        public void itemSelected(UserAccounts userAccounts) {
            if (userAccounts != null) {
                mUserAccounts = userAccounts;
                tvSpinnerFrom.setVisibility(View.GONE);
                linFromAccountInfo.setVisibility(View.VISIBLE);
                tvFromAccountName.setText(userAccounts.name);
                tvFromAccountBalance.setText(userAccounts.getFormattedBalance(fragment.getContext()));
                tvFromAccountNumber.setText(userAccounts.number);
                if (userAccounts instanceof UserAccounts.Cards) {
                    UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                    imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(fragment.getContext(), 50, 55));
                    tvSpinnerFrom.setVisibility(View.GONE);
                    byte[] min = userCard.getByteArrayMiniatureImg();
                    if (min!=null)
                        Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                    else
                        Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeResource(fragment.getContext().getResources(),R.drawable.arrow_left));
                    //try {
                    //    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, userCard.readObject(false));//card.miniatureIm
                    //} catch (IOException | ClassNotFoundException e) {
                    //    e.printStackTrace();
                    //    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, userCard.miniatureIm);
                    //}
                } else if (userAccounts instanceof UserAccounts.DepositAccounts) {
                    tvMultiFrom.setVisibility(View.GONE);
                    tvStatusFrom.setVisibility(View.GONE);
                    imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(fragment.getContext(), 40, 45));
                    //imgTypeFrom.setImageDrawable(getResources().getDrawable(R.drawable.ic_ico_dep));
                    /*if (userAccounts.currency.equals("KZT")){
                        imgTypeFrom.setImageResource(R.drawable.tenge_1);
                    } else if (userAccounts.currency.equals("USD")){
                        imgTypeFrom.setImageResource(R.drawable.usd_1);
                    } else if (userAccounts.currency.equals("EUR")){
                        imgTypeFrom.setImageResource(R.drawable.eur_1);
                    } else {*/
                        imgTypeFrom.setImageResource(R.drawable.ic_image_dark_account_depo);//ic_ico_dep
                    //}
                    if (userAccounts instanceof UserAccounts.WishAccounts) {
                        UserAccounts.WishAccounts wishAccounts = (UserAccounts.WishAccounts) userAccounts;
                        tvFromAccountName.setText(wishAccounts.wishName);
                    }
                } else {
                    tvStatusFrom.setVisibility(View.VISIBLE);
                    tvMultiFrom.setVisibility(View.GONE);
                    if (userAccounts.accountType == 2) {
                        tvStatusFrom.setText(R.string.current_card_accounts);
                    } else if (userAccounts.accountType == 1) {
                        tvStatusFrom.setText(R.string.current_account_type);
                    }
                    imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(fragment.getContext(), 40, 45));
                    /*if (userAccounts.currency.equals("KZT")){
                        imgTypeFrom.setImageResource(R.drawable.tenge_2);
                    } else if (userAccounts.currency.equals("USD")){
                        imgTypeFrom.setImageResource(R.drawable.usd_2);
                    } else if (userAccounts.currency.equals("RUB")){
                        imgTypeFrom.setImageResource(R.drawable.rub_2);
                    } else if (userAccounts.currency.equals("GBP")){
                        imgTypeFrom.setImageResource(R.drawable.gbp_2);
                    } else if (userAccounts.currency.equals("CNY")){
                        imgTypeFrom.setImageResource(R.drawable.cny_2);
                    } else if (userAccounts.currency.equals("EUR")){
                        imgTypeFrom.setImageResource(R.drawable.eur_2);
                    } else {*/
                        imgTypeFrom.setImageResource(R.drawable.ic_image_dark_account_current);//bank_account
                    //}
                }
            }
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (mUserAccounts == null) {
                    tvSpinnerFrom.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = mUserAccounts.name;
                }
            } else {
                if (mUserAccounts != null)
                    mApplicationParam.Value = mUserAccounts.name;
            }
            return valid;
        }
    }

    class SpinnerViewHolder extends BaseViewHolder implements ActivityResultCallback<ApplicationTypeDto.DropdownData> {

        @BindView(R.id.selected_param_hint)
        TextInputLayout paramHint;
        @BindView(R.id.selected_param)
        EditText selectedParam;

        public SpinnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivityResultCallback = SpinnerViewHolder.this;
                    Intent intent = new Intent(fragment.getContext(), NavigationActivity.class);
                    intent.putExtra("isCustomList", true);
                    intent.putExtra(CustomListFragment.CUSTOM_LIST_EXTRA, mApplicationParam.DropDownList);
                    fragment.startActivityForResult(intent, ApplicationParamsFragment.SELECT_ITEM_REQUEST_CODE);
                }
            });
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (selectedParam.getText().toString().isEmpty()) {
                    selectedParam.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = selectedParam.getText().toString();
                }
            } else {
                mApplicationParam.Value = selectedParam.getText().toString();
            }
            return valid;
        }

        @Override
        public void itemSelected(ApplicationTypeDto.DropdownData dropdownData) {
            selectedParam.setError(null);
            selectedParam.setText(dropdownData.Value);
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

        public void onCheckboxChecked() {
            paramName.setError(null);
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (mMultiCheckBoxAdapter.getResult().isEmpty()) {
                    paramName.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = mMultiCheckBoxAdapter.getResult();
                }
            } else {
                mApplicationParam.Value = mMultiCheckBoxAdapter.getResult();
            }
            return valid;
        }
    }

    class CheckboxViewHolder extends BaseViewHolder {

        @BindView(R.id.checkbox)
        CheckBox mCheckBox;

        public CheckboxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCheckBox.setError(null);
                    }
                }
            });
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (!mCheckBox.isChecked()) {
                    mCheckBox.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = String.valueOf(mCheckBox.isChecked());
                }
            } else {
                mApplicationParam.Value = String.valueOf(mCheckBox.isChecked());
            }
            return valid;
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

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (clientPhone.getText().toString().isEmpty()) {
                    clientPhone.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = clientPhone.getText().toString();
                }
            } else {
                mApplicationParam.Value = clientPhone.getText().toString();
            }
            return valid;
        }
    }

    /*class SelectDateViewHolder extends BaseViewHolder implements DatePickerDialog.OnDateSetListener {

        @BindView(R.id.param_date)
        View selectDateView;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public SelectDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // TODO
           *//* final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
           // datePickerDialog.setStartDate(year, month, day);
            selectDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.show(fragment.getActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
                }
            });*//*
        }

        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            GregorianCalendar date = new GregorianCalendar(year, month, day);
            CharSequence dateString = VIEW_DATE_FORMAT.format(date.getTime());
            tvDate.setError(null);
            tvDate.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.gray_black_56_56_56));
            tvDate.setText(dateString);
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (tvDate.getText().toString().isEmpty()) {
                    tvDate.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = tvDate.getText().toString();
                }
            } else {
                mApplicationParam.Value = tvDate.getText().toString();
            }
            return valid;
        }
    }*/

    class CurrentDateViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_current_date)
        TextView tvCurrentDate;

        public CurrentDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Calendar calendar = Calendar.getInstance();
            CharSequence dateString = VIEW_DATE_FORMAT.format(calendar.getTime());
            tvCurrentDate.setText(dateString);
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (tvCurrentDate.getText().toString().isEmpty()) {
                    tvCurrentDate.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    mApplicationParam.Value = tvCurrentDate.getText().toString();
                }
            } else {
                mApplicationParam.Value = tvCurrentDate.getText().toString();
            }
            return valid;
        }
    }

    class CompoundSpinnerViewHolder extends BaseViewHolder implements ActivityResultCallback<ApplicationTypeDto.DropdownData> {

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

        private boolean isParentClick;

        public CompoundSpinnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentBranchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isParentClick = true;
                    mActivityResultCallback = CompoundSpinnerViewHolder.this;
                    Intent intent = new Intent(fragment.getContext(), NavigationActivity.class);
                    intent.putExtra("isCustomList", true);
                    intent.putExtra(CustomListFragment.CUSTOM_LIST_EXTRA, mApplicationParam.DropDownList);
                    fragment.startActivityForResult(intent, ApplicationParamsFragment.SELECT_ITEM_REQUEST_CODE);
                }
            });
        }

        @Override
        public boolean onButtonClickAndValidate() {
            boolean valid = true;
            if (mApplicationParam.Required) {
                if (parentBranchField.getText().toString().isEmpty()) {
                    parentBranchField.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                }
                if (childBranchField.getText().toString().isEmpty()) {
                    childBranchField.setError(fragment.getString(R.string.error_empty));
                    valid = false;
                } else {
                    try {
                        mApplicationParam.Value = childBranchField.getText().toString().split(" ")[0];
                    }catch (Exception ignored){}
                }
            } else {
                try {
                    mApplicationParam.Value = childBranchField.getText().toString().split(" ")[0];
                }catch (Exception ignored){}
            }
            return valid;
        }

        @Override
        public void itemSelected(final ApplicationTypeDto.DropdownData dropdownData) {
            if (isParentClick) {
                parentBranchField.setError(null);
                childBranchField.setError(null);
                parentBranchField.setText(dropdownData.Value);
                childBranchField.setText(dropdownData.JsonList.split("\\|")[0]);
                childBranchLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isParentClick = false;
                        ArrayList<ApplicationTypeDto.DropdownData> branches = new ArrayList<>();
                        for (String branch : dropdownData.JsonList.split("\\|")) {
                            branches.add(new ApplicationTypeDto.DropdownData(branch));
                        }
                        Intent intent = new Intent(fragment.getContext(), NavigationActivity.class);
                        intent.putExtra("isCustomList", true);
                        intent.putExtra(CustomListFragment.CUSTOM_LIST_EXTRA, branches);
                        fragment.startActivityForResult(intent, ApplicationParamsFragment.SELECT_ITEM_REQUEST_CODE);
                    }
                });
            } else {
                childBranchField.setText(dropdownData.Value);
            }
        }
    }

    class ClientSexViewHolder extends SpinnerViewHolder {

        public ClientSexViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            final ArrayList<ApplicationTypeDto.DropdownData> dropdownDatas = new ArrayList<ApplicationTypeDto.DropdownData>() {{
                add(new ApplicationTypeDto.DropdownData(fragment.getString(R.string.male)));
                add(new ApplicationTypeDto.DropdownData(fragment.getString(R.string.female)));
            }};
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivityResultCallback = ClientSexViewHolder.this;
                    Intent intent = new Intent(fragment.getContext(), NavigationActivity.class);
                    intent.putExtra("isCustomList", true);
                    intent.putExtra(CustomListFragment.CUSTOM_LIST_EXTRA, dropdownDatas);
                    fragment.startActivityForResult(intent, ApplicationParamsFragment.SELECT_ITEM_REQUEST_CODE);
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        private Toast mToast;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.footer)
        public void onFooterClick() {
            if (validate()) {
                mApplicationTypeDto.Params = filledApplicationParams;
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(mApplicationTypeDto));
                    mHistoryApllicationsImpl.createApplication(fragment.getContext(), jsonObject);
                    String xml = jsonObject.toString();
                    if (xml.length() > 2000) {
                        for (int i = 0; i < xml.length(); i += 2000) {
                            if (i + 2000 < xml.length())
                                Log.i("rescounter", xml.substring(i, i + 2000));
                            else
                                Log.i("rescounter", xml.substring(i));
                        }
                    } else
                        Log.i("rescounter", xml);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException when convert ApplicationTypeDto model " + e.getLocalizedMessage());
                }
            } else {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(itemView.getContext(), R.string._t_fill_all_required_, Toast.LENGTH_SHORT);
                mToast.show();
            }
        }

        private boolean validate() {
            boolean valid = true;
            for (ButtonClickObservable buttonClickObservable : mObservables) {
                if (!buttonClickObservable.onButtonClickAndValidate()) {
                    valid = false;
                }
            }
            return valid;
        }
    }

    interface ActivityResultCallback<T> {
        void itemSelected(T t);
    }

    interface ButtonClickObservable {
        boolean onButtonClickAndValidate();
    }
}
