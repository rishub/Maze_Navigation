package com.example.kevinwu.maze_navigation.models;

import java.util.ArrayList;

/**
 * Created by Kevin on 2/10/2017.
 */

public class MazeFactory {

    public static Maze getMaze(int mazeNo) {
        Maze maze = null;
        if(mazeNo == 1) {
            maze = new Maze();
            boolean[][] vLines = new boolean[][]{
                    {true ,false,false,false,true ,false,false},
                    {true ,false,false,true ,false,true ,true },
                    {false,true ,false,false,true ,false,false},
                    {false,true ,true ,false,false,false,true },
                    {true ,false,false,false,true ,true ,false},
                    {false,true ,false,false,true ,false,false},
                    {false,true ,true ,true ,true ,true ,false},
                    {false,false,false,true ,false,false,false}
            };
            boolean[][] hLines = new boolean[][]{
                    {false,false,true ,true ,false,false,true ,false},
                    {false,false,true ,true ,false,true ,false,false},
                    {true ,true ,false,true ,true ,false,true ,true },
                    {false,false,true ,false,true ,true ,false,false},
                    {false,true ,true ,true ,true ,false,true ,true },
                    {true ,false,false,true ,false,false,true ,false},
                    {false,true ,false,false,false,true ,false,true }
            };
            maze.setVerticalLines(vLines);
            maze.setHorizontalLines(hLines);
            maze.setStartPosition(0, 0);
            maze.setFinalPosition(7,7);
            maze.setMazeNum(1);

            Point linkPoint = new Point(7,7,2);
            Pair link = new Pair(linkPoint, "Right");

            Point linkPoint1 = new Point(7,6,2);
            Pair link1 = new Pair(linkPoint1, "Right");

            ArrayList<Pair> mazeLinks = new ArrayList<>();
            mazeLinks.add(link);
            mazeLinks.add(link1);

            maze.setLinks(mazeLinks);

        }
        //other mazes
        if(mazeNo == 2) {
            maze = new Maze();
            boolean[][] vLines = new boolean[][]{
                    {false,false,false,true ,false,false,false},
                    {false,false,true ,false,true ,false,false},
                    {false,false,true ,true ,false,false,false},
                    {false,false,true ,true ,true ,false,false},
                    {false,false,true ,false,true ,false,false},
                    {true ,false,false,true ,false,true ,false},
                    {true ,false,true ,true ,false,false,false},
                    {false,false,true ,false,false,false,true }
            };
            boolean[][] hLines = new boolean[][]{
                    {false,true ,true ,true ,false,true ,true ,true },
                    {true ,true ,false,false,true ,true ,true ,false},
                    {false,true ,true ,false,false,false,true ,true },
                    {true ,true ,false,false,false,true ,true ,false},
                    {false,true ,true ,true ,true ,false,true ,false},
                    {false,false,true ,false,false,true ,true ,true },
                    {false,true ,false,false,true ,true ,false,false}
            };
            maze.setVerticalLines(vLines);
            maze.setHorizontalLines(hLines);
            maze.setStartPosition(0, 7);
            maze.setFinalPosition(7, 0);
            maze.setMazeNum(2);

            Point linkPoint = new Point(7,0,1);
            Pair link = new Pair(linkPoint, "Right");

            Point linkPoint1 = new Point(3,0,3);
            Pair link1 = new Pair(linkPoint1, "Up");

            ArrayList<Pair> mazeLinks = new ArrayList<>();
            mazeLinks.add(link);
            mazeLinks.add(link1);

            maze.setLinks(mazeLinks);

        }
        if(mazeNo == 3) {
            maze = new Maze();
            boolean[][] vLines = new boolean[][]{
                    {false, true , false, true , false ,true , false},
                    {false, false, true , true , true , true , true },
                    {true , false, false, true , true , false, true },
                    {true , false, false, false, true , false, true },
                    {true , false, true , true , false, false, false},
                    {false, true , true , false, true , true , true },
                    {false, false, false, false, true , true , true },
                    {false, false, true , false, false, true , false}
            };
            boolean[][] hLines = new boolean[][]{
                    {true , false, false, false, false, false, false, false},
                    {false, true , true , false, false, true , false, false},
                    {false, false, true , true , false, false, true , true },
                    {false, true , true , true , true , true , false, false},
                    {false, false, false, false, false, true , false, true },
                    {true , true , false, true , false, false, false, false},
                    {false, true , true , false, true , false, false, false}
            };
            maze.setVerticalLines(vLines);
            maze.setHorizontalLines(hLines);
            maze.setStartPosition(3, 7);
            maze.setFinalPosition(7, 2);
            maze.setMazeNum(3);

            Point linkPoint = new Point(7,2,1);
            Pair link = new Pair(linkPoint, "Right");

            Point linkPoint1 = new Point(0,0,4);
            Pair link1 = new Pair(linkPoint1, "Left");

            ArrayList<Pair> mazeLinks = new ArrayList<>();
            mazeLinks.add(link);
            mazeLinks.add(link1);

            maze.setLinks(mazeLinks);
        }
        if(mazeNo == 4) {
            maze = new Maze();
            boolean[][] vLines = new boolean[][]{
                    {false, false, true , true , false ,false , false},
                    {false, true , true , true , true , false, true },
                    {false, true , false, true , true , true , false},
                    {true , false, false, false, true , false, true },
                    {false, false, true , false, false, true , false},
                    {true , true , true , false, true , true , true },
                    {false, true , true , false, true , false, true },
                    {false, false, false, false, false, false, false}
            };
            boolean[][] hLines = new boolean[][]{
                    {true , false, false, false, false, true , true , false},
                    {false, true , false, false, true , false, false, false},
                    {true , false, true , true , false, false, true , true },
                    {false, true , false, true , true , true , false, false},
                    {false, false, false, false, true , false, false, true },
                    {true , false, false, true , false, false, false, false},
                    {false, true , true , false, true , true , true , false}
            };
            maze.setVerticalLines(vLines);
            maze.setHorizontalLines(hLines);
            maze.setStartPosition(7, 0);
            maze.setFinalPosition(3, 0);
            maze.setMazeNum(4);

            Point linkPoint = new Point(3,0,3);
            Pair link = new Pair(linkPoint, "Up");

            ArrayList<Pair> mazeLinks = new ArrayList<>();
            mazeLinks.add(link);

            maze.setLinks(mazeLinks);
        }
        return maze;
    }

}
