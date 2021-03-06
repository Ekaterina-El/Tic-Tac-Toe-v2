package el.ka.tictactoe.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import el.ka.tictactoe.R
import kotlin.math.min
import kotlin.math.roundToInt

class GameBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var eventListener: GameBoardEventListener? = null

    fun setEventListener(el: GameBoardEventListener) {
        eventListener = el
    }

    private var gameType: GameType = GameType.Robot

    fun setGameType(newGameType: GameType) {
        gameType = newGameType
        startNewGame()
    }

    private var currentGameState: GameState = GameState.Game

    private fun setCurrentGameState(newGameState: GameState) {
        currentGameState = newGameState
    }

    private val winLines = mutableListOf<MutableList<Int>>()
    private val playerOChoice = mutableListOf<Int>()
    private val playerXChoice = mutableListOf<Int>()
    private val winnerChoice = mutableListOf<Int>()

    private lateinit var currentPlayer: Player
    private var boardStateList = mutableListOf<State>()
    private var boardList = mutableListOf<Rect>()

    private var boardSize = 0
    private var size = 0

    private var _countOfCells = 0
    private val countOfCells: Int
        get() = _countOfCells

    fun setCountOfCells(newSize: Int) {
        boardSize = (size / newSize)
        _countOfCells = newSize
        if (boardSize > 0) {
            initBoard()
        }
        createSolutions(newSize)
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
    private var winnerLineColor = Color.BLACK


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

    private fun setCurrentPlayer(player: Player) {
        currentPlayer = player

        if (currentGameState == GameState.Game) {
            if (
                player == Player.O
                && gameType == GameType.Robot
            ) {
                robotMove()
            }

            if (currentGameState == GameState.Game) {
                eventListener?.onChangePlayer(currentPlayer)
            }

        }
    }

    private fun startNewGame() {
        initBoardState()
        setCurrentGameState(GameState.Game)
        setCurrentPlayer(Player.X)

        playerXChoice.clear()
        playerOChoice.clear()
        winnerChoice.clear()

//        boardList

        invalidate()
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
        if (winnerChoice.size != 0) {
            drawWinnerLine(canvas)
        }
        drawBoard(canvas)
        drawCells(canvas)
        super.onDraw(canvas)
    }

    private fun robotMove() {
        var randomCell = getRandom(0, boardStateList.size - 1)
        while (boardStateList[randomCell] != State.Blank
        ) {
            randomCell = getRandom(0, boardStateList.size - 1)
        }
        val event = MotionEvent.obtain(
            SystemClock.uptimeMillis(),
            SystemClock.uptimeMillis(),
            MotionEvent.ACTION_UP,
            boardList[randomCell].exactCenterX(),
            boardList[randomCell].exactCenterY(),
            0
        )
        onTouchEvent(event)
    }

    private fun getRandom(min: Int, max: Int): Int =
        (Math.random() * (max - min) + min).roundToInt()


    private fun drawWinnerLine(canvas: Canvas) {
        paint.color = winnerLineColor

        val start = boardList[winnerChoice.first()]
        val end = boardList[winnerChoice.last()]
        path.moveTo(start.exactCenterX(), start.exactCenterY())
        path.lineTo(end.exactCenterX(), end.exactCenterY())
        canvas.drawPath(path, paint)
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
        path.reset()
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
                // ???????????? ??????????????????????
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
                // ?????????????? ??????????????????????
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

    // region ?????????????????? ??????????????
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
        if (currentGameState == GameState.Game) {
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
                findWinner()

                if (currentGameState == GameState.Game) {
                    setCurrentPlayer(currentPlayer.not())
                }
                if (isDraw()) {
                    eventListener?.onDraw()
                }
                invalidate()
            }
        } else {
            startNewGame()
        }
        return true
    }

    private fun findWinner() {
        val state = if (currentPlayer == Player.X) {
            if (playerXChoice.size < countOfCells) return
            State.Cross
        } else {
            if (playerOChoice.size < countOfCells) return
            State.Circle
        }

        when {
            isWin(state) -> Log.d("Find_Winner", "$state was won")
            isDraw() -> {
                eventListener?.onDraw()
            }
            else -> return
        }
    }

    private fun isWin(player: State): Boolean {
        for (i in 0 until winLines.size) {
            if (checkLine(player, winLines[i])) {
                setCurrentGameState(GameState.Won)
                eventListener?.onWin(player.toPlayer()!!)
                return true
            }
        }

        return false
    }

    private fun createSolutions(size: Int) {
        winLines.clear()

        // ???? ??????????????????????
        for (row in 0 until size) {
            val solution = mutableListOf<Int>()
            for (col in 0 until size) {
                solution.add(size * row + col)
            }
            winLines.add(solution)
        }

        // ???? ??????????????????
        winLines.addAll(rowToCol(winLines))


        // ??????????????????
        val d1 = mutableListOf<Int>()
        val d2 = mutableListOf<Int>()

        for ((row, col) in (0 until size).withIndex()) {
            d1.add(row * size + col)
            d2.add(row * size + (size - col - 1))
        }

        winLines.add(d1)
        winLines.add(d2)
    }

    private fun rowToCol(winLines: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val result = mutableListOf<MutableList<Int>>()

        for (col in 0 until winLines.size) {
            val r = mutableListOf<Int>()
            for (row in 0 until winLines.size) {
                r.add(winLines[row][col])
            }
            result.add(r)
        }

        return result
    }

    private fun checkLine(player: State, solution: MutableList<Int>): Boolean {
        val response = when (player) {
            State.Cross -> {
                solution.all {
                    playerXChoice.contains(it)
                }
            }
            State.Circle -> {
                solution.all {
                    playerOChoice.contains(it)
                }
            }
            else -> false
        }

        if (response) {
            winnerChoice.addAll(solution)
        }
        return response
    }

    private fun isDraw(): Boolean {
        var isDraw = playerOChoice.size + playerXChoice.size == countOfCells * countOfCells
        if (isDraw) {
            setCurrentGameState(GameState.Draw)
        }
        return isDraw
    }


    // endregion

    companion object {
        const val defCountOfCells = 3
        const val DEFAULT_STROKE_WIDTH = 2F

        enum class State {
            Blank,
            Cross,
            Circle;

            fun toPlayer(): Player? {
                return when (this) {
                    Cross -> Player.X
                    Circle -> Player.O
                    else -> null
                }
            }
        }

        enum class GameState {
            Game,
            Won,
            Draw
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

        enum class GameType {
            Robot,
            Friend
        }
    }
}

