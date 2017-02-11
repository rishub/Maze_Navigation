package com.example.kevinwu.maze_navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Kevin on 2/10/2017.
 */

public class GameView extends View {

    //width and height of the whole maze and width of lines which
    //make the walls
    private int width, height, lineWidth;
    //size of the maze i.e. number of cells in it
    private int mazeSizeX, mazeSizeY;
    //width and height of cells in the maze
    float cellWidth, cellHeight;
    //the following store result of cellWidth+lineWidth
    //and cellHeight+lineWidth respectively
    float totalCellWidth, totalCellHeight;
    //the finishing point of the maze
    private int mazeFinishX, mazeFinishY;
    public static Maze maze;
    private Activity m_context;
    private Paint line = new Paint();
    private Paint red = new Paint();
    private Paint background = new Paint();
    private Paint my_color = new Paint();

    public GameView(Context context) {
        super(context);
        Maze maze = MazeFactory.getMaze(1);
        init(null, 0, maze);
    }

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        Maze maze = MazeFactory.getMaze(1);
        init(attributeSet, 0, maze);
    }

    public GameView(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        Maze maze = MazeFactory.getMaze(1);
        init(attributeSet, defStyle, maze);
    }

        private void init(AttributeSet attrs, int defStyle, Maze maze){
        my_color.setColor(Color.RED);
        background.setColor(Color.LTGRAY);
        red.setColor(Color.RED);
        line.setColor(Color.WHITE);
        this.maze = maze;
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
//        setFocusable(true);
//        this.setFocusableInTouchMode(true);
    }

    public GameView(Context context, Maze maze){
        super(context);
        this.m_context = (Activity) context;
        this.maze = maze;
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
        my_color.setColor(Color.RED);
        background.setColor(Color.LTGRAY);
        red.setColor(Color.RED);
        line.setColor(Color.WHITE);
        setFocusable(true);
        this.setFocusableInTouchMode(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = (w < h)?w:h;
        height = width;         //for now square mazes
        lineWidth = 1;          //for now 1 pixel wide walls
        cellWidth = (width - ((float)mazeSizeX*lineWidth)) / mazeSizeX;
        totalCellWidth = cellWidth+lineWidth;
        cellHeight = (height - ((float)mazeSizeY*lineWidth)) / mazeSizeY;
        totalCellHeight = cellHeight+lineWidth;
        red.setTextSize(cellHeight*0.75f);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        //fill in the background
        super.onDraw(canvas);
        canvas.drawRect(0, 0, width, height, background);
        boolean[][] hLines = maze.getHorizontalLines();
        boolean[][] vLines = maze.getVerticalLines();
        //iterate over the boolean arrays to draw walls
        for(int i = 0; i < mazeSizeX; i++) {
            for(int j = 0; j < mazeSizeY; j++){
                float x = j * totalCellWidth;
                float y = i * totalCellHeight;
                if(j < mazeSizeX - 1 && vLines[i][j]) {
                    //we'll draw a vertical line
                    canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                }
                if(i < mazeSizeY - 1 && hLines[i][j]) {
                    //we'll draw a horizontal line
                    canvas.drawLine(x,               //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
            }
        }
        int currentX = maze.getCurrentX(),currentY = maze.getCurrentY();
        //draw the ball
        canvas.drawCircle((currentX * totalCellWidth)+(cellWidth/2),   //x of center
                (currentY * totalCellHeight)+(cellWidth/2),  //y of center
                (cellWidth*0.45f),                           //radius
                red);
        //draw the finishing point indicator
        canvas.drawText("F",
                (mazeFinishX * totalCellWidth)+(cellWidth*0.25f),
                (mazeFinishY * totalCellHeight)+(cellHeight*0.75f),
                red);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent evt) {
//        boolean moved;
//        switch(keyCode) {
//            case KeyEvent.KEYCODE_DPAD_UP:
//                moved = maze.move(Maze.UP);
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                moved = maze.move(Maze.DOWN);
//                break;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                moved = maze.move(Maze.RIGHT);
//                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                moved = maze.move(Maze.LEFT);
//                break;
//            default:
//                return super.onKeyDown(keyCode,evt);
//        }
//        if(moved) {
//            //the ball was moved so we'll redraw the view
//            invalidate();
//        }
//        return true;
//    }
}
