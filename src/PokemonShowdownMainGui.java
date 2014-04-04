import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;import java.awt.*;
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
	boolean battleInProgress, whoseTurn;  //whoseTurn = true if p1, false if p2
	Pokemon p1Active, p2Active;
	int p1ActiveIndex, p2ActiveIndex, p1Move, p2Move, switchFaint, p1Switch, p2Switch; //switch -1 if no switch, 0-5 switch to that slot
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
		p1Switch = -1;
		p2Switch = -1;
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
		}
		
		for(Pokemon p : p1Pokemon)
      Pokemon.loadMoveSet(p);
    
    for(Pokemon p : p2Pokemon)
      Pokemon.loadMoveSet(p);
    
    p1Active = p1Pokemon.get(0);
    p2Active = p2Pokemon.get(0);
    p1ActiveIndex = 0;
    p2ActiveIndex = 0;
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
	  
	  //buff and status effect data
	  currPlayerPokemonStatusEffects.setText(p1Active.getStatusEffect());
	  for (int x = 0; x < p1Active.getBuffs().size(); x ++)
	  {
		  currPlayerPokemonStatusEffects.setText(currPlayerPokemonStatusEffects.getText() + " " + p1Active.getBuffs().get(x));
	  }
	  
	  opPlayerPokemonHP.setValue((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()));
	  opPlayerPokemonStatusEffects.setText(p2Active.getStatusEffect());
	  for (int x = 0; x < p2Active.getBuffs().size(); x ++)
	  {
		  opPlayerPokemonStatusEffects.setText(opPlayerPokemonStatusEffects.getText() + " " + p2Active.getBuffs().get(x));
	  }
	  
	  
	  
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
	  
	  //buff and status effect data
	  currPlayerPokemonStatusEffects.setText(p2Active.getStatusEffect());
	  for (int x = 0; x < p2Active.getBuffs().size(); x ++)
	  {
		  currPlayerPokemonStatusEffects.setText(currPlayerPokemonStatusEffects.getText() + " " + p2Active.getBuffs().get(x));
	  }
	  
	  opPlayerPokemonHP.setValue((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()));
	  opPlayerPokemonStatusEffects.setText(p1Active.getStatusEffect());
	  for (int x = 0; x < p1Active.getBuffs().size(); x ++)
	  {
		  opPlayerPokemonStatusEffects.setText(opPlayerPokemonStatusEffects.getText() + " " + p1Active.getBuffs().get(x));
	  }
    
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

	//TODO check status effects - paralyze burn frozen sleep before a pokemon moves
	//TODO check status effects and item effects after both pokemon move
	public void turnMove()
	{
		if (p1Switch != -1)  //P1 switches out
		{
			p1Active = p1Pokemon.get(p1Switch);
			if (p2Move != -1)
			{
			  performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
			}
		}
		
		if (p2Switch != -1)  //p2 Switches out
		{
			p2Active = p2Pokemon.get(p2Switch);
			if (p1Move != -1)
      {
        performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
      }
		}
		
		if (!(p1Move == -1) && !(p2Move == -1))  //Both players use moves
		{
		  //same priority
		  if (p1Active.getMoveSet().get(p1Move).getPriority() == p2Active.getMoveSet().get(p2Move).getPriority())
		  {
		    //p1 faster
		    if (p1Active.getSpeed() > p2Active.getSpeed())
	      {
	        performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
	        if (p2Active.getHP() > 0)
          {
            performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
          }
	      }
		    //both players same spd
	      else if (p1Active.getSpeed() == p2Active.getSpeed())
	      {
	        //p1 wins roll goes first
	        if (Math.random() > .5)
	        {
	          performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
	          if (p2Active.getHP() > 0)
	          {
	            performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
	          }
	        }
	        //p2 wins roll goes first
	        else
	        {
	          performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
	          {
	            if (p1Active.getHP() > 0)
	            {
	              performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
	            }
	          }
	        }
	      }
		    //p2 faster
	      else
	      {
	        performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
	        if (p1Active.getHP() > 0)
          {
            performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
          }
	      }
		  }
		  //priorities unequal
		  else
		  {
		    //p1 higher priority
		    if (p1Active.getMoveSet().get(p1Move).getPriority() > p2Active.getMoveSet().get(p2Move).getPriority())
		    {
		      performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
          if (p2Active.getHP() > 0)
          {
            performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
          }
		    }
		    else
		    {
		      performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move));
          if (p1Active.getHP() > 0)
          {
            performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move));
          }
		    }
		  }
		  
		  //after turn effects
		  
		}
	}
	
	public void freeSwap (int player)
	{
	  for (int x = 0; x < 4; x ++)
	  {
	    currPokemonMoves.get(x).setEnabled(false);
	  }
	  megaevo.setEnabled(false);
	  if (player == 1)
	  {
	    switchFaint = 1;
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
	  else
	  {
	    switchFaint = 2;
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
	}
	
	public int performAttack(Pokemon attacker, Pokemon defender, Move attack)
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
	  p1Move = -1;
	  p2Move = -1;
	  p1Switch = -1;
	  p2Switch = -1;
    for (int x = 0; x < 4; x ++)
    {
      if (ae.getSource().equals(currPokemonMoves.get(x)))
      {
        if (whoseTurn)
        {
          p1Move = x;
        }
        else
        {
          p2Move = x;
        }
      }
    }
    for (int x = 0; x < 6; x ++)
    {
      if (ae.getSource().equals(currSwitchablePokemon.get(x)))
      {
        if (switchFaint != -1)
        {
          if (switchFaint == 1)
          {
            p1Active = p1Pokemon.get(x);
          }
          else
          {
            p2Active = p2Pokemon.get(x);
          }
          switchFaint = -1;
        }
        else if (whoseTurn)
        {
          p1Switch = x;
        }
        else
        {
          p2Switch = x;
        }
      }
    }
    if (!ae.getSource().equals(megaevo))
    {
      if (whoseTurn)
      {
        whoseTurn = false;
        p2Turn();
      }
      else if (!whoseTurn)
      {
        whoseTurn = true;
        turnMove();
      }
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
    ResultSet resMoves;
    ResultSet resMoveData;
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
      
      String query_pokemonMoves = "select * from pokemon_moves";
      
      String query_MoveInfo = "select * from moves where id =";
      
      stmt = con.createStatement();
      
      resName = stmt.executeQuery(query_pokemon);
      

      while(resName.next()) // this loop adds all of the pokemon
      {
        
        int temporaryID = Integer.parseInt(resName.getString("id"));
   
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
      System.out.println("ALL POKEMON LOADED :: NOW LOADING POKEMON MOVES");
      
      resMoves = stmt.executeQuery(query_pokemonMoves);
      
      while(resMoves.next())
      {
    	  Move.addToMovePool(new Move(Integer.parseInt(restTypes.getString("pokemon_id")), Integer.parseInt(restTypes.getString("move_id"))));
      }
      //Move.printMoveList();
      /*
      for(Move m :  Move.getMovePool())
      {
    	  query_MoveInfo+=m.getMoveID();
    	  resMoveData = stmt.executeQuery(query_MoveInfo);
    	  ///parse data from the move retrieved
    	  String power = restTypes.getString("power");
    	  String pp = restTypes.getString("pp");
    	  String accuracy = restTypes.getString("accuracy");
    	  String priority = restTypes.getString("priority");
    	  String damage_class_id = restTypes.getString("damage_class_id");
    	  String effect_id = restTypes.getString("effect_id");
    	  String type_id = restTypes.getString("type_id");
    	  String name = restTypes.getString("identifier");
    	  
    	  m.setType(Integer.parseInt(type_id));
    	  m.setName(name);
    	  m.setPower(Integer.parseInt(power));
    	  m.setPP(Integer.parseInt(pp));
    	  m.setPriority(Integer.parseInt(priority));
    	  if(accuracy != null)
    		  m.setAccuracy(Integer.parseInt(accuracy));
    	  
    	  //reset the query string
    	  query_MoveInfo = "select * from moves where id =";
      }
      */
      //System.out.println("ALL POKEMON MOVES LOADED");
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
