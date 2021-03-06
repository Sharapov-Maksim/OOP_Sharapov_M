package javafx.msharapov;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

class Snake {
    final static int INITIAL_LENGTH = 1;

    private final List<Joint> joints = new ArrayList<>();
    private Direction direction = Direction.RIGHT;

    Snake() {
        for (int i = 0; i < INITIAL_LENGTH; i++) {
            joints.add(new Joint(5 - i, 5));
        }
    }

    void move() {
        for (int i = joints.size() - 1; i > 0; i--) {
            Joint curr = joints.get(i);
            Joint prev = joints.get(i - 1);

            curr.x = prev.x;
            curr.y = prev.y;
        }

        Joint head = joints.get(0);

        switch (direction) {
            case UP -> {
                if (head.y == 0) {
                    head.y = Board.HEIGHT;
                }
                head.y--;
            }
            case DOWN -> {
                if (head.y == Board.HEIGHT - 1) {
                    head.y = -1;
                }
                head.y++;
            }
            case LEFT -> {
                if (head.x == 0) {
                    head.x = Board.WIDTH;
                }
                head.x--;
            }
            case RIGHT -> {
                if (head.x == Board.WIDTH - 1) {
                    head.x = -1;
                }
                head.x++;
            }
        }
    }

    void grow() {
        joints.add(new Joint(-1, -1));
    }

    boolean checkCollision() {
        for (int i = joints.size() - 1; i > 3; i--) {
            Joint head = joints.get(0);
            Joint joint = joints.get(i);

            if (head.x == joint.x && head.y == joint.y) {
                return true;
            }
        }

        return false;
    }

    boolean checkCollision(Apple apple) {
        Joint head = joints.get(0);
        return head.x == apple.getX() &&
                head.y == apple.getY();
    }

    void changeDirection(Direction newDirection) {
        switch (newDirection) {
            case UP -> {
                if (direction != Direction.DOWN) {
                    direction = Direction.UP;
                }
            }
            case DOWN -> {
                if (direction != Direction.UP) {
                    direction = Direction.DOWN;
                }
            }
            case LEFT -> {
                if (direction != Direction.RIGHT) {
                    direction = Direction.LEFT;
                }
            }
            case RIGHT -> {
                if (direction != Direction.LEFT) {
                    direction = Direction.RIGHT;
                }
            }
        }
    }

    void draw(GraphicsContext gc) {
        for (int i = 0; i < joints.size(); i++) {
            Joint joint = joints.get(i);
            int size = Board.CELL_SIZE;

            gc.setFill(i == 0 ? Color.DARKGREEN : Color.FORESTGREEN);
            gc.fillRect(joint.x * size, joint.y * size, size, size);
        }
    }

    int getLength() {
        return joints.size();
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static class Joint {
        int x, y;

        Joint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
