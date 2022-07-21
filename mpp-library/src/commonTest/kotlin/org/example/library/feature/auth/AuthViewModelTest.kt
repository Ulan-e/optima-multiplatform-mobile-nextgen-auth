package org.example.library.feature.auth

import dev.icerock.moko.mvvm.test.TestViewModelScopeRule
import dev.icerock.moko.mvvm.test.createTestEventsDispatcher
import dev.icerock.moko.test.cases.InstantTaskRule
import dev.icerock.moko.test.cases.TestCases
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.forms.FormDataContent
import org.example.library.createSharedFactory
import org.example.library.feature.auth.di.AuthFactory
import org.example.library.feature.auth.presentation.AuthViewModel
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AuthViewModelTest : TestCases() {

    private val viewModelScopeRule = TestViewModelScopeRule()

    override val rules: List<Rule> = listOf(
        InstantTaskRule(),
        viewModelScopeRule
    )

    @Ignore // ignore while ios not passed
    @Test
    fun `success login`() {
        val authFactory = createAuthFactory { request ->
            when (request.url.encodedPath) {
                "/signin" -> {
                    val body = request.body as FormDataContent
                    assertEquals(expected = "am@icerock.dev", actual = body.formData["email"])
                    assertEquals(expected = "000000", actual = body.formData["password"])
                    respondOk()
                }
                "/push" -> {
                    respondOk()
                }
                else -> {
                    respondBadRequest()
                }
            }
        }
        val viewModel: AuthViewModel = authFactory.createAuthViewModel(
            eventsDispatcher = createTestEventsDispatcher()
        )

        // TODO
    }

    private fun createAuthFactory(mock: MockRequestHandler): AuthFactory {
        val sharedFactory = createSharedFactory(mock = mock)
        return sharedFactory.authFactory
    }
}
