import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javax.swing.*;

public class PokemonShowdownMainGui implements ActionListener
{
	String p1Name, p2Name;
	boolean p1Switch, p2Switch, battleInProgress, whoseTurn;  //whoseTurn = true if p1, false if p2
	Pokemon p1Active, p2Active;
	int p1ActiveIndex, p2ActiveIndex;
	JLabel currPlayerAndAllPokemon, opPlayerAndAllPokemon, currPlayerActiveImg, opPlayerActiveImg,
	  currPlayerPokemonStatusEffects, opPlayerPokemonStatusEffects, previousMovesLog, currentTurnEvents;
	JProgressBar currPlayerPokemonHP, opPlayerPokemonHP;
	JScrollBar movesLogScroll;
	ArrayList<Pokemon> p1Pokemon, p2Pokemon, pokemonPool;
	ArrayList<JButton> currPokemonMoves, currSwitchablePokemon;
	JCheckBox megaevo;

	JFrame jfrm;
	GridBagLayout gb;
	GridBagConstraints gbc;
	
	
	PokemonShowdownMainGui() throws FileNotFoundException
	{
		jfrm = new JFrame("Pokemon Showdown");
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		jfrm.setLayout(gb);
		jfrm.setSize(1280,720);
		//create the arraylists
		pokemonPool = new ArrayList<Pokemon>();
		p1Pokemon = new ArrayList<Pokemon>();
		p2Pokemon = new ArrayList<Pokemon>();
		//loadPokemonDB();

		whoseTurn = true;
	
		experimentalPokemonDBLoader(); // load all pokemon
		System.out.println(pokemonPool);
		jfrm.setVisible(true);
		
		previousMovesLog = new JLabel("<html>HELLLOOO");
		gbc.gridx = 7; gbc.gridy = 0;
		gbc.gridheight = 7; gbc.gridwidth = 2;
		jfrm.add(previousMovesLog, gbc);
		
		currPokemonMoves = new ArrayList<JButton>();
		currSwitchablePokemon = new ArrayList<JButton>();
		for (int x = 0; x < 4; x ++)
		{
		  currPokemonMoves.add(new JButton("Hello"));
		  gbc.gridx = 2 + x; gbc.gridy = 5;
		  jfrm.add(currPokemonMoves.get(x), gbc);
		}
		for (int x = 0; x < 6; x ++)
		{
		  currSwitchablePokemon.add(new JButton("Hello"));
		  gbc.gridx = 1 + x; gbc.gridy = 6;
		  jfrm.add(currSwitchablePokemon.get(x), gbc);
		}
		
		
		
		
		
		currPlayerPokemonStatusEffects = new JLabel("");
		opPlayerPokemonStatusEffects = new JLabel("");
		
		
		
		
		
		
		currPlayerPokemonHP = new JProgressBar();
		opPlayerPokemonHP = new JProgressBar();
		
		
		
	  
		megaevo = new JCheckBox("<html>Mega<br>Evolution");
		
		
		
		
		
		
		new SetupGui(this);
		battleInProgress = true;
		/*
		while (battleInProgress)
		{
		  
		}
		*/
	}
	
	//Mechanics
	
	public void createOneVsOne()
	{
		p1Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
		p2Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
		p1Active = p1Pokemon.get(0);
		p2Active = p2Pokemon.get(0);
		p1ActiveIndex = 0;
		p2ActiveIndex = 0;
	}
	
	public void createSixVsSix()
	{
		for (int x = 0; x < 6; x ++)
		{
			p1Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
			p2Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
			p1Active = p1Pokemon.get(0);
			p2Active = p2Pokemon.get(0);
			p1ActiveIndex = 0;
	    p2ActiveIndex = 0;
		}
	}
	
	public void addNames(String p1n, String p2n)
	{
		p1Name = p1n; 
		p2Name = p2n;
	}
	
