package de.fluchtwege.untitled.lessons

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import de.fluchtwege.untitled.R
import de.fluchtwege.untitled.Untitled
import de.fluchtwege.untitled.addlesson.AddLessonActivity
import de.fluchtwege.untitled.databinding.FragmentLessonsBinding
import de.fluchtwege.untitled.persistance.RepositoryId
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LessonsFragment : Fragment() {

    @Inject
    lateinit var lessonsRepository: LessonsRepository

    @Inject
    lateinit var repositoryId: RepositoryId

    lateinit var viewModel: LessonsViewModel
    lateinit var lessonAdapter: LessonAdapter

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Untitled.appComponent.inject(this)
        repositoryId.saveId("ocuhd")
        viewModel = LessonsViewModel(lessonsRepository)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentLessonsBinding.inflate(inflater!!)
        val layoutManager = LinearLayoutManager(context)

        lessonAdapter = LessonAdapter(viewModel, context)

        binding.lessons.adapter = lessonAdapter
        binding.lessons.layoutManager = layoutManager
        binding.lessons.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        disposable = viewModel.loadLessons(lessonAdapter::notifyDataSetChanged)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_lessons, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_lesson -> addLesson()
            R.id.load_vocabulary -> loadVocabulary()
            R.id.store_vocabulary -> storeVocabulary()
            R.id.clear_vocabulary -> clearVocabulary()
        }
        return true
    }

    private fun addLesson() {
        val openAddLesson = Intent(context, AddLessonActivity::class.java)
        startActivity(openAddLesson)
    }

    private fun clearVocabulary() {
        disposable = viewModel.clearRepository()

    }

    private fun storeVocabulary() {
        disposable = viewModel.storeVocabulary()
    }

    private fun loadVocabulary() {
        disposable = viewModel.loadVocabulary(this::notifyChanges)

    }

    private fun notifyChanges() {
        lessonAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}