package el.ka.tictactoe.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import el.ka.tictactoe.R
import kotlin.math.min

class GameBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val playerOChoice = mutableListOf<Int>()
    private val playerXChoice = mutableListOf<Int>()

    private lateinit var currentPlayer: Player
    private var boardStateList = mutableListOf<State>()
    private var boardList = mutableListOf<Rect>()

    private var boardSize = 0
    private var size = 0

    private var redrawBoard = false

    private var _countOfCells = 0
    val countOfCells: Int
        get() = _countOfCells

    fun setCountOfCells(newSize: Int) {
        boardSize = (size / newSize)
        _countOfCells = newSize
        if (boardSize > 0) {
            initBoard()
        }
    }

    private var attr: TypedArray =
        context.theme.obtainStyledAttributes(attrs, R.styleable.GameBoardView, 0, 0)

    private val path = Path()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE

        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        strokeWidth = density * DEFAULT_STROKE_WIDTH
    }

    private var borderColor = Color.BLACK
    private var crossColor = Color.BLACK
    private var circleColor = Color.BLACK

    init {
        startNewGame()
        borderColor = attr.getColor(
            R.styleable.GameBoardView_borderColor,
            Color.BLACK
        )

        crossColor = attr.getColor(
            R.styleable.GameBoardView_crossColor,
            Color.BLACK
        )

        circleColor = attr.getColor(
            R.styleable.GameBoardView_circleColor,
            Color.BLACK
        )

        val boardSize = attr.getInteger(
            R.styleable.GameBoardView_countOfCells,
            defCountOfCells
        )
        setCountOfCells(
            boardSize
        )

    }

    private fun startNewGame() {
        currentPlayer = Player.X
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(size, widthSize)
            else -> size
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(size, heightSize)
            else -> size
        }


        size = min(width, height)
        setCountOfCells(countOfCells)

        setMeasuredDimension(size, size)
    }

    private fun initBoard() {
        initBoardState()
        generateBoard()
        redrawBoard = true
    }

    private fun initBoardState() {
        boardStateList.clear()

        for (index in 1..(countOfCells * countOfCells)) {
            boardStateList.add(State.Blank)
        }
    }

    private fun generateBoard() {
        var row = 0
        val cells = countOfCells * countOfCells
        boardList.clear()

        for (index in 1..cells) {
            var column = (index % countOfCells) - 1
            if (column < 0) {
                column = countOfCells - 1
            }
            val rect = Rect()
            with(rect) {
                top = row * boardSize
                left = column * boardSize
                right = left + boardSize
                bottom = top + boardSize
            }
            boardList.add(rect)

            if (index % countOfCells == 0) {
                row = index / countOfCells
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        drawBoard(canvas)
        drawCells(canvas)
        super.onDraw(canvas)

    }

    private fun drawCells(canvas: Canvas) {
        boardList.forEachIndexed { index, cell ->
            when (boardStateList[index]) {
                State.Circle -> drawCircle(canvas, cell)
                State.Cross -> drawCross(canvas, cell)
            }
        }
    }

    private fun drawCross(canvas: Canvas, block: Rect) {
        paint.color = crossColor
        val offset = ((block.right - block.left) * 0.2).toInt()
        path.moveTo(block.left.toFloat() + offset, block.top.toFloat() + offset)
        path.lineTo(block.right.toFloat() - offset, block.bottom.toFloat() - offset)
        path.moveTo(block.right.toFloat() - offset, block.top.toFloat() + offset)
        path.lineTo(block.left.toFloat() + offset, block.bottom.toFloat() - offset)
        canvas.drawPath(path, paint)
    }

    private fun drawCircle(canvas: Canvas, block: Rect) {
        paint.color = circleColor
        canvas.drawCircle(
            block.exactCenterX(),
            block.exactCenterY(),
            boardSize.toFloat() / 3,
            paint
        )
    }


    private fun drawBoard(canvas: Canvas) {
        paint.color = borderColor
        var nextLastOfRow = countOfCells - 1

        boardList.forEachIndexed { index, rect ->
            if (index < countOfCells * (countOfCells - 1)) {
                // нижний разделитель
                canvas.drawLine(
                    rect.left.toFloat(),
                    rect.bottom.toFloat(),
                    rect.right.toFloat(),
                    rect.bottom.toFloat(),
                    paint
                )
            }

            if (index == nextLastOfRow) {
                println(nextLastOfRow)
                nextLastOfRow += countOfCells
            } else {
                // боковой разделитель
                canvas.drawLine(
                    rect.right.toFloat(),
                    rect.top.toFloat(),
                    rect.right.toFloat(),
                    rect.bottom.toFloat(),
                    paint
                )
            }

        }
    }

    // region Слушатели событий
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> updateBoardState(event)
                else -> true
            }
        }
        return true
    }

    private fun updateBoardState(event: MotionEvent): Boolean {
        boardList.find { rect ->
            rect.contains(event.x.toInt(), event.y.toInt())
        }?.apply {
            val index = boardList.indexOf(this)
            if (boardStateList[index] != State.Blank) return true

            if (currentPlayer == Player.X) {
                playerXChoice.add(index)
                boardStateList[index] = State.Cross
            } else {
                playerOChoice.add(index)
                boardStateList[index] = State.Circle
            }
            //findWinner()
            currentPlayer = currentPlayer.not()
            invalidate()
        }
        return true
    }
    // endregion

    companion object {
        const val defCountOfCells = 3
        const val DEFAULT_STROKE_WIDTH = 2F

        enum class State {
            Blank,
            Cross,
            Circle
        }

        enum class Player {
            X,
            O;

            operator fun not(): Player {
                return when (this) {
                    X -> O
                    O -> X
                }
            }
        }
    }
}

