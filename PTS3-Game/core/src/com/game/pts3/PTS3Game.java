package com.game.pts3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import network.Client.Client;
import com.game.classes.Game;
import network.Client.IClientEvents;

public class PTS3Game extends ApplicationAdapter {
	SpriteBatch batch;
	Stage stage;
	Texture img;
	float red = 1;
	float green = 1;
	Skin skin;

	Game game;
	Chat chat;

	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		/*
		BitmapFont font = new BitmapFont();
		font.setColor(Color.BLUE);
		skin = new Skin();
		skin.add("default", font);

		Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("background", new Texture(pixmap));

		TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.up = skin.newDrawable("background", Color.GRAY);
		buttonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		buttonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
		buttonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		buttonStyle.font = skin.getFont("default");
		skin.add("default", buttonStyle);
		*/

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        chat = new Chat(new TextArea("test", skin));
        chat.getTextArea().setPosition(300,300);
        chat.getTextArea().setWidth(500);
        chat.getTextArea().setHeight(200);
        stage.addActor(chat.getTextArea());

		TextButton test = new TextButton("Connect with server!", skin);
		test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game == null){
                    game = new Game(new Client("localhost"));
                    game.getClient().addListener(chat);
                } else{
                    game.getClient().sendMessageWhisper("?", "hallo!");
                }

            }
        });
		test.setPosition(100,100);
		stage.addActor(test);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(red, green, 0.343f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
//		batch.begin();
//		batch.draw(img, 50, 50);
//		batch.end();

		if(Gdx.input.isKeyPressed(Keys.LEFT) && red > 0.1f){
			red-= 0.01f;
//			game.getClient().sendMessageWhisper("?", "Hallo");
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && red < 0.9f){
			red+= 0.01f;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) && green > 0.1f){
			green-= 0.01f;
		}
		if(Gdx.input.isKeyPressed(Keys.UP) && green < 0.9f){
			green+= 0.01f;

		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
