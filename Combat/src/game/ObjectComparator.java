package game;
import java.util.ArrayList;

/*
 * @author Emily Clark
 */

//this contains methods to compare objects to determine if they are present within a cell.
public class ObjectComparator implements ObjectComparatorADT {
	
	public ObjectComparator()
	{
		//empty constructor
	}

	boolean isPresent = false;
	int itemLoc = -1;
	
	/**
	 * Searches the Usable ArrayList for the object listed in Cell at a current location.
	 * @param String item
	 * @param ArrayList usableobj
	 * @return integer listing the index location in Array of desired object.
	 * @return will return -1 if item is not found.
	 */
	public int isPresentUsable(String item, ArrayList<Usable> usableobj)
	{
		for(int i=0; i<usableobj.size(); i++)
		{
			if(item.equals(usableobj.get(i).getID()))
			{
			
				itemLoc = i;
				return itemLoc;
			}
			
		}
		return itemLoc;
	}
	
	/**
	 * Searches the Equipable ArrayList for the object listed in Cell at a current location.
	 * @param String item
	 * @param ArrayList equipbj
	 * @return integer listing the index location in Array of desired object.
	 * @return will return -1 if item is not found.
	 */
	public int isPresentEquipable(String item, ArrayList<Equipable> equipobj)
	{
		
		for(int i=0; i<equipobj.size(); i++)
		{
			if(item.equals(equipobj.get(i).getID()))
			{
				
				itemLoc = i;
				return itemLoc;
			}
			
		}
		return itemLoc;
		
	}
	
	/**
	 * Searches the KeyItems ArrayList for the object listed in Cell at a current location.
	 * @param String item
	 * @param ArrayList keyitemobj
	 * @return integer listing the index location in Array of desired object.
	 * @return will return -1 if item is not found.
	 */
	public int isPresentKeyItem(String item, ArrayList<KeyItems> keyitemobj)
	{
		for(int i=0; i<keyitemobj.size(); i++)
		{
			if(item.equals(keyitemobj.get(i).getID()))
			{
				itemLoc = i;
				return itemLoc;
			}
			
		}
		return itemLoc;
	}
	
	/**
	 * Searches the Encounter ArrayList for an encounter listed in Cell at a current location.
	 * @param String cellEnc
	 * @param ArrayList encobj
	 * @return integer listing the index location in Array of desired encounter.
	 * @return will return -1 if encounter is not found.
	 */
	/*
	public int isPresentEntity(String cellEnc, ArrayList<Encounter> encobj)
	{
		for(int i=0; i<encobj.size(); i++)
		{
			if(cellEnc.equals(encobj.get(i).getCombatants().get()))
			{
				itemLoc = i;
				return itemLoc;
			}
			
		}
		return itemLoc;
	}
	*/
	/**
	 * Returns a KeyItems object
	 * @param String item
	 * @param ArrayList keyitemobj
	 * @return KeyItems object
	 */
	public KeyItems keyItemFound(String item, ArrayList<KeyItems >keyitemobj)
	{
		KeyItems newItem = keyitemobj.get(isPresentKeyItem(item, keyitemobj));
		
		return newItem;
	}

	/**
	 * Returns a Usable object
	 * @param String item
	 * @param ArrayList usableobj
	 * @return Usable object
	 */
	public Usable usableItemFound(String item, ArrayList<Usable> usableobj)
	{
		Usable newItem = (Usable) usableobj.get(isPresentUsable(item, usableobj));
		return newItem;
	}
	
	/**
	 * Returns a Equipable object
	 * @param String item
	 * @param ArrayList equipobj
	 * @return Equipable object
	 */
	public Equipable equipableItemFound(String item, ArrayList<Equipable> equipobj)
	{
		Equipable_Armor newItem = (Equipable_Armor) equipobj.get(isPresentEquipable(item, equipobj));
		
		return newItem;
	}
	
	/*
	 * Searches the Usable ArrayList for the object listed in Cell at a current location.
	 * @param String teamID
	 * @param teamID should be pulled from Cell.getEncounter();
	 * @param ArrayList usableobj
	 * @return integer listing the index location in Array of desired object.
	 * @return will return -1 if item is not found.
	 */
	public ArrayList<Entity> isEncounterPresent(String teamID, ArrayList<Encounter> encounterobj)
	{
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		for(int i=0; i<encounterobj.size(); i++)
		{
			if(teamID.equals(encounterobj.get(i).getCombatants().get(i).getTeamId()))
			{
				itemLoc = i;
				entityList.add(encounterobj.get(i).getCombatants().get(i));
				return entityList;			
			}
			
		}
		
		return entityList;
		
	}
}
