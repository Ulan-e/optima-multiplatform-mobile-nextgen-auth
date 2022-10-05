package kz.optimabank.optima24.fragment.requests;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;

/**
 * Created by Max on 10.05.2018.
 */

public class CancelFragment extends ATFFragment{
    @BindView(R.id.edComment) EditText edComment;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.btnSaveCommit) Button btnSaveCommit;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_cancel, container, false);
        ButterKnife.bind(this, view);
        initToolBar();
        btnSaveCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edComment.getText().toString().isEmpty()){
                    edComment.setError(getString(R.string.error_empty));
                    //Toast.makeText(getActivity(),getString(R.string.at_you),Toast.LENGTH_LONG).show();
                } else {
                    Intent result = new Intent();
                    result.putExtra("COMMENT" , edComment.getText().toString());
                    getActivity().setResult( CommonStatusCodes.SUCCESS, result);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    private void initToolBar() {
        tvTitle.setText(getString(R.string.comment));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
