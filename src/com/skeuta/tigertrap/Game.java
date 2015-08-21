package com.skeuta.tigertrap;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.PointF;
import android.graphics.RectF;

public class Game {
	public final int PLAYER_TIGER = 0;
	public final int PLAYER_GOAT = 1;
	
	private final int numLines = 14;
	private final float lineStartXRatio[] = {0.5f,0.5f,0.5f,0.5f,0f,0.2f,0.4f,0.6f,0.8f,1f,0f,0f,0f,0.2f};
	private final float lineStartYRatio[] = {0f,0f,0f,0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.45f,0.7f,1f};
	private final float lineEndXRatio[] = {0.2f,0.4f,0.6f,0.8f,0f,0.2f,0.4f,0.6f,0.8f,1f,1f,1f,1f,0.8f};
	private final float lineEndYRatio[] = {0.2f,0.2f,0.2f,0.2f,0.7f,1f,1f,1f,1f,0.7f,0.2f,0.45f,0.7f,1f};
	private final float allPointsX[] = {0.5f,0f,0.2f,0.4f,0.6f,0.8f,1f,0f,0.2f,0.4f,0.6f,0.8f,1f,0f,0.2f,0.4f,0.6f,0.8f,1f,0.2f,0.4f,0.6f,0.8f};
	private final float allPointsY[] = {0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.45f,0.45f,0.45f,0.45f,0.45f,0.45f,0.7f,0.7f,0.7f,0.7f,0.7f,0.7f,1f,1f,1f,1f};
	private final int neighborPoints[][] = {
			{2,3,4,5},			// point 0
			{2,7},				// point 1
			{0,1,3,8},			// point 2
			{0,2,4,9},			// Point 3
			{0,3,5,10},			// Point 4
			{0,4,6,11},			// Point 5
			{5,12},					// Point 6
			{1,8,13},			// 7
			{2,7,9,14},		// 8
			{3,8,10,15},		// 9
			{4,9,11,16},		//10
			{5,10,12,17},		//11
			{6,11,18},			//12
			{7,14},				//13
			{8,13,15,19},					//14
			{9,14,16,20},					//15
			{10,15,17,21},					//16
			{11,16,18,22},					//17
			{12,17},					//18
			{14,20},					//19
			{15,19,21},					//20
			{16,20,22},					//21					
			{17,21}						//22
			};
	private final int skippingPointArray[][] = { 
			{8,9,10,11},		//point 0
			{3,13},				//point 1
			{4,14},				//point 2
			{1,5,15},				//point 3
			{2,6,16},					//point 4
			{3,17},					//point 5
			{4,18},				//point 6
			{9},
			{0,10,19},			// 8
			{0,7,11,20},			//9
			{0,8,12,21},			//10
			{0,9,22},			//11
			{10},
			{1,15},				//13
			{2,16},				//14
			{3,13,17},			//15
			{4,14,18},
			{5,15},				//17
			{6,16},				//18
			{8,21},				//19
			{9,22},				//20
			{10,19},			//21
			{11,20}
	};
	// this array tells you that if a tiger is placed in a point where should the goat be placed to skip it
	private final int neighborGoatToSkip[][] = {
			{2,3,4,5},			//0
			{2,7},				//1
			{3,8},					//2
			{2,4,9},					//3
			{3,5,10},					//4
			{4,11},					//5
			{5,12},					//6
			{8},					//7
			{2,9,14},					//8
			{3,8,10,15},					//9
			{4,9,11,16},					//10
			{5,10,17},					//11
			{11},					//12
			{7,14},					//13
			{8,15},					//14
			{9,14,16},					//15
			{10,15,17},					//16
			{11,16},					//17
			{12,17},					//18
			{14,20},					//19
			{15,21},					//20
			{16,20},					//21
			{17,21},					//22
	};
	private final int player0MaxCoins  = 3;
	private final int player1MaxCoins = 15;
	private final int numBoardPoints = 23;
	private final int NUM_GOATS_CAPT_FOR_TIGER_WIN = 5;
	
