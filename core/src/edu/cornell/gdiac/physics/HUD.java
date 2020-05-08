package edu.cornell.gdiac.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import edu.cornell.gdiac.util.SoundController;

public class HUD {

    /** Static variables */
    private static int FONT_SIZE = 24;
    private static int FONT_COLOR = 0xc0bab2;
    private static String FONT_FILE = "shared/Asul.ttf";
    private static String HUD_FILE = "shared/hud.png";
    private static String GOLEM_PADDING = "            ";
    private static String TIMER_PADDING = "                                      ";

    /** Declaration for new Stage */
    private Stage stage;
    private Viewport viewport;

    /** Golem & Time Tracking Variables */
    private static int numCurrentHosts;
    private static int numTotalHosts;
    private static float timeCount;
    private static int worldTimer;
    private static int minutes;
    private static int seconds;

    /** Scene2D Widgets */
    private Table table;
    private Stack stack;
    private static Label timeLabel;
    private static Label hostCounterLabel;
    private Texture hostCounterTexture;

    /** Initialization */
    public HUD(PolygonSpriteBatch spriteBatch) {
        numCurrentHosts = 0;
        numTotalHosts = 0;

        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(viewport, spriteBatch);

        /* Create label for the text */
        Label.LabelStyle counterLS = new Label.LabelStyle();

        /* Use Asul Font Font */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        BitmapFont asulFont = generator.generateFont(parameter);
        generator.dispose();

        counterLS.font = asulFont;
        counterLS.fontColor = new Color(Color.SKY);
        hostCounterLabel = new Label(GOLEM_PADDING + numCurrentHosts+ " / " + numTotalHosts, counterLS);

        /* For the text and the image to overlay */
        stack = new Stack();
        hostCounterTexture = new Texture(Gdx.files.internal(HUD_FILE));
        Image counterBackground = new Image(hostCounterTexture);
        stack.add(counterBackground);
        stack.add(hostCounterLabel);

        /* Create timer counter */
        timeLabel = new Label(TIMER_PADDING + worldTimer, counterLS);
        stack.add(timeLabel);

        /* Create golem table to add stack */
        table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(stack).expandX().padTop(5).padLeft(5).left();
        // golemTable.setDebug(true);

        stage.addActor(table);
    }

    /** Gets the Stage */
    public Stage getStage() { return stage; }

    /** Updates the Timer */
    public static void update(float dt) {
        timeCount += dt;

        if (timeCount >= 1) {
            worldTimer++;
            minutes = (int) Math.floor(worldTimer / 60);
            seconds = worldTimer - (minutes * 60);
            timeLabel.setText(String.format(TIMER_PADDING + "%d:%02d", minutes, seconds));
            timeCount = 0;
        }

    }

    /** Increments the number of current hosts possessed */
    public static void incrementCurrHosts() {
        if (numCurrentHosts < numTotalHosts) {
            numCurrentHosts+= 1;
            hostCounterLabel.setText(GOLEM_PADDING + numCurrentHosts + " / " + numTotalHosts);
        }
    }

    /** Sets the total number of hosts that need to be possessed */
    public static void setNumTotalHosts(int num) {
        numTotalHosts = num;
        hostCounterLabel.setText(GOLEM_PADDING + numCurrentHosts + " / " + numTotalHosts);
    }

    /** Clears the HUD */
    public static void clearHUD() {
        minutes = 0;
        seconds = 0;
        timeCount = 0;
        worldTimer = 0;
        numTotalHosts = 0;
        numCurrentHosts = 0;

        timeLabel.setText(String.format(TIMER_PADDING + "%d:%02d", minutes, seconds));
    }

    /** Disposes the elements used */
    public void dispose() {
        hostCounterTexture.dispose();
        stage.dispose();
    }
}