	public void p1Turn()
	{
	  //currPlayerAndAllPokemon.setText(p1Name, );  Set to p1name and imgs for all pokemon
	  //opPlayerAndAllPokemon.setText(p2Name, );  Set to p2name and imgs for all pokemon
	  currPlayerActiveImg.setIcon(p1Active.getImg());  //get img of p1activepokemon
	  opPlayerActiveImg.setIcon(p2Active.getImg());  //get img of p2activepokemon
	  
	  currPlayerPokemonHP.setValue((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()));
	  String se = "";
	  for (int x = 0; x < p1Active.getStatusEffects().size(); x ++)
	  {
	    se += p1Active.getStatusEffects().get(x) + " ";
	  }
	  currPlayerPokemonStatusEffects.setText(se);
	  
	  opPlayerPokemonHP.setValue((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()));
	  se = "";
	  for (int x = 0; x < p2Active.getStatusEffects().size(); x ++)
	  {
      se += p2Active.getStatusEffects().get(x) + " ";
    }
	  opPlayerPokemonStatusEffects.setText(se);
	  
	  
	  
	  for (int x = 0; x < 4; x ++)
	  {
	    currPokemonMoves.get(x).setEnabled(true);
	    currPokemonMoves.get(x).setText(p1Active.getMoveSet().get(x).toString());
	    if (!p1Active.getMoveSet().get(x).useable())
	    {
	      currPokemonMoves.get(x).setEnabled(false);
	    }
	  }
	  for (int x = 0; x < 6; x ++)
	  {
	    currSwitchablePokemon.get(x).setEnabled(true);
	    currSwitchablePokemon.get(x).setText(p1Pokemon.get(x).toString());
	    if (x == p1ActiveIndex || p1Pokemon.get(x).getHP() < 1)
	    {
	      currSwitchablePokemon.get(x).setEnabled(false);
	    }
	  }
	}
	
	public void p2Turn()
	{
	  //currPlayerAndAllPokemon.setText(p2Name, );  Set to p2name and imgs for all pokemon
	  //opPlayerAndAllPokemon.setText(p1Name, );  Set to p1name and imgs for all pokemon
	  currPlayerActiveImg.setIcon(p2Active.getImg());  //get img of p2activepokemon
      opPlayerActiveImg.setIcon(p1Active.getImg());  //get img of p1activepokemon
	  
	  currPlayerPokemonHP.setValue((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()));
    String se = "";
    for (int x = 0; x < p2Active.getStatusEffects().size(); x ++)
    {
      se += p2Active.getStatusEffects().get(x) + " ";
    }
    currPlayerPokemonStatusEffects.setText(se);
    
    opPlayerPokemonHP.setValue((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()));
    se = "";
    for (int x = 0; x < p1Active.getStatusEffects().size(); x ++)
    {
      se += p1Active.getStatusEffects().get(x) + " ";
    }
    opPlayerPokemonStatusEffects.setText(se);
    
    
    
    for (int x = 0; x < 4; x ++)
    {
      currPokemonMoves.get(x).setEnabled(true);
      currPokemonMoves.get(x).setText(p2Active.getMoveSet().get(x).toString());
      if (!p2Active.getMoveSet().get(x).useable())
      {
        currPokemonMoves.get(x).setEnabled(false);
      }
    }
    for (int x = 0; x < 6; x ++)
    {
      currSwitchablePokemon.get(x).setEnabled(true);
      currSwitchablePokemon.get(x).setText(p2Pokemon.get(x).toString());
      if (x == p2ActiveIndex || p2Pokemon.get(x).getHP() < 1)
      {
        currSwitchablePokemon.get(x).setEnabled(false);
      }
    }
  }

	
	public void turnMove()
	{
		if (p1Switch)
		{
			
		}
		if (p2Switch)
		{
			
		}
		if (!p1Switch && !p2Switch)
		{
			if (p1Active.getSpeed() > p2Active.getSpeed())
			{
				
			}
			else if (p1Active.getSpeed() == p2Active.getSpeed())
			{
				
			}
			else
			{
				
			}
		}
	}
	
	
	
	
	public int dmgDone(Pokemon attacker, Pokemon defender, Move attack)
	{
		if (attack.getType().equals("p"))
		{
			return (int)((((2 *  attacker.getLevel() / 5 + 2 ) * attacker.getAttack() * attack.getPower() 
					/ defender.getDefense() / 50) + 2) * stabResult(attacker, attack) * 
					weakResist(defender, attack) * ((int)(Math.random() * 16) + 85) / 100 );
		}
		else
		{
			return (int)((((2 *  attacker.getLevel() / 5 + 2 ) * attacker.getSpAttack() * attack.getPower() 
					/ defender.getSpDefense() / 50) + 2) * stabResult(attacker, attack) *
					weakResist(defender, attack) * ((int)(Math.random() * 16) + 85) / 100 );
		}
	//http://www.serebii.net/games/damage.shtml
	}
	
