package pl.sbandurski.expandablemenu.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private val size = 300
    private val backgroundColor = Color.BLUE

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL

        val radius = size / 2f

        canvas?.drawCircle(size / 2f, size / 2f, radius, paint)
    }

}