	private float width;
	private float height;
	private Line[] linesArray = new Line[numLines];    
	private int currPlayer;
	private boolean AIAsTiger;
	private int numPlayers;
	private boolean player0RunState;
	private boolean player1RunState;
	private ArrayList<Integer> player0LocIndex = new ArrayList<Integer>();
	private ArrayList<Integer> player1LocIndex = new ArrayList<Integer>();
	private ArrayList<Integer> randomGoatPlaced = new ArrayList<Integer>();
	private BoardPoint[] boardPointArray = new BoardPoint[numBoardPoints];
	private int indexOfSelectedPoint;
	private boolean actionMoveFlag;
	private PointF currLocOfSelectedPoint;
	private int tigerWinCounter;
	private int player1CurrCoinCount;
	private boolean debugMode;
	
	public Game(RectF boardDime){
		width = boardDime.width();
		height = boardDime.height();
		AIAsTiger = false;
		System.out.println("Width of board" + width + "\n" + "Height of board" + height);
		//drawing the lines- first step
		//use set fns to call the variables from lines class
		for (int curr_line=0; curr_line<numLines;curr_line++) {
			linesArray[curr_line] = new Line();
			linesArray[curr_line].setLineInfo(lineStartXRatio[curr_line]*width,lineStartYRatio[curr_line]*height,
					lineEndXRatio[curr_line]*width,lineEndYRatio[curr_line]*height);
		}
		//creating the board points
		for (int currNum=0;currNum<numBoardPoints;currNum++){
			boardPointArray[currNum] = new BoardPoint(new PointF(allPointsX[currNum]*width,allPointsY[currNum]*height),
														neighborPoints[currNum]);
		}
		resetGame();
		
		//debugMode = true;
		if(debugMode){
			int player0DebugArray[] = {0,1,18};
			int player1DebugArray[] = {2,3,4,5,6,7,8,9,10,11,13};
			player0RunState = true;
			for(int debugIndex=0; debugIndex<player0DebugArray.length;debugIndex++){
				player0LocIndex.add(debugIndex,Integer.valueOf(player0DebugArray[debugIndex]));
				boardPointArray[player0LocIndex.get(debugIndex)].setIsOccupied(true);
			}
			for(int debugIndex=0; debugIndex<player1DebugArray.length; debugIndex++){
				player1LocIndex.add(debugIndex, Integer.valueOf(player1DebugArray[debugIndex]));
				boardPointArray[player1LocIndex.get(debugIndex)].setIsOccupied(true);
				player1CurrCoinCount = 11;
			}
		}
		
	}
	
	public int getNumLines(){
		return numLines;
		
	}     
	public boolean getAIAsTiger() {
		return AIAsTiger;
	}
	
	// currInder value comes from i value of the FOR loop in graphicalview.java 
	public Line getLineValues(int currIndex){
		   
		return linesArray[currIndex];
	}
	
	public int getCurrPlayer() {
		return currPlayer;
	}
	public int getNumPlayers(){
		return numPlayers;
	}
	public void updateCurrPlayer() {
		currPlayer = currPlayer==0 ? 1 : 0; // if 0 it returns 1 else returns 0
			
	}

	public int getNumCoins(int player) {
		if (player==0) {
			return player0LocIndex.size();
		} else {
			return player1LocIndex.size();
		}
	}
	