	public double stabResult(Pokemon user, Move ability)
	{
		for (int x = 0; x < user.getType().size(); x ++)
		{
			if (user.getType().get(x).equals(ability.getType()))
			{
				return 1.5;
			}
		}
		return 1;
		
	}
	
	public double weakResist(Pokemon defender, Move ability)
	{
		//TODO add weakness/resistance calcs later
	  return 0;
	}
	
	
	
	public void actionPerformed(ActionEvent ae)
  {
    for (int x = 0; x < 4; x ++)
    {
      if (ae.getSource().equals(currPokemonMoves.get(x)))
      {
        
      }
    }
    if (!ae.getSource().equals(megaevo))
    {
      //Turn check here
    }
    
  }

	
	
	
	
	
	
	
	
	
//do not use this method to load it is depreicated
  public void loadPokemonDB() throws FileNotFoundException
  {
    File inFile = new File("resources/pokemonstats.txt");
    Scanner inScan = new Scanner(inFile);
    
    /* Order of input
    inScan.nextLine();//HP
    inScan.nextLine();//attack
    inScan.nextLine();//defense
    inScan.nextLine();//spattack
    inScan.nextLine();//spdefense
    inScan.nextLine();//speed
     */
    while(inScan.hasNextLine())
    {
        String tempName = inScan.nextLine();
        int tempHP = Integer.parseInt(inScan.nextLine().trim());
        int tempAttack = Integer.parseInt(inScan.nextLine().trim());
        int tempDefense = Integer.parseInt(inScan.nextLine().trim());
        int tempSpAttack = Integer.parseInt(inScan.nextLine().trim());
        int tempSpDefense = Integer.parseInt(inScan.nextLine().trim());
        int tempSpeed = Integer.parseInt(inScan.nextLine().trim());
        pokemonPool.add(new Pokemon(tempName , tempHP, tempAttack, tempDefense, tempSpAttack, tempSpDefense, tempSpeed));
        //System.out.println(new Pokemon(tempName , tempHP, tempAttack, tempDefense, tempSpAttack, tempSpDefense, tempSpeed));
     }
  }
  
