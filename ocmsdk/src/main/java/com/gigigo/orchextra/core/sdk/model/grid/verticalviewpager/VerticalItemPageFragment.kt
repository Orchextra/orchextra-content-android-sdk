package com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader
import com.gigigo.orchextra.ocm.OCManager
import com.gigigo.orchextra.ocm.customProperties.ViewLayer
import com.gigigo.orchextra.ocm.customProperties.ViewType
import com.gigigo.orchextra.ocmsdk.R
import timber.log.Timber

private const val ARG_NAME = "name"
private const val ARG_IMAGE_URL = "imageUrl"
private const val ARG_CUSTOM_PROPERTIES = "custom_properties"

class VerticalItemPageFragment : Fragment() {

  private var name: String? = null
  private var imageUrl: String? = null
  private var customProperties: HashMap<String, Any>? = null

  private lateinit var verticalItemImageView: ImageView
  private lateinit var verticalItemContainer: View
  private lateinit var progress: ProgressBar
  private lateinit var layerView: ViewGroup
  private var isLoading = false

  private var onClickVerticalItem: OnItemClick? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      name = it.getString(ARG_NAME)
      imageUrl = it.getString(ARG_IMAGE_URL)
      customProperties = it.getSerializable(ARG_CUSTOM_PROPERTIES) as HashMap<String, Any>?
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_vertical_item_pager_view, container, false)

    verticalItemContainer = view.findViewById(R.id.verticalItemContainer)
    verticalItemImageView = view.findViewById(R.id.verticalItemImageView)
    progress = view.findViewById(R.id.notification_progress)
    layerView = view.findViewById(R.id.layer_view)

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    verticalItemImageView.setOnClickListener {
      onClickVerticalItem?.onClickItem(verticalItemContainer)
    }

    Timber.d("Name: %s", name)
    Timber.d("Image: %s", imageUrl)
    OcmImageLoader.load(this, imageUrl).into(verticalItemImageView)

    hideLoading()
    layerView.removeAllViews()

    if (customProperties != null) {
      showLoading()
      OCManager.notifyCustomizationForContent(customProperties!!, ViewType.GRID_CONTENT
      ) { customizations ->
        for (viewCustomizationType in customizations) {

          if (viewCustomizationType is ViewLayer) {
            val view = viewCustomizationType.view
            layerView.addView(view)
          }
        }
        hideLoading()
      }
    }

  }

  fun setOnClickItem(onClickHorizontalItem: OnItemClick) {
    this.onClickVerticalItem = onClickHorizontalItem
  }

  interface OnItemClick {
    fun onClickItem(view: View?)
  }

  private fun showLoading() {
    progress.visibility = View.VISIBLE
    isLoading = true
  }

  private fun hideLoading() {
    progress.visibility = View.GONE
    isLoading = false
  }

  companion object {
    @JvmStatic
    fun newInstance(name: String, imageUrl: String, customProperties: HashMap<String, Any>) =
        VerticalItemPageFragment().apply {
          arguments = Bundle().apply {
            putString(ARG_NAME, name)
            putString(ARG_IMAGE_URL, imageUrl)
            putSerializable(ARG_CUSTOM_PROPERTIES, customProperties)
          }
        }
  }
}