	// on action_down this method identifies which coin is selected by which player
	public boolean identifyCoin(PointF currPoint){
		for(int i=0; i<boardPointArray.length; i++){
			if(boardPointArray[i].touchContains(currPoint) && boardPointArray[i].getIsOccupied()){
				if(currPlayer == 0 && player0RunState == true && player0LocIndex.contains(Integer.valueOf(i))){
					indexOfSelectedPoint = i;
					currLocOfSelectedPoint = boardPointArray[indexOfSelectedPoint].getBoardPoint();
					actionMoveFlag = true;//so now the move flag is set only when a touch happens on a valid point n not on any random spot. it throws error otherwise
					return true;
				}
				else if(currPlayer == 1 && player1RunState == true && player1LocIndex.contains(Integer.valueOf(i))){
					indexOfSelectedPoint = i;
					//System.out.println("the index value for player 1 is" + i);
					currLocOfSelectedPoint = boardPointArray[indexOfSelectedPoint].getBoardPoint();
					actionMoveFlag = true;//so now the move flag is set only when a touch happens on a valid point n not on any random spot. it throws error otherwise
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean updateMoveCoin(PointF point){
		//actionMoveFlag = true; //this line is moved to identifycoin() method because i dont want the action move flag to be set 
		//whenever a move action happens randomly on the screen because this flag is independent of whether a coin is touched or not.
		
		currLocOfSelectedPoint = point;
		return true;
		
	}
	public boolean updateCoin(PointF loc) {
		// this method checks where the touch happens and updates the x,y value based on the touch
		// used for both 1 player and 2 player
		boolean coinUpdated = false;
		for(int i=0;i<boardPointArray.length;i++){
			if(boardPointArray[i].touchContains(loc) && !(boardPointArray[i].getIsOccupied())){
				if (currPlayer==0 && player0RunState == false) {
					player0LocIndex.add(Integer.valueOf(i));
					boardPointArray[i].setIsOccupied(true);
					if (player0LocIndex.size() == player0MaxCoins)
						player0RunState = true;
					coinUpdated=true;
				} else if (currPlayer==1 && player1RunState == false) {
					player1LocIndex.add(Integer.valueOf(i));
					boardPointArray[i].setIsOccupied(true);
					player1CurrCoinCount++;
					// Use counter instead of player1LocIndex.size because a goat can
					// get killed while in InitState and player1LocIndex will be reduced.
					if (player1CurrCoinCount == player1MaxCoins)
						player1RunState = true;
					coinUpdated=true;
				}
				if (currPlayer==0 && player0RunState==true && actionMoveFlag == true) {
					actionMoveFlag = false; //u set actionmove flag false here bcas u want to check if the staring point is a valid pointby check if it is true otherwise it still thinks that the starting point the starting point of the previous move 
					// Check if final position is a neighbor
					boolean moveHappened = false;
					if(boardPointArray[indexOfSelectedPoint].neighborCheck(i)){
						moveHappened = true;
					} else {
						for(int index=0; index<skippingPointArray[indexOfSelectedPoint].length; index++){
							if((skippingPointArray[indexOfSelectedPoint][index] == i) && 
									(player1LocIndex.contains(Integer.valueOf(neighborGoatToSkip[indexOfSelectedPoint][index])))){
								tigerWinCounter++;
								int tempIndex = neighborGoatToSkip[indexOfSelectedPoint][index];
								player1LocIndex.remove(Integer.valueOf(tempIndex));
								boardPointArray[tempIndex].setIsOccupied(false);
								moveHappened = true;
							}
						}
					}
					if (moveHappened) {
						int indexOfPlayer0Array = player0LocIndex.indexOf(Integer.valueOf(indexOfSelectedPoint));//pass the index of player0array that has the index value of originally selected point
						player0LocIndex.set(indexOfPlayer0Array, Integer.valueOf(i)); //now i refers to the end point. loc is the end point in run state.change the orig value to new value
						boardPointArray[indexOfSelectedPoint].setIsOccupied(false);
						boardPointArray[i].setIsOccupied(true);// i is the tiger value
						coinUpdated = true;
					}	
				}
				if (currPlayer==1 && player1RunState==true && actionMoveFlag == true) {
					actionMoveFlag = false;
					if(boardPointArray[indexOfSelectedPoint].neighborCheck(i)){
						int indexOfPlayer1Array = player1LocIndex.indexOf(Integer.valueOf(indexOfSelectedPoint));
						player1LocIndex.set(indexOfPlayer1Array, Integer.valueOf(i));
						boardPointArray[indexOfSelectedPoint].setIsOccupied(false);
						boardPointArray[i].setIsOccupied(true);
						coinUpdated = true;
					}
				}
			}
			if(coinUpdated){ //the function will return true as soon as it finds the coin in such a way that conditions r satisfied and once found u dont have to go through the loop rest of the times.
				return true;
			}
		}
		actionMoveFlag = false; //this one helps in mving the coin from an invalid point back to its original point.Cos when a coin is moved to an invalid point, it will not go through the first if itself and it will directly go to false fixing that point
		
		return false; //when none of the conditions work or if the for loop ends return false
	}
	
	//this fn gets called after updateActionMove method
	public PointF getCoinPosition(int CurrPlayer, int CurrCoin) {
		int index;
		PointF point;
		boolean selection;
		selection = (CurrPlayer==0)?false:true;		
		if (selection) {
			index = (player1LocIndex.get(CurrCoin)).intValue();
		} else {
			index = (player0LocIndex.get(CurrCoin)).intValue();
		}
		point = boardPointArray[index].getBoardPoint();
		//this is performed during action move.if the point that is touched is the point in that particular loc
		// & moveflag is set then that is the point to be moved
		if (actionMoveFlag && (indexOfSelectedPoint == index)) 
			point = currLocOfSelectedPoint;
		return point;
	}

	//AI implementation
	public void goatAsComputer(){
		System.out.println("Came inside goatAsComputer");
		// pick a random spot
		// check if the neighbor is tiger i.e if he can kill the goat
		if (player1RunState==false) {
			boolean placeGoatAtThePoint;
			int currGoatPoint;
			// TODO:do the check for the starting point of the goat and the skip the points that has already been checked
			Random randomPoint = new Random();
			currGoatPoint = randomPoint.nextInt(boardPointArray.length);//this picks a random value with in the range
			do {
				placeGoatAtThePoint = true;
				System.out.println("Curr goat index" + currGoatPoint);
				if(!(boardPointArray[currGoatPoint].getIsOccupied())){
					System.out.println("the curr goat point flag is not occupied" );
					//check if goat's neighbor has a tiger i.e goat is placed next to a tiger and the other neighbor of goat i.e the skipping point of tiger is occupied
					//then return tru`e i.e valid move
					for(int temp=0;temp< neighborPoints[currGoatPoint].length;temp++){
						System.out.println("temp value to check neighbor points" + temp);
						if(player0LocIndex.contains(neighborPoints[currGoatPoint][temp])){
							System.out.println("this means that tiger is the neighbor" + neighborPoints[currGoatPoint][temp]);
							int playerPlacedTigerHere = neighborPoints[currGoatPoint][temp];
							for(int curr=0;curr<neighborGoatToSkip[playerPlacedTigerHere].length;curr++){
								if(currGoatPoint == neighborGoatToSkip[playerPlacedTigerHere][curr]){
									if(!(boardPointArray[skippingPointArray[playerPlacedTigerHere][curr]].getIsOccupied())){
										System.out.println("checking for skipping point array occupied " + skippingPointArray[playerPlacedTigerHere][curr]);
										placeGoatAtThePoint = false;
										break;
									}
								}
							}
							if(!placeGoatAtThePoint){
								break;
							}
						}
					}
					if(placeGoatAtThePoint){
						player1LocIndex.add(Integer.valueOf(currGoatPoint));
						boardPointArray[currGoatPoint].setIsOccupied(true);		
						player1CurrCoinCount++;
						if (player1CurrCoinCount == player1MaxCoins){
							System.out.println("currcoincount" + player1CurrCoinCount);
							player1RunState = true;	
						}
					}
				} else {
					placeGoatAtThePoint = false;
				}
				if (!placeGoatAtThePoint) {
					currGoatPoint++;
					if (currGoatPoint==numBoardPoints) {
						currGoatPoint = 0;
					}
					System.out.println("currgoatvalue after incrementing" + currGoatPoint);
				}
			} while (!placeGoatAtThePoint);
		} else {
			// Goat run state
			// TODO: throws error when all the goats are killed.Fix it.
			Random randomGoat = new Random();
			int currGoatIndexToMove = randomGoat.nextInt(player1LocIndex.size());
			boolean placeGoatHere = false; 
			int count = 0;
			do{
				int currGoatToMove = player1LocIndex.get(currGoatIndexToMove);
				System.out.println("random goat selected to move"+currGoatIndexToMove);
				if (count==0){
					boardPointArray[currGoatToMove].setIsOccupied(false);// this is to check if the goat can be killed in the condition where it is moved from
					System.out.println("when count is 0" + neighborPoints[currGoatToMove][count] );
				}
				int currGoatRandomNeighbor = neighborPoints[currGoatToMove][count];
				if(!(boardPointArray[currGoatRandomNeighbor].getIsOccupied())){
					placeGoatHere = true;
					for(int temp=0;temp<neighborPoints[currGoatRandomNeighbor].length;temp++){
						if(player0LocIndex.contains(neighborPoints[currGoatRandomNeighbor][temp])){
							int playerPlacedTigerHere = neighborPoints[currGoatRandomNeighbor][temp];
							System.out.println("the tiger is the neighbor" + playerPlacedTigerHere );
							for(int curr=0;curr<neighborGoatToSkip[playerPlacedTigerHere].length;curr++){
								if(currGoatRandomNeighbor == neighborGoatToSkip[playerPlacedTigerHere][curr] 
										&& !(boardPointArray[skippingPointArray[playerPlacedTigerHere][curr]].getIsOccupied())){
									placeGoatHere = false;
									break;
								}
							}
						}
						if (placeGoatHere==false)
							break;
					}
				}
				else
					placeGoatHere = false;
				//move coin
				if(placeGoatHere){
					player1LocIndex.set(currGoatIndexToMove, Integer.valueOf(currGoatRandomNeighbor));
					boardPointArray[currGoatRandomNeighbor].setIsOccupied(true);
				} else {
					count++;
					if (count == neighborPoints[currGoatToMove].length) {
						boardPointArray[currGoatToMove].setIsOccupied(true);
						count = 0;
						currGoatIndexToMove++;
						if (currGoatIndexToMove==player1LocIndex.size()) {
							currGoatIndexToMove = 0;
						}
					}
				}
			} while(!placeGoatHere);
		}
	}

	public boolean tigerWins(){
		if (currPlayer==0) {
		   return (tigerWinCounter == NUM_GOATS_CAPT_FOR_TIGER_WIN);
		}
		return false;
	}
	
	public boolean goatWins(){
		if(currPlayer==1 && player0RunState==true){
			boolean goatWinsFlag = true;
			for(int temp=0;temp<player0LocIndex.size();temp++){
				int currTigerIndex = Integer.valueOf(player0LocIndex.get(temp));
				for(int neighborIndex=0; neighborIndex<neighborPoints[currTigerIndex].length; neighborIndex++){
					if(!(boardPointArray[neighborPoints[currTigerIndex][neighborIndex]].getIsOccupied())){
						goatWinsFlag=false;
						int neighborOfCurrTiger = neighborPoints[currTigerIndex][neighborIndex];
						System.out.println("initial neighbor points " + neighborOfCurrTiger);
					}
				}
				for(int skipIndex=0; skipIndex<skippingPointArray[currTigerIndex].length; skipIndex++){
					if(!(boardPointArray[skippingPointArray[currTigerIndex][skipIndex]].getIsOccupied()) 
							&& player1LocIndex.contains(neighborGoatToSkip[currTigerIndex][skipIndex])){
						goatWinsFlag=false;
						System.out.println("status of the goat win is" + goatWinsFlag);
					}
				}			
			}
			return goatWinsFlag;     
		}
		return false;
			
	}
	
	public void resetGame(){
		player0LocIndex.clear();
		player1LocIndex.clear();
		// clear other variables too
		currPlayer = 0;
		numPlayers = 2;
		tigerWinCounter = 0;
		player0RunState = false; //to indicate if the player has started moving the coins or still placing the coins
		player1RunState = false;
		actionMoveFlag = false;
		debugMode = false;
		player1CurrCoinCount = 0;
		for(int temp=0;temp<numBoardPoints;temp++){
			boardPointArray[temp].setIsOccupied(false);
		}
	}
}
