package uiView;

import gameplay.commandServices.GameService;
import gameplay.newGame.PlayerInitializer;
import pojos.entity.PlayerEntity;
import pojos.environment.Cell;

import java.util.ArrayList;
import java.util.List;

import commandListener.Init;
import uiView.classes.GUI_Client;
import uiView.classes.LoadGameWindow;
import uiView.classes.MainMenu;
import uiView.classes.NewGameWindow;
import uiView.classes.PlayWindow;
import utilities.Logs;

public class UIMain {

	public static PlayWindow play;
	public static PlayerEntity player = new PlayerEntity();
	public static List<Cell> cells = new ArrayList<Cell>();
	static PlayerInitializer playerinit = new PlayerInitializer();
	public static Init init = new Init();

	final static boolean DEBUG_LOAD = false;
	final boolean DEBUG_SAVE = false;

	public static void main (String [] args) {

		MainMenu menu = new MainMenu();
		boolean nGame = menu.getNGame();
		if(nGame == true){//open new game window
			NewGameWindow window = new NewGameWindow();	            
			player = window.getNewPlayer();

		}
		else{   //load game
			new LoadGameWindow();
		}

		try{
			GUI_Client.main(null);
			play = GUI_Client.getPlayWindow();
			Logs.LOGGER.info("Play Window launched.");
		} catch(Exception e) {
			Logs.LOGGER.severe("Exception when trying to play GUI_Client.getPlayWindow()");
		}

		boolean run = true;
		while(run) {
			//TODO
			GameService system = new GameService(play, run);

			init.initializeListeners(play, system, player);
		}
	}

}
