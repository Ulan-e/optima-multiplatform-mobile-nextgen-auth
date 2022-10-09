package kz.optimabank.optima24.activity;

import android.os.Bundle;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.settings.CardLimitListFragment;

public class CardLimitActivity extends OptimaActivity {
    private static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_limit);
        beginTransaction();
        if (savedInstanceState == null) {
            beginTransaction();
        }
    }

    private void beginTransaction() {
        CardLimitListFragment cardLimitListFragment = new CardLimitListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CODE, getIntent().getIntExtra(CODE, 0));
        cardLimitListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, cardLimitListFragment, null)
                .commit();
    }
}