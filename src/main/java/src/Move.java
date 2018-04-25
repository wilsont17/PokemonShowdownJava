/* Sam Ginzburg and Lang Gao
 * 
 * This class contains all of the information pertaining to moves in the game. 
 * Each move contains information such as its name, description, and other important data.
 * All of this information is parsed from the sqlite database.
*/



import java.util.ArrayList;


public class Move
{
	private static ArrayList<Move> movePool = new ArrayList<>(); // the list of all moves loaded
	
	
	private String name;
	private String description;
	private int power;
	private int hitChance;
	private String type; //p for physical, s for special, e for statuseffect
	private String dmgType;
	private int maxPP;
	private int currPP;
	private int moveID;
	private int pokemonID; // this identifies the pokemon that can use this move
	private int priority;
	private int effectID;
	private int effectChance; // this remains null unless specified otherwise

	Move()
    {
		this.name = "";
	}
	
	Move(String name, String description, int power, int hitChance)
	{
		
		this.name = name;
		this.description = description;
		this.power = power;
		this.hitChance = hitChance;
	}
	
	Move(int pokemonID, int moveID)
	{
		this.pokemonID = pokemonID;
		this.moveID = moveID;
	}
	
	// Copy constructor
	Move(Move m)
	{
		this.name = m.name;
		this.description = m.description;
		this.power = m.power;
		this.hitChance = m.hitChance;
		this.type = m.type;
		this.dmgType = m.dmgType;
		this.maxPP = m.maxPP;
		this.currPP = m.currPP;
		this.moveID = m.moveID;
		this.pokemonID = m.pokemonID;
		this.priority = m.priority;
		this.effectID  = m.effectID;
		
	}
	
	public void setEffectChance(int chance)
	{
		this.effectChance = chance;
	}
	
	public int getEffectChance() { return this.effectChance; }
	
	public void setEffectID(int id)
	{
		this.effectID = id;
	}
	
	public int getEffectID()
	{
		return this.effectID;
	}
	
	
	public int getMoveID()
	{
		return this.moveID;
	}
  
	public String toString()
	{
		return name + " " + moveID + " " + type + " " + description;
	}
  
	public String getName()
	{
		return name;
	}
	
	public int getPower()
	{
		return power;
	}
  
	public String getType ()
	{
		return type;
	}
	public String getDmgType()
	{
		return dmgType;
	}
	
	public int getPriority()
	{
		return priority;
	}
	
	public int getHitChance ()
	{
	  return hitChance;
	}
  
	public boolean useable()
  	{
		return (currPP > 0);
  	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setPP(int pp)
	{
		this.currPP = pp;
		this.maxPP = pp;
	}
	
	public void setPriority(int p)
	{
		this.priority = p;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setAccuracy(int acc)
	{
		this.hitChance = acc;
	}

	public void setPower(int power)
	{
		this.power = power;
	}
	
	public static Move getMoveByID(int id)
	{
	  for(Move m : movePool)
	  {
	    if(m.getMoveID() == id)
	    {
	    	return new Move(m); // returns a copy of the move found
	    }
	  }
	  
	  return null; // move not found
	}
	
	public void setType(String s)
	{
	  this.type = s;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Move && ((Move) o).getMoveID() == this.moveID)
			return false;
		return true;
	}
	
	public int compareTo(Object o)
	{
		if(o.equals(this))
		{
			return 0;
		}
		
		return -999;
	}
	
	public void setDmgType(int type)
	{
	     switch(type) 
	        {
	          case 1:
	        	  this.dmgType = "normal";
	        	  break;
	          case 2:
	        	  this.dmgType = "fighting";
		        	 
	        	  break;
	          case 3:
	        	  this.dmgType = "flying";
		           break;
	          case 4:
	        	  this.dmgType = "poison";
		          break;
	          case 5:
	        	  this.dmgType = "ground";
		     	  break;
	          case 6:
	        	  this.dmgType = "rock";
		          break;
	          case 7:
	        	  this.dmgType = "bug";
		           break;
	          case 8:
	        	  this.dmgType = "ghost";
	        	  break;
	          case 9:
	        	  this.dmgType = "steel";
	        	  break;
	          case 10:
	        	  this.dmgType = "fire";
	        	  break;
	          case 11:
	        	  this.dmgType = "water";
	        	  break;
	          case 12:
	        	  this.dmgType = "grass";
	        	  break;
	          case 13:
	        	  this.dmgType = "electric";
	        	  break;
	          case 14:
	        	  this.dmgType = "psychic";
	        	  break;
	          case 15:
	        	  this.dmgType = "ice";
	        	  break;
	          case 16:
	        	  this.dmgType = "dragon";
	        	  break;
	          case 17:
	        	  this.dmgType = "dark";
	        	  break;
	          case 18:
	        	  this.dmgType = "fairy";
	        	   break;
	          case 10001:
	        	  this.dmgType = "unknown";
	        	  break;
	          case 10002:
	        	  this.dmgType = "shadow";
	        	  break;
			  default:
			  	  break;

	        }
	}
	
	public static void printMoveList()
	{
		System.out.println(movePool);
	}
	
	public static void addToMovePool(Move m)
	{
		movePool.add(m);
	}
	
	public static ArrayList<Move> getMovePool()
	{
		return movePool;
	}
}
