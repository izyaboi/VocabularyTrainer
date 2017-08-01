package de.fluchtwege.untitled.lessons

import de.fluchtwege.untitled.models.Lesson
import de.fluchtwege.untitled.models.Question
import de.fluchtwege.untitled.persistance.UntitledDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RoomLessonRepository(val lessonsController: LessonsController, val database: UntitledDatabase) : LessonsRepository {

    override fun getLesson(name: String) = database
            .lessonDao()
            .getLesson(name)
            .subscribeOn(Schedulers.io())

    override fun getLessons(): Flowable<List<Lesson>> = Flowable
            .merge(database.lessonDao().getLessons(), loadLessons())
            .subscribeOn(Schedulers.io())
            .filter { result -> !result.isEmpty() }

    override fun addQuestion(question: Question) = Completable.complete()

    private fun loadLessons() = lessonsController
            .getLessons()
            .delay(5000, TimeUnit.MILLISECONDS)//just to test
            .doOnNext {
                // database.lessonDao().clearLessons()
                for (lesson in it) {
                    database.lessonDao().insert(lesson)
                }
            }
}