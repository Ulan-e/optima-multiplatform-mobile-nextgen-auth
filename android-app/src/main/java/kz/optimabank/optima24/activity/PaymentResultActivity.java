package kz.optimabank.optima24.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;

public class PaymentResultActivity extends OptimaActivity {
    @BindView(R.id.tvResultText) TextView tvResultText;
    @BindView(R.id.imgResult) ImageView imgResult;
    @BindView(R.id.btnBack) Button btnBack;

    public static final String KEY_RESULT = "KEY_RESULT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_result_activity);
        ButterKnife.bind(this);

        boolean success = getIntent().getBooleanExtra(KEY_RESULT, false);

        if (success) {
            tvResultText.setText(getResources().getString(R.string.payment_accepted_for_processing));
        } else {
            tvResultText.setText(getResources().getString(R.string.error_contactless_payment));
            imgResult.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
