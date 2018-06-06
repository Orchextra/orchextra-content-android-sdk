package com.gigigo.orchextra.core.sdk.ui.views.textviews

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import com.gigigo.orchextra.core.sdk.ui.FontCache
import com.gigigo.orchextra.ocm.OCManager
import com.gigigo.orchextra.ocmsdk.R


class OcmTextView : AppCompatTextView {

  private val tvContext: Context
  private val tvAttrs: AttributeSet?

  companion object {
    const val TEXT_TYPE_LIGHT = "0"
    const val TEXT_TYPE_MEDIUM = "1"
    const val TEXT_TYPE_NORMAL = "2"
    const val TEXT_TYPE_TITLE = "3"
  }

  constructor(context: Context) : super(context) {
    tvContext = context
    tvAttrs = null

    init()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    tvContext = context
    tvAttrs = attrs

    init()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr) {
    tvContext = context
    tvAttrs = attrs

    init()
  }

  private fun init() {
    initTypeface()
    initLetterSpacing()
    initTextChangedListener()
  }

  private fun initTextChangedListener() {
    if (isTitleTextView()) {
      addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
          if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            removeTextChangedListener(this)
            if (!s.toString().isEmpty()) {
              val newText = StringBuilder()
              val length = s.length
              for (i in 0 until length) {
                newText.append(s[i]).append(" ")
              }

              text = newText
            }
            addTextChangedListener(this)
          }
        }
      })
    }
  }

  private fun isTitleTextView(): Boolean {
    val a = context.theme.obtainStyledAttributes(
        tvAttrs,
        R.styleable.OcmTextViewTypeface,
        0, 0)

    val selectedTypeFace = a.getString(R.styleable.OcmTextViewTypeface_ocm_typeface)

    if (selectedTypeFace == TEXT_TYPE_TITLE) {
      return true
    }

    return false
  }

  private fun initLetterSpacing() {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && isTitleTextView()) {
      letterSpacing = 0.2f
    }
  }

  private fun initTypeface() {
    val injector = OCManager.getInjector()
    if (injector != null) {
      val styleUi = injector.provideOcmStyleUi()
      if (styleUi != null && !TextUtils.isEmpty(styleUi.titleFontPath)) {
        val attrTypeFace = getAttrTypeFace()

        if (attrTypeFace != null) {
          this.typeface = FontCache.getFont(tvContext, attrTypeFace)
        } else {
          this.typeface = FontCache.getFont(tvContext, styleUi.titleFontPath)
        }

      }
    }
  }

  private fun getAttrTypeFace(): String? {
    if (tvAttrs != null && context != null) {
      val a = context.theme.obtainStyledAttributes(
          tvAttrs,
          R.styleable.OcmTextViewTypeface,
          0, 0)

      try {
        val selectedTypeFace = a.getString(R.styleable.OcmTextViewTypeface_ocm_typeface)
        val provideOcmStyleUi = OCManager.getInjector().provideOcmStyleUi()

        return when (selectedTypeFace) {
          TEXT_TYPE_LIGHT -> provideOcmStyleUi.lightFontPath
          TEXT_TYPE_MEDIUM -> provideOcmStyleUi.mediumFontPath
          TEXT_TYPE_NORMAL -> provideOcmStyleUi.normalFonPath
          TEXT_TYPE_TITLE -> provideOcmStyleUi.titleFontPath
          else -> null
        }
      } finally {
        a.recycle()
      }
    }

    return null
  }
}
