package pl.sbandurski.expandablemenu

import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import android.view.View
import androidx.cardview.widget.CardView
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import pl.sbandurski.expandablemenu.expandablemenu.CMenuOptions
import pl.sbandurski.expandablemenu.expandablemenu.ExpandableMenuAnimationStateListener


class MainActivity : AppCompatActivity() {

    private var isCollapsed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        menu.setOnClickListener(this)
        menu.setButtonsMenu(this, R.menu.menu)
    }
}