  public void experimentalPokemonDBLoader()
  {
    Statement stmt;
    ResultSet resName;
    ResultSet resStats;
    ResultSet restTypes;
    Connection con;
    // this segment of code loads the driver that handles the databse
    try
    {
      Driver d = (Driver)Class.forName("org.sqlite.JDBC").newInstance();
      DriverManager.registerDriver(d);
      
      String url = "jdbc:sqlite:resources/pokemon-database.sqlite";
      con = DriverManager.getConnection(url);
      
      String query_pokemon = "select * from pokemon";
      
      String query_pokemonStats = "select * from pokemon_stats";
      
      String query_pokemonTypes = "select * from pokemon_types";
      
      
      stmt = con.createStatement();
      
      resName = stmt.executeQuery(query_pokemon);
      

      while(resName.next()) // this loop adds all of the pokemon
      {
        //System.out.println(resName.getString("id")); // gets pokemon ID
        int temporaryID = Integer.parseInt(resName.getString("id"));
  
        
        //System.out.println(Pokemon.getPokemonByID(temporaryID,pokemonPool) + "       !!!!!experimental!!!!");
        //System.out.println(resName.getString("identifier")); //gets pokemon name
        
        pokemonPool.add(new Pokemon(resName.getString("identifier"), temporaryID));
      
      
      }
      
      resStats = stmt.executeQuery(query_pokemonStats);
      
      while(resStats.next())
      {
       //System.out.println(resStats.getString("pokemon_id"));
       //System.out.println(resStats.getString("stat_id")); //1 = hp, 2 = attack, 3 = defense, 4 = sp attack, 5 = sp defense, 6=speed, 
       //System.out.println(resStats.getString("base_stat")); // 7 = accuracy, 8 = evasion
        
        String tempstatID = resStats.getString("stat_id");
        String baseStat = resStats.getString("base_stat");
        int switchCase = Integer.parseInt(tempstatID.trim());
        int statValue = Integer.parseInt(baseStat);
        //System.out.println(switchCase + " " + baseStat);
        
        switch(switchCase)
        {
            case 1:
              Pokemon.setHP(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , statValue);
              break;
            case 2:
             Pokemon.setAttack(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , statValue);
             break;
            case 3:
             Pokemon.setDefense(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , statValue);
             break;
            case 4:
             Pokemon.setSpAttack(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , statValue);
             break;
            case 5:
             Pokemon.setSpDefense(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , statValue);
             break;
            case 6:
             Pokemon.setSpeed(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , statValue);
             break;
            case 7:
              //no need now
            break;
            case 8:
              //no need now
            break;
          }
        
      }
      
      restTypes = stmt.executeQuery(query_pokemonTypes);
       
      while(restTypes.next())
      {
        
        String pokeID = restTypes.getString("pokemon_id");
        String typeID = restTypes.getString("type_id");
        //System.out.println(pokeID + " " + typeID);
        //System.out.println(Integer.parseInt(typeID));
    	//System.out.println("adding to " + Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool));
        
        switch(Integer.parseInt(typeID.trim())) 
        {
          case 1:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("normal");
        	  break;
          case 2:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("fighting");
        	  break;
          case 3:
              Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("flying");
              break;
          case 4:
              Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("poison");
              break;
          case 5:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("ground");
        	  break;
          case 6:
              Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("rock");
              break;
          case 7:
              Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("bug");
              break;
          case 8:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("ghost");
        	  break;
          case 9:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("steel");
        	  break;
          case 10:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("fire");
        	  break;
          case 11:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("water");
        	  break;
          case 12:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("grass");
        	  break;
          case 13:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("electric");
        	  break;
          case 14:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("psychic");
        	  break;
          case 15:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("ice");
        	  break;
          case 16:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("dragon");
        	  break;
          case 17:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("dark");
        	  break;
          case 18:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("fairy");
        	  break;
          case 10001:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("unknown");
        	  break;
          case 10002:
        	  Pokemon.getPokemonByID(Integer.parseInt(pokeID), pokemonPool).addType("shadow");
        	  break;
        }
        
      }
     
      
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    
        
  }
    

}


	 /*  pokemon types by id
	 * 1 :::::  normal
	2 :::::  fighting
	3 :::::  flying
	4 :::::  poison
	5 :::::  ground
	6 :::::  rock
	7 :::::  bug
	8 :::::  ghost
	9 :::::  steel
	10 :::::  fire
	11 :::::  water
	12 :::::  grass
	13 :::::  electric
	14 :::::  psychic
	15 :::::  ice
	16 :::::  dragon
	17 :::::  dark
	18 :::::  fairy
	10001 :::::  unknown
	10002 :::::  shadow
	 */