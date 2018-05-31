package com.gigigo.orchextra.ocm.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.gigigo.orchextra.ocmsdk.R
import kotlinx.android.synthetic.main.view_scrollable_content_arrow.view.arrow_1
import kotlinx.android.synthetic.main.view_scrollable_content_arrow.view.arrow_2
import kotlinx.android.synthetic.main.view_scrollable_content_arrow.view.arrow_3

class ScrollableContentArrow @JvmOverloads constructor(context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

  val ANIMATION_DURATION: Long = 235

  init {
    LayoutInflater.from(context).inflate(R.layout.view_scrollable_content_arrow, this, true)

    animateArrow()
  }

  private fun animateArrow() {

    arrow_1.animate().setDuration(ANIMATION_DURATION).alpha(1.0f)

    Handler().postDelayed({
      arrow_2.animate().setDuration(ANIMATION_DURATION).alpha(1.0f)
    }, ANIMATION_DURATION)

    Handler().postDelayed({
      arrow_3.animate().setDuration(ANIMATION_DURATION).alpha(1.0f)
    }, ANIMATION_DURATION * 2)

    Handler().postDelayed({
      arrow_1.animate().setDuration(ANIMATION_DURATION).alpha(0f)
      arrow_2.animate().setDuration(ANIMATION_DURATION).alpha(0f)
      arrow_3.animate().setDuration(ANIMATION_DURATION).alpha(0f)
    }, ANIMATION_DURATION * 3)

    Handler().postDelayed({
      animateArrow()
    }, ANIMATION_DURATION * 4)
  }
}