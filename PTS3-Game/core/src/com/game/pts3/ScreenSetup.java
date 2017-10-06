package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import network.Client.Client;

public class ScreenSetup implements Screen {
    Stage stage;
    private Skin skin;
    private Game game;
    final private AssetManager manager;

    public ScreenSetup(final Game game, AssetManager assetManager){
        stage = new Stage();
        this.game = game;
        this. manager = assetManager;
        skin = manager.get("data/uiskin.json", Skin.class);
        Gdx.input.setInputProcessor(stage);

        /**
         * Labels
         */
        Label lblWelcome = new Label("Welcome to game!\n Please enter your name!", skin);
        lblWelcome.setPosition(10, 80);
        lblWelcome.setSize(90,90);
        Label lblIP = new Label("Enter IP address", skin);
        lblIP.setPosition(270,40);
        lblIP.setSize(90,90);

        /**
         * TextField
         */
        final TextField tfName = new TextField("", skin);
        tfName.setSize(250, 30);
        tfName.setPosition(10, 40);
        final TextField tfIP = new TextField("localhost", skin);
        tfIP.setSize(250,30);
        tfIP.setPosition(270,40);

        /**
         * TextButton
         */
        TextButton btnStart = new TextButton("To game lobby", skin);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String string = "?";
                string = tfName.getText();
                com.game.classes.Game gameState = new com.game.classes.Game(new Client(tfIP.getText()));
                game.setScreen(new ScreenLobby(game, string, gameState, manager));
            }
        });
        btnStart.setSize(250,20);
        btnStart.setPosition(10,10);

        stage.addActor(tfName);
        stage.addActor(tfIP);
        stage.addActor(lblWelcome);
        stage.addActor(lblIP);
        stage.addActor(btnStart);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /**
         * Clear screen and set colour.
         */
        Gdx.gl.glClearColor( 1, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
