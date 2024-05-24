package com.martinsnikiforovs.flappybird;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
	private static Context context;

	private static AndroidLauncher sApplication;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sApplication = this;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();


		initialize(new FlappyBird(), config);
	}
	public static class MyTextInputListener implements Input.TextInputListener {
		@Override
		public void input (String text) {
			Preferences sharedPreferences = Gdx.app.getPreferences("My Preferences");
			sharedPreferences.putString("userName", text);
			Log.d("lol", text);
			sharedPreferences.flush();
			Log.d("lol", sharedPreferences.getString("userName"));


		}

		@Override
		public void canceled () {
		}
	}

}
