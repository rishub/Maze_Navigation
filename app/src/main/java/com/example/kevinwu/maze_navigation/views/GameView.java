package com.example.kevinwu.maze_navigation.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Matrix;

import com.andretietz.android.controller.DirectionView;
import com.andretietz.android.controller.InputView;
import com.example.kevinwu.maze_navigation.R;
import com.example.kevinwu.maze_navigation.models.Maze;
import com.example.kevinwu.maze_navigation.models.MazeFactory;
import com.example.kevinwu.maze_navigation.models.Pair;
import com.example.kevinwu.maze_navigation.models.PlayerInfo;
import com.example.kevinwu.maze_navigation.models.Point;
import com.example.kevinwu.maze_navigation.models.Item;
import com.example.kevinwu.maze_navigation.models.Character;
import com.example.kevinwu.maze_navigation.services.BluetoothService;

import java.util.ArrayList;

import static android.R.attr.start;

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
    private Activity m_context;
    private ArrayList<Pair> mazeLinks;
    private ArrayList<Item> mazeItems;
    private ArrayList<Maze> mazes;
    private Paint line = new Paint();
    private Paint red = new Paint();
    private Paint door = new Paint();
    private Character character;

    private TextView numberKeys;
    private TextView numberDynamites;

    private boolean dynamiteClick = false;
    private boolean keyClick = false;

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

    public GameView(Context context, ArrayList<Maze> mazeIn, ArrayList<Item> item, Character chara, int mazeNumber) {
        super(context);
        this.m_context = (Activity) context;
        this.mazes = mazeIn;
        this.maze = mazes.get(mazeNumber);
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
        mazeLinks = maze.getLinks();
        mazeItems = item;
        red.setColor(Color.RED);
        line.setColor(Color.argb(255, 192, 107, 43));
        line.setStrokeWidth(8);
        door.setColor(Color.argb(255, 128, 128, 128));
        door.setStrokeWidth(8);
        setWillNotDraw(false);

        character = chara;

        LayoutInflater.from(getContext()).inflate(R.layout.activity_game, this);
        DirectionView directionView = (DirectionView) findViewById(R.id.viewDirection);
        directionView.setOnButtonListener(this);

        Button b_key = (Button) findViewById(R.id.item_key_button);
        b_key.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                keyClick = true;
            }
        });

        Button b_dynamite = (Button) findViewById(R.id.item_dynamite_button);
        b_dynamite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dynamiteClick = true;
            }
        });

        numberKeys = (TextView) findViewById(R.id.item_key);
        numberDynamites = (TextView) findViewById(R.id.item_dynamite);

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

        Drawable d = getResources().getDrawable(R.drawable.ground, null);
        d.setBounds(0, 0, width, height);
        d.draw(canvas);

        boolean[][] hLines = maze.getHorizontalLines();
        boolean[][] vLines = maze.getVerticalLines();
        boolean[][] vDoors = maze.getVerticalDoors();
        //iterate over the boolean arrays to draw walls
        for (int i = 0; i < mazeSizeX; i++) {
            for (int j = 0; j < mazeSizeY; j++) {
                float x = j * totalCellWidth;
                float y = i * totalCellHeight;

                if (j < mazeSizeX - 1) {
                    if (vLines[i][j]) {
                        //we'll draw a vertical line
                        canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                    }
                    if (vDoors[i][j]) {
                        canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            door);
                    }
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

        // Draw the character
        Drawable b;
        switch (character.getDirection()) {
            case "Up":
                b = getResources().getDrawable(R.drawable.player_up, null);
                break;
            case "Down":
                b = getResources().getDrawable(R.drawable.player_down, null);
                break;
            case "Left":
                b = getResources().getDrawable(R.drawable.player_left, null);
                break;
            case "Right":
                b = getResources().getDrawable(R.drawable.player_right, null);
                break;
            default:
                b = getResources().getDrawable(R.drawable.player_down, null);
                break;
        }
        float xPos = (currentX * totalCellWidth) + (cellWidth / 2);
        float yPos = (currentY * totalCellHeight) + (cellWidth / 2);
        b.setBounds((int) (xPos - cellWidth/2), (int) (yPos - cellHeight/2), (int) (xPos + cellWidth/2), (int) (yPos  + cellHeight/2));
        b.draw(canvas);

        // Draw the items
        Drawable g;
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        if (mazeItems != null) {
            for (int i = 0; i < mazeItems.size(); i++) {
                if (!mazeItems.get(i).getPickedUp()) {
                    int[] item_pos = mazeItems.get(i).getItemPosition();
                    if (item_pos[0] != maze.getMazeNum())
                        continue;
                    String item_id = mazeItems.get(i).getItemID();
                    if (currentX == item_pos[1] && currentY == item_pos[2]) {
                        System.out.println("Picked up item");
                        mazeItems.get(i).pickUp();
                        character.addItemToInventory(item_id);
                        continue;
                    }
                    switch (item_id) {  // Currently only have two items
                        case "Key":
                            g = getResources().getDrawable(R.drawable.key, null);
                            break;
                        case "Dynamite":
                            g = getResources().getDrawable(R.drawable.dynamite, null);
                            break;
                        default:
                            g = transparentDrawable;
                            break;
                    }
                    float xItemPos = (item_pos[1] * totalCellWidth) + (cellWidth / 2);
                    float yItemPos = (item_pos[2] * totalCellHeight) + (cellWidth / 2);
                    g.setBounds((int) (xItemPos - cellWidth / 3), (int) (yItemPos - cellWidth / 3), (int) (xItemPos + cellHeight / 3), (int) (yItemPos + cellHeight / 3));
                    g.draw(canvas);
                }
            }
        }

        int arr[] = countItems(character);
        numberKeys.setText(Integer.toString(arr[0]));
        numberDynamites.setText(Integer.toString(arr[1]));

        // Use keys when clicked on
        if (keyClick && arr[0] > 0) {
            ArrayList<Item> my_items = character.getInventory();
            for (Item a: my_items) {
                if (a.getItemID().equals("Key") && !a.isUsed()) {
                    boolean open = false;
                    switch (character.getDirection()) {
                        case "Up":
                            if (currentY != 0 && maze.isDoor("Horizontal", currentY - 1, currentX)) {
                                maze.openDoors("Horizontal", currentY - 1, currentX);
                                open = true;
                            }
                            break;
                        case "Down":
                            if (currentY != mazeSizeY - 1 && maze.isDoor("Horizontal", currentY, currentX)) {
                                maze.openDoors("Horizontal", currentY, currentX);
                                open = true;
                            }
                            break;
                        case "Left":
                            if (currentX != 0 && maze.isDoor("Vertical", currentY, currentX - 1)) {
                                maze.openDoors("Vertical", currentY, currentX - 1);
                                open = true;
                            }
                            break;
                        case "Right":
                            if (currentX != mazeSizeX - 1 && maze.isDoor("Vertical", currentY, currentX)) {
                                maze.openDoors("Vertical", currentY, currentX);
                                open = true;
                            }
                            break;
                    }
                    if (open)
                        a.useItem();

                    keyClick = false;
                    break;
                }
            }
        }

        // Use dynamites when clicked on
        if (dynamiteClick && arr[1] > 0) {
            ArrayList<Item> my_items = character.getInventory();
            for (Item a: my_items) {
                if (a.getItemID().equals("Dynamite") && !a.isUsed()) {
                    boolean boom = false;
                    switch (character.getDirection()) {
                        case "Up":
                            if (currentY != 0 && maze.isWall("Horizontal", currentY - 1, currentX)) {
                                maze.bombWalls("Horizontal", currentY - 1, currentX);
                                boom = true;
                            }
                            break;
                        case "Down":
                            if (currentY != mazeSizeY - 1 && maze.isWall("Horizontal", currentY, currentX)) {
                                maze.bombWalls("Horizontal", currentY, currentX);
                                boom = true;
                            }
                            break;
                        case "Left":
                            if (currentX != 0 && maze.isWall("Vertical", currentY, currentX - 1)) {
                                maze.bombWalls("Vertical", currentY, currentX - 1);
                                boom = true;
                            }
                            break;
                        case "Right":
                            if (currentX != mazeSizeX - 1 && maze.isWall("Vertical", currentY, currentX)) {
                                maze.bombWalls("Vertical", currentY, currentX);
                                boom = true;
                            }
                            break;
                    }
                    if (boom)
                        a.useItem();

                    dynamiteClick = false;
                    break;
                }
            }
        }

        // draw the maze link location indicators
        if (mazeLinks != null) {
            for (int i = 0; i < mazeLinks.size(); i++) {
                Point point = (Point) mazeLinks.get(i).getPoint();
                String linkDirection = (String) mazeLinks.get(i).getDirection();
                canvas.drawText(String.valueOf(linkDirection.charAt(0)),
                        (point.getX() * totalCellWidth) + (cellWidth * 0.25f),
                        (point.getY() * totalCellHeight) + (cellHeight * 0.75f),
                        red);
            }
        }

        mazeNum.setText(String.format("Maze #%d", maze.getMazeNum()));
    }

    private int[] countItems(Character chara) {
        ArrayList<Item> my_items = chara.getInventory();
        int[] array = new int[2];
        array[0] = 0;
        array[1] = 0;
        for (Item i : my_items) {
            if (!i.isUsed()) {
                String name = i.getItemID();
                if (name.equals("Key"))
                    array[0]++;
                else if (name.equals("Dynamite"))
                    array[1]++;
            }
        }
        return array;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// Controller Methods //////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onInputEvent(View view, int buttons) {
        switch (view.getId()) {
            case R.id.viewDirection:
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
        if (!direction.equals("NONE"))
            character.setDirection(direction);
        return direction;
    }

    private boolean playerMove(String direction){
        boolean moved = false;

        if (mazeLinks != null) {
            for (int i = 0; i < mazeLinks.size(); i++) {
                Point point = (Point) mazeLinks.get(i).getPoint();
                String linkDirection = (String) mazeLinks.get(i).getDirection();
                if (currentX == point.getX() && currentY == point.getY() &&
                        direction.equals(linkDirection)) {
                    Maze nextMaze = mazes.get(point.getMazeLink());
                    GameView nextGameView = new GameView(m_context, mazes, mazeItems, character, point.getMazeLink() - 1);
                    maze.resetPosition();
                    m_context.setContentView(nextGameView);
                }
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
            //the player was moved so we'll redraw the view
            invalidate();
        }
        return true;
    }
}
