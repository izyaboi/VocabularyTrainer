package de.fluchtwege.untitled.lessons

import de.fluchtwege.untitled.models.Lesson
import de.fluchtwege.untitled.models.Question
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LessonsRepository {

    fun getLessons() : Flowable<List<Lesson>>

    fun getLesson(name: String) : Flowable<Lesson>

    fun addQuestion(question: Question) : Completable
}