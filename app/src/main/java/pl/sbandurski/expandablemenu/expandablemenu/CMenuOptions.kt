package pl.sbandurski.expandablemenu.expandablemenu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.annotation.MenuRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import android.transition.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import kotlinx.android.synthetic.main.expandablemenu_layout.view.*
import kotlinx.android.synthetic.main.expandablemenu_separator.view.*
import pl.sbandurski.expandablemenu.R


class CMenuOptions(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mMenu: Menu? = null
    private var mSeparator: View? = null
    private var mBackground: View? = null
    private var mButtonContainer: ExpandableMenuButtonContainer? = null
    private var mClickListener: View.OnClickListener? = null
    private var mIsAnimating = false
    private var mIsOpen = false

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        Log.d("TEST123", "CMenuOptions -> constructor($context, $attrs)")
        setOnClickListener(this)
    }

    constructor(context: Context) : this(context, null, 0) {
        Log.d("TEST123", "CMenuOptions -> constructor($context)")
    }

    companion object {
        const val NO_DIMENSION = 1
        const val CLOSE_MORPH_TRANSFORM_DURATION: Long = 70
    }

    init {
        Log.d("TEST123", "CMenuOptions -> init()")
        initViews(context)
        setInitialFabIcon()
    }

    private fun initViews(context: Context) {
        Log.d("TEST123", "CMenuOptions -> initViews($context)")
        inflate(context, R.layout.expandablemenu_layout, this)
        expandableMenu_fab.setOnClickListener(this)
        mBackground = expandableMenu_background
        mButtonContainer = expandableMenu_button_container
    }

    private fun setInitialFabIcon() =
        expandableMenu_fab.setImageResource(R.drawable.expandablemenu_ic_overflow)

    override fun setOnClickListener(listener: View.OnClickListener) {
        Log.d("TEST123", "CMenuOptions -> setOnClickListener($listener)")
        mClickListener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("TEST123", "CMenuOptions -> onTouchEvent($event)")
        return super.onTouchEvent(event)
    }

    /**
     * OnClick floating action button.
     * */
    override fun onClick(v: View?) {
        Log.d("TEST123", "CMenuOptions -> onClick($v), $mIsOpen")
        when (v?.id) {
            R.id.expandableMenu_fab -> {
                if (!mIsAnimating) {
                    mIsAnimating = true
                    if (v?.id == R.id.expandableMenu_fab) {
                        if (mIsOpen) collapse(null)
                        else expand(null)
                    } else {
                        if (mIsOpen) {
                            mClickListener?.onClick(v)
                            collapse(null)
                        }
                    }
                }
            }
        }
    }

    fun setButtonsMenu(@MenuRes menuId: Int) = setButtonsMenu(context, menuId)

    @SuppressLint("RestrictedApi")
    fun setButtonsMenu(context: Context?, @MenuRes menuId: Int) {
        mMenu = MenuBuilder(context)
        val inflater = SupportMenuInflater(context)
        inflater.inflate(menuId, mMenu)
        addButonsFromMenu(context, mMenu)
//        mSeparator = mButtonContainer?.addSeparator(context)
        animateButtons(false)
    }

    private fun addButonsFromMenu(context: Context?, menu: Menu?) {
        for (i in 0 until menu!!.size()) {
            addButton(context, menu.getItem(i))
        }
    }

    private fun addButton(context: Context?, item: MenuItem?) {
        val button = mButtonContainer?.addButton(context, item?.itemId, item?.title, item?.icon)
        button?.setOnClickListener(this)
    }

    private fun animateButtons(isOpen: Boolean) {
        Log.d("TEST123", "CMenuOptions -> animateButtons($isOpen)")
        for (i in 0 until mButtonContainer!!.childCount) {
            when (isOpen) {
                true -> 1
                else -> 0
            }.apply {
                mButtonContainer?.getChildAt(i)?.scaleX = this.toFloat()
                mButtonContainer?.getChildAt(i)?.scaleY = this.toFloat()
            }
        }
    }

    private fun animateBackground(isOpen: Boolean) {
        Log.d("TEST123", "CMenuOptions -> animateBackground($isOpen)")
        val params = mBackground?.layoutParams
        params?.width = when (isOpen) {
            true -> (mButtonContainer!!.measuredWidth).also { expandableMenu_fab!!.animate().rotation(200f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(300).start() }
            else -> NO_DIMENSION.also { expandableMenu_fab!!.animate().rotation(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(300).start() }
        }
        mBackground?.layoutParams = params
    }

    fun open(listener: ExpandableMenuAnimationStateListener) = expand(listener)

    fun close(listener: ExpandableMenuAnimationStateListener) = collapse(listener)

    private fun expand(listener: ExpandableMenuAnimationStateListener?) {
        Log.d("TEST123", "CMenuOptions -> expand($listener)")
        val drawable = resources.getDrawable(
            R.drawable.expandablemenu_ic_menu_animatable,
            null
        ) as AnimatedVectorDrawable
        expandableMenu_fab.setImageDrawable(drawable)
        drawable.start()

        val transitions = OpenMorphTransition(mButtonContainer as ViewGroup)
        transitions.addListener(object : Transition.TransitionListener {

            override fun onTransitionEnd(transition: Transition?) {
                listener?.onOpenAnimationEnd()
                mIsAnimating = false
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }
        })
        TransitionManager.beginDelayedTransition(this, transitions)

        animateBackground(true)
        animateButtons(true)

        mIsOpen = true
    }

    private fun collapse(listener: ExpandableMenuAnimationStateListener?) {
        val drawable = resources.getDrawable(
            R.drawable.expandablemenu_ic_close_animatable,
            null
        ) as AnimatedVectorDrawable
        expandableMenu_fab.setImageDrawable(drawable)
        drawable.start()

        val transitions = CloseMorphTransition(mButtonContainer as ViewGroup)
        transitions.addListener(object : Transition.TransitionListener {

            override fun onTransitionEnd(transition: Transition?) {
                listener?.onCloseAnimationEnd()
                mIsAnimating = false
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }
        })
        TransitionManager.beginDelayedTransition(this, transitions)

        animateBackground(false)
        animateButtons(false)

        mIsOpen = false
    }

    private class OpenMorphTransition(viewGroup: ViewGroup) : TransitionSet() {

        init {
            val changeBound = ChangeBounds()
            changeBound.excludeChildren(R.id.expandableMenu_button_container, true)
            addTransition(changeBound)

            val transform = ChangeTransform()
            for (i in 0 until viewGroup.childCount) {
                transform.addTarget(viewGroup.getChildAt(i))
            }
            addTransition(transform)

            ordering = TransitionSet.ORDERING_SEQUENTIAL
        }
    }

    private class CloseMorphTransition(viewGroup: ViewGroup) : TransitionSet() {

        init {
            val changeBound = ChangeBounds()
            changeBound.excludeChildren(R.id.expandableMenu_button_container, true)

            val transform = ChangeTransform()
            for (i in 0 until viewGroup.childCount) {
                transform.addTarget(viewGroup.getChildAt(i))
            }
            transform.duration = CLOSE_MORPH_TRANSFORM_DURATION
            addTransition(transform)
            addTransition(changeBound)

            ordering = TransitionSet.ORDERING_SEQUENTIAL
        }
    }
}