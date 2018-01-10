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

    var aQuery : AQuery? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_first, container, false)

        if(arguments != null) {
            aQuery = AQuery(context)
            val img_url = arguments.getString("image_url")
            aQuery!!.id(v.findViewById<ImageView>(R.id.frag_card_img)).image(img_url)
        }
        return v
    }
}

