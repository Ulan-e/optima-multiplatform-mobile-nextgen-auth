package kg.optima.mobile.base.di

import org.koin.core.component.KoinComponent
import org.koin.dsl.ModuleDeclaration

interface IntentFactory<IH, SM> : KoinComponent {
	val intentHandler: IH
	val stateMachine: SM
}