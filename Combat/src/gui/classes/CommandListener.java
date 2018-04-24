package gui.classes;

import game.Cell;
import game.Encounter;
import game.Equipable;
import game.Equipable_Armor;
import game.Equipable_Weapon;
import game.KeyItems;
import game.Location;
import game.ObjectComparator;
import game.Player;
import game.Usable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JTextArea;
import querymachine.QueryMachine;

public class CommandListener {
    final String INSTANCE = "DN1";
    public static PlayWindow play;
    private Player player;
    private Cell[][][] cellList;
    private ArrayList<Usable> usableList;
    private ArrayList<Equipable> equipList;
    private ArrayList<KeyItems> keyList;
    private ArrayList<Encounter> encList;

    public CommandListener() {
        MainMenu menu = new MainMenu();
        boolean nGame = menu.getNGame();
        if(nGame == true){//open new game window
            NewGameWindow window = new NewGameWindow();
            this.player = window.getNewPlayer();

        }
        else{   //load game
            new LoadGameWindow();
        }
        
        
        try{
        GUI_Client.main(null);
        play = GUI_Client.getPlayWindow();
        } catch(Exception e) {
        	System.out.print("Exception");
        }
        try{
            newGame();
        }catch(Exception e){
            System.out.print("Hah got one!");
        }
        boolean run = true;
        int count = 0;
        while(run) {
        			
        			System.out.println("Iteration " + ++count);        			
        			listen(play , run);
        		}
    }

    /**
     * Uses parsed input in a {command}{parameter} format in order to filter
     * user input based on help file commands Input comes from JTextField and
     * output goes to JTextArea, both in PlayWindow class.
     *
     * Every /command should generate some sort of output for the user to see
     * the effect of their actions. If no command is recognized, then output
     * warns user of an invalid input, referencing the help file for more
     * instructions.
     *
     * Case-switch determines which methods or blocks of code are run, and which
     * output is to be sent to the JTextArea for the user to see.
     *
     *
     *
     * @param play Instance of play window in which the input/output can be
     * found
     * @param run Boolean representing whether or not the listener loop should
     * still run after current iteration
     */
    public void listen(PlayWindow play, boolean run) {

        String[] stringArray = inputParser(play.requestInput());
        
        String command = stringArray[0];
        String parameter = stringArray[1];
        
        
        String output = "";
        //String copycat = "/" + play.getOutputBox().getText();

        switch (command) {

            //if no parameter is present in the {command}{parameter} structure, then output is set
            //	to current room's description
            //otherwise, parameter may fall into entity, item, equipment, etc., and should be output
            //	based upon that
            case "/look":
                //inspect room
                if (parameter == null) {
                    //TO_DO
                    output = cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].getDesc();
                } //inspect object of command
                else {
                    Object looked = parseParameter(parameter);
                    
                    if (looked == null) {
                    		output = "You can look all day, but you still won't find it, " + this.player.getName() + ".";
                    }
                    else if(looked instanceof Usable) {
                    		output = ((Usable) looked).getDescription();
                    }
                    else if(looked instanceof Equipable ) {
                    		output = ((Equipable) looked).getDescription();
                    }
                    else if(looked instanceof KeyItems) {
                    		output = ((KeyItems) looked).getDescription();
                    
                    }

                }
                break;
                
            case "/inspect":
            		String name = inspectCell();
            		
            		if(name == null) {
            			output = "You search long and hard, but your effort turns up nothing of interest.";
            		}
            		else {
            			output = "By your sharp eyes or by good fortune, you find " + name + "!";
            		}
            		
            		break;
                

            case "/take":
            		if (parameter == null) {
                    //TO_DO
                    output = "Oh, come one. You've got to give me more info than that!";
                } //inspect object of command
                else {
                    Object taken = parseParameter(parameter);
                    
                    if(taken == null) {
                    		output = "I'm not sure what you were expecting to take...";
                    }
                    else if(taken instanceof Usable) {
                    		Usable item = (Usable)taken;
                    		usableList.remove(item);
                    		
                    		player.getItemList().add(item);
                    		
                    		output = "You have added " + item.getName() + " to your inventory.";
                    }
                    else if (taken instanceof KeyItems) {
                    		KeyItems item = (KeyItems)taken;
                    		keyList.remove(item);
                    		
                    		player.getKeyItemsList().add(item);
                    		
                    		output = "Hm. This looks interesting. You add " + item.getName() + " to your pack.";
                    }
                    
                    else {
                    		output = "That looks awkward to just carry around.";
                    }

                }
                break;
                
            
            case "/drop":
            		if (parameter == null) {
                    //TO_DO
                    output = "Stop, drop, and roll is often useful, but not here.";
                } //inspect object of command
                else {
                    //TO_DO
                		Object dropped = searchInventory(parameter);
                		
                		if(dropped == null) {
                			output = "You must first HAVE that in order to drop it.";
                		}
                		
                		else {
                			if (dropped instanceof KeyItems) {
                				KeyItems item = (KeyItems)dropped;
                				output = "You have a feeling that you may need " + item.getName() + ". It wouldn't be wise to drop it.";
                			}
                			
                			else if(dropped instanceof Usable) {
                        		Usable item = (Usable)dropped;
                        		player.getItemList().remove(item);
                        		                        		
                        		output = "You have removed " + item.getName() + " from your inventory.";
                        }
                			
                			else if(dropped instanceof Equipable) {
                				Equipable item = (Equipable)dropped;
                				
                				if(item.getName().equals(player.getWornArmor().getName())) {
                					player.setWornArmor(null);
                				}
                				if(item.getName().equals(player.getUsedWeapon().getName())) {
                					player.setUsedWeapon(null);
                				}
                				
                				output = "Well, you've dropped " + item.getName() + ", and it crumbles to dust "
                					   + "when it hits the floor.\n\n Happy now?";
                				
                			}
                			
                			else {
                        		output = "However you managed to get your hands on that thing, it's gone now...";
                        }
                			
                		}
                		
                
                    
                    
                }
                break;

            case "/use":
            		if (parameter == null) {
                    //TO_DO
                    output = "Giving more information would be ... USEful.";
                } //inspect object of command
                else {
                    //TO_DO
                		Object used = searchInventory(parameter);
                		
                		if (used == null) {
                			output = "I could use my weight in gold. You could use a" + parameter  
                				+    ". Not everyone can get what they want, though, can they, " + player.getName()
                				+    "?";	
                		}
                		else {
                			if (used instanceof KeyItems) {
                				KeyItems item = (KeyItems)used;
                				output = "You twirl " + item.getName() + " around in your fingers."
                						+"While fun, you're not entirely sure how or where to use it..."
                						+"\n\nIt's best to keep looking.";
                			}
                			
                			else if(used instanceof Usable) {
                        		Usable item = (Usable)used;
                        		item.useItem(null, player);
                        		                        		
                        		output = "You have used " + item.getName() + ".";
                        }
                			
                			else if(used instanceof Equipable) {                				
                			
                				output = "You really need to find a better time for sparring.";
                				
                			}
                			
                			else {
                        		output = "You can't use that.";
                        }
                			
                		}
                		
                }
                break;

            case "/equip":
            		if (parameter == null) {
                    //TO_DO
                    output = "Are you sure you're equipped for this task if you can't give me more information?";
                } //inspect object of command
                else {
                    //TO_DO
                		Object equipped = parseParameter(parameter);
                		
                		if(equipped == null) {
                			output = "You'll have no such luck equipping that.";
                		}
                		
                		else if (equipped instanceof Equipable_Armor) {
                				Equipable_Armor armor = (Equipable_Armor)equipped;
                				Equipable old = player.getWornArmor();
            					player.setWornArmor(armor);
            					
            					output = "As soon as you finish donning " + armor.getName() + ", "
            							+old.getName() + " crumbles to dust. How convenient.";
            			}
                		else if (equipped instanceof Equipable_Weapon) {
                				Equipable_Weapon weapon = (Equipable_Weapon)equipped;
                				Equipable old = player.getUsedWeapon();
            					player.setUsedWeapon(weapon);
            					
            					output = "As soon as you pick up " + weapon.getName() + ", "
            							+ old.getName() + " crumbles to dust. How convenient.";
            			}
                		
                		else {
                			output = "Yeah, good luck fighting with that.";
                		}
                				
                		
                }
                break;

            case "/inventory":
            case "/inv":
            	
            		if(this.player.getItemList() == null) {
            			output = "Nothing to see here.";
            		}
            		else if(this.player.getItemList().isEmpty()) {
            			output = "You don't have anything in your backpack. Poor you.";
            		}
            		else {
            			for(Usable item : player.getItemList()) {
            				output += item +  "\n";
            			}
            		}
            		
            	
                break;

            case "/equipment":
            		Equipable armor = this.player.getWornArmor();
            		Equipable weapon = this.player.getUsedWeapon();
            		
	            	if(armor == null && weapon == null) {
	        			output = "You might as well be naked! Maybe that's why the villagers ushered you into this cave...\n\nHmmmm.....";
	        		}
	        		else {
	        			if(armor != null) {
	        				output += "You are wearing " + armor.getName() + ".\n";
	        			}
	        			if(weapon != null) {
	        				output += "You are wielding " + weapon.getName() + ".\n";
	        			}
	        		}
            	
                break;

            case "/status":
            case "/stats":
                output = this.player.printEntityInfo();
                break;

            //determines if movement can be made in the direction sent by the user
            //direction is parsed from /command and streamlined into a single character: n, s, e, or w
            //	so the input for applicable methods should rely on that passed char
            //if player can move that direction, sets new current cell and outputs that cell's description
            //if not, outputs some sort of error to user
            case "/north":
            case "/n":
            case "/south":
            case "/s":
            case "/east":
            case "/e":
            case "/west":
            case "/w":

                char direction = command.charAt(1);

                output = "You'd like to go that way, wouldn't you?";
                switch (direction) {
                    case 'n':
                        //check to see of movement possible
                        if (cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].isNorth()) {
                            player.getLocation().setY(player.getLocation().getY() + 1);
                            output = cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].getDesc();
                        }

                        break;
                    case 's':
                        //check to see of movement possible
                        if (cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].isSouth()) {
                            player.getLocation().setY(player.getLocation().getY() - 1);
                            output = cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].getDesc();
                        }
                        break;
                    case 'e':
                        //check to see of movement possible
                        if (cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].isEast()) {
                            player.getLocation().setX(player.getLocation().getX() + 1);
                            output = cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].getDesc();
                        }
                        break;
                    case 'w':
                        //check to see of movement possible
                        if (cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].isWest()) {
                            player.getLocation().setX(player.getLocation().getX() - 1);
                            output = cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].getDesc();
                        }
                        break;
                    default:
                        output = "This should never happen, please contact your nearest dev.";

                }

                break;

            case "/help":
                output = "Your cries for help go answered, and text appears before your eyes.";
                new HelpWin();
                break;

            case "/quit":
                output = "Next time, hero.";
                run = false;
                //new MainMenu();
                play.exitGame();
                play = null;
                break;

            case "/save":
                		output = "You're past the point of no return.";
            		//play.outGUI("Enter the name of your save file.\n)"
                //       + "WARNING: Duplicating a save file name will override the original save.");
                //String fileName = play.requestInput();
                //output = "TEST SAVE";
                break;
                

            //if command is not recognized, outputs a flavorful error and references the /help command for assistance
            default:
                output = "Your mutterings echo softly, but go unanswered.\n"
                        + "[try again, or type '/help' for assistance]";
                break;

        }//end switch

        //sends output generated from user selection to the GUI window
        play.outGUI(output);

    }//end commandListener()
    
    private Object searchInventory(String search) {
    	
    		ArrayList<Usable> itemList = player.getItemList();
		ArrayList<KeyItems> keyItemList = player.getKeyItemsList();
		ArrayList<Equipable> equipmentList = new ArrayList<>();
		equipmentList.add(player.getUsedWeapon());
		equipmentList.add(player.getWornArmor());
		
		for(Usable item : itemList) {
			if (item.getName().equalsIgnoreCase(search)){
				return item;
			}
		}
		
		for(KeyItems item : keyItemList) {
			if (item.getName().equalsIgnoreCase(search)){
				return item;
			}
		}
		
		for(Equipable item : equipmentList) {
			if (item.getName().equalsIgnoreCase(search)){
				return item;
			}
		}
		
		return null;
		
		
    }
    
    
    //WIP
    private Object parseParameter(String parameter) {

	
	    	Cell currentCell = player.getCurrentCell(cellList);
	    	parameter = parameter.toLowerCase();
	
	    	ObjectComparator comp = new ObjectComparator();
	    	int notFound = -1;
	
	    	//private ArrayList<Usable> usableList;
	    	//private ArrayList<Equipable> equipList;
	    	//private ArrayList<KeyItems> keyList;
	    	//private ArrayList<Encounter> encList;

	    	//ArrayList<Entity>
	    	
	    	
	    	int usableIndex = comp.isPresentUsable(currentCell.getItem(), usableList);
	    	int equipIndex = comp.isPresentEquipable(currentCell.getItem(), equipList);
	    	int keyIndex = comp.isPresentKeyItem(currentCell.getKeyItem(), keyList);
	    	//int entityIndex = equipIndex;
	
	    	if(usableIndex == notFound && equipIndex == notFound && keyIndex == notFound ) {
	    		// parameter not found
	    	}
	    	else {
	    		if (usableIndex > notFound) {
	
	    			Usable item = usableList.get(usableIndex);
	    			if (item.getName().toLowerCase().equals(parameter)){
	    				return item;
	    			}
	    		}
	    		if (equipIndex > notFound) {
	
	    			Equipable item = equipList.get(equipIndex);
	    			if (item.getName().toLowerCase().equals(parameter)){
	    				return item;
	    			}
	    		}
	    		if (keyIndex > notFound) {
	
	    			KeyItems item = keyList.get(keyIndex);
	    			if (item.getName().toLowerCase().equals(parameter)){
	    				return item;
	    			}
	    		}
	    	}
	
	    	return null;


    }
    
    private String inspectCell() {
    	
    	ObjectComparator comp = new ObjectComparator();
    	Cell currentCell = player.getCurrentCell(cellList);
    	
    	int notFound = -1;
    	
    	int usableIndex = comp.isPresentUsable(currentCell.getItem(), usableList);
    	int equipIndex = comp.isPresentEquipable(currentCell.getItem(), equipList);
    	int keyIndex = comp.isPresentKeyItem(currentCell.getKeyItem(), keyList);
    	
    	if(usableIndex == notFound && equipIndex == notFound && keyIndex == notFound ) {
    		// parameter not found
    	}
    	else 
    	{
    		if (usableIndex > notFound) {

    			Usable item = usableList.get(usableIndex);
    				return item.getName();
    			
    		}
    		if (equipIndex > notFound) {

    			Equipable item = equipList.get(equipIndex);
    			return item.getName();
    			
    		}
    		if (keyIndex > notFound) {

    			KeyItems item = keyList.get(keyIndex);
    			return item.getName();
    			
    		}
    	}
    	

    	return null;
    	
    }
    

    /**
     * Parses input into an array with the format {verb},{object}. If no
     * parameter is present, that field will be null.
     *
     * @param input
     * @return String array with command and parameter.
     */
    public String[] inputParser(String input) {

        String[] stringArray = new String[2];

        input = input.trim().toLowerCase();

        boolean containsSpace = input.contains(" ");

        //will be -1 if no space present
        int indexOfSpace = input.indexOf(" ");

        stringArray[0] = (containsSpace) ? input.substring(0, input.indexOf(" ")) : input;
        stringArray[1] = (containsSpace) ? input.substring(indexOfSpace + 1) : null;

        return stringArray;

    }

    public void newGame() throws SQLException, IOException {
        // create new instance of the game for the player using the input from the creator
        //save player
    		Location tempLoc = new Location();
    		player.setLocation(tempLoc);
    	
        player.getLocation().setX(0);
        player.getLocation().setY(3);
        player.getLocation().setZ(0);
        System.out.print("ayylmao");
        loadInstance(INSTANCE);
    }

    public void loadInstance(String instance) throws SQLException, IOException {
        //execute query for cells in instance
        QueryMachine theDestroyer = new QueryMachine();
        ArrayList<Cell> cellobj;
        cellobj = theDestroyer.getCellInstance(instance);
        ArrayList<Usable> usableobj;
        usableobj = theDestroyer.getHPUsableInstance(cellobj);
        usableobj = theDestroyer.getSPUsableInstance(cellobj, usableobj);
        ArrayList<Equipable> equipableobj;
        equipableobj = theDestroyer.getArmorInstance(cellobj);
        ArrayList<KeyItems> keyitemobj;
        keyitemobj = theDestroyer.getKeyItemsInstance(cellobj);
        ArrayList<Encounter> encounterobj;
        encounterobj = theDestroyer.getEncounterInstance(cellobj);
        Cell[][][] cellArray = theDestroyer.getCellArray(cellobj);
        cellList = cellArray;
        usableList = usableobj;
        equipList = equipableobj;
        encList = encounterobj;
        keyList = keyitemobj;

    }

}
