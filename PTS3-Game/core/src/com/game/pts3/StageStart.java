package com.game.pts3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.classes.Character;
import com.game.classes.Player;

public class StageStart extends Game {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Texture testChar;
    private Character character;

    public StageStart(){
    }

    @Override
    public void create() {
        testChar = new Texture(Gdx.files.internal("maan.png"));

        //tiledMap = new TmxMapLoader().load("data/maps/???");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);
        camera.update();

        this.setScreen(new ScreenLoad(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}