package com.skeuta.tigertrap;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements GameListener,OnClickListener {
	
	GraphicalView gView;
	final CharSequence[] playerNoList = {"One Player","Two Player"};
	final CharSequence[] playerType = {"Goat","Tiger"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newButton = (Button) findViewById(R.id.newgameButton);
        newButton.setOnClickListener(this);
        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(this);
        
        
        /*
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGameFinish(View View,int i) {
		// TODO Auto-generated method stub
		System.out.println("in main activity");
	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Game Status");
		if (i == 0) {
			alertDialogBuilder.setMessage("The Tiger won the game!\nDo you want to restart game?");
		}
		else if(i == 1){
			alertDialogBuilder.setMessage("The Goat won the game!\nDo you want to reset the game?");
		}
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {					
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							    Intent i = new Intent(getBaseContext(),MainActivity.class); //change it to your main class
							    //the following 2 tags are for clearing the backStack and start fresh
							    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							    finish();
							    startActivity(i);
								//gView.callResetGame();
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						})
						.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.newgameButton:
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Choose player");
			dialog.setItems(playerNoList, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						//can use intent to pass values from one activity to other.but not sure for class. u can test it out
						gView = new GraphicalView(getBaseContext(),MainActivity.this,true);
						setContentView(gView);
					}
					else{
						gView = new GraphicalView(getBaseContext(),MainActivity.this,false);
						setContentView(gView);
					}
				}
				});
			dialog.show();
			break;
		case R.id.aboutButton:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("About Game");
			alert.setIcon(R.drawable.ic_launcher);
			alert.setMessage("This game has two players.One is the tiger and the other is the goat. "
					+ " \nThere are 3 tigers and 15 goats provided"
					+ " \nThe tiger hunts for the goat and the goat attempts to block the tiger."
					+ " \nThe tiger can kill the goat if the point next to the goat is empty."
					+ " \nThe goat can block the tiger by surrounding the tiger so that the tiger cannot move."
					+ " \nThe tiger can win by killing 5 goats and the goat can win by surrounding all the tigers");
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int a) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			alert.show();
			break;
		}
	}

}
