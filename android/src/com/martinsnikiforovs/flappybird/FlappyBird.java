// Mārtiņš Nikiforovs 221RDB386 8.grupa
package com.martinsnikiforovs.flappybird;

//   Šīs speles projekta GitHub repo. saite



//      interesting control mechanism  -  kontroles ir speciāli invertētas / apgrieztas otrādāk,
//      itkā tu kontrolētu ačgārnās valsts -  Australijas kaķi.  Visi assets ir manis paša zīmēti.=
//      Ņemot vērā, ka flappy-bird spēlē nav iespējams uzvaret, vai iegūt neizšķirtu rezultātu, tad
//      šajā spēlē ir tikai zaudēšanas message. Šāda veida spēli nebija iespējams ar manu prasmju līmeni
//      veidot Kotlin, tapēc tika izmantota Java :)

//Izmantotie voti / atsauces
//Visparējai iedvesamai lai saprastu ka varētu kods funkcionēt:
//      1)  https://github.com/rusahang/FlappyBird
//      2)  https://github.com/ellisonchan/ComposeBird
//Dokumentācija, pamācības un piemēri par libGDX specifiskam lietām:
//      1)  https://libgdx.com/dev/
//      2)  https://libgdx.com/wiki/
//      3)  https://libgdx.com/wiki/start/project-generation
//      4)  https://libgdx.com/wiki/preferences
//      5)  https://youtu.be/EJwXzmUQChg?si=ervO4w32JK9VS8t2
//Informatīvs video lai gūtu ieskatu par iespejamu punktu skaitīsanas sistemu
//        1)https://www.youtube.com/watch?v=_cV7cgQFDo0

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture logo;
    Texture gameover;
    Texture[] cats;
    int flapState = 0;
    float catY = 0;
    float velocity = 0;
    boolean vibrato = false;
    private long nextTapTime = 0;

    private long trackSpriteChange = 0;



    Circle catCirle;
    int score = 0;
    int scoring = 0;
    BitmapFont font;
    int gameState = 0;
    float gravity = -2;
    Texture topWater;
    Texture bottomWater;
    float gap = 600;
    float maxWaterOffset;
    Random randomGenerator;
    float waterVelocity = 4;
    int numberOfWater = 4;
    float[] waterX = new float[numberOfWater];
    float[] waterOffset = new float[numberOfWater];
    float distanceBetweenWater;
    Rectangle[] topWaterCol;
    Rectangle[] bottomWaterCol;
    Preferences sharedPreferences;
    String userName;

    Texture topWall;
    Texture bottomWall;

    String name;



    @Override
    public void create() {

        AndroidLauncher.MyTextInputListener listener = new AndroidLauncher.MyTextInputListener();
        batch = new SpriteBatch();
        background = new Texture("ute.png");
        logo = new Texture("sigma.png");
        gameover = new Texture("gameover.png");

        catCirle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        cats = new Texture[2];
        cats[0] = new Texture("flyincat1.png");
        cats[1] = new Texture("flyincat2.png");

        topWater = new Texture("topwater.png");
        bottomWater = new Texture("bottomwater.png");
        topWall = new Texture("topwall.png");
        bottomWall = new Texture("bottomwall.png");

        maxWaterOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenWater = Gdx.graphics.getWidth() * 3 / 4;
        topWaterCol = new Rectangle[numberOfWater];
        bottomWaterCol = new Rectangle[numberOfWater];

        sharedPreferences = Gdx.app.getPreferences("My Preferences");

        userName = sharedPreferences.getString("userName", "");
        sharedPreferences.putString("userName", "");
        sharedPreferences.flush();



            promptForName();


        startGame();
    }


    private String promptForName() {
        AndroidLauncher.MyTextInputListener listener = new AndroidLauncher.MyTextInputListener();
        Gdx.input.getTextInput(listener,"What is your name?", "", "");

        return "helo";

    }

    public void startGame() {

        catY = Gdx.graphics.getHeight() / 2 - cats[0].getHeight() / 2;

        for (int i = 0; i < numberOfWater; i++) {

            waterOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            waterX[i] = Gdx.graphics.getWidth() / 2 - topWater.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenWater;

            topWaterCol[i] = new Rectangle();
            bottomWaterCol[i] = new Rectangle();

        }

    }

    @Override
    public void render () {

        batch.begin();
        font.setColor(Color.BLACK);

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());




        if (gameState == 1) {

            if (waterX[scoring] < Gdx.graphics.getWidth() / 2) {

                score++;

                Gdx.app.log("Score", String.valueOf(score));

                if (scoring < numberOfWater - 1) {

                    scoring++;

                } else {

                    scoring = 0;

                }

            }

            if (Gdx.input.isTouched()) {

                velocity = 30;

            }



            for (int i = 0; i < numberOfWater; i++) {

                if (waterX[i] < - topWater.getWidth()) {

                    waterX[i] += numberOfWater * distanceBetweenWater;
                    waterOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                } else {

                    waterX[i] = waterX[i] - waterVelocity;



                }

                batch.draw(topWater, waterX[i], (Gdx.graphics.getHeight() / 2 + gap + waterOffset[i]) - 600, topWater.getWidth(), topWater.getHeight() + 600);
                batch.draw(bottomWater, waterX[i], (Gdx.graphics.getHeight() / 2 - gap - bottomWater.getHeight() + waterOffset[i]) - 600, bottomWater.getWidth(), bottomWater.getHeight() + 600);

                topWaterCol[i] = new Rectangle(waterX[i], (Gdx.graphics.getHeight() / 2 + gap  + waterOffset[i]) - 600, topWater.getWidth(), topWater.getHeight() + 600);
                bottomWaterCol[i] = new Rectangle(waterX[i], (Gdx.graphics.getHeight() / 2 - gap  - bottomWater.getHeight() + waterOffset[i]) - 600, bottomWater.getWidth(), bottomWater.getHeight() + 600);
            }

            for (int i = 0; i < numberOfWater; i++) {


                if (Intersector.overlaps(catCirle, topWaterCol[i]) || Intersector.overlaps(catCirle, bottomWaterCol[i])) {

                    gameState = 2;

                }

            }

            batch.draw(bottomWall, 0, 0,Gdx.graphics.getWidth(),100f);
            batch.draw(topWall, 0, Gdx.graphics.getHeight()-100,Gdx.graphics.getWidth(), 100f);

            Rectangle topWall = new Rectangle(0, 0,Gdx.graphics.getWidth(),100f);
            Rectangle bottomWall = new Rectangle(0, Gdx.graphics.getHeight()-100,Gdx.graphics.getWidth(), 100f);

            if (Intersector.overlaps(catCirle, topWall) || Intersector.overlaps(catCirle, bottomWall)) {

                gameState = 2;

            }



            if (catY > 0) {

                velocity = velocity + gravity;
                catY -= velocity;

            } else {

                gameState = 2;

            }

            font.getData().setScale(10);
            font.setColor(Color.WHITE);
            font.draw(batch, String.valueOf(score), 100, 250);
            font.setColor(Color.BLACK);


        } else if (gameState == 0) {
            String name = sharedPreferences.getString("userName");
            int hiScore = sharedPreferences.getInteger("highScore");
            font.getData().setScale(6);

            font.draw(batch, "Hi " + String.valueOf(name), 100, 300);
            batch.draw(logo,  50, 1000, 900, 600);
            font.draw(batch, "High Score " + String.valueOf(hiScore), 100, 200);




            if (Gdx.input.justTouched()) {

                gameState = 1;


            }

        } else if (gameState == 2) {


            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
            font.draw(batch,  String.valueOf(score), 770, 780);

            if (vibrato == false){
                Gdx.input.vibrate(1000);
                nextTapTime = System.currentTimeMillis() + 1000;
                vibrato = true;
            }

            catY = 10000;

if(System.currentTimeMillis() >= nextTapTime){
    font.draw(batch,  "Tap to play", 200, 580);
}

            if (Gdx.input.justTouched() && System.currentTimeMillis() >= nextTapTime) {
                gameState = 0;
                vibrato = false;
                startGame();
                sharedPreferences.putInteger("highScore", score);
                sharedPreferences.flush();
                score = 0;
                scoring = 0;
                velocity = 0;


            }


        }
if(System.currentTimeMillis() >= trackSpriteChange) {
    if (flapState == 0) {
        flapState = 1;
        trackSpriteChange = System.currentTimeMillis() + 200;

    } else {
        flapState = 0;
        trackSpriteChange = System.currentTimeMillis() + 200;

    }
}



        batch.draw(cats[flapState], Gdx.graphics.getWidth() / 2 - cats[flapState].getWidth() / 2, catY);



        catCirle.set(Gdx.graphics.getWidth() / 2, catY + cats[flapState].getHeight() / 2, cats[flapState].getWidth() / 3 );




        batch.end();



    }


}