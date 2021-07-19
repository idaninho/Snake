package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final int width=600;
    private static final int height=width;
    private static final int rows=20;
    private static final int columns=rows;
    private static final int sqSize=width/rows;
    private static final String[] foodImages=new String[] {"/img/ic_apple.png","/img/ic_berry.png","/img/ic_cherry.png","/img/ic_coconut_.png","/img/ic_peach.png",
    "/img/ic_orange.png","/img/ic_pomegranate.png", "/img/ic_tomato.png", "/img/ic_watermelon.png"};

    private static final int right=0;
    private static final int left=1;
    private static final int up=2;
    private static final int down=3;

    private GraphicsContext gc;
    private List<Point> snakeBody=new ArrayList<>();
    private Point snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score=0;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Snake");
        Group root=new Group();
        Canvas canvas=new Canvas(width,height);
        root.getChildren().add(canvas);
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc=canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                if (currentDirection != left) {
                    currentDirection = right;
                }
            }
                else if (code == KeyCode.LEFT || code == KeyCode.A) {
                if (currentDirection != right) {
                    currentDirection = left;
                }
            }
                else if (code==KeyCode.UP|| code==KeyCode.W) {
                if (currentDirection != down) {
                    currentDirection = up;
                }
            }
                else if (code==KeyCode.DOWN|| code==KeyCode.S) {
                if (currentDirection != up) {
                    currentDirection = down;
                }
            }
        });
        for (int i=0; i<3;i++)
            snakeBody.add(new Point(5,rows/2));
        snakeHead=snakeBody.get(0);
        generateFood();

        Timeline timeline=new Timeline(new KeyFrame(Duration.millis(130),e->run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void run(GraphicsContext gc) {
        if (gameOver){
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7",70));
            gc.fillText("Game Over",width/3.5,height/2);
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();

        for (int i=snakeBody.size()-1;i>=1;i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        switch (currentDirection){
            case right:
                moveRight();
                break;
            case left:
                moveLeft();
                break;
            case up:
                moveUp();
                break;
            case down:
                moveDown();
                break;
        }
        gameOver();
        eatFood();
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i=0;i<rows;i++) {
            for (int j=0;j <columns; j++) {
                if ((i+j)%2==0)
                    gc.setFill(Color.web("AAD751"));
                else
                    gc.setFill(Color.web("A2D149"));
                gc.fillRect(i*sqSize,j*sqSize,sqSize,sqSize);

            }

        }
    }

    private void generateFood() {
        start:
        while(true) {
            foodX=(int) (Math.random()*rows);
            foodY=(int) (Math.random()*columns);

            for (Point snake:snakeBody) {
                if (snake.getX()==foodX && snake.getY()==foodY) {
                    continue start;
                }
            }
            foodImage=new Image(foodImages[(int) (Math.random()*foodImages.length)]);//choose food in random
            break;
        }
    }

    private void drawFood(GraphicsContext gc) {
        gc.drawImage(foodImage,foodX*sqSize,foodY*sqSize,sqSize,sqSize);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snakeHead.getX()*sqSize,snakeHead.getY()*sqSize,sqSize-1,sqSize-1,35,35);
        for (int i=1;i<snakeBody.size();i++)
            gc.fillRoundRect(snakeBody.get(i).getX()*sqSize,snakeBody.get(i).getY()*sqSize,sqSize-1,sqSize-1,20,20);
    }

    private void moveRight() {
        snakeHead.x++;
    }
    private void moveLeft() {
        snakeHead.x--;
    }
    private void moveUp() {
        snakeHead.y--;
    }
    private void moveDown() {
        snakeHead.y++;
    }

    public void gameOver() {
        if (snakeHead.x<0 || snakeHead.y<0|| snakeHead.x*sqSize>=width|| snakeHead.y*sqSize>=height) {
            gameOver=true;
        }

        //destroy itself
        for (int i=1;i<snakeBody.size();i++) {
            if (snakeHead.x==snakeBody.get(i).getX()&&snakeHead.getY()==snakeBody.get(i).getY()){
                gameOver=true;
                break;
            }
        }
    }

    private void eatFood() {
        if (snakeHead.getX()==foodX&&snakeHead.getY()==foodY) {
            snakeBody.add(new Point(-1,-1));
            generateFood();
            score++;
        }
    }

    private void drawScore(){
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7",35));
        gc.fillText("Score: "+score,10,35);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
