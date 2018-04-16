package com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader
import com.gigigo.orchextra.ocmsdk.R

private const val ARG_NAME = "name"
private const val ARG_IMAGE_URL = "imageUrl"

class VerticalItemPageFragment : Fragment() {

  private var name: String? = null
  private var imageUrl: String? = null

  private lateinit var verticalItemImageView: ImageView
  private lateinit var verticalItemContainer: View

  private var onClickVerticalItem: OnItemClick? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      name = it.getString(ARG_NAME)
      imageUrl = it.getString(ARG_IMAGE_URL)
    }
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater!!.inflate(R.layout.fragment_vertical_item_pager_view, container, false)

    verticalItemContainer = view.findViewById(R.id.verticalItemContainer)
    verticalItemImageView = view.findViewById(R.id.verticalItemImageView)

    return view
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    verticalItemImageView.setOnClickListener {
      onClickVerticalItem?.onClickItem(verticalItemContainer)
    }

    OcmImageLoader.load(this, imageUrl).into(verticalItemImageView)
  }

  fun setOnClickItem(onClickHorizontalItem: OnItemClick) {
    this.onClickVerticalItem = onClickHorizontalItem
  }

  interface OnItemClick {
    fun onClickItem(view: View?)
  }

  companion object {
    @JvmStatic
    fun newInstance(name: String, imageUrl: String) =
        VerticalItemPageFragment().apply {
          arguments = Bundle().apply {
            putString(ARG_NAME, name)
            putString(ARG_IMAGE_URL, imageUrl)
          }
        }
  }
}
