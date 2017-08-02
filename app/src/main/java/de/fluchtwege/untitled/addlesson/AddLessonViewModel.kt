package de.fluchtwege.untitled.addlesson

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import de.fluchtwege.untitled.lessons.LessonsRepository
import de.fluchtwege.untitled.models.Lesson

class AddLessonViewModel(val lessonsRepository: LessonsRepository) : BaseObservable() {

    @Bindable
    var lessonName: String = ""

    @Bindable
    var description: String = ""

    fun save(onSaved: () -> Unit) = lessonsRepository.addLesson(Lesson(lessonName, description, emptyList()))
            .subscribe({ onSaved()}, { onError(it) })

    private fun onError(error: Throwable) {
        Log.e("ERROR", "we had a problem", error)
    }

}