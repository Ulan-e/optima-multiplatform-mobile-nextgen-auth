package kz.optimabank.optima24.notifications.ui.notificationsList;

import static org.koin.java.KoinJavaComponent.inject;
import static kz.optimabank.optima24.utility.Constants.BANK_ID;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import kg.optima.mobile.R;
import kg.optima.mobile.databinding.FragmentNotificationListBinding;
import kg.optima.mobile.feature.auth.component.AuthPreferences;
import kotlin.Lazy;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.notifications.models.items.BaseNotificationItem;
import kz.optimabank.optima24.notifications.ui.adapter.NotificationsAdapter;
import kz.optimabank.optima24.notifications.ui.notificationDetails.NotificationDetailsFragment;

public class NotificationListFragment extends ATFFragment {

    private static final String IS_FROM_SINGLE_NOTIFICATION = "isFromSingleNotification";
    private static final String NOTIFICATION_ID = "notificationId";

    private Lazy<AuthPreferences> authPreferences = inject(AuthPreferences.class);

    private FragmentNotificationListBinding binding;
    private NotificationsListViewModel model;
    private String bankId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbar.setNavigationOnClickListener(toolbar -> navigateToMenuActivity());

        initViewModel();
        getBankId();
        getUnreadNotifications();
        initSwipeRefresh();
        getLoadingState();
        checkIfFromSingleNotification();
        handleOnBackPressed();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initViewModel() {
        model = new ViewModelProvider(
                this,
                new NotificationsListViewModelFactory(requireActivity().getApplication())
        ).get(NotificationsListViewModel.class);
    }

    private void initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(this::getUnreadNotifications);
        binding.swipeRefreshLayout.setColorSchemeColors(getResources()
                .getIntArray(R.array.swipe_refresh_color)
        );
    }

    // получение айди банка пользователя
    private void getBankId() {
        SharedPreferences preferences = getPreferences(requireContext());
        if(preferences.getString(BANK_ID, null) != null) {
            bankId = preferences.getString(BANK_ID, "");
        }else {
           bankId = authPreferences.getValue().getUserInfo().getBankId();
        }
    }

    private void getLoadingState() {
        binding.loadingProgress.setVisibility(View.VISIBLE);
        model.loadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (!isLoading) {
                binding.loadingProgress.setVisibility(View.GONE);
            }
        });
    }

    // метод для получения непрочитанных сообщений
    private void getUnreadNotifications() {
        if (bankId != null) {
            model.getAllNotifications(bankId).observe(getViewLifecycleOwner(), items -> {
                binding.swipeRefreshLayout.setRefreshing(false);

                if (items != null) {
                    setAdapter(items);
                }
            });
        }
    }

    private void setAdapter(List<BaseNotificationItem> notificationList) {
        setNotificationsPlaceholder(notificationList);

        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(itemClickListener());
        notificationsAdapter.setData(notificationList);
        binding.notificationsRecyclerView.setAdapter(notificationsAdapter);
    }

    // показываем текст если нет уведомлений
    public void setNotificationsPlaceholder(List<BaseNotificationItem> notificationList) {
        if (notificationList.size() == 0) {
            binding.textEmptyPlaceholder.setVisibility(View.VISIBLE);
            binding.textEmptyPlaceholder.setText(
                    getActivity().getResources().getString(R.string.empty_notifications_placeholder)
            );
        } else {
            binding.textEmptyPlaceholder.setVisibility(View.GONE);
        }
    }

    private NotificationsAdapter.NotificationClickListener itemClickListener() {
        return (view, notificationId) -> {
            ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {
                }

                @Override
                public void onAnimationEnd(View view) {
                    openNotificationDetailsScreen(notificationId);
                }

                @Override
                public void onAnimationCancel(View view) {
                }
            };
            clickAnimation(view, animatorListener);
        };
    }

    private void openNotificationDetailsScreen(String notificationId) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(
                        R.id.fragment_container_view,
                        NotificationDetailsFragment.newInstance(notificationId),
                        null
                )
                .commit();
    }

    private void checkIfFromSingleNotification() {
        if (getArguments().getBoolean(IS_FROM_SINGLE_NOTIFICATION)) {
            openNotificationsDetails(getArguments().getString(NOTIFICATION_ID));
            getArguments().clear();
        }
    }

    // экран детали уведомления
    private void openNotificationsDetails(String notificationId) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, NotificationDetailsFragment.newInstance(notificationId))
                .commit();
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToMenuActivity();
            }
        });
    }

    private void navigateToMenuActivity() {
        Intent intent = new Intent(requireContext(), MenuActivity.class);
        startActivity(intent);
    }
}