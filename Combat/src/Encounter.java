
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author Jason Richter, Samuel Fiscus
 */
public class Encounter implements EncounterADT {

    private TreeMap<Integer, Entity> rekt = new TreeMap<>();
    private PriorityQueue<Entity> turn = new PriorityQueue<>();
    
    private TreeMap<Integer, Entity> entities = new TreeMap<>();

    public Encounter() {
    }

    public Encounter(ArrayList<Entity> combatants) {
        //populate tree sorted by initiative
        for(Entity temp : combatants){
            entities.put(temp.getStats().getInitiative(), temp);
        }
        //System.out.println(entities); //test print
    }

    public void RunCombat() {
        boolean valid = runQueue(); //initial enqueue, never runs combat if failed
        if (valid) {
            System.out.println("I got this far!");
            
            //test print
            for(Map.Entry<Integer,Entity> entry: entities.entrySet()){
                System.out.println(entry.getValue());
            }
            
            System.out.println("Defeated:");
            for(Map.Entry<Integer,Entity> entry : rekt.entrySet()){
                System.out.println("\t" + entry.getValue().getName());
            }
        }
        else {
            System.out.println("No conflict here.");
            
            
        }
        
        //all entries currently removed from entities
    }

    private boolean runQueue() {
        boolean teamCheck = false; //boolean to check team compositions
        
        int firstTeam = entities.lastEntry().getValue().getTeamId();
        Entity temp;
        
        for(Map.Entry<Integer,Entity> entry: entities.entrySet()){
            temp = entry.getValue();
            if(temp.getStats().isAlive()){
                //System.out.println(temp.getName());
                if(temp.getTeamId() != firstTeam && teamCheck == false)
                    teamCheck = true;
            }
            else{ //can remove Entity from combat and add to rekt
                rekt.put(entry.getKey(), entry.getValue());
            } 
        }
        for(Map.Entry<Integer,Entity> entry: rekt.entrySet()){
            int key = entry.getKey();
            if (entities.containsKey(key) ){
                entities.remove(key);
            }
        }
        return teamCheck; //returns boolean representing validity of fight
    }
}
