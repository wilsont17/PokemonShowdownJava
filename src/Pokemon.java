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
	private String statusEffect;
	private ArrayList<String> buffs;
	private ArrayList<Move> moves;
	private ArrayList<String> type;
	private int patklvl, spatklvl, pdeflvl, spdeflvl, spdlvl;
	private String item;
	private String ability;
	
	private ImageIcon imgIcon;
	
	
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
		statusEffect = "";
		buffs = new ArrayList<String>();
		moves = new ArrayList<Move>();  //load in moves when pokemon class is updated
		type = new ArrayList<String>();
		patklvl = 0;
		spatklvl = 0;
		pdeflvl = 0;
		spdeflvl = 0;
		spdlvl = 0;
	}
	
	//For Use with experimental loader
	Pokemon(String name, int ID)
	{
	  this.name = name;
	  this.ID = ID;
	  statusEffects = new ArrayList<String>();
	  moves = new ArrayList<Move>();  //load in moves when pokemon class is updated
	  type = new ArrayList<String>();
	  loadImageIcon();
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
		return this.level;
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
		return (int)(this.attack * ((0.5 * patklvl) + 1));
	}
	
	public int getDefense()
	{
		return (int)(this.defense * ((0.5 * pdeflvl) + 1));
	}
	
	public int getSpAttack()
	{
		return (int)(this.spAttack * ((0.5 * spatklvl) + 1));
	}

	public int getSpDefense()
	{
		return (int)(this.spDefense * ((0.5 * spdeflvl) + 1));
	}
	
	public int getSpeed()
	{
		return (int)(this.speed * ((0.5 * spdlvl) + 1));
	}
	
	public String getStatusEffect()
	{
	  return statusEffect;
	}
	
	public ArrayList<String> getBuffs()
	{
	  return this.buffs;
	}
	
	public ArrayList<Move> getMoveSet ()
	{
	  return this.moves;
	}
	
	public ImageIcon getImg ()
	{
	  //TODO return the img of the pokemon
	  return this.imgIcon;
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
	  temp+=type+" ";
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
	
	public void loadImageIcon()
	{
		ImageIcon temp = null;
		try
		{
			temp = new ImageIcon("resources/icon/"+this.ID+".png");
		}catch(Exception e)
		{
			temp = new ImageIcon("resources/icon/0.png");
			System.out.println("could not find image of pokemon !!!! Loading default icon");
		}
		
		this.imgIcon = temp;
	}
	
	
}
