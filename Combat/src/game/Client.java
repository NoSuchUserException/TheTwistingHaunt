package game;

/**
 *
 * @author jason
 */
public class Client {
    
    final String INSTANCE = "DN1";
    
    private Player player;
    private Cell[][][] cellList;
    
    
    public Client() {
    }
    
    public void newGame(Player player){
        // create new instance of the game for the player using the input from the creator
        //save player
        this.player = player;
        loadInstance(INSTANCE);
    }
    
    public void loadGame(){
        //load game for play
        
    }
    
    public void move(char input){
        
        
        switch(input){
            case 'n':
                if(cellList[player.getLocation().getX()][player.getLocation().getY()][player.getLocation().getZ()].isNorth()){
                    
                }
                break;
            case 's':
                
                break;
            case 'e':
                
                break;
            case 'w':
                
                break;
            
        }
        
    }
    
    public void loadInstance(String instance){
        //execute query for cells in instance
        
        
    }
    
    public void saveGame(){
        //save game state for later loading
    }
    
}


