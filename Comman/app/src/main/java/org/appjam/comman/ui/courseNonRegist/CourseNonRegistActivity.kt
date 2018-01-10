import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidquery.AQuery
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lecture_subsection.*
import kotlinx.android.synthetic.main.lecture_subsection_chapterlist_item.view.*
import kotlinx.android.synthetic.main.lecture_subsection_course_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.PopupData
import org.appjam.comman.ui.CourseSubsection.CourseSubPopupActivity
import org.appjam.comman.ui.courseNonRegist.EnrollPopupActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads


/**
 * Created by KSY on 2018-01-04.
 */
class CourseNonRegistActivity : AppCompatActivity() {

    companion object {
        private val TAG = "CourseNonRegistActivity"
    }

    private var courseMetaData: CoursesData.CourseMetadata? = null
    private var chaptersInfoList: List<PopupData.PopupContentInfo> = listOf()
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)

        val recycler_view = lecture_subsection_list_view
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = LectureSubAdapter()

        Toast.makeText(this, intent.getIntExtra("courseID", 0).toString(), Toast.LENGTH_SHORT).show()

        disposables.add(APIClient.apiService.getCourseMetaInfo(
                PrefUtils.getUserToken(this@CourseNonRegistActivity), intent.getIntExtra("courseID", 0))  //defaultValue 0넣는게 맞을까?
                .setDefaultThreads()
                .subscribe({ response ->
                    courseMetaData = response.result[0]
                    card_lecture_name_tv.text = courseMetaData?.title
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getPopupContentInfos(
                PrefUtils.getUserToken(this@CourseNonRegistActivity), intent.getIntExtra("courseID", 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    chaptersInfoList = response.result
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val aQuery = AQuery(this@CourseNonRegistActivity)
            aQuery.id(itemView.lecture_subsection_course_profile_iv).image(courseMetaData?.supplier_thumbnail)
            itemView.lecture_subsection_course_name_tv.text = courseMetaData?.title
            itemView.lecture_subsection_instructor_name_tv.text = courseMetaData?.name
            itemView.lecture_subsection_course_exp_tv.text = courseMetaData?.info
            itemView.lecture_subsection_popup_layout.setOnClickListener {
                val intent = Intent(this@CourseNonRegistActivity, CourseSubPopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                startActivity(intent)
            }
            itemView.lecture_regist_btn.setOnClickListener {
                val intent = Intent(this@CourseNonRegistActivity, EnrollPopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                startActivity(intent)
            }
        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO 맛보기 강좌 넣을때 여기다가 추가
    }


    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = "${chaptersInfoList[position].priority}장"
//            itemView.lecture_subsection_chapterlist_totalnum_tv.text = "총 ${chaptersInfoList[position].size}강"
            itemView.lecture_subsection_chapterlist_chapname_tv.text = chaptersInfoList[position].title
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LectureSubAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ListUtils.TYPE_HEADER -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_subsection_course_item, parent, false)
                    HeaderViewHolder(view)
                }
                ListUtils.TYPE_SECOND_HEADER -> {
                    val view: View = layoutInflater.inflate(R.layout.activity_lecture_regist, parent, false)
                    SecondHeaderViewHolder(view)
                }
                ListUtils.TYPE_ELEM -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_subsection_chapterlist_item, parent, false)
                    ElemViewHolder(view)
                }
                else -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_list_footer, parent, false)
                    FooterViewHolder(view)
                }
            }
        }

        override fun getItemCount() = chaptersInfoList.size + 3

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 2)
            } else if (holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as HeaderViewHolder).bind()
            } else if (holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER) {
                (holder as SecondHeaderViewHolder)
            } else
                holder as FooterViewHolder
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER
            1 -> ListUtils.TYPE_SECOND_HEADER
            (itemCount - 1) -> ListUtils.TYPE_FOOTER
            else -> ListUtils.TYPE_ELEM

        }
    }
}