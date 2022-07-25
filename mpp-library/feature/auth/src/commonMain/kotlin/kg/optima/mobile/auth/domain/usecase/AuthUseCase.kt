package kg.optima.mobile.auth.domain.usecase

import kg.optima.mobile.auth.domain.AuthModel
import kg.optima.mobile.base.domain.BaseUseCase

abstract class AuthUseCase<Model, Result : AuthModel> : BaseUseCase<Model, Result>()