import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
	private String statusEffect; //SLP, PSN, PAR, BRN, FRZ
	private int turnsStatus;
	private ArrayList<String> buffs;
	private ArrayList<Move> moves;
	private ArrayList<Move> possibleMoveSet;
	private ArrayList<String> type;
	private int patklvl, spatklvl, pdeflvl, spdeflvl, spdlvl;
	private String item;
	private String ability;
	
	private ImageIcon imgIcon;
	
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
		turnsStatus = 0;
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
	  //statusEffects = new ArrayList<String>();
	  moves = new ArrayList<Move>();  //load in moves when pokemon class is updated
	  possibleMoveSet = new ArrayList<Move>();
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
	
	public String getName()
	{
		return name;
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
		int spd = (int)(this.speed * ((0.5 * spdlvl) + 1));
		if (this.getStatusEffect().equals("PAR"))
		{
			return (int)(spd * .25);
		}
		return spd;
	}
	
	public String getStatusEffect()
	{
	  return statusEffect;
	}
	
	public int getTurnsStatus()
	{
		return turnsStatus;
	}
	
	public ArrayList<String> getBuffs()
	{
	  return this.buffs;
	}
	
	public ArrayList<Move> getMoveSet ()
	{
	  return this.moves;
	}
	
	public ArrayList<Move> getPossibleMoveSet()
	{
	  return this.possibleMoveSet;
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
	
	public void addToPossibleMoveSet(Move m)
	{
		//System.out.println(m + " :::::  " + this.possibleMoveSet);
		this.possibleMoveSet.add(m);
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
	
	private static void randomMoveSelectionFromPool(Pokemon p)
	{
		int rand;
		
		for(int x =0; x<4; x++)
		{
			rand = (int)(Math.random() * p.getPossibleMoveSet().size());
			p.moves.add(new Move(p.getPossibleMoveSet().get(rand))); // makes a copy of the move, puts it in moves arraylist
		}
		
		System.out.println(p.moves);
		
	}
	
	
	
	
	public static void loadMoveSet(Pokemon p)
	{
	  Connection con;
	  ResultSet r1 = null;
	  ResultSet r2 = null;
	  ResultSet r3 = null;
	  Statement stmt;
	  
	  try
	  {
	    Driver d = (Driver)Class.forName("org.sqlite.JDBC").newInstance();
	    DriverManager.registerDriver(d);
	    String url = "jdbc:sqlite:resources/pokemon-database.sqlite";
	    con = DriverManager.getConnection(url);
	    
	    String query_moveList = "select * from pokemon_moves where pokemon_id =" + p.getID();
	    String query_MoveInfo = "select * from moves where id =";
	    String query_description = "select * from move_effect_prose where id=";
	    String moveID = "";
	    stmt = con.createStatement();
	    
	    
      //load moves corresponding to pokemon
	    r1 = stmt.executeQuery(query_moveList);
	    while(r1.next())
	    {
	    	moveID = r1.getString("move_id");
	     	p.addToPossibleMoveSet(Move.getMoveByID(Integer.parseInt(moveID)));
	    }
	     //do this for each element of the moveset
	    for(Move m : p.getPossibleMoveSet())
	    {
	    	query_MoveInfo = "select * from moves where id =" + m.getMoveID();
	    	r2 = stmt.executeQuery(query_MoveInfo);
	    	while(r2.next())
	 	    {
	 	      String power = r2.getString("power");
	 	      String pp = r2.getString("pp");
	 	      String accuracy = r2.getString("accuracy");
	 	      String priority = r2.getString("priority");
	 	      String damage_class_id = r2.getString("damage_class_id");
	 	      String effect_id = r2.getString("effect_id");
	 	      String effect_chance = r2.getString("effect_chance");
	 	      String type_id = r2.getString("type_id");
	 	      String name = r2.getString("identifier");
	 	      String ID = r2.getString("id");
	 	      
	 	      
	 	      m.setEffectID(Integer.parseInt(effect_id));
	 	      m.setPP(Integer.parseInt(pp));
	 	      m.setType(Integer.parseInt(type_id));
	 	      m.setName(name);
	 	      m.setPriority(Integer.parseInt(priority));
	 	    }
	    
	    }
	    
	    for(Move m : p.getPossibleMoveSet())
	    {
	    	//now load description
	    	query_description = "select * from move_effect_prose where move_effect_id=" + m.getEffectID();
	    	r3 = stmt.executeQuery(query_description);
	    	String desc = r3.getString("short_effect");
	    	m.setDescription(desc);
	    }
	    
	    //System.out.println(p + " ::::::  " + p.getPossibleMoveSet());
	    
	   
      /*
      p.setType(Integer.parseInt(type_id));
      p.setName(name);
      p.setPower(Integer.parseInt(power));
      p.setPP(Integer.parseInt(pp));
      p.setPriority(Integer.parseInt(priority));
      if(accuracy != null)
        p.setAccuracy(Integer.parseInt(accuracy));
	    */
	    
    } catch(Exception e)
    {
      e.printStackTrace();
    }
	  
	  randomMoveSelectionFromPool(p);
	}
	
	
}
