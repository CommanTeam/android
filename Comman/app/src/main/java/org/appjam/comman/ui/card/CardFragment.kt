package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.androidquery.AQuery
import com.google.gson.Gson
import org.appjam.comman.R
import org.appjam.comman.network.data.CardData

/**
 * Created by KSY on 2017-12-31.
 */
class CardFragment : Fragment(){
    var aQuery : AQuery? = null
    private var cardResponse : CardData.CardResponse? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

//        var aQuery = AQuery(context)
//        val thumbnailUrl = greetingInfo?.userImg
//        aQuery.id(itemView.main_profile_img).image(thumbnailUrl)
//        var aQuery = AQuery(context)
//        val thumbnailUrl = PrefUtils.getString(context, PrefUtils.USER_THUMBNAIL)
//        aQuery.id(itemView.main_profile_img).image(thumbnailUrl)
        val v = inflater!!.inflate(R.layout.fragment_card_first, container, false)

        if(arguments != null) {
            aQuery = AQuery(context)
            val gson = Gson()
            Toast.makeText(context, arguments.getString("cardInfoList"), Toast.LENGTH_SHORT).show()
            cardResponse = gson.fromJson(arguments.getString("cardInfoList"), CardData.CardResponse::class.java)
            val position = arguments.getInt("position")
            val image_url = cardResponse!!.result[position].image_path
            aQuery!!.id(v.findViewById<ImageView>(R.id.frag_card_img)).image(image_url)
        }
        return v
    }

}

