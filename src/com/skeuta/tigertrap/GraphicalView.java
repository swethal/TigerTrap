package com.skeuta.tigertrap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class GraphicalView extends View{
	Paint paint;
	private Game game;
	private Bitmap ball_blue;
	private Bitmap ball_green;
	private RectF gameBoard;
	private GameListener gListener;
	private Point size;
	private boolean AIModeFlag;
	
	
	public GraphicalView(Context context,GameListener listener,boolean playerFlag) {
		super(context);
		this.gListener = listener;
		setFocusable(true); //necessary for getting the touch events
		size = new Point();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getSize(size);
		System.out.println("width of screen is "+size.x);
		System.out.println("height of screen is "+size.y);
		// create a variable in Game class for a virtual window and make sure the corners dont go to the end of the screen
		gameBoard = new RectF((size.x/14),(size.y/24),size.x-(size.x/14),size.y-(size.y/5));
		game = new Game(gameBoard); //Have all the x,y values of the game points in Game class
		paint = new Paint();
	    ball_blue = BitmapFactory.decodeResource(getResources(),R.drawable.tiger);
	    ball_green = BitmapFactory.decodeResource(getResources(),R.drawable.goat);
	    AIModeFlag = playerFlag;
	}
	
	protected void onDraw(Canvas canvas){
		Bitmap selected_bitmap;
		paint.setColor(Color.BLACK);
		// u pass the currIndex Value from the i value of FOR loop and then get the index of each line to currLine.
		// This will help in drwing each line on the screen
		for(int i=0; i<game.getNumLines(); i++){
			Line currLine = game.getLineValues(i);
			//gameboard.left & gameboard.top both has value 50. so u add it to currline values to shift the line 50 pts.
			//so the line wont go to very end.
			canvas.drawLine(gameBoard.left+currLine.getStartX(),gameBoard.top+currLine.getStartY(),
					gameBoard.left+currLine.getEndX(),gameBoard.top+currLine.getEndY(),paint);
		}
		for(int i=0; i< game.getNumPlayers(); i++){
			if (i==0) {
				selected_bitmap = ball_blue;
			} else {
				selected_bitmap = ball_green;
			}
			for(int j=0; j< game.getNumCoins(i); j++){ 
					PointF point = game.getCoinPosition(i, j);
					//making the point to be center of the circle so subtracting width & height from x & y value of the point
					canvas.drawBitmap(selected_bitmap,gameBoard.left+point.x-(selected_bitmap.getWidth()/2),
							gameBoard.top+point.y-(selected_bitmap.getHeight()/2),null);  
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventAction = event.getAction();

		int X = (int) event.getX();
		int Y = (int) event.getY();
		float XwithinGameBoard = X - gameBoard.left;
		float YwithinGameBoard = Y - gameBoard.top;
		PointF locWithinGameBoard = new PointF(XwithinGameBoard,YwithinGameBoard);
		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:// touch down so check if the finger is on a ball
			//identify the touch point and store it here
			game.identifyCoin(locWithinGameBoard);
			break;
		case MotionEvent.ACTION_MOVE:
			//move the coin along the finger
			//TODO: move should be done only in run state.
			//game.updateCoin(X,Y);//pass the x,y value of the coin u drag so that the coin can be seen all through the drag
			//game.updateCoin(loc);
			game.updateMoveCoin(locWithinGameBoard);
			break;
		case MotionEvent.ACTION_UP:
			// after the finger touch is taken out
			if(! AIModeFlag){
				if (game.updateCoin(locWithinGameBoard)) {
					game.updateCurrPlayer();
				}
			}
			else {
				// Tiger's turn (because only user can control tiger) can change in future
				if (game.updateCoin(locWithinGameBoard)) {
					if(game.tigerWins()){		
						Toast.makeText(getContext(), "Hurray!! The tiger wins the game", Toast.LENGTH_LONG).show();
						callGameOverDialogBox(0);
						break;
					}
					if(game.goatWins()){
						Toast.makeText(getContext(), "Hurray!! The goat wins the game", Toast.LENGTH_LONG).show();
						callGameOverDialogBox(1);
						break;
					}
					game.updateCurrPlayer();
					// AI controls goat
					game.goatAsComputer();
					if(game.tigerWins()){		
						Toast.makeText(getContext(), "Hurray!! The tiger wins the game", Toast.LENGTH_LONG).show();
						callGameOverDialogBox(0);
						break;
					}
					if(game.goatWins()){
						Toast.makeText(getContext(), "Hurray!! The goat wins the game", Toast.LENGTH_LONG).show();
						callGameOverDialogBox(1);
						break;
					}
					game.updateCurrPlayer();
				}
			}
		}
		//TODO: get the specific positions where the coins r placed n invalidate jus those positions
		invalidate();
		return true;
		
	}
	
	public void callResetGame(){
		game.resetGame();
	}
	
	 
	 public void callGameOverDialogBox(int value){
		 gListener.onGameFinish(this,value);		
		 System.out.println("after listener is invoked");
	 }
}