/*
class GameBoardView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var boardStateList = mutableListOf<State>()
    private var boardList = mutableListOf<Rect>()

    private var attr: TypedArray =
        context.theme.obtainStyledAttributes(attrs, R.styleable.GameBoardView, 0, 0)

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE

        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        strokeWidth = density * DEFAULT_STROKE_WIDTH
    }

    private var size = 0
    private var boardSize = 300

    private var borderColor = Color.BLACK


    private var _countOfCells = 0
    val countOfCells: Int
        get() = _countOfCells

    fun setCountOfCells(newSize: Int) {
        _countOfCells = newSize
//        invalidate()
    }

    init {
        isSaveEnabled = true

        setCountOfCells(
            attr.getInteger(
                R.styleable.GameBoardView_boardSize,
                3
            )
        )

        initBoardState()
    }

    private fun initBoardState() {
        for (index in 1..(countOfCells * countOfCells)) {
            boardStateList.add(State.Blank)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /*val widthMode = MeasureSpec.getMode(widthMeasureSpec)
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

        setMeasuredDimension(width, height)*/

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size =
            widthMeasureSpec.coerceAtMost(heightMeasureSpec)

        setMeasuredDimension(size, size)
    }

    @SuppressLint("ResourceType")
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = w.coerceAtMost(h)
        boardSize = size / countOfCells
        this.generateBoard()
    }

    private fun generateBoard() {
        var row = 0
        val cells = countOfCells * countOfCells

        for (index in 1..cells) {
            var column = (index % countOfCells) - 1
            if (column < 0) {
                column = countOfCells - 1
            }
            val rect = Rect()
            with(rect) {
                top = row * boardSize
                left = column * boardSize
                right = left + boardSize
                bottom = top + boardSize
            }
            boardList.add(rect)

            if (index % countOfCells == 0) {
                row = index / countOfCells
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawBoard(canvas)
        super.onDraw(canvas)
    }

    private fun drawBoard(canvas: Canvas) {
        paint.color = borderColor
        var nextLastOfRow = countOfCells - 1

        boardList.forEachIndexed { index, rect ->
            if (index < countOfCells * (countOfCells - 1)) {
                // нижний разделитель
                canvas.drawLine(
                    rect.left.toFloat(),
                    rect.bottom.toFloat(),
                    rect.right.toFloat(),
                    rect.bottom.toFloat(),
                    paint
                )
            }

            if (index == nextLastOfRow) {
                println(nextLastOfRow)
                nextLastOfRow += countOfCells
            } else {
                // боковой разделитель
                canvas.drawLine(
                    rect.right.toFloat(),
                    rect.top.toFloat(),
                    rect.right.toFloat(),
                    rect.bottom.toFloat(),
                    paint
                )
            }

        }
    }


    companion object {
        const val DEFAULT_STROKE_WIDTH = 3F

        enum class State {
            Blank,
            Cross,
            Circle
        }
    }
}
 */

/*

private val boardSize = 300

override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    val size = w.coerceAtMost(h)
    boardSize = size / countOfCells
    this.generateBoard()
}



override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(
        (width / 2).toFloat(),
        (height / 2).toFloat(),
        (width / 2).toFloat(),
        paint
    )
    super.onDraw(canvas)

}

companion object {
    const val defSize = 300
    const val defBoardSize = 3
}*/
