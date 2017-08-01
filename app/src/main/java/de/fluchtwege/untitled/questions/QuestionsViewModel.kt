package de.fluchtwege.untitled.questions

import android.databinding.BaseObservable
import android.databinding.Bindable
import de.fluchtwege.untitled.BR
import de.fluchtwege.untitled.lessons.LessonsRepository
import de.fluchtwege.untitled.models.Question
import io.reactivex.Scheduler

class QuestionsViewModel(val lessonName: String, val lessonsRepository: LessonsRepository) : BaseObservable() {

    var isLoading = false
    var questions: List<Question> = emptyList()

    fun loadQuestions(onQuestionsLoaded: () -> Unit,  scheduler: Scheduler) = lessonsRepository
            .getLesson(lessonName)
            .observeOn(scheduler)
            .doOnSubscribe { setProgressVisible(true) }
            .subscribe(
                    {
                        setProgressVisible(false)
                        questions = it.questions
                        onQuestionsLoaded()
                    },
                    { onError(it) })

    private fun setProgressVisible(showProgress: Boolean) {
        isLoading = showProgress
        notifyPropertyChanged(BR.progressVisible)
    }

    private fun onError(error: Throwable) {
        setProgressVisible(false)
    }

    fun getQuestionViewModel(position: Int) = QuestionViewModel(questions[position])

    fun getNumberOfQuestions() = questions.size

    @Bindable
    fun isProgressVisible()  = isLoading


}