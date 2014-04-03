import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Pokemon 
{
	private int level;
	private int maxHP;
	private int HP;
	private int attack;
	private int defense;
	private int spAttack;
	private int spDefense;
	private int speed;
	private int ID;
	private int accuracy; //do these later
	private int evasion; // 
	private String name;
	private ArrayList<String> statusEffects;
	private ArrayList<Move> moves;
	private ArrayList<String> type;
	
	ArrayList<Move> possibleMoves;
	
	Pokemon(String name, int hp, int attack, int defense, int spAttack, int spDefense, int speed)
	{
		this.name = name;
		this.maxHP = hp;
		this.HP = hp;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		statusEffects = new ArrayList<String>();
		moves = new ArrayList<Move>();  //load in moves when pokemon class is updated
		type = new ArrayList<String>();
	}
	
	//For Use with experimental loader
	Pokemon(String name, int ID)
	{
	  this.name = name;
	  this.ID = ID;
	}
	
	public void modifyHP(int amount)
	{
		this.HP+=amount;
	}
	
	public void modifyAttack(int amount)
	{
		this.attack+=amount;
	}
	
	public void modifyDefense(int amount)
	{
		this.defense+=amount;
	}
	
	public void modifySpAttack(int amount)
	{
		this.spAttack+=amount;
	}
	
	public void modifySpDefense(int amount)
	{
		this.spDefense+=amount;
	}
	
	public void modifySpeed(int amount)
	{
		this.speed+=amount;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getMaxHP()
	{
		return this.maxHP;
	}
	
	public int getHP()
	{
		return this.HP;
	}
	
	public int getAttack()
	{
		return this.attack;
	}
	
	public int getDefense()
	{
		return this.defense;
	}
	
	public int getSpAttack()
	{
		return this.spAttack;
	}

	public int getSpDefense()
	{
		return this.spDefense;
	}
	
	public int getSpeed()
	{
		return this.speed;
	}
	
	public ArrayList<String> getStatusEffects()
	{
	  return statusEffects;
	}
	
	public ArrayList<Move> getMoveSet ()
	{
	  return moves;
	}
	
	public ImageIcon getImg ()
	{
	  //TODO return the img of the pokemon
	  return null;
	}
	
	public int getID()
	{
	  return this.ID;
	}
	
	public void setMoves()
	{
	  //TODO get 4 random moves and add it to the pokemon
	}
	
	public static void setHP(Pokemon p, int hp) // sets current HP
	{
	  p.HP = hp;
	}
	
	 public static void setAttack(Pokemon p, int attack)
	 {
	   p.attack = attack;
	 }
	 
   public static void setDefense(Pokemon p, int defense) 
   {
     p.defense = defense;
   }
   
   public static void setSpAttack(Pokemon p, int spAttack) 
   {
     p.spAttack = spAttack;
   }
   
   public static void setSpDefense(Pokemon p, int spDefense) 
   {
     p.spDefense = spDefense;
   }
   
   public static void setSpeed(Pokemon p, int speed) 
   {
     p.speed = speed;
   }
	
	public String toString()
	{
	  String temp = "";
	  temp+=name+" ";
	  temp+=HP+" ";
	  temp+=attack+" ";
	  temp+=defense+" ";
	  return temp;
	}
	
	public ArrayList<String> getType()
	{
		return type;
	}
	
	public static Pokemon getPokemonByID(int ID, ArrayList<Pokemon> pokemonPool)
	{
	  for(Pokemon p : pokemonPool)
	  {
	    //System.out.println("searching " + p);
	    if(p.getID() == ID) // find the right pokemon, return it
	    {
	      //System.out.println("found pokemon with ID     " +p);
	      return p;
	    }
	  }
	  //else could not find the pokemon
	  System.out.println("could not find pokemon");
    return null;
	}
	
	public void addType(String type)
	{
	  this.type.add(type);
	}
	
	
}
