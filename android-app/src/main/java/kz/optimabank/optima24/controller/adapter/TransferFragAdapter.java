package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.utility.Constants.CLICK_ITEM_TAG;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.TransferForeignCurrencyToAnotherBank;
import static kz.optimabank.optima24.utility.Constants.TransferMoneyToAnotherBank;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.payment.PaymentAdapFragment;
import kz.optimabank.optima24.fragment.template.TemplateInterface;
import kz.optimabank.optima24.fragment.transfer.TransfersFragment;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.base.TransferModel;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

public class TransferFragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        PaymentTemplateOperationImpl.CallbackOperationPayment, TransferTemplateOperationImpl.CallbackOperationTransfer, TemplateInterface {
    ArrayList<Object> data;
    Context context;
    private static final int PAYMENT = 1;
    private static final int HEADER_PAYMENT = 2;
    private static final int HEADER_TRANSFER = 3;
    private static final int TRANSFER = 4;
    private static final int HEADER_PAYMENT_TEMPLATE = 5;
    private static final int PAYMENT_TEMPLATE = 6;
    private static final int FOOTER_PAYMENT_TEMPLATE = 7;
    private static final int TRANSFER_TEMPLATE = 9;
    private static final int HEADER_SPACING = 10;
    private static final int HEADER_INVISIBLE = 11;
    private static final int ITEM_SERVICE = 12;
    private static final String GET_BUNDLE = "getBundle";
    private static final String ADAP = "isADAP";
    private static final String TTF = "isTTF";
    private static final String ONE = "1";
    private static final String WHAT_IS = "whatIs";
    private static final String CIT = "CIT";
    private static final String PTF = "PTF";
    private static int iji = 0;
    private final OnItemClickListener mOnItemClickListener;
    private PaymentTemplateOperation paymentTemplateOperation;
    private TransferTemplateOperation transferTemplateOperation;
    TemplateActivity templateActivity = new TemplateActivity();
    private int position;
    private final int currentForTemplate;

    long invoiceId;
    int paymentServiceId;
    String categoryName;
    /*boolean isFirstMass;
    int pos;
    ArrayList<Integer> hren = new ArrayList();
    ArrayList<Integer> hren2 = new ArrayList();
    int[] massFirst = new int[15];
    int[] massThird = new int[15];*/

    PaymentContextController paymentController;
    PaymentAdapFragment paymentAdapFragment;
    TransfersFragment transfersFragment;

    public TransferFragAdapter(Context context, ArrayList<Object> data, OnItemClickListener onItemClickListener, int currentForTemplate) {
        this.data = data;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
        this.currentForTemplate = currentForTemplate;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == HEADER_PAYMENT || viewType == HEADER_TRANSFER || viewType == HEADER_PAYMENT_TEMPLATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new VHHeader(view);
        } else if (viewType == FOOTER_PAYMENT_TEMPLATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_footer, parent, false);
            viewHolder = new VHFooter(view);
        } else if (viewType == PAYMENT_TEMPLATE || viewType == TRANSFER_TEMPLATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_swipe_item, parent, false);
            viewHolder = new VHSItem(view);
        } else if (viewType == HEADER_SPACING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_spacing, parent, false);
            viewHolder = new VHSpacing(view);
        } else if (viewType == HEADER_INVISIBLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer_item_list, parent, false);
            viewHolder = new VHItemInvisible(view);
        } else if (viewType == ITEM_SERVICE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_service_list_item, parent, false);
            viewHolder = new VHService(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer_item_list, parent, false);
            viewHolder = new VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        int viewType = mHolder.getItemViewType();
        if (viewType == PAYMENT || viewType == HEADER_PAYMENT) {
            configurePaymentViewHolder(mHolder, position);
            Log.i("onBindViewHolderforsize", " --- " + ++iji);
        } else if (viewType == TRANSFER || viewType == HEADER_TRANSFER) {
            configureTransferViewHolder(mHolder, position);
        } else if (viewType == PAYMENT_TEMPLATE) {
            configurePaymentTemplateViewHolder(mHolder, position);
        } else if (viewType == TRANSFER_TEMPLATE) {
            configureTransferTemplateViewHolder(mHolder, position);
        } else if (viewType == ITEM_SERVICE) {
            configurePaymentServiceViewHolder(mHolder, position);
        } else if (viewType == HEADER_PAYMENT_TEMPLATE || viewType == FOOTER_PAYMENT_TEMPLATE) {
            configureTemplateViewHolder(mHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && !data.isEmpty()) {
            if (data.get(position) instanceof TransferModel) {
                TransferModel temp = (TransferModel) data.get(position);
                if (temp.code == Constants.HEADER_ID) {
                    return HEADER_TRANSFER;
                } else {
                    return TRANSFER;
                }
            } else if (data.get(position) instanceof PaymentCategory) {
                PaymentCategory paymentCategory = (PaymentCategory) data.get(position);
                if (paymentCategory.code == Constants.HEADER_ID) {
                    return HEADER_PAYMENT;
                } else {
                    return PAYMENT;
                }
            } else if (data.get(position) instanceof Templates) {
                Templates templates = (Templates) data.get(position);
                if (templates.code == Constants.HEADER_ID) {
                    return HEADER_PAYMENT_TEMPLATE;
                } else if (templates.code == Constants.FOOTER_ID) {
                    return FOOTER_PAYMENT_TEMPLATE;
                } else if (templates instanceof TemplatesPayment) {
                    return PAYMENT_TEMPLATE;
                } else if (templates instanceof TemplateTransfer) {
                    return TRANSFER_TEMPLATE;
                }
            } else if (data.get(position) instanceof Integer) {
                return HEADER_SPACING;
            } else if (data.get(position) instanceof String) {
                return HEADER_INVISIBLE;
            } else if (data.get(position) instanceof PaymentService) {
                return ITEM_SERVICE;
            }
        }
        return -1000;
    }

    private void configurePaymentServiceViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        PaymentService paymentService = (PaymentService) data.get(position);
        if (paymentService != null) {
            if (mHolder instanceof VHService) {
                VHService holder = (VHService) mHolder;
                holder.tvTitle.setText(paymentService.name);
            }
        }
    }

    private void configurePaymentViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        PaymentCategory paymentCategory = (PaymentCategory) data.get(position);
        Log.i("cogurePaymntViewHold", " " + data.size());
        if (paymentCategory != null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader) mHolder;
                holder.textviewName.setText(paymentCategory.getName());
            } else {
                /*if (!isFirstMass)
                    pos = position;
                    for (int i=0; i<15; i++){
                        massFirst[i] = pos;
                        hren.add(pos);
                        pos += 3;
                        massThird[i] = massFirst[i]+2;
                        hren2.add(pos+2);
                    }
                isFirstMass=true;
*/
                VHItem holder = (VHItem) mHolder;
                //if (paymentCategory.getName().length()>15) {
                holder.tvTitle.setText(paymentCategory.getName());
                //}else{
                //   holder.tvTitle.setText(paymentCategory.getName()+"\n");
                //}
                Log.i("configurePayment", "position = " + position);
                holder.relTIL.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_shadow));
                Utilities.setMargins(holder.relTIL, 17, 17, 17, 17);
                if (paymentCategory.alias != null) {
                    //setPaymentImage(context,holder.imgItem,paymentCategory.getExternalId());
                    Log.i("alias", "alias() = " + paymentCategory.alias);
                    //Utilities.setPaymentImage(context, holder.imgItem, paymentCategory.alias); // ic_image_red_payments_games
                    switch (paymentCategory.alias) {
                        case "20":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_webpay));
                            break;
                        case "12":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_taxi));
                            break;
                        case "14":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_socialnetworks));
                            break;
                        case "1":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_public));
                            break;
                        case "9":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_other));
                            break;
                        case "15":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_money));
                            break;
                        case "2":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_mobile));
                            break;
                        case "10":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_mobile));
                            break;
                        case "21":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_internetstore));
                            break;
                        case "5":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_internet));
                            break;
                        case "13":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_games));
                            break;
                        case "16":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_gameplatforms));
                            break;
                        case "3":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_fixed));
                            break;
                        case "11":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_education));
                            break;
                        case "17":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_dealer));
                            break;
                        case "6":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_cable));
                            break;
                        case "7":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_budget));
                            break;
                        case "4":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_budget));
                            break;
                        case "8":
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_beauty));
                            break;
                        default:
                            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_other));//star
                            break;
                    }
                /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(15*3,0,15*3,0);

                if (hren.contains(position) || hren2.contains(position))
                    holder.relForMRG.setLayoutParams(layoutParams);*/

                    //holder.tvType.setVisibility(View.GONE);
                    //holder.tvSum.setVisibility(View.GONE);
                }
            }
        }
    }

    private void configureTemplateViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Templates template = (Templates) data.get(position);
        if (template != null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader) mHolder;
                holder.textviewName.setText(template.name);
            } else if (mHolder instanceof VHFooter) {
                VHFooter holder = (VHFooter) mHolder;
                holder.btnFooter.setText(template.name);
            }
        }
    }

    private void configureTransferViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        TransferModel transferModel = (TransferModel) data.get(position);
        if (mHolder instanceof VHHeader) {
            VHHeader holder = (VHHeader) mHolder;
            holder.textviewName.setText(transferModel.name);
        } else {
            VHItem holder = (VHItem) mHolder;
            String name = transferModel.name;
            holder.tvTitle.setText(name);
            holder.relTIL.setBackground(null);
            Utilities.setMargins(holder.relTIL, 0, 0, 0, 0);
            Log.i("ADAPTER", "nameTRANSFER = " + name);
            Log.i("ADAPTER", "nameTRANSFERCARD = " + context.getString(R.string.transfer_card));
            Log.i("ADAPTER", "nameTRANSFERCARDVISATOVISA = " + context.getString(R.string.transfer_card_visa_to_visa_for_item));
            Log.i("ADAPTER", "nameTRANSFERTENGE = " + context.getString(R.string.transfer_swift_tengeN));
            Log.i("ADAPTER", "nameTRANSFERSWIFT = " + context.getString(R.string.transfer_swift));
            if (name.equals(context.getString(R.string.transfer_card))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_internal));
            } else if (name.equals(context.getString(R.string.transfer_card_visa_to_visa_for_item))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_transfer_visa_to_visa));
            } else if (name.equals(context.getString(R.string.transfer_phone_number))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_transfer_by_phone));
            } else if (name.equals(context.getString(R.string.transfer_swift_tengeN))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_local));
            } else if (name.equals(context.getString(R.string.transfer_swift))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_swift));
            } else if (name.equals(context.getString(R.string.transfer_card_elcard_to_elcard_for_item))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_transfer_elcard_to_elcard));
            }
        }
    }

    private void configurePaymentTemplateViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        TemplatesPayment paymentTemplate = (TemplatesPayment) data.get(position);
        VHSItem holder = (VHSItem) mHolder;

        holder.tvSwitchOff.setText(paymentTemplate.autoPayStatus2 == 1 ? context.getString(R.string.switch_off) : context.getString(R.string.switch_on));
        Templates template = (TemplatesPayment) data.get(position);
        holder.tvType.setVisibility(View.VISIBLE);
        holder.tvSum.setVisibility(View.VISIBLE);
        holder.tvTitle.setText(paymentTemplate.name);
        holder.tvType.setText(context.getResources().getString(R.string.t_payments_title));
        holder.btnDelete.setOnClickListener(onClick(holder, template));
        holder.btnChange.setOnClickListener(onClick(holder, template));
        holder.btnPay.setOnClickListener(onClick(holder, template));
        holder.btnSwitchOff.setOnClickListener(onClick(holder, template));

        holder.tvSum.setText(getFormattedBalance(paymentTemplate.amount, "KGS"));

        if (paymentTemplate.isAutoPay) {
            holder.btnSwitchOff.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy_2));
            Log.i("autoPayStatus", "paymentTemplate.autoPayStatus = " + paymentTemplate.autoPayStatus2);
            switch (paymentTemplate.autoPayStatus2) {
                case 1:
                    holder.tvStatus.setText(R.string.auto_payment_activ2);
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_19_136_52));
                    break;
                case 2:
                    holder.tvStatus.setText(R.string.auto_payment_stop);
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_atf));
                    break;
                case 3:
                    holder.tvStatus.setText(R.string.auto_payment_stop_by_manager);
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_atf));
                    break;
                case 4:
                    holder.tvStatus.setText(R.string.auto_payment_out_of_count);
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_atf));
                    break;
            }
        } else {
            holder.btnSwitchOff.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy));
        }
    }

    private void configureTransferTemplateViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        TemplateTransfer transferTemplate = (TemplateTransfer) data.get(position);

        if (transferTemplate != null) {
            Templates template = (TemplateTransfer) data.get(position);
            VHSItem holder = (VHSItem) mHolder;
            holder.tvSwitchOff.setText(transferTemplate.StandingInstructionStatus == 1 ? context.getString(R.string.switch_off) : context.getString(R.string.switch_on));
            holder.tvTitle.setText(transferTemplate.getName());
            holder.tvType.setText(context.getResources().getString(R.string.transfer_title));
            holder.tvPay.setText(R.string.transfer_action);
            holder.tvSum.setText(getFormattedBalance(transferTemplate.Amount, transferTemplate.getCurrency()));
            holder.btnDelete.setOnClickListener(onClick(holder, template));
            holder.btnChange.setOnClickListener(onClick(holder, template));
            holder.btnPay.setOnClickListener(onClick(holder, template));
            holder.btnSwitchOff.setOnClickListener(onClick(holder, template));


            if (transferTemplate.isStandingInstruction()) {
                holder.btnSwitchOff.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy_2));
                //holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart_auto_payment));
                Log.i("autoPayStatus", "transferTemplate.getStandingInstructionStatus() = " + transferTemplate.getStandingInstructionStatus());
                switch (transferTemplate.getStandingInstructionStatus()) {
                    case 1:
                        holder.tvStatus.setText(R.string.auto_payment_activ2);
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_19_136_52));
                        break;
                    case 2:
                        holder.tvStatus.setText(R.string.auto_payment_stop);
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_atf));
                        break;
                    case 3:
                        holder.tvStatus.setText(R.string.auto_payment_stop_by_manager);
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_atf));
                        break;
                    case 4:
                        holder.tvStatus.setText(R.string.auto_payment_out_of_count);
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_atf));
                        break;
                }
            } else {
                holder.tvStatus.setVisibility(View.GONE);
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy));
                //holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
            }
        }
    }

    public void updateList(ArrayList<Object> list) {
        data = list;
        notifyDataSetChanged();
    }

    @Override
    public void deleteTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            transfersFragment = (TransfersFragment) ((MenuActivity) context).tabAdapter.getRegisteredFragment(((MenuActivity) context).viewPager.getCurrentItem());

            TransferAndPayment transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerUpdateCallBack(new TransferAndPaymentImpl.UpdateCallback() {
                @Override
                public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
                }

                @Override
                public void jsonTransferSubscriptionsResponse(int statusCode, String errorMessage) {
                    transfersFragment.setAdapter();
                }
            });
            transferAndPayment.getTransferTemplate(context);
            try {
                data.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
            //context.startActivity(new Intent(context,context.getClass()));
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            getErrorDialog(errorMessage).show();
        }
    }

    @Override
    public void changeActiveTransferTemplate(int statusCode, String errorMessage) {
        if (statusCode != 0) {
            Toast.makeText(context, context.getString(R.string.test_message), Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.template_updated_successfully);
            builder.setMessage(R.string.update_page);
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent intent = new Intent(context, context.getClass());
                    context.startActivity(intent);
                }
            });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void deletePaymentTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            paymentAdapFragment = (PaymentAdapFragment) ((MenuActivity) context).tabAdapter.getRegisteredFragment(((MenuActivity) context).viewPager.getCurrentItem());

            TransferAndPayment transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerUpdateCallBack(new TransferAndPaymentImpl.UpdateCallback() {
                @Override
                public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
                    paymentAdapFragment.setAdapter();
                }

                @Override
                public void jsonTransferSubscriptionsResponse(int statusCode, String errorMessage) {
                }
            });
            transferAndPayment.getPaymentSubscriptions(context);
            try {
                data.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
            //context.startActivity(new Intent(context,context.getClass()));
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            getErrorDialog(errorMessage).show();
        }
    }

    @Override
    public void quickPaymentResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void changeActivePaymentTemplate(int statusCode, String errorMessage) {
        if (statusCode != 0) {
            Toast.makeText(context, context.getString(R.string.test_message), Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.template_updated_successfully);
            builder.setMessage(R.string.update_page);
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent intent = new Intent(context, context.getClass());
                    context.startActivity(intent);
                }
            });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void openPaymentFragment(int position, int tag) {
        TemplatesPayment templatesPayment = (TemplatesPayment) data.get(position);
        Intent intent = new Intent(context, TemplateActivity.class);
        intent.putExtra(GET_BUNDLE, getBundle(tag, templatesPayment));
        intent.putExtra(ADAP, ONE);
        intent.putExtra(TTF, ONE);

        int checkInvoice = ((MenuActivity) context).checkInvoice(templatesPayment, tag);
        if (checkInvoice == 0 || checkInvoice == 1) {
            if (tag == Constants.TAG_CHANGE) {
                intent.putExtra(WHAT_IS, CIT);
            } else {
                return;
            }
        } else {
            intent.putExtra("whatIs", "PTF");
        }
        context.startActivity(intent);
    }

    @Override
    public void openTransferFragment(int position, int tag) {
        TemplateTransfer templatesTransfer = (TemplateTransfer) data.get(position);

        Intent intent = new Intent(context, TemplateActivity.class);
        intent.putExtra("getBundle", getBundle(tag, templatesTransfer));
        intent.putExtra("isADAP", "1");
        intent.putExtra("isTTF", "1");

        if (templatesTransfer.getProductType() == TransferMoneyToAnotherBank) {
            intent.putExtra("whatIs", "TIT");
        } else if (templatesTransfer.getProductType() == TransferForeignCurrencyToAnotherBank) {
            intent.putExtra("whatIs", "TIC");
        } else {
            intent.putExtra("whatIs", "TTF");
        }
        context.startActivity(intent);
    }

    private AlertDialog getErrorDialog(String error) {
        return new AlertDialog.Builder(context)
                .setMessage(error)
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_alert)
                .setTitle(R.string.alert_error)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        }).create();
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        //@BindView(R.id.tvType) TextView tvType;
        //@BindView(R.id.tvSum) TextView tvSum;
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.relTIL)
        RelativeLayout relTIL;

        private static final long CLICK_ITEM_INTERVAL = 600;
        private long lastTimeClicked = System.currentTimeMillis();

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            relTIL.setOnClickListener(view -> {

                // предотвратил быстрый повторный клик на айтем
                long now = System.currentTimeMillis();
                if (now - lastTimeClicked < CLICK_ITEM_INTERVAL) {
                    return;
                }
                lastTimeClicked = now;

                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            });
        }
    }

    public class VHItemInvisible extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.relTIL)
        RelativeLayout relTIL;

        private VHItemInvisible(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            relTIL.setVisibility(View.GONE);
        }
    }

    public class VHSItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvSum)
        TextView tvSum;
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.btnDelete)
        LinearLayout btnDelete;
        @BindView(R.id.btnChange)
        LinearLayout btnChange;
        @BindView(R.id.btnPay)
        LinearLayout btnPay;
        @BindView(R.id.tvPay)
        TextView tvPay;
        @BindView(R.id.btnSwitchOff)
        LinearLayout btnSwitchOff;
        @BindView(R.id.tvSwitchOff)
        TextView tvSwitchOff;

        public VHSItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "VHSItem-TransferFragAdapter");
                    ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                        }

                        @Override
                        public void onAnimationEnd(View view) {

                            if (getItemViewType() == PAYMENT_TEMPLATE) {
                                openPaymentFragment(getAdapterPosition(), Constants.CLICK_ITEM_TAG);
                            } else if (getItemViewType() == TRANSFER_TEMPLATE) {
                                GeneralManager.setIsFromTemplate(true);
                                openTransferFragment(getAdapterPosition(), Constants.CLICK_ITEM_TAG);
                            }
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                        }
                    };
                    clickAnimation(view, animatorListener);
                }
            });
        }
    }

    /*public int checkInvoice(Templates template, int tag) {
        if(template instanceof TemplatesPayment) {
            TemplatesPayment templatesPayment = (TemplatesPayment) template;
            paymentController = PaymentContextController.getController();
            PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
            if(paymentService.IsInvoiceable) {
                if(tag == Constants.TAG_CHANGE) {
                    return 1;
                }
                for (Invoices invoice : GeneralManager.getInstance().getInvoices()) {
                    if(invoice.getSubscriptionId() == templatesPayment.id) {
                        Intent intent = new Intent(context, InvoiceAblePaymentActivity.class);
                        intent.putExtra("InvoiceId",invoice.getInvoiceId());
                        intent.putExtra("paymentServiceId", paymentService.id);
                        intent.putExtra(CATEGORY_NAME,paymentService.name);
                        context.startActivity(intent);
                        //this invoice, there is a receipt
                        return 0;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage(context.getString(R.string.not_invoices));
                builder.setPositiveButton(context.getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                //this invoice, There is no receipt
                return 1;
            } else {
                //This is not an invoice
                return 2;
            }
        }
        //error
        return -1000;
    }*/

    public View.OnClickListener onClick(final RecyclerView.ViewHolder mHolder, final Templates templates) {
        Log.i("TAG", "onClick-TransferFragAdapter");
        paymentTemplateOperation = new PaymentTemplateOperationImpl();
        paymentTemplateOperation.registerCallBack(this);

        transferTemplateOperation = new TransferTemplateOperationImpl();
        transferTemplateOperation.registerTransferCallBack(this);
        return view -> {
            switch (view.getId()) {
                case R.id.btnDelete:
                    if (templates instanceof TemplateTransfer) {
                        position = mHolder.getAdapterPosition();
                        TemplateTransfer templateTransfer = (TemplateTransfer) templates;
                        transferTemplateOperation.deleteTransferTemplate(context, templateTransfer.getId());
                    } else if (templates instanceof TemplatesPayment) {
                        position = mHolder.getAdapterPosition();
                        TemplatesPayment templatePayment = (TemplatesPayment) templates;
                        paymentTemplateOperation.deletePaymentTemplate(context, templatePayment.id);
                    }
                    break;
                case R.id.btnChange:

                    if (templates instanceof TemplateTransfer) {
                        openTransferFragment(mHolder.getAdapterPosition(), Constants.TAG_CHANGE);
                    } else if (templates instanceof TemplatesPayment) {
                        openPaymentFragment(mHolder.getAdapterPosition(), Constants.TAG_CHANGE);
                    }
                    break;
                case R.id.btnPay:
                    if (templates instanceof TemplatesPayment) {
                        openPaymentFragment(mHolder.getAdapterPosition(), CLICK_ITEM_TAG);
                    } else if (templates instanceof TemplateTransfer) {
                        GeneralManager.setIsFromTemplate(false);
                        openTransferFragment(mHolder.getAdapterPosition(), Constants.CLICK_ITEM_TAG);
                    }
                    break;
                case R.id.btnSwitchOff:
                    if (templates instanceof TemplatesPayment) {
                        TemplatesPayment templatesPayment = (TemplatesPayment) templates;
                        int checkInvoice = ((MenuActivity) context).checkInvoice(templatesPayment, Constants.TAG_PAY);
                        if (checkInvoice == 0 || checkInvoice == 1) {
                            Log.i("autoPayStatus2", "autoPayStatus2 = " + templatesPayment.autoPayStatus2);
                            paymentTemplateOperation.changeActivePaymentTemplate(context, templatesPayment.id, templatesPayment.autoPayStatus2 != 1);
                        } else {
                            Log.i("autoPayStatus2", "autoPayStatus2 = " + templatesPayment.autoPayStatus2);
                            paymentTemplateOperation.changeActivePaymentTemplate(context, templatesPayment.id, templatesPayment.autoPayStatus2 != 1);
                        }
                    } else if (templates instanceof TemplateTransfer) {
                        TemplateTransfer templatesTransfer = (TemplateTransfer) templates;
                        Log.i("isStandingInstruction", "isStandingInstruction = " + templatesTransfer.isStandingInstruction());
                        Log.i("isStandingInstruction", "templatesTransfer.StandingInstructionStatus  = " + templatesTransfer.StandingInstructionStatus);
                        transferTemplateOperation.changeActiveTransferTemplate(context, templatesTransfer.getId(), templatesTransfer.StandingInstructionStatus != 1);
                    }
                    break;
            }
        };
    }

    private int checkInvoice(TemplatesPayment template) {
        TemplatesPayment templatesPayment = (TemplatesPayment) template;
        paymentController = PaymentContextController.getController();
        PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
        if (paymentService.IsInvoiceable) {
            for (Invoices invoice : GeneralManager.getInstance().getInvoices()) {
                if (invoice.getSubscriptionId() == templatesPayment.id) {
                    categoryName = paymentService.name;
                    invoiceId = invoice.getInvoiceId();
                    paymentServiceId = paymentService.id;
                    //this invoice, there is a receipt
                    return 0;
                }
            }
            //this invoice, There is no receipt
            return 1;
        } else {
            //This is not an invoice
            return 2;
        }
        //error
    }

    private Bundle getBundle(int tag, Templates templates) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("template", templates);
        bundle.putInt("actionTag", tag);
        return bundle;
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header)
        TextView textviewName;

        private VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHFooter extends RecyclerView.ViewHolder {
        @BindView(R.id.btnFooter)
        Button btnFooter;

        private VHFooter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TemplateActivity.class);
                    intent.putExtra("isADAP", "0");
                    Log.d("TAG", "onClick");
                    intent.putExtra("isTTA", currentForTemplate);
                    context.startActivity(intent);
                }
            });
        }
    }

    public class VHService extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        private VHService(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "VHService-TransferFragAdapter");
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
            Drawable leftDrawable = AppCompatResources.getDrawable(itemView.getContext(), R.drawable.chevron_right);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
        }
    }

    public class VHSpacing extends RecyclerView.ViewHolder {
        @BindView(R.id.view)
        View view;

        private VHSpacing(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) context.getResources().getDimension(R.dimen.search_app_bar_height);
            params.width = RecyclerView.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
        }
    }
}
