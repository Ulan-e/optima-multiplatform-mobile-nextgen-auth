package kz.optimabank.optima24.fragment.account;

import static android.app.Activity.RESULT_OK;
import static kz.optimabank.optima24.utility.Constants.IS_SHOW_TIP_DEF_CARD;
import static kz.optimabank.optima24.utility.Constants.SUCCESS;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.overlay.BalloonOverlayAnimation;
import com.skydoves.balloon.overlay.BalloonOverlayCircle;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.activity.AccountDetailsActivity;
import kz.optimabank.optima24.activity.InterfaceFormActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.controller.adapter.AccountListAdapter;
import kz.optimabank.optima24.controller.adapter.CardsAdapter;
import kz.optimabank.optima24.db.controllers.DigitizedCardController;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.account.model.UrgentMessage;
import kz.optimabank.optima24.fragment.account.viewModel.AccountsListViewModel;
import kz.optimabank.optima24.fragment.account.viewModel.AccountsViewModelFactory;
import kz.optimabank.optima24.model.gson.response.Data;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.SmsWithTextSend;
import kz.optimabank.optima24.model.manager.CardStatusChangeEvent;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountsImpl;
import kz.optimabank.optima24.model.service.CardDataImpl;
import kz.optimabank.optima24.model.service.CategoriesImpl;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.utility.AnimationUtils;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.IntegerListener;
import kz.optimabank.optima24.utility.Utilities;
import okhttp3.OkHttpClient;

