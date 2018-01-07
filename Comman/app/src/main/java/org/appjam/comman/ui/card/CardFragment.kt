package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidquery.AQuery
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
class CardFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var aQuery = AQuery(context)
        val cardUrl=arguments.getString("card_img")

//        var aQuery = AQuery(context)
//        val thumbnailUrl = greetingInfo?.userImg
//        aQuery.id(itemView.main_profile_img).image(thumbnailUrl)
//        var aQuery = AQuery(context)
//        val thumbnailUrl = PrefUtils.getString(context, PrefUtils.USER_THUMBNAIL)
//        aQuery.id(itemView.main_profile_img).image(thumbnailUrl)
        val v = inflater!!.inflate(R.layout.fragment_card_first, container, false)
        aQuery.id(v.findViewById<ImageView>(R.id.frag_card_img)).image(cardUrl)
        return v
    }
}

