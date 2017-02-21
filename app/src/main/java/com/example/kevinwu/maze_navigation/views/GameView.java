package com.example.kevinwu.maze_navigation.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andretietz.android.controller.DirectionView;
import com.andretietz.android.controller.InputView;
import com.example.kevinwu.maze_navigation.R;
import com.example.kevinwu.maze_navigation.models.Maze;
import com.example.kevinwu.maze_navigation.models.MazeFactory;
import com.example.kevinwu.maze_navigation.models.Pair;
import com.example.kevinwu.maze_navigation.models.PlayerInfo;
import com.example.kevinwu.maze_navigation.models.Point;
import com.example.kevinwu.maze_navigation.services.BluetoothService;

import java.util.ArrayList;

/**
 * Created by Kevin on 2/10/2017.
 */

public class GameView extends RelativeLayout implements InputView.InputEventListener {

    //testing direction buttons
    private TextView textDirection;
    //display the maze number
    private TextView mazeNum;
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
    //the current point of the player
    private int currentX, currentY;
    private Maze maze;
    private PlayerInfo player;
    private Activity m_context;
    private ArrayList<Pair> mazeLinks;
    private Paint line = new Paint();
    private Paint red = new Paint();
    private Paint background = new Paint();

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    public GameView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(attributeSet, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
    }

    public GameView(Context context, Maze maze) {
        super(context);
        this.m_context = (Activity) context;
        this.maze = maze;
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
        mazeLinks = maze.getLinks();
        background.setColor(Color.LTGRAY);
        red.setColor(Color.RED);
        line.setColor(Color.BLACK);
        setWillNotDraw(false);

        LayoutInflater.from(getContext()).inflate(R.layout.activity_game, this);
        DirectionView directionView = (DirectionView) findViewById(R.id.viewDirection);
        directionView.setOnButtonListener(this);
        textDirection = (TextView) findViewById(R.id.textView);
        mazeNum = (TextView) findViewById(R.id.mazeNumber);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = (w < h) ? w : h;
        height = width;         //for now square mazes
        lineWidth = 1;          //for now 1 pixel wide walls
        cellWidth = (width - ((float) mazeSizeX * lineWidth)) / mazeSizeX;
        totalCellWidth = cellWidth + lineWidth;
        cellHeight = (height - ((float) mazeSizeY * lineWidth)) / mazeSizeY;
        totalCellHeight = cellHeight + lineWidth;
        red.setTextSize(cellHeight * 0.75f);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        //fill in the background
        super.onDraw(canvas);
        canvas.drawRect(0, 0, width, height, background);
        boolean[][] hLines = maze.getHorizontalLines();
        boolean[][] vLines = maze.getVerticalLines();
        //iterate over the boolean arrays to draw walls
        for (int i = 0; i < mazeSizeX; i++) {
            for (int j = 0; j < mazeSizeY; j++) {
                float x = j * totalCellWidth;
                float y = i * totalCellHeight;
                if (j < mazeSizeX - 1 && vLines[i][j]) {
                    //we'll draw a vertical line
                    canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                }
                if (i < mazeSizeY - 1 && hLines[i][j]) {
                    //we'll draw a horizontal line
                    canvas.drawLine(x,               //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
            }
        }
        currentX = maze.getCurrentX();
        currentY = maze.getCurrentY();
        //draw the ball
        canvas.drawCircle((currentX * totalCellWidth) + (cellWidth / 2),   //x of center
                (currentY * totalCellHeight) + (cellWidth / 2),  //y of center
                (cellWidth * 0.45f),                           //radius
                red);

        player.setPlayerColor(red);
        player.setPlayerX(currentX);
        player.setPlayerY(currentY);
        player.setPlayerMazeNum(maze.getMazeNum());

        // draw the maze link location indicators
        for(int i = 0; i < mazeLinks.size(); i++){
            Point point = (Point) mazeLinks.get(i).getPoint();
            String linkDirection = (String) mazeLinks.get(i).getDirection();
            canvas.drawText(String.valueOf(linkDirection.charAt(0)),
                    (point.getX() * totalCellWidth) + (cellWidth * 0.25f),
                    (point.getY() * totalCellHeight) + (cellHeight * 0.75f),
                    red);
        }
        //draw the finishing point indicator
//        canvas.drawText("F",
//                (mazeFinishX * totalCellWidth) + (cellWidth * 0.25f),
//                (mazeFinishY * totalCellHeight) + (cellHeight * 0.75f),
//                red);
        System.out.println("Maze number: " + maze.getMazeNum());
        mazeNum.setText(String.format("Maze #%d", maze.getMazeNum()));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// Controller Methods //////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onInputEvent(View view, int buttons) {
        switch (view.getId()) {
            case R.id.viewDirection:
                //Log.d("Kevin", "buttons: " + buttons);
                textDirection.setText(String.format("Action: %s", directionButtonsToString(buttons)));
                playerMove(directionButtonsToString(buttons)); // player can go up down left right
                break;
        }
    }

    private String directionButtonsToString(int buttons) {
        String direction = "NONE";
        switch (buttons&0xff) {
            case DirectionView.DIRECTION_DOWN:
                direction = "Down";
                break;
            case DirectionView.DIRECTION_LEFT:
                direction = "Left";
                break;
            case DirectionView.DIRECTION_RIGHT:
                direction = "Right";
                break;
            case DirectionView.DIRECTION_UP:
                direction = "Up";
                break;
            case DirectionView.DIRECTION_DOWN_LEFT:
                direction = "Down Left";
                break;
            case DirectionView.DIRECTION_UP_LEFT:
                direction = "Up Left";
                break;
            case DirectionView.DIRECTION_DOWN_RIGHT:
                direction = "Down Right";
                break;
            case DirectionView.DIRECTION_UP_RIGHT:
                direction = "Up Right";
                break;

        }
        return direction;
    }

    private boolean playerMove(String direction){
        boolean moved = false;

        for(int i = 0; i < mazeLinks.size(); i++){
            Point point = (Point) mazeLinks.get(i).getPoint();
            String linkDirection = (String) mazeLinks.get(i).getDirection();
            if(currentX == point.getX() && currentY == point.getY() &&
                    direction.equals(linkDirection))
            {
                Maze nextMaze = MazeFactory.getMaze(point.getMazeLink());
                GameView nextGameView = new GameView(m_context, nextMaze);
                m_context.setContentView(nextGameView);
            }
        }

        switch(direction) {
            case "Up":
                moved = maze.move(Maze.UP);
                break;
            case "Down":
                moved = maze.move(Maze.DOWN);
                break;
            case "Right":
                moved = maze.move(Maze.RIGHT);
                break;
            case "Left":
                moved = maze.move(Maze.LEFT);
                break;
        }
        if(moved) {
            //the ball was moved so we'll redraw the view
            invalidate();
        }
        return true;
    }
}
