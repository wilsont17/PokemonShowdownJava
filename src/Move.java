import java.util.ArrayList;


public class Move
{
	private static ArrayList<Move> movePool = new ArrayList<Move>(); // the list of all moves loaded
	
	
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
	private int effect_id;
	private int effect_chance; // this remains null unless specified otherwise
	
	
	
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
		this.effect_id  = m.effect_id;
		
	}
	
	public void setEffectChance(int chance)
	{
		this.effect_chance = chance;
	}
	
	public int getEffectChance()
	{
		return this.getEffectChance();
	}
	
	public void setEffectID(int id)
	{
		this.effect_id = id;
	}
	
	public int getEffectID()
	{
		return this.effect_id;
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
	
	
	public static Move getMoveByID(int ID)
	{
	  for(Move m : movePool)
	  {
	    if(m.getMoveID() == ID)
	    {
	    	//System.out.println("adding " + new Move(m));
	    	return new Move(m); // returns a copy of the move found
	    }
	  }
	  
	  return null; // move not found
	}
	
	public void setType(String s)
	{
	  this.type = s;
	}
	
	
	public void setType(int type)
	{
	     switch(type) 
	        {
	          case 1:
	        	  this.type = "normal";
	        	  break;
	          case 2:
	        	  this.type = "fighting";
		        	 
	        	  break;
	          case 3:
	        	  this.type = "flying";
		           break;
	          case 4:
	        	  this.type = "poison";
		          break;
	          case 5:
	        	  this.type = "ground";
		     	  break;
	          case 6:
	        	  this.type = "rock";
		          break;
	          case 7:
	        	  this.type = "bug";
		           break;
	          case 8:
	        	  this.type = "ghost";
	        	  break;
	          case 9:
	        	  this.type = "steel";
	        	  break;
	          case 10:
	        	  this.type = "fire";
	        	  break;
	          case 11:
	        	  this.type = "water";
	        	  break;
	          case 12:
	        	  this.type = "grass";
	        	  break;
	          case 13:
	        	  this.type = "electric";
	        	  break;
	          case 14:
	        	  this.type = "psychic";
	        	  break;
	          case 15:
	        	  this.type = "ice";
	        	  break;
	          case 16:
	        	  this.type = "dragon";
	        	  break;
	          case 17:
	        	  this.type = "dark";
	        	  break;
	          case 18:
	        	  this.type = "fairy";
	        	   break;
	          case 10001:
	        	  this.type = "unknown";
	        	  break;
	          case 10002:
	        	  this.type = "shadow";
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
