package com.game.pts3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.pts3.StageStart;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GridScape: Online";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new StageStart(), config);
	}
}
