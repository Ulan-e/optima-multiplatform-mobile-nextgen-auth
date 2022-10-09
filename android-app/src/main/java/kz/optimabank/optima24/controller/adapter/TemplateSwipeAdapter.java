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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.confirm.SuccessOperation;
import kz.optimabank.optima24.fragment.payment.PaymentTemplateFragment;
import kz.optimabank.optima24.fragment.payment.invoice.ChangeInvoiceTemplate;
import kz.optimabank.optima24.fragment.template.TemplateInterface;
import kz.optimabank.optima24.fragment.transfer.TransferInterbankCurrencyTemplate;
import kz.optimabank.optima24.fragment.transfer.TransferInterbankTemplate;
import kz.optimabank.optima24.fragment.transfer.TransferTemplateFragment;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;

/**
 * Created by Timur on 28.04.2017.
 */

public class TemplateSwipeAdapter extends RecyclerView.Adapter<TemplateSwipeAdapter.ViewHolder> implements
        PaymentTemplateOperationImpl.CallbackOperationPayment, TransferTemplateOperationImpl.CallbackOperationTransfer,
        TransferAndPaymentImpl.UpdateCallback, TemplateInterface {
    private static final String TAG = TemplateSwipeAdapter.class.getSimpleName();

    private static final int PAYMENT_TEMPLATE = 1;
    private static final int TRANSFER_TEMPLATE = 2;
    private static final String GET_BUNDLE = "getBundle";
    private static final String ADAP = "isADAP";
    private static final String TTF = "isTTF";
    private static final String ONE = "1";
    private static final String WHAT_IS = "whatIs";
    private static final String CIT = "CIT";
    private static final String PTF = "PTF";
    private PaymentTemplateOperation paymentTemplateOperation;
    private TransferTemplateOperation transferTemplateOperation;
    ArrayList<Object> data;
    PaymentContextController paymentController;
    Context context;
    int position;

    long invoiceId;
    int paymentServiceId;
    String categoryName;

    String isTransferAtTempl;
    SuccessOperation successOperation = new SuccessOperation();

    public TemplateSwipeAdapter(Context context, ArrayList<Object> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public TemplateSwipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if (viewType == PAYMENT_TEMPLATE || viewType == TRANSFER_TEMPLATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_swipe_item, parent, false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TemplateSwipeAdapter.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        Templates template = null;
        if (viewType == PAYMENT_TEMPLATE) {
            template = (TemplatesPayment) data.get(position);
            configurePaymentTemplateViewHolder(holder, (TemplatesPayment) template);
        } else if (viewType == TRANSFER_TEMPLATE) {
            template = (TemplateTransfer) data.get(position);
            configureTransferTemplateViewHolder(holder, (TemplateTransfer) template);
        }
        if (template != null) {
            if (template instanceof TemplatesPayment) {
                TemplatesPayment templates = (TemplatesPayment) template;
                holder.tvSwitchOff.setText(templates.autoPayStatus2 == 1 ? context.getString(R.string.switch_off) : context.getString(R.string.switch_on));
            } else if (template instanceof TemplateTransfer) {
                TemplateTransfer templates = (TemplateTransfer) template;
                holder.tvSwitchOff.setText(templates.StandingInstructionStatus == 1 ? context.getString(R.string.switch_off) : context.getString(R.string.switch_on));
            }
            holder.btnDelete.setOnClickListener(onClick(holder, template));
            holder.btnChange.setOnClickListener(onClick(holder, template));
            holder.btnPay.setOnClickListener(onClick(holder, template));
            holder.btnSwitchOff.setOnClickListener(onClick(holder, template));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && !data.isEmpty()) {
            if (data.get(position) instanceof TemplatesPayment) {
                return PAYMENT_TEMPLATE;
            } else if (data.get(position) instanceof TemplateTransfer) {
                return TRANSFER_TEMPLATE;
            }
        }
        return -1000;
    }


    @Override
    public void openPaymentFragment(int position, int tag) {
        if (position == -1) {
            return;
        }
        ATFFragment fragment;
        TemplatesPayment templatesPayment = (TemplatesPayment) data.get(position);
        int checkInvoice = ((TemplateActivity) context).checkInvoice(templatesPayment, tag);
        if (checkInvoice == 0 || checkInvoice == 1) {
            if (tag == Constants.TAG_CHANGE) {
                fragment = new ChangeInvoiceTemplate();
                isTransferAtTempl = "0";
            } else {
                return;
            }
        } else {
            fragment = new PaymentTemplateFragment();
            isTransferAtTempl = "0";
        }
        fragment.setArguments(getBundle(tag, templatesPayment));
        ((TemplateActivity) context).navigateToPage(fragment, true);
    }

    @Override
    public void openTransferFragment(int position, int tag) {
        if (position == -1) {
            return;
        }
        TemplateTransfer templatesTransfer = (TemplateTransfer) data.get(position);
        ATFFragment fragment = null;
        Log.i("templatesTransfer", "templatesTransfer.getProductType() = " + templatesTransfer.getProductType());
        if (templatesTransfer.getProductType() == TransferMoneyToAnotherBank) {
            fragment = new TransferInterbankTemplate();
            isTransferAtTempl = "1";
        } else if (templatesTransfer.getProductType() == TransferForeignCurrencyToAnotherBank) {
            fragment = new TransferInterbankCurrencyTemplate();
            isTransferAtTempl = "1";
        } else {
            fragment = new TransferTemplateFragment();
            isTransferAtTempl = "1";
        }
        fragment.setArguments(getBundle(tag, templatesTransfer));
        ((TemplateActivity) context).navigateToPage(fragment, true);
    }

    private void configurePaymentTemplateViewHolder(RecyclerView.ViewHolder mHolder, TemplatesPayment paymentTemplate) {
        if (paymentTemplate != null) {
            ViewHolder holder = (ViewHolder) mHolder;
            holder.tvTitle.setText(paymentTemplate.name);
            holder.tvType.setText(context.getResources().getString(R.string.t_payments_title));

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
                //holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy));
            }
        }
    }

    private void configureTransferTemplateViewHolder(RecyclerView.ViewHolder mHolder, TemplateTransfer transferTemplate) {
        if (transferTemplate != null) {
            ViewHolder holder = (ViewHolder) mHolder;
            holder.tvPay.setText(context.getString(R.string.transfer_action));
            holder.tvTitle.setText(transferTemplate.getName());
            holder.tvType.setText(context.getResources().getString(R.string.transfer_title));
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvSum.setText(getFormattedBalance(transferTemplate.Amount, transferTemplate.getCurrency()));

            if (transferTemplate.isStandingInstruction()) {
                holder.btnSwitchOff.setVisibility(View.VISIBLE);
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
                holder.tvStatus.setVisibility(View.GONE);
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy));
                //holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
            }
        }
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

    public View.OnClickListener onClick(final RecyclerView.ViewHolder mHolder, final Templates templates) {
        paymentTemplateOperation = new PaymentTemplateOperationImpl();
        paymentTemplateOperation.registerCallBack(this);

        transferTemplateOperation = new TransferTemplateOperationImpl();
        transferTemplateOperation.registerTransferCallBack(this);
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
//                            if (templateTransfer.ProductType!=110)
                            openTransferFragment(mHolder.getAdapterPosition(), Constants.TAG_CHANGE);
                        } else if (templates instanceof TemplatesPayment) {
                            openPaymentFragment(mHolder.getAdapterPosition(), Constants.TAG_CHANGE);
                        }
                        break;
                    case R.id.btnPay:
                        if (templates instanceof TemplatesPayment) {
                            openPaymentFragment(mHolder.getAdapterPosition(), CLICK_ITEM_TAG);
                        } else if (templates instanceof TemplateTransfer) {
                            TemplateTransfer templateTransfer = (TemplateTransfer) templates;
                            Log.i("ChargesType", "templateTransfer.ProductType = " + templateTransfer.ProductType);
//                            if (templateTransfer.ProductType!=110)
                            GeneralManager.setIsFromTemplate(false);
                            openTransferFragment(mHolder.getAdapterPosition(), Constants.CLICK_ITEM_TAG);
                        }
                        break;
                    case R.id.btnSwitchOff:
                        if (templates instanceof TemplatesPayment) {
                            TemplatesPayment templatesPayment = (TemplatesPayment) templates;
                            int checkInvoice = ((TemplateActivity) context).checkInvoice(templatesPayment, Constants.TAG_PAY);
                            if (checkInvoice == 0 || checkInvoice == 1) {
                                paymentTemplateOperation.changeActivePaymentTemplate(context, templatesPayment.id, templatesPayment.autoPayStatus2 != 1);
                            } else {
                                paymentTemplateOperation.changeActivePaymentTemplate(context, templatesPayment.id, templatesPayment.autoPayStatus2 != 1);
                            }
                        } else if (templates instanceof TemplateTransfer) {
                            TemplateTransfer templatesTransfer = (TemplateTransfer) templates;
                            transferTemplateOperation.changeActiveTransferTemplate(context, templatesTransfer.getId(), templatesTransfer.StandingInstructionStatus != 1);
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void deleteTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            TransferAndPayment transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerUpdateCallBack(this);
            transferAndPayment.getTransferTemplate(context);
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
            TransferAndPayment transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerUpdateCallBack(this);
            transferAndPayment.getPaymentSubscriptions(context);
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
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == 0) {
            try {
                data.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
        } else {
            getErrorDialog(errorMessage).show();
        }
    }

    @Override
    public void jsonTransferSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == 0) {
            try {
                data.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
        } else {
            getErrorDialog(errorMessage).show();
        }
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

    private Bundle getBundle(int tag, Templates templates) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("template", templates);
        bundle.putInt("actionTag", tag);
        bundle.putString("isTransferAtTempl", isTransferAtTempl);
        return bundle;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (getItemViewType() == PAYMENT_TEMPLATE) {
                                Log.e("1", "TemplateSwipeAdapter");

                                openPaymentFragment(getAdapterPosition(), Constants.CLICK_ITEM_TAG);
                                Log.i("OQKWOE", "qaweokoqwk5");
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
            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    successOperation.isTemp = true;
                }
            });
        }
    }
}
