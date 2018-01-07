package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidquery.AQuery
import kotlinx.android.synthetic.main.fragment_card_first.view.*
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
            aQuery!!.id(v.frag_card_img).image(arguments.getString("image_url"))
        }
        return v
    }
}

