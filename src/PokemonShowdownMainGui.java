import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.*;

public class PokemonShowdownMainGui 
{
	String p1Name, p2Name;
	boolean p1Switch, p2Switch, p1Moved, p2Moved;
	Pokemon p1Active, p2Active;
	JLabel p1AndAllPokemon, p2AndAllPokemon, p1ActiveImg, p2ActiveImg,
	  p1StatusEffects, p2StatusEffects, previousMovesLog, currentTurnEvents;
	JProgressBar p1PokemonHP, p2PokemonHP;
	JScrollBar movesLogScroll;
	ArrayList<Pokemon> p1Pokemon, p2Pokemon, pokemonPool;

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

    experimentalPokemonDBLoader();
    System.out.println(pokemonPool);
		jfrm.setVisible(true);
		
		previousMovesLog = new JLabel("<html>");
		gbc.gridx = 8; gbc.gridy = 0;
		gbc.gridheight = 9; gbc.gridwidth = 3;
		jfrm.add(previousMovesLog, gbc);
		
		new SetupGui(this);
		
		
	}
	
	//Mechanics
	
	public void createOneVsOne()
	{
		p1Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
		p2Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
		p1Active = p1Pokemon.get(0);
		p2Active = p2Pokemon.get(0);
	}
	
	public void createSixVsSix()
	{
		for (int x = 0; x < 6; x ++)
		{
			p1Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
			p2Pokemon.add(pokemonPool.get((int)(Math.random()* pokemonPool.size())));
			p1Active = p1Pokemon.get(0);
			p2Active = p2Pokemon.get(0);
		}
	}
	
	public void addNames(String p1n, String p2n)
	{
		p1Name = p1n; 
		p2Name = p2n;
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
			 // System.out.println(resStats.getString("pokemon_id"));
			 // System.out.println(resStats.getString("stat_id")); //1 = hp, 2 = attack, 3 = defense, 4 = sp attack, 5 = sp defense, 6=speed, 
 			  //System.out.println(resStats.getString("base_stat")); // 7 = accuracy, 8 = evasion
 			  
 			  String tempstatID = resStats.getString("stat_id");
 			  int switchCase = Integer.parseInt(tempstatID);
 			  int statValue = Integer.parseInt(resStats.getString("base_stat"));
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
 			 
 			 
 			  //System.out.println(resStats.getString("effort"));
      }
      
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		    
	}
		

}

