import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Pokemon 
{
	private int level = 99;
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
	

	//For Use with experimental loader
	Pokemon(String name, int ID)
	{
		turnsStatus = 0;
		patklvl = 0;
		spatklvl = 0;
		pdeflvl = 0;
		spdeflvl = 0;
		spdlvl = 0;
	  this.statusEffect = "";
	  this.name = name;
	  this.ID = ID;
	  //statusEffects = new ArrayList<String>();
	  moves = new ArrayList<Move>();  //load in moves when pokemon class is updated
	  possibleMoveSet = new ArrayList<Move>();
	  buffs = new ArrayList<String>();
	  type = new ArrayList<String>();
	  loadImageIcon();
	}
	
	//clone constructor
	
	Pokemon(Pokemon p)
	{
		this.name = p.getName();
		this.ID = p.getID();
		this.HP = p.getHP();
		this.maxHP = p.getHP();
		this.attack = p.getAttack();
	    this.defense = p.getDefense();
	    this.spAttack = p.getSpAttack();
	    this.spDefense = p.getSpDefense();
	    this.speed = p.getSpeed();
	    this.buffs = p.getBuffs();
	    this.moves = p.getMoves();
	    this.type = p.getType();
	    this.patklvl = 0;
	    this.spatklvl = 0;
	    this.pdeflvl = 0;
	    this.spdeflvl = 0;
	    this.spdlvl = 0;
	    this.statusEffect = p.getStatusEffect();
	    this.loadImageIcon();
	    this.possibleMoveSet = p.possibleMoveSet;
	    loadImageIcon();
	}
	
	
	public Pokemon clone()
	{
		return new Pokemon(this);
	}
	
	public int getpatklvl()
	{
	  return this.patklvl;
	}
	
	 public int getspatklvl()
	 {
	   return this.spatklvl;
	 }
	 
	 public int getpdeflvl()
	 {
	   return this.pdeflvl;
	 }
	 
   public int getspdeflvl()
   {
     return this.spdeflvl;
   }
   
   public int getspdlvl()
   {
     return this.spdlvl;
   } 
	  
	public ArrayList<Move> getMoves()
	{
	  return this.moves;
	}
	
	
	public void modifyHP(int amount)
	{
		this.HP+=amount;
	}
	
	public void modifyAttackLevel(int amount)
	{
		this.attack+=amount;
	}
	
	public void modifyDefenseLevel(int amount)
	{
		this.defense+=amount;
	}
	
	public void modifySpAttackLevel(int amount)
	{
		this.spAttack+=amount;
	}
	
	public void modifySpDefenseLevel(int amount)
	{
		this.spDefense+=amount;
	}
	
	public void modifySpeedLevel(int amount)
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
		System.out.println(this.getStatusEffect());
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
	
	public void setTurnsStatus(int t)
	{
	  turnsStatus = t;
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
	  return this.imgIcon;
	}
	
	public int getID()
	{
	  return this.ID;
	}
	
	
	public static void setHP(Pokemon p, int hp) // sets current HP
	{
	  p.HP = hp;
	}
	
	public static void setMaxHP(Pokemon p, int mhp) // sets current HP
	{
	  p.maxHP = mhp;
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
	
	public String getTypeAsString()
	{
		String t = "";
		for (int x = 0; x < type.size(); x ++)
		{
			t += type.get(x) + " ";
		}
		return t;
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
			temp = new ImageIcon("resources/icons/"+this.ID+".png");
		}catch(Exception e)
		{
			temp = new ImageIcon("resources/icons/0.png");
			System.out.println("could not find image of pokemon !!!! Loading default icon");
		}
		
		this.imgIcon = temp;
	}
	
	private static void randomMoveSelectionFromPool(Pokemon p)
	{
		int rand;
		boolean duplicate = false;
		
		while(p.moves.size() != 4)
		{
			rand = (int)(Math.random() * p.getPossibleMoveSet().size());
			Move temp = new Move(p.getPossibleMoveSet().get(rand));
			System.out.println(temp);
			
			for(Move m : p.moves)
			{
				if(m.getMoveID() == temp.getMoveID())
				{
					duplicate = true;
				}
			}

			if(!duplicate)
			{
				p.moves.add(temp);
			}
			else
			{
				duplicate = false;
			}
		}
		
		System.out.println(p + "     " + p.moves);
		
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
	 	      
	 	      m.setType(damage_class_id);
	 	      
	 	      switch(Integer.parseInt(damage_class_id))
	 	      {
		 	      case 1:
		 	    	  m.setType("e");
		 	    	  break;
		 	      case 2:
		 	    	  m.setType("p");
		 	    	  break;
		 	      case 3:
		 	    	  m.setType("s");
		 	    	  break;
	 	      }
	 	      
	 	      if(accuracy != null)
	 	    	  m.setAccuracy(Integer.parseInt(accuracy));
	 	      m.setPower(Integer.parseInt(power));
	 	      m.setEffectID(Integer.parseInt(effect_id));
	 	      m.setPP(Integer.parseInt(pp));
	 	      m.setDmgType(Integer.parseInt(type_id));
	 	      m.setName(name);
	 	      m.setPriority(Integer.parseInt(priority));
	 	     }
	    
	    }
	    
	    for(Move m : p.getPossibleMoveSet())
	    {
	    	//now load description
	    	query_description = "select * from move_effect_prose where move_effect_id=" + m.getEffectID();
	    	r3 = stmt.executeQuery(query_description);
	    	String desc = r3.getString("effect");
	    	String short_desc = r3.getString("short_effect");
	    	//System.out.println(m + " :::: " + short_desc);
	    	m.setDescription(short_desc);
	    }
	
    } catch(Exception e)
    {
      e.printStackTrace();
    }
	  
	  randomMoveSelectionFromPool(p);
	}
	
	
}
