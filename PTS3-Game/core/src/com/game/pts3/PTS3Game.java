package com.game.pts3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PTS3Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	float red = 1;
	float green = 1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(red, green, 0.343f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 50, 50);
		batch.end();

		if(Gdx.input.isKeyPressed(Keys.LEFT) && red > 0.1f){
			red-= 0.1f;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && red < 0.9f){
			red+= 0.1f;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) && green > 0.1f){
			green-= 0.1f;
		}
		if(Gdx.input.isKeyPressed(Keys.UP) && green < 0.9f){
			green+= 0.1f;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