public class AccountListFragment extends ATFFragment implements CategoriesImpl.Callback,
        AccountsImpl.Callback, SwipeRefreshLayout.OnRefreshListener, CardDataImpl.Callback, CardsAdapter.CardClickListener, SmsWithTextImpl.SendOtpWithTextCallback, SmsWithTextImpl.IsOtpKeyValidCallback, AccountsListViewModel.UrgentMessageListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.cards_recycler)
    RecyclerView cards_recyclerView;
    @BindView(R.id.urgent_message_layout)
    ConstraintLayout urgentMessageLayout;
    @BindView(R.id.text_urgent_message)
    TextView textUrgentMessage;
    @BindView(R.id.btn_close)
    ImageButton btnClose;

    ArrayList<UserAccounts> account;
    Accounts accounts;
    AccountListAdapter adapter;
    IntegerListener.ObservableInteger obsInt;
    static int i;
    int itog;
    int loadedURLs = 0;
    int totalLoaded = 0;

    boolean isClickableRecyclerView, hasRefresh;
    private Picasso picasso;
    ArrayList<UserAccounts.Cards> cardsWithImageUrl = new ArrayList<>();
    ArrayList<Target> targetListM = new ArrayList<>();
    ArrayList<Target> targetListF = new ArrayList<>();
    private ProgressDialog progressDialog;
    GeneralManager generalManager;
    private static final int FOOTER_REQUEST = -3;
    private final ArrayMap<String, DigitizedCard> digitizedCardsMap = new ArrayMap<>();
    DigitizedCardController digitizedCardController = null;

    private ArrayList<Data> listData = new ArrayList<>();

    private static final int PHONE_NUMBER_CHANGED_STATUS_CODE = -10011;
    private static final int INCORRECT_SMS_CODE = -10001;
    private static final int USER_BLOCKED = -100;
    private static final int HANDLER_DELAY = 1000;
    private static final int CHANGE_NUMBER_OTP_KEY = 8;
    private static final int SMS_CODE_LENGTH = 4;

    private SmsWithTextSend smsWithTextSend;
    private AlertDialog alertDialog;
    private View viewAlertDialog;
    private AccountsListViewModel model;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        model = new ViewModelProvider(this, new AccountsViewModelFactory(requireActivity()
                .getApplication()))
                .get(AccountsListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        hideSoftKeyBoard(getActivity());
        initSwipeRefreshLayout();
        view.findViewById(R.id.open_amount).setOnClickListener(new View.OnClickListener() {        // Клик по кнопке Открытие счёта
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InterfaceFormActivity.class);
                requireActivity().startActivity(intent);
            }
        });
        Log.d("onCreateView", " = " + GeneralManager.getInstance().getStatements().isEmpty());
        obsInt = new IntegerListener.ObservableInteger();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (GeneralManager.getInstance().getSessionId() != null) {
            if (accounts == null)
                accounts = new AccountsImpl();
            accounts.registerCallBack(this);

            smsWithTextSend = new SmsWithTextImpl();
            smsWithTextSend.registerSmsSendOtpKeyValidation(this);
            smsWithTextSend.registerOtpKeyValidation(this);
            Log.d("onActivityCreated", "-GM.isNeedToUpdateAccounts() = " + GeneralManager.getInstance().isNeedToUpdateAccounts());
            if (GeneralManager.getInstance().getAllCards().isEmpty()) {
                accounts.getAccounts(getActivity(), false, false);
                showProgressIfUpdating();
            } else {
                setAdapter();
            }

        }
    }

    private void showTipDefaultCard(){
        if(adapter != null) {
            adapter.setAnchorViewListener(imageView -> {
                Balloon balloon = new Balloon.Builder(requireContext())
                        .setWidthRatio(1.0f)
                        .setHeight(BalloonSizeSpec.WRAP)
                        .setLayout(R.layout.layout_tip)
                        .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
                        .setArrowPosition(0.92f)
                        .setIsVisibleOverlay(true)
                        .setOverlayColorResource(R.color.black_alpha_0)
                        .setOverlayPadding(6f)
                        .setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
                        .setDismissWhenClicked(false)
                        .setArrowSize(10)
                        .setMarginHorizontal(10)
                        .setCornerRadius(10f)
                        .setDismissWhenClicked(false)
                        .setDismissWhenTouchOutside(false)
                        .setDismissWhenOverlayClicked(false)
                        .setDismissWhenLifecycleOnPause(false)
                        .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .setOverlayShape(new BalloonOverlayCircle(1f))
                        .build();

                    if(!balloon.isShowing()){
                    balloon.showAlignBottom(imageView);
                }

                AppCompatButton btnClose = balloon.getContentView().findViewById(R.id.btn_ok);
                btnClose.setOnClickListener(v -> balloon.dismiss());
            });
        }
    }

    // получаем активное срочное сообщения для пользователя
    private void getUrgentMessage() {
        model.registerListener(this);
        model.requestNoticeMessage();
    }

    // показываем срочное сообщение
    private void showAlertUrgentMessage(UrgentMessage message) {
        urgentMessageLayout.setVisibility(View.VISIBLE);
        textUrgentMessage.setText(message.getMessage());

        btnClose.setOnClickListener(view -> {
            AnimationUtils.goneView(urgentMessageLayout);
            OptimaApp.Companion.getInstance().changeUrgentMessageState(true);
        });
    }

    @Override
    public void onStart() {
        Log.d("onStart", "-GM.isNeedToUpdateAccounts = " + GeneralManager.getInstance().isNeedToUpdateAccounts());
        if (GeneralManager.getInstance().isNeedToUpdateAccounts()) {
            getAccounts(true);
        }

        if (GeneralManager.isUpdateAccList()) {
            GeneralManager.setUpdateAccList(false);
            Log.d("onStart-", "GM.isUpdateAccList = " + GeneralManager.isUpdateAccList());
            getAccounts(false);
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        isClickableRecyclerView = true;
        Log.d("onResume", "-GM.isNeedToUpdateAccounts = " + GeneralManager.getInstance().isNeedToUpdateAccounts());
        if (!GeneralManager.getInstance().isNeedToUpdateAccounts()) {
            setAdapter();
        }

        if (GeneralManager.getInstance().isNeedToUpdateAfterDefaultStatusCard()) {
            accounts.getAccounts(getActivity(), true, true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        swipeRefreshLayout.setRefreshing(false);
        if (hasRefresh && accounts != null) {
            accounts.cancelAccountsRequest();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
       // releaseTipShow();
    }

    private void releaseTipShow(){
        SharedPreferences.Editor editor = Utilities.getPreferences(requireContext()).edit();
        editor.putBoolean(IS_SHOW_TIP_DEF_CARD, false);
        editor.apply();
    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        if (accounts == null) {
            accounts = new AccountsImpl();
            accounts.registerCallBack(this);
            accounts.getAccounts(getActivity(), true, true);
        } else {
            accounts.getAccounts(getActivity(), true, true);
        }
    }

    private void startDetailsActivity(UserAccounts account) {
        Intent intent = new Intent(parentActivity, AccountDetailsActivity.class);
        intent.putExtra(AccountDetailsActivity.ACCOUNT, account);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setupContactlessCards();
            }
        }
    }

    @Override
    public void jsonAccountsResponse(int statusCode, String errorMessage) {
        if (isAttached()) {
            if (progressDialog != null) {
                stopProgress();
            }
            if (statusCode == SUCCESS) {
                loadedURLs = 0;
                totalLoaded = 0;
                setAdapter();
                Log.e("success","success");
                showTipDefaultCard();
                GeneralManager.getInstance().setNeedToUpdateAccounts(false);
                GeneralManager.getInstance().setNeedToUpdateAfterDefaultStatusCard(false);
                getUrgentMessage();
            } else if (statusCode == PHONE_NUMBER_CHANGED_STATUS_CODE) {
                GeneralManager.getInstance().setNeedToUpdateAccounts(false);

                // отправка запроса на потверждение нового номера телефона
                smsWithTextSend.sendOtpWithText(requireContext(), CHANGE_NUMBER_OTP_KEY);
            } else {
                if (errorMessage != null) {
                    onError(errorMessage);
                }
            }
        }
    }

    @Override
    public void jsonAccountsOperationsResponse(int statusCode, String errorMessage) {
        if (statusCode == SUCCESS) {
//            if (isAttached())
//                initChart();
        } else {
//            chartNoDat();
            if (errorMessage != null) {
                onError(errorMessage);
            }
        }
    }

    // потверждение смс кода при смене номера телефона
    @SuppressLint("InflateParams")
    private void showInputSmsCodeDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        viewAlertDialog = inflater.inflate(R.layout.dialog_input_sms_code, null);
        alertDialog = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setView(viewAlertDialog).create();
        EditText inputCode = viewAlertDialog.findViewById(R.id.input_sms_code);
        Button btnSend = viewAlertDialog.findViewById(R.id.btn_send_sms_code);

        btnSend.setOnClickListener(button -> {
            String smsCode = inputCode.getText().toString();
            if (!smsCode.isEmpty() && smsCode.length() == SMS_CODE_LENGTH) {
                if (smsWithTextSend != null) {
                    // отправляем СМС-код потверждения на сервер
                    smsWithTextSend.sendSmsForOtpValidation(
                            requireContext(),
                            CHANGE_NUMBER_OTP_KEY,
                            smsCode
                    );
                }
            } else {
                Snackbar.make(viewAlertDialog, requireContext().getString(R.string.text_sms), Snackbar.LENGTH_SHORT).show();
            }
        });

        Window window = alertDialog.getWindow();
        if (window != null) window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CardStatusChangeEvent event) {
        Log.d("TAG", "onMessageEvent AccountListFragment");
        if (isAttached()) {
            setupContactlessCards();
        }
    }

    private void setAdapter() {
        if (isAdded()) {
            final Intent[] intent = new Intent[1];
            account = GeneralManager.getInstance().getThreeAll(getActivity());
            //account.add(new UserAccounts(FOOTER_REQUEST));
            if (!GeneralManager.getInstance().hasVisibleAccounts())
                account.add(new UserAccounts(-4));
            adapter = new AccountListAdapter(requireContext(), account, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                    ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (account != null && !account.isEmpty() && isClickableRecyclerView) {
                                isClickableRecyclerView = false;
                                UserAccounts userAccounts = account.get(position);
                                if (userAccounts.code == FOOTER_REQUEST) {
                                    intent[0] = new Intent(getActivity(), NavigationActivity.class);
                                    intent[0].putExtra("isRequests", true);
                                    startActivity(intent[0]);
                                } else if (userAccounts.code != Constants.HEADER_ID) {
                                    startDetailsActivity(userAccounts);
                                }
                            }
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                        }
                    };
                    clickAnimation(view, animatorListener);
                }
            });

            //Log.i("loadAllImage", "GeneralManager.getInstance().getAllCards() = " + GeneralManager.getInstance().getAllCards());
            cardsWithImageUrl.clear();
            for (UserAccounts.Cards card : GeneralManager.getInstance().getAllCards()) {
                if ((card.imageList.full != null && !card.imageList.full.isEmpty() ||
                        card.imageList.miniature != null && !card.imageList.miniature.isEmpty())) {
                    cardsWithImageUrl.add(card);
                }
            }
            try {
                Log.i("loadAllImage", "cardsWithImageUrl = " + cardsWithImageUrl);
                if (cardsWithImageUrl.size() * 2 != loadedURLs && totalLoaded < 2)
                    loadAllImage(cardsWithImageUrl);
                else
                    recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                adapterReady();
                //recyclerView.setAdapter(adapter);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 0);
        }
    }

    private void loadAllImage(ArrayList<UserAccounts.Cards> accounts) {
        final int allURLs = accounts.size() * 2;
        loadedURLs = 0;
        totalLoaded++;

        obsInt.set(loadedURLs);
        Log.i("loadAllImage", " obsInt.get()= " + obsInt.get() + "     " + loadedURLs);
        obsInt.setOnIntegerChangeListener(new IntegerListener.OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                Log.i("loadAllImage", " onIntegerChanged = " + newValue);
                if (allURLs == loadedURLs) {
                    Log.i("loadAllImage", " allURLs == loadedURLs");
                    adapterReady();
                    //recyclerView.setAdapter(adapter);
                }
            }
        });
        targetListF.clear();
        targetListM.clear();
        if (generalManager == null) {
            generalManager = GeneralManager.getInstance();
        }
        try {
            for (final UserAccounts.Cards account : accounts) {
                if (account.imageList.miniature != null && !account.imageList.miniature.isEmpty() ||
                        account.imageList.full != null && !account.imageList.full.isEmpty())
                    loadMinBitmap(account, account.imageList.miniature, account.imageList.full);
            }
        } catch (Exception ignored) {
        }

        if (totalLoaded >= 2) {
            adapterReady();
            //recyclerView.setAdapter(adapter);
        } else if (allURLs != loadedURLs) {
            loadAllImage(cardsWithImageUrl);
        }
    }

    public void writeObject(Bitmap bitmap, boolean full, UserAccounts.Cards card) throws IOException {
        try {
            if (generalManager == null) {
                generalManager = GeneralManager.getInstance();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,true);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 50, byteArrayOutputStream); //TODO will need be tested
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            //byteArrayOutputStream.toByteArray();
            if (full) {
                generalManager.fullBimapForCards.put(card.code, byteArray);
                //card.fullImStr = byteArray;
                Log.i("USERAccBitmap", "byteArray full = " + byteArray.length);
                //fullImStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                generalManager.minBimapForCards.put(card.code, byteArray);
                //card.miniatureImStr = byteArray;
                Log.i("USERAccBitmap", "byteArray min = " + byteArray.length);
                //miniatureImStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        } catch (Exception ignored) {
        }
    }

    private void adapterReady() {
        recyclerView.setAdapter(adapter);
    }

    private void loadMinBitmap(final UserAccounts.Cards account, String minURL, String fullURL) {
        if (picasso == null) {
            final OkHttpClient client = ServiceGenerator.okHttpClient(getActivity(), false);
            picasso = new Picasso.Builder(getActivity())
                    .downloader(new OkHttp3Downloader(client))
                    .build();
        }

        Log.i("loadAllImage", "minURL = " + (!minURL.isEmpty() ? minURL : "absent"));
        Log.i("loadAllImage", "fullURL = " + (!fullURL.isEmpty() ? fullURL : "absent"));

        targetListM.add(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Log.i("loadAllImage", " write");
                    writeObject(bitmap, false, account);
                    //account.writeObject(bitmap,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadedURLs++;
                obsInt.set(loadedURLs);
                Log.i("loadAllImage", " success");
                updateImagesForDigitizedCard(account, false);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("loadAllImage", "failed");
                loadedURLs++;
                obsInt.set(loadedURLs);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        if (!minURL.isEmpty()) {
            picasso.load(Constants.API_BASE_URL + minURL)
                    .into(targetListM.get(targetListM.size() - 1));
        } else {
            loadedURLs++;
            obsInt.set(loadedURLs);
            Log.i("loadAllImage", " loadedURLs++ = " + loadedURLs);
        }

        targetListF.add(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Log.i("loadAllImage", " write");
                    //account.writeObject(bitmap,true);
                    writeObject(bitmap, true, account);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadedURLs++;
                obsInt.set(loadedURLs);
                Log.i("loadAllImage", " success");
                updateImagesForDigitizedCard(account, true);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                loadedURLs++;
                obsInt.set(loadedURLs);
                Log.i("loadAllImage", "failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        if (!fullURL.isEmpty()) {
            picasso.load(Constants.API_BASE_URL + fullURL)
                    .into(targetListF.get(targetListF.size() - 1));
        } else {
            loadedURLs++;
            obsInt.set(loadedURLs);
            Log.i("loadAllImage", " loadedURLs++ = " + loadedURLs);
        }
    }

    private void updateImagesForDigitizedCard(UserAccounts.Cards card, boolean isFull) {
        if (digitizedCardController == null)
            digitizedCardController = DigitizedCardController.getController();
        if (digitizedCardsMap == null)
            if (digitizedCardsMap.isEmpty()) {
                Log.i("byteArrayFullImgUPDATE", "111");
                ArrayList<DigitizedCard> digitizedCards = digitizedCardController.getAllCards(getPreferences(OptimaApp.Companion.getInstance()).getString(Constants.userPhone, ""));
                for (DigitizedCard digitizedCard : digitizedCards) {
                    digitizedCardsMap.put(digitizedCard.getRbsNumber(), digitizedCard);
                }
            }
        if (isFull)
            digitizedCardController.updateImagesDigitizedCardFull(digitizedCardsMap.get(card.rbsNumber), card.getByteArrayFullImg());
        else
            digitizedCardController.updateImagesDigitizedCardMiniature(digitizedCardsMap.get(card.rbsNumber), card.getByteArrayMiniatureImg());
    }

    private void initSwipeRefreshLayout() {
        int actionBarSize = getActionBarSize(getActivity());
        int progressViewStart = getResources().getDimensionPixelSize(R.dimen.app_bar_height) - actionBarSize;
        int progressViewEnd = progressViewStart + (int) (actionBarSize * 1.5f);
//        swipeRefreshLayout.setProgressViewOffset(true, - actionBarSize, actionBarSize);
        swipeRefreshLayout.setProgressViewEndTarget(true, actionBarSize);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));

        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void jsonCategoriesResponse(int statusCode, String errorMessage) {
        if (statusCode == SUCCESS) {
        }
    }

    private void setupContactlessCards() {
    }

    // получаем счета и карты пользователя
    private void getAccounts(boolean isNeedUpdate) {
        if (accounts != null) {
            accounts = new AccountsImpl();
            accounts.registerCallBack(this);
            accounts.getAccounts(getActivity(), true, isNeedUpdate);
        }
    }

    // возвращаемся в главный экран с задержкой
    private void backToMainScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            Intent intent = new Intent(requireActivity(), UnauthorizedTabActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        }, HANDLER_DELAY);
    }

    public void showProgressIfUpdating() {
        if (progressDialog == null) {
            progressDialog = Utilities.progressDialog(getActivity(), getActivity().getResources().getString(R.string.t_loading));
        }
        progressDialog.show();
    }

    public void stopProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onSuccessfulResponse(int statusCode, String errorMessage, List<Data> dataList) {   //Принятие данных с CardImpl
        if (statusCode == 200 && dataList != null) {
            listData.addAll(dataList);
        }
    }

    @Override
    public void onCardClick(Data cardModel) {    // Слушать клика по списку
        Intent intent = new Intent(getContext(), InterfaceFormActivity.class);
        intent.putExtra("number_card", "9999-9999-9999-9999");
        startActivity(intent);
    }

    @Override
    public void setResponseSendOtpWithText(int statusCode, String errorMessage) {
        if (statusCode == SUCCESS) {
            showInputSmsCodeDialog();
        } else {
            onError(errorMessage);
            backToMainScreen();
        }
    }

    @Override
    public void setResponseIsOtpKeyValid(int statusCode, String errorMessage) {
        if (statusCode == SUCCESS && alertDialog != null) {
            alertDialog.dismiss();
            getAccounts(true);
        } else if (statusCode == INCORRECT_SMS_CODE) {
            if (viewAlertDialog != null) {
                Snackbar.make(viewAlertDialog, errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        } else if (statusCode == USER_BLOCKED) {
            Snackbar.make(viewAlertDialog, errorMessage, Snackbar.LENGTH_SHORT).show();

            backToMainScreen();
        }
    }

    @Override
    public void setMessage(UrgentMessage urgentMessage) {
        if (urgentMessage != null) {
            if (urgentMessage.getActive()) {

                // проверяем не нажата ли была кнопка закрытия срочного уведомления
                if (!OptimaApp.Companion.getInstance().getUrgentMessageState()) {
                    showAlertUrgentMessage(urgentMessage);
                }
            }
        }
    }
}

