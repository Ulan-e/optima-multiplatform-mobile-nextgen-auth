package kg.optima.mobile.android.ui.features.common.bankContacts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.welcome.WelcomeIntent
import kg.optima.mobile.common.presentation.welcome.WelcomeState
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarContent
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

object BankContacts : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val product = remember {
            CommonFeatureFactory.create<WelcomeIntent, WelcomeState>()
        }
        val state = product.state
        val intent = product.intent

        val model by state.stateFlow.collectAsState(initial = null)

        val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

        val context = LocalContext.current
        val url = "https://api.whatsapp.com/send?phone=" + 996550890380
        val whatsAppIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        val whatsAppIntentCall = Intent.createChooser(whatsAppIntent, null)

        MainContainer(
            mainState = model,
            sheetInfo = bottomSheetState.value,
            toolbarInfo = ToolbarInfo(
                navigationIcon = NavigationIcon(onBackClick = {}),
                content = ToolbarContent.Text(text = "Контакты")
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(
                        top = Deps.Spacing.standardPadding * 2, start = Deps.Spacing.spacing,
                        end = Deps.Spacing.bigMarginTop - 30.dp
                    ),
            ) {
                Row(modifier = Modifier
                    .clickable {
                        bottomSheetState.value = BottomSheetInfo(
                            title = "Приложение “Optima24” хочет открыть WhatsApp",
                            buttons = listOf(
                                ButtonView.Primary(
                                    text = "Открыть",
                                    composeColor = ComposeColor.composeColor(ComposeColors.Green),
                                    onClickListener = ButtonView.OnClickListener.onClickListener {
                                        context.startActivity(whatsAppIntentCall)
                                    }
                                ),
                                ButtonView.Transparent(
                                    text = "Отменить",
                                    onClickListener = ButtonView.OnClickListener.onClickListener {
                                        bottomSheetState.value = null
                                    })
                            )
                        )
                    }
                    .padding(bottom = Deps.Spacing.marginFromInput * 4)) {
                    Image(
                        modifier = Modifier
                            .padding(end = Deps.Spacing.standardPadding)
                            .size(Deps.Size.checkboxSize),
                        painter = painterResource(id = R.drawable.whatsapp_icon),
                        contentDescription = "whatsapp icon"
                    )
                    Column {
                        Text(
                            modifier = Modifier.padding(bottom = Deps.Spacing.swiperTopMargin),
                            fontSize = Headings.H5.sp,
                            color = ComposeColors.DarkGray,
                            lineHeight = 20.sp,
                            text = "Написать в WhatsApp"
                        )
                        Text(
                            fontSize = Headings.H2.sp,
                            fontWeight = FontWeight(500),
                            color = ComposeColors.PrimaryBlack,
                            text = "+996 (990) 90-59-59"
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .clickable {
                            context.startActivity(callMethod(phoneNumber = "9595"))
                        }
                        .padding(bottom = Deps.Spacing.marginFromInput * 4)
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = Deps.Spacing.standardPadding)
                            .size(Deps.Size.checkboxSize),
                        painter = painterResource(id = R.drawable.phone_call),
                        contentDescription = "phone_call icon"
                    )
                    Column() {
                        Text(
                            modifier = Modifier.padding(bottom = Deps.Spacing.swiperTopMargin),
                            fontSize = Headings.H5.sp,
                            color = ComposeColors.DarkGray,
                            lineHeight = 20.sp,
                            text = "Короткий номер для абонентов сети Beeline, MegaCom и O! (звонок бесплатный)"
                        )
                        Text(
                            fontSize = Headings.H2.sp,
                            color = ComposeColors.PrimaryBlack,
                            fontWeight = FontWeight(500),
                            text = "9595"
                        )
                    }
                }
                Row(modifier = Modifier
                    .clickable {
                        context.startActivity(callMethod(phoneNumber = "+996 (312) 90-59-59"))
                    }
                    .padding(bottom = Deps.Spacing.marginFromInput * 4)) {
                    Image(
                        modifier = Modifier
                            .padding(end = Deps.Spacing.standardPadding)
                            .size(Deps.Size.checkboxSize),
                        painter = painterResource(id = R.drawable.image_call_voice),
                        contentDescription = "call_center icon"
                    )
                    Column() {
                        Text(
                            modifier = Modifier.padding(bottom = Deps.Spacing.swiperTopMargin),
                            fontSize = Headings.H5.sp,
                            lineHeight = 20.sp,
                            color = ComposeColors.DarkGray,
                            text = "Круглосуточная поддержка держателей карточек"
                        )
                        Text(
                            fontSize = Headings.H2.sp,
                            color = ComposeColors.PrimaryBlack,
                            fontWeight = FontWeight(500),
                            text = "+996 (312) 90-59-59"
                        )
                    }
                }
                Row(modifier = Modifier
                    .clickable {
                        context.startActivity(callMethod(phoneNumber = "0-800-800-00-00"))
                    }
                    .padding(bottom = Deps.Spacing.marginFromInput * 4)) {
                    Image(
                        modifier = Modifier
                            .padding(end = Deps.Spacing.standardPadding)
                            .size(Deps.Size.checkboxSize),
                        painter = painterResource(id = R.drawable.image_call_person),
                        contentDescription = "cal_voicemail icon"
                    )
                    Column() {
                        Text(
                            modifier = Modifier.padding(bottom = Deps.Spacing.swiperTopMargin),
                            fontSize = Headings.H5.sp,
                            color = ComposeColors.DarkGray,
                            lineHeight = 20.sp,
                            text = "Бесплатная линия со стационарных телефонов ОАО “Кыргызтелеком”"
                        )
                        Text(
                            fontSize = Headings.H2.sp,
                            color = ComposeColors.PrimaryBlack,
                            fontWeight = FontWeight(500),
                            text = "0-800-800-00-00"
                        )
                    }
                }
            }
        }
    }

    private fun callMethod(phoneNumber: String): Intent? {
        val callIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("tel:$phoneNumber")
        }
        return Intent.createChooser(callIntent, null)
    }
}