/*
 * The Twisting Haunt
 * Emily Clark
 * 4-10-18
 * Does the heavy lifting utilizing database package and is class that is directly called upon in game logic.
 */
import java.util.ArrayList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import databaseconnector.CredentialIOConfig;
import databaseconnector.DBConnect;

public class QueryMachine implements QueryMachineADT
{
	DBConnect dbReader = new DBConnect();
    CredentialIOConfig credIO = new CredentialIOConfig();
    QueryDB generator = new QueryDB();
    ResultSet resultSet = null;
    String res = "";
    String varOne = "";
    String varTwo = "";
    String varThree = "";
    QueryStatCreator statGod = new QueryStatCreator();
    QueryEntCreator entGod = new QueryEntCreator();
    
    public QueryMachine()
    {
    	//empty constructor
    }
    
    /**
	 * Gets all parameters necessary to create a Cell object and returns parameters as array.
	 * @param int x
	 * @param int y
	 * @param int z
	 * @param above values are coordinates for Cell location.
	 * @return String [7]
	 * Will require parsing location 0, 1, 2 to int once called.
	 * Does NOT create the object.
	 * Connects to a remote database, queries data, parses into array, returns array.
	 */
    public String[] getCell(int x, int y, int z) throws SQLException, IOException
    {
    		/**
    		 * convert input to string for queries
    		 */
    		String [] obj = new String[6];
    		varOne = "" + x;
    		varTwo = "" + y;
    		varThree = "" + z;
    		PreparedStatement pstmt = dbReader.Connect(credIO.getUser(), credIO.getPass()).prepareStatement(generator.getCell());
	    pstmt.setString( 1, varOne);
	    pstmt.setString(2, varTwo);
	    pstmt.setString(3, varThree);
	    resultSet = pstmt.executeQuery();
	    while(resultSet.next())
	    {
	    	 // this part here will allow items that query lists to populate
	    	 	String xString = resultSet.getString("XCoord");
	    		String yString = resultSet.getString("YCoord");
	    		String zString = resultSet.getString("ZCoord");
	    		String description = resultSet.getString("Description");
	    		String encounters = resultSet.getString("Encounters");
	    		String items = resultSet.getString("Items");
	    		String keyItems = resultSet.getString("KeyItems");
	    		String reqItem = resultSet.getString("ReqItem");
	    		obj = new String[]{xString, yString, zString, description, encounters, items, keyItems, reqItem};
	    }//end while
	     return obj;
    } 
	/**
	 * Gets all parameters necessary to create a Useable object and returns parameters as array.
	 * @param String pkid
	 * @param above value is primary key id that is retrieved from cell array, location 5
	 * @return String [3]
	 * Will require parsing location 0 to int once called.
	 * Does NOT create the object.
	 * Connects to a remote database, queries data, parses into array, returns array.
	 */
	public String[] getUseable(String pkid) throws SQLException, IOException
	{
		String [] obj = new String[2];
		varOne = pkid;
		PreparedStatement pstmt = dbReader.Connect(credIO.getUser(), credIO.getPass()).prepareStatement(generator.getUseable());
		pstmt.setString( 1, varOne);
		resultSet = pstmt.executeQuery();
		while(resultSet.next())
		{
			// this part here will allow items that query lists to populate
			String potency = resultSet.getString("Potency");
    	 		String itemName = resultSet.getString("ItemName");
    	 		String description = resultSet.getString("ItemDescription");
    	 		obj = new String[]{potency, itemName, description};
		}//end while
     return obj;
	}
	
	/**
	 * Gets all parameters necessary to create a Equipable object and returns parameters as array.
	 * @param String pkid
	 * @param above value is primary key id that is retrieved from cell array, location 5
	 * @return String [3]
	 * Will require parsing location 0 to int once called.
	 * Does NOT create the object.
	 * Connects to a remote database, queries data, parses into array, returns array.
	 */
	public String[] getEquipable(String pkid) throws SQLException, IOException
	{
		String [] obj = new String[2];
		varOne = pkid;
		PreparedStatement pstmt = dbReader.Connect(credIO.getUser(), credIO.getPass()).prepareStatement(generator.getEquipable());
		pstmt.setString( 1, varOne);
		resultSet = pstmt.executeQuery();
		while(resultSet.next())
		{
			// this part here will allow items that query lists to populate
			String potency = resultSet.getString("Potency");
    	 		String itemName = resultSet.getString("ItemName");
    	 		String description = resultSet.getString("ItemDescription");
    	 		obj = new String[]{potency, itemName, description};
		}//end while
     return obj;
	}
	
	/**
	 * Gets all parameters necessary to create a KeyItem object and returns parameters as array.
	 * @param String pkid
	 * @param above value is primary key id that is retrieved from cell array, location 6.
	 * @return String [2]
	 * Does NOT create the object.
	 * Connects to a remote database, queries data, parses into array, returns array.
	 */
	public String[] getKeyItem(String pkid) throws SQLException, IOException
	{
		String [] obj = new String[2];
		varOne = pkid;
		PreparedStatement pstmt = dbReader.Connect(credIO.getUser(), credIO.getPass()).prepareStatement(generator.getKeyItems());
		pstmt.setString( 1, varOne);
		resultSet = pstmt.executeQuery();
		while(resultSet.next())
		{
			// this part here will allow items that query lists to populate
    	 		String itemName = resultSet.getString("ItemName");
    	 		String description = resultSet.getString("ItemDescription");
    	 		obj = new String[]{itemName, description};
		}//end while
     return obj;
	}
	
	/**
	 * Gets all parameters necessary to create Entity objects, creates the objects, then passes them as an array
	 * @param String pkid
	 * @param above value is primary key id that is retrieved from cell array, location 4.
	 * @return ArrayList<Entity>
	 * DOES create Entity objects to populate arrayList.
	 * Connects to a remote database, queries data, creates object, parses into array, returns array.
	 */
	public ArrayList<Entity> getEncounter(String pkid) throws SQLException, IOException
	{
		//first we need to get the encounter's specific entities
		//this one is an arraylist instead of array because it can be anywhere from 1-5 entities		
		varOne = pkid;
		ArrayList<Entity> encounter = new ArrayList<Entity>();
		PreparedStatement pstmt = dbReader.Connect(credIO.getUser(), credIO.getPass()).prepareStatement(generator.getEncounterTeam());
		pstmt.setString( 1, varOne);
		resultSet = pstmt.executeQuery();
		while(resultSet.next())
		{
			// this part here will allow items that query lists to populate
    	 		String npc1 = resultSet.getString("NPC1");
    	 		String npc2 = resultSet.getString("NPC2");
    	 		String npc3 = resultSet.getString("NPC3");
    	 		String npc4 = resultSet.getString("NPC4");
    	 		String npc5 = resultSet.getString("NPC5");

    	 			String [] tempEnt1 = entGod.makeEntities(npc1);
        	 		Entity ent1 = new Entity (statGod.makeStatBlock(tempEnt1[0]), tempEnt1[1], tempEnt1[2], tempEnt1[3]);
        	 		encounter.add(ent1);

    	 			String [] tempEnt2 = entGod.makeEntities(npc2);
        	 		Entity ent2 = new Entity (statGod.makeStatBlock(tempEnt2[0]), tempEnt2[1], tempEnt2[2], tempEnt2[3]);
        	 		encounter.add(ent2);

    	 			String [] tempEnt3 = entGod.makeEntities(npc3);
        	 		Entity ent3 = new Entity (statGod.makeStatBlock(tempEnt3[0]), tempEnt3[1], tempEnt3[2], tempEnt3[3]);
        	 		encounter.add(ent3);
        	 		

    	 			String [] tempEnt4 = entGod.makeEntities(npc4);
        	 		Entity ent4 = new Entity (statGod.makeStatBlock(tempEnt4[0]), tempEnt4[1], tempEnt4[2], tempEnt4[3]);
        	 		encounter.add(ent4);

    	 			String [] tempEnt5 = entGod.makeEntities(npc5);
        	 		Entity ent5 = new Entity (statGod.makeStatBlock(tempEnt5[0]), tempEnt5[1], tempEnt5[2], tempEnt5[3]);
        	 		encounter.add(ent5);

		}
		return encounter;
	}
	
	/**
	 * Gets all parameters necessary to create Entity objects, creates the objects, then passes them as an array
	 * @param String pkid
	 * @param above value is primary key id that is retrieved from cell array, location 4.
	 * @return ArrayList<Entity>
	 * DOES create StatBlock objects in order to construct Entity objects.
	 * DOES create Entity objects to populate arrayList.
	 * Connects to a remote database, queries data, creates object, parses into array, returns array.
	 */
	public ArrayList<Entity> getEncounter2(String pkid) throws SQLException, IOException
	{
		ArrayList<Entity> encounter = new ArrayList<Entity>();
		return encounter;
	}

}//end class