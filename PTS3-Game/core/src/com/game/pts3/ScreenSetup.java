package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScreenSetup implements Screen {

    Stage stage;
    private Skin skin;
    private Game game;

    public ScreenSetup(final Game game){
        stage = new Stage();
        this.game = game;
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        Label lblWelcome = new Label("Welcome to game!\n Please enter your name!", skin);
        lblWelcome.setPosition(10, 80);
        lblWelcome.setSize(90,90);

        final TextField tfName = new TextField("", skin);
        tfName.setSize(250, 30);
        tfName.setPosition(10, 40);

        TextButton btnStart = new TextButton("To game lobby", skin);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String string = "?";
                string = tfName.getText();
                game.setScreen(new ScreenLobby(game, string));
            }
        });
        btnStart.setSize(250,20);
        btnStart.setPosition(10,10);

        stage.addActor(tfName);
        stage.addActor(lblWelcome);
        stage.addActor(btnStart);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
