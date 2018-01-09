package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidquery.AQuery
import com.google.gson.Gson
import org.appjam.comman.R
import org.appjam.comman.network.data.CardData
import org.appjam.comman.util.PrefUtils

/**
 * Created by KSY on 2017-12-31.
 */
class CardFragment : Fragment(){
    var aQuery : AQuery? = null
    private var cardResponse : CardData.CardResponse? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_first, container, false)

        if(arguments != null) {
            aQuery = AQuery(context)
            val gson = Gson()
//            Toast.makeText(context, arguments.getString("cardInfoList"), Toast.LENGTH_SHORT).show()
//            cardResponse = gson.fromJson(arguments.getString("cardInfoList"), CardData.CardResponse::class.java)
//            var position = 0
//            if(arguments.getInt("position") != null)
//                position = arguments.getInt("position")
            val img_url = arguments.getString("image_url")
//            val image_url = cardResponse!!.result[position].image_path
            aQuery!!.id(v.findViewById<ImageView>(R.id.frag_card_img)).image(img_url)
           // Toast.makeText(context, "굳 : " + arguments.getInt("position").toString() + "잡 : " + arguments.getInt("course_ID"), Toast.LENGTH_SHORT).show()
            PrefUtils.putCurrentLecturePosition(context,arguments.getInt("position") )
            PrefUtils.putLectureOfCoursePosition(context, arguments.getInt("position"), arguments.getInt("course_ID"))
        }
        return v
    }
}

