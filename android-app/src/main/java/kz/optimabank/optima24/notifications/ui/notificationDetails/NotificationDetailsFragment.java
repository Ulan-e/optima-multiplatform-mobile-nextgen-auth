package kz.optimabank.optima24.notifications.ui.notificationDetails;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import kz.optimabank.optima24.databinding.FragmentNotificationDetailsBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.NotificationIdBody;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kz.optimabank.optima24.utility.Constants.BANK_ID;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;
import static kz.optimabank.optima24.utility.DateConverterUtils.stringDateTimeFromDate;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

public class NotificationDetailsFragment extends ATFFragment {

    private FragmentNotificationDetailsBinding binding;
    private NotificationsDetailsViewModel model;
    private String notificationId;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentNotificationDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbar.setNavigationOnClickListener(toolbar ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
        getArgs();
        initViewModel();

        makeAndGetReadNotification();
        getLoadingState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initViewModel() {
        model = new ViewModelProvider(
                this,
                new NotificationsDetailsViewModelFactory(requireActivity().getApplication())
        ).get(NotificationsDetailsViewModel.class);
    }

    private void getArgs() {
        Bundle args = getArguments();
        notificationId = args != null ? args.getString(NOTIFICATION_ARG_ID) : null;
        Log.d(PUSH_TAG, "NotificationDetails received notificationId " + notificationId);
    }

    private void makeAndGetReadNotification() {
        if (notificationId == null) return;
        String bankId = getBankId();
        NotificationIdBody requestBody = getRequestBody(notificationId);

        model.getReadNotification(bankId, requestBody).observe(
                getViewLifecycleOwner(),
                this::showNotificationInfo
        );
    }

    private void getLoadingState() {
        model.loadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading != null) {
                if (isLoading) {
                    binding.scrollableContent.setVisibility(GONE);
                    binding.loadingProgress.setVisibility(VISIBLE);
                } else {
                    binding.loadingProgress.setVisibility(GONE);
                    binding.scrollableContent.setVisibility(VISIBLE);
                }
            }
        });
    }

    // получение айди банка пользователя
    private String getBankId() {
        SharedPreferences preferences = getPreferences(requireContext());
        return preferences.getString(BANK_ID, "");
    }

    // request body для запроса
    private NotificationIdBody getRequestBody(String notificationId) {
        NotificationIdBody body = new NotificationIdBody();
        body.setNotificationId(notificationId);
        return body;
    }

    // показываем инфо о уведомлении
    private void showNotificationInfo(Notification notification) {
        if (notification != null) {
            binding.titleNotification.setText(notification.getTitle());
            binding.messageNotification.setText(notification.getText());
            String textDate = stringDateTimeFromDate(notification.getSentDate());
            binding.dateNotification.setText(textDate);
        }
    }

    public static NotificationDetailsFragment newInstance(String notificationId) {
        NotificationDetailsFragment fragment = new NotificationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(NOTIFICATION_ARG_ID, notificationId);
        fragment.setArguments(args);
        return fragment;
    }
}