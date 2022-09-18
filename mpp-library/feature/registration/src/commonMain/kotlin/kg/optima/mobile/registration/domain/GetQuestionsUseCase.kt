package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.domain.model.QuestionEntity
import kg.optima.mobile.registration.domain.model.QuestionsListEntity

class GetQuestionsUseCase(
	private val registrationRepository: RegistrationRepository,
) : BaseUseCase<Unit, QuestionsListEntity>() {

	override suspend fun execute(model: Unit): Either<Failure, QuestionsListEntity> {

		// TODO мок воспросы
		val result = mutableListOf(
			QuestionEntity("Какие 5 последних цифр вашей кредитной карты?", "1"),
			QuestionEntity("Как звали вашего лучшего друга детства?", "2"),
			QuestionEntity("Какое имя вашей бабушки? ", "3"),
			QuestionEntity("Какая кличка вашего животного?", "4"),
			QuestionEntity("Какая кличка вашего животного?", "5"),
			QuestionEntity("Ваш любимый цвет?", "6")
		)
		var success = true
		registrationRepository.getQuestions().map {
			it.data?.map {
				result.add(
					QuestionEntity(
						question = it.question,
						questionId = it.questionId,
					)
				)
			}
			success = it.isSuccess
		}
		return Either.Right(QuestionsListEntity(success,result))
	}

}