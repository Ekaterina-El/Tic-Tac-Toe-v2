package el.ka.tictactoe.ui.customView

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import el.ka.tictactoe.R
import kotlin.math.min
import kotlin.math.roundToInt

class GameBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var attr: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.GameBoardView, 0, 0)
    private val paint: Paint = Paint()
    private var size = 0F

    init {
        size = attr.getDimension(
            R.styleable.GameBoardView_size, defSize.toFloat()
        )

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 30F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)



        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(size, widthSize.toFloat()).roundToInt()
            else -> size.toInt()
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(size, heightSize.toFloat()).roundToInt()
            else -> size.toInt()
        }

        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            (width/2).toFloat(),
            (height/2).toFloat(),
            (width/2).toFloat(),
            paint
        )
        super.onDraw(canvas)

    }
    companion object {
        const val defSize = 300
    }
}