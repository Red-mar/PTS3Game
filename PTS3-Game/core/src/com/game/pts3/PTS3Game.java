package com.game.pts3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
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
	EventListener enterText;

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

        chat = new Chat(new TextArea("Welcome to the game!\n", skin));
        chat.getTextArea().setPosition(10,100);
        chat.getTextArea().setWidth(500);
        chat.getTextArea().setHeight(200);

        final TextField textField = new TextField("", skin);
        textField.setPosition(10, 40);
        textField.setWidth(500);
        textField.setHeight(50);

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
		test.setPosition(10,10);
		test.setWidth(250);
		test.setHeight(20);
		TextButton btnSendMessage = new TextButton("Send Message", skin);
		enterText = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game == null || game.getClient() == null ){
                    chat.getTextArea().appendText("Geen connectie met een server.\n");
                } else {
                    game.getClient().readInput(textField.getText());
                    textField.setText("");
                }
            }
        };

		btnSendMessage.addListener(enterText);
		btnSendMessage.setPosition(260, 10);
		btnSendMessage.setWidth(250);
		btnSendMessage.setHeight(20);

		stage.addActor(test);
		stage.addActor(btnSendMessage);
        stage.addActor(chat.getTextArea());
        stage.addActor(textField);
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
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
            enterText.handle(new ChangeListener.ChangeEvent());
        }

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
