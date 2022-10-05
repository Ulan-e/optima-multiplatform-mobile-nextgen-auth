package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.utility.Constants.CLICK_ITEM_TAG;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.TransferMoneyToAnotherBank;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.setPaymentImage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.fragment.template.TemplateInterface;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.base.TransferModel;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;

/**
 * Created by Timur on 10.04.2017.
 */

public class TransferAndPaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
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
    private static final String GET_BUNDLE = "getBundle";
    private static final String ADAP = "isADAP";
    private static final String TTF = "isTTF";
    private static final String ONE = "1";
    private static final String WHAT_IS = "whatIs";
    private static final String CIT = "CIT";
    private static final String PTF = "PTF";
    private static final String TIT = "TIT";

    private final OnItemClickListener mOnItemClickListener;
    private PaymentTemplateOperation paymentTemplateOperation;
    private TransferTemplateOperation transferTemplateOperation;
    TemplateActivity templateActivity = new TemplateActivity();
    private int position;
    PaymentContextController paymentController;

    public TransferAndPaymentAdapter(Context context, ArrayList<Object> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
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
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer_paymant_list, parent, false);
            viewHolder = new VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        int viewType = mHolder.getItemViewType();
        if (viewType == PAYMENT || viewType == HEADER_PAYMENT) {
            configurePaymentViewHolder(mHolder, position);
        } else if (viewType == TRANSFER || viewType == HEADER_TRANSFER) {
            configureTransferViewHolder(mHolder, position);
        } else if (viewType == PAYMENT_TEMPLATE) {
            configurePaymentTemplateViewHolder(mHolder, position);
        } else if (viewType == TRANSFER_TEMPLATE) {
            configureTransferTemplateViewHolder(mHolder, position);
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
            }
        }
        return -1000;
    }

    private void configurePaymentViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        PaymentCategory paymentCategory = (PaymentCategory) data.get(position);
        if (paymentCategory != null) {
            if (mHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader) mHolder;
                holder.textviewName.setText(paymentCategory.getName());
            } else {
                VHItem holder = (VHItem) mHolder;
                holder.tvTitle.setText(paymentCategory.getName());
                if (paymentCategory.getExternalId() != null) {
                    setPaymentImage(context, holder.imgItem, paymentCategory.alias);
                }
                holder.tvType.setVisibility(View.GONE);
                holder.tvSum.setVisibility(View.GONE);
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
            if (name.equals(context.getString(R.string.transfer_card))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_internal));
            } else if (name.equals(context.getString(R.string.transfer_swift_tenge))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_local));
            } else if (name.equals(context.getString(R.string.transfer_swift))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_swift));
            }
            holder.tvType.setVisibility(View.GONE);
            holder.tvSum.setVisibility(View.GONE);
        }
    }

    private void configurePaymentTemplateViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        TemplatesPayment paymentTemplate = (TemplatesPayment) data.get(position);
        VHSItem holder = (VHSItem) mHolder;
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
            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy_2));
        } else {
            holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy));
        }
    }

    private void configureTransferTemplateViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        TemplateTransfer transferTemplate = (TemplateTransfer) data.get(position);
        if (transferTemplate != null) {
            Templates template = (TemplateTransfer) data.get(position);
            VHSItem holder = (VHSItem) mHolder;
            holder.tvTitle.setText(transferTemplate.getName());
            holder.tvType.setText(context.getResources().getString(R.string.transfer_title));
            holder.tvPay.setText(R.string.transfer_action);
            holder.tvSum.setText(getFormattedBalance(/*context,*/transferTemplate.Amount, transferTemplate.getCurrency()));
            holder.btnDelete.setOnClickListener(onClick(holder, template));
            holder.btnChange.setOnClickListener(onClick(holder, template));
            holder.btnPay.setOnClickListener(onClick(holder, template));
            holder.btnSwitchOff.setOnClickListener(onClick(holder, template));

            if (transferTemplate.isStandingInstruction()) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy_2));
                //holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart_auto_payment));
                switch (transferTemplate.getStandingInstructionStatus()) {
                    case 1:
                        holder.tvStatus.setText(R.string.auto_payment_activ);
                        break;
                    case 2:
                        holder.tvStatus.setText(R.string.auto_payment_stop);
                        break;
                    case 3:
                        holder.tvStatus.setText(R.string.auto_payment_stop_by_manager);
                        break;
                    case 4:
                        holder.tvStatus.setText(R.string.auto_payment_out_of_count);
                        break;
                }
            } else {
                holder.tvStatus.setVisibility(View.GONE);
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_copy));
                // holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
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
            TransferAndPayment transferAndPayment = new TransferAndPaymentImpl();
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

    }

    @Override
    public void deletePaymentTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            TransferAndPayment transferAndPayment = new TransferAndPaymentImpl();
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
            intent.putExtra(WHAT_IS, PTF);
        }
        context.startActivity(intent);
    }

    @Override
    public void openTransferFragment(int position, int tag) {
        TemplateTransfer templatesTransfer = (TemplateTransfer) data.get(position);

        if (templatesTransfer.ProductType != 110) {
            Intent intent = new Intent(context, TemplateActivity.class);
            intent.putExtra(GET_BUNDLE, getBundle(tag, templatesTransfer));
            intent.putExtra(ADAP, ONE);
            intent.putExtra(TTF, ONE);

            if (templatesTransfer.getProductType() == TransferMoneyToAnotherBank) {
                intent.putExtra(WHAT_IS, TIT);
            } else {
                intent.putExtra(WHAT_IS, TTF);
            }
            context.startActivity(intent);
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

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvSum)
        TextView tvSum;
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.tvStatus)
        TextView tvStatus;

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
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

        public VHSItem(View itemView) {
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
                    Log.d("TAG", "position = " + mHolder.getAdapterPosition());
                    Toast.makeText(context, "SwitchOff", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
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
                    context.startActivity(intent);
                }
            });
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
