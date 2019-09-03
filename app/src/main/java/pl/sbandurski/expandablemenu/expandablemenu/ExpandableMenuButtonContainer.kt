package pl.sbandurski.expandablemenu.expandablemenu

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import pl.sbandurski.expandablemenu.R

class ExpandableMenuButtonContainer(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    fun addButton(context: Context?, buttonId: Int?, title: CharSequence?, drawableIcon: Drawable?) : AppCompatImageView = addButton(context, buttonId, title, drawableIcon, null)

    fun addButton(context: Context?, buttonId: Int?, title: CharSequence?, drawableIcon: Drawable?, index: Int?) : AppCompatImageView {
        val optionButton = LayoutInflater.from(context).inflate(R.layout.expandablemenu_button, this, false) as AppCompatImageView
        optionButton.run {
            setImageDrawable(drawableIcon)
            contentDescription = title
            id = buttonId!!
        }
        if (index == null) addView(optionButton)
        else addView(optionButton, index)
        return optionButton
    }

    fun addSeparator(context: Context?) : View {
        val separator = LayoutInflater.from(context).inflate(R.layout.expandablemenu_separator, this, false)
        addView(separator, childCount / 2)
        return separator
    }
}