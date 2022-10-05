package kz.optimabank.optima24.fragment.account;

import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ATFAccount;

public class AccountSettingsFragment extends ATFFragment {
	ATFAccount account;

	public static AccountSettingsFragment newInstance(ATFAccount account) {
		AccountSettingsFragment fragment = new AccountSettingsFragment();
		fragment.account = account;
		return fragment;
	}
}
