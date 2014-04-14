/* Sam Ginzburg and Lang Gao
 * 
 * This class loads the GUI for the pokemon showdown, as well as loading the initial lists of all pokemon necessary
 * This class also contains all of the logic for the pokemon battles.
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javax.swing.*;

public class PokemonShowdownMainGui implements ActionListener, AdjustmentListener
{
	String p1Name, p2Name;
	boolean battleInProgress, whoseTurn;  //whoseTurn = true if p1, false if p2
	Pokemon p1Active, p2Active;
	int turnNum, p1ActiveIndex, p2ActiveIndex, p1Move, p2Move, switchFaint, p1Switch, p2Switch; //switch -1 if no switch, 0-5 switch to that slot
	JLabel currPlayerAndAllPokemon, opPlayerAndAllPokemon, currPlayerActiveImg, opPlayerActiveImg,
	  currPlayerPokemonStatusEffects, opPlayerPokemonStatusEffects, previousMovesLog, currTurnEvents;
	JProgressBar currPlayerPokemonHP, opPlayerPokemonHP;
	JScrollBar movesLogScroll;
	ArrayList<Pokemon> p1Pokemon, p2Pokemon, pokemonPool;
	ArrayList<JButton> currPokemonMoves, currSwitchablePokemon;
	int[] p1FieldDebuffs, p2FieldDebuffs;
	int[][] weaknessesAndResistances;

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
		
		//weaknessesAndResistances = {}

		JPanel test = new JPanel();
		
		experimentalPokemonDBLoader(); // load all pokemon
		jfrm.setVisible(true);
		
		previousMovesLog = new JLabel("<html>", SwingConstants.LEFT);
		//previousMovesLog.setPreferredSize(new Dimension(300,300));
		JScrollPane jsp = new JScrollPane(previousMovesLog,    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(300,300));
		jsp.getVerticalScrollBar().addAdjustmentListener(this);
		
		
		test.add(jsp);
		gbc.gridx = 7; gbc.gridy = 0;
		gbc.gridheight = 7; gbc.gridwidth = 2;
		jfrm.add(test, gbc);
		
		currPokemonMoves = new ArrayList<JButton>();
		currSwitchablePokemon = new ArrayList<JButton>();
		
		gbc.gridheight = 1; gbc.gridwidth = 1;
		
		for (int x = 0; x < 4; x ++)
		{
		  currPokemonMoves.add(new JButton("<html>     "));
		  currPokemonMoves.get(x).addActionListener(this);
		  gbc.gridx = 2 + x; gbc.gridy = 7;
		  jfrm.add(currPokemonMoves.get(x), gbc);
		}
		for (int x = 0; x < 6; x ++)
		{
		  currSwitchablePokemon.add(new JButton("<html>"));
		  currSwitchablePokemon.get(x).setIcon(new ImageIcon("resources/icons/0.png"));
		  currSwitchablePokemon.get(x).addActionListener(this);
		  gbc.gridx = 1 + x; gbc.gridy = 8;
		  jfrm.add(currSwitchablePokemon.get(x), gbc);
		}
		
		
		currPlayerAndAllPokemon = new JLabel("<html>");
		gbc.gridx = 0; gbc.gridy = 3;
		jfrm.add(currPlayerAndAllPokemon, gbc);
		opPlayerAndAllPokemon = new JLabel("<html>");
		gbc.gridx = 6; gbc.gridy = 3;
		jfrm.add(opPlayerAndAllPokemon, gbc);
		
		
		currPlayerPokemonStatusEffects = new JLabel("");
		gbc.gridx = 2; gbc.gridy = 4;
		jfrm.add(currPlayerPokemonStatusEffects, gbc);
		opPlayerPokemonStatusEffects = new JLabel("");
		gbc.gridx = 4; gbc.gridy = 1;
		jfrm.add(opPlayerPokemonStatusEffects, gbc);
		
		
		currPlayerActiveImg = new JLabel();
		currPlayerActiveImg.setVerticalTextPosition(SwingConstants.TOP);
		currPlayerActiveImg.setHorizontalTextPosition(SwingConstants.CENTER);
		gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
		jfrm.add(currPlayerActiveImg, gbc);
		opPlayerActiveImg = new JLabel();
		opPlayerActiveImg.setVerticalTextPosition(SwingConstants.TOP);
		opPlayerActiveImg.setHorizontalTextPosition(SwingConstants.CENTER);
		gbc.gridx = 5; gbc.gridy = 0;
		jfrm.add(opPlayerActiveImg, gbc);
		
		gbc.gridwidth = 1;
		
		currPlayerPokemonHP = new JProgressBar();
		currPlayerPokemonHP.setMinimumSize(new Dimension(200, 25));
		gbc.gridx = 2; gbc.gridy = 4;
		jfrm.add(currPlayerPokemonHP, gbc);
		opPlayerPokemonHP = new JProgressBar();
		opPlayerPokemonHP.setMinimumSize(new Dimension(200, 25));
		gbc.gridx = 4; gbc.gridy = 0;
		jfrm.add(opPlayerPokemonHP, gbc);
		
		currPlayerPokemonHP.setStringPainted(true);
		opPlayerPokemonHP.setStringPainted(true);
		
		currTurnEvents = new JLabel ("<html>");
		gbc.gridx = 1; gbc.gridy = 5;
		gbc.gridwidth = 6;
		gbc.gridheight = 2;
		jfrm.add(currTurnEvents, gbc);
		
		new SetupGui(this);
		
	}
	
	//Mechanics
	
	public void createOneVsOne()
	{
		p1Pokemon = new ArrayList<Pokemon>();
	  	p2Pokemon = new ArrayList<Pokemon>();

		p1Move = -1;
		p2Move = -1;
		p1Switch = -1;
		p2Switch = -1;
		switchFaint = -1;
		previousMovesLog.setText("<html>");
		currTurnEvents.setText("<html>");
		battleInProgress = true;
		whoseTurn = true;
		turnNum = 1;
		
		p1Pokemon.add((pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone());
		p2Pokemon.add((pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone());
		
		for (int x = 1; x < 6; x ++)
		{
			currSwitchablePokemon.get(x).setEnabled(false);
		}
		
		Pokemon.loadMoveSet(p1Pokemon.get(0));
		Pokemon.loadMoveSet(p2Pokemon.get(0));
		
		
    while(!pokemonHelper(p1Pokemon) && !pokemonHelper(p1Pokemon))
    {
      System.out.println("invalid pokemon detected --- regenerating list");
      p1Pokemon.clear();
      p2Pokemon.clear();
      for (int x = 0; x < 6; x ++)
      {
        Pokemon temp1 =  (pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone();
        Pokemon temp2 =  (pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone();
        Pokemon.loadMoveSet(temp1);
        Pokemon.loadMoveSet(temp2);
        p1Pokemon.add(temp1);
        p2Pokemon.add(temp2);
      }
      
    }
		
		
		p1Active = p1Pokemon.get(0);
		p2Active = p2Pokemon.get(0);
		
		
		p1ActiveIndex = 0;
		p2ActiveIndex = 0;
	}
	
	public void createSixVsSix()
	{
		p1Pokemon = new ArrayList<Pokemon>();
	  	p2Pokemon = new ArrayList<Pokemon>();
		p1Move = -1;
		p2Move = -1;
		p1Switch = -1;
		p2Switch = -1;
		switchFaint = -1;
		previousMovesLog.setText("<html>");
		currTurnEvents.setText("<html>");
		battleInProgress = true;
		whoseTurn = true;
		turnNum = 1;
		for (int x = 0; x < 6; x ++)
		{
			p1Pokemon.add((pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone());
			p2Pokemon.add((pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone());
		}
		
		for(Pokemon p : p1Pokemon)
			Pokemon.loadMoveSet(p);
    
		for(Pokemon p : p2Pokemon)
			Pokemon.loadMoveSet(p);
		
		while(!pokemonHelper(p1Pokemon) && !pokemonHelper(p1Pokemon))
		{
		  System.out.println("invalid pokemon detected --- regenerating list");
			for (int x = 0; x < 6; x ++)
			{
			  Pokemon temp1 =  (pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone();
			  Pokemon temp2 =  (pokemonPool.get((int)(Math.random()* pokemonPool.size()))).clone();
			  Pokemon.loadMoveSet(temp1);
			  Pokemon.loadMoveSet(temp2);
        p1Pokemon.add(temp1);
				p2Pokemon.add(temp2);
			}
			
		}
		

    
	    p1Active = p1Pokemon.get(0);
	    p2Active = p2Pokemon.get(0);
	    p1ActiveIndex = 0;
	    p2ActiveIndex = 0;
	    
	}
	
	private boolean pokemonHelper(ArrayList<Pokemon> list)
	{
		for(Pokemon p : list)
		{
			if(p.getPossibleMoveSet().size() < 4)
				return false;
		}
		return true;
	}
	
	public void addNames(String p1n, String p2n)
	{
		p1Name = p1n; 
		p2Name = p2n;
	}
	
	public JFrame getjfrm()
	{
		return jfrm;
	}
	
	public void p1Turn()
	{
	  String i = "It is now " + p1Name + "'s Turn";
	  new InstructionsGui(this, i);
	  
	  currPlayerAndAllPokemon.setText(p1Name);
	  opPlayerAndAllPokemon.setText(p2Name);
	  currPlayerActiveImg.setText("<html>" + p1Active.getName() + "<br>" + p1Active.getTypeAsString());
	  opPlayerActiveImg.setText("<html>" + p2Active.getName() + "<br>" + p2Active.getTypeAsString());
	  currPlayerActiveImg.setIcon(p1Active.getImg());  //get img of p1activepokemon
	  opPlayerActiveImg.setIcon(p2Active.getImg());  //get img of p2activepokemon
	  
	  currPlayerPokemonHP.setValue((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()));
	  currPlayerPokemonHP.setString(currPlayerPokemonHP.getValue() + "%");
	  
	  //buff and status effect data
	  currPlayerPokemonStatusEffects.setText(p1Active.getStatusEffect());
	  for (int x = 0; x < p1Active.getBuffs().size(); x ++)
	  {
		  currPlayerPokemonStatusEffects.setText(currPlayerPokemonStatusEffects.getText() + " " + p1Active.getBuffs().get(x));
	  }
	  
	  opPlayerPokemonHP.setValue((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()));
	  opPlayerPokemonHP.setString(opPlayerPokemonHP.getValue() + "%");
	  opPlayerPokemonStatusEffects.setText(p2Active.getStatusEffect());
	  for (int x = 0; x < p2Active.getBuffs().size(); x ++)
	  {
		  opPlayerPokemonStatusEffects.setText(opPlayerPokemonStatusEffects.getText() + " " + p2Active.getBuffs().get(x));
	  }
	  
	  
	  
	  for (int x = 0; x < 4; x ++)
	  {
	    currPokemonMoves.get(x).setEnabled(true);
	    
	    //TODO copy stuff below about currPokemonMoves.get(x).setText() to p2
	    //TODO also do this for p1 and p2 pokemon switch buttons
	    currPokemonMoves.get(x).setText("<html>" + p1Active.getMoveSet().get(x).getName());

	    String cPMxsTTT = "<html>" + p1Active.getMoveSet().get(x).getType() + " attack / "
          + p1Active.getMoveSet().get(x).getDmgType() + " type <br>"
          + p1Active.getMoveSet().get(x).getPower() + " power / ";
      if (p1Active.getMoveSet().get(x).getHitChance() == 0)
      {
        cPMxsTTT += "cannot miss";
      }
      else
      {
        cPMxsTTT += p1Active.getMoveSet().get(x).getHitChance() + " % hitchance";
      }
      currPokemonMoves.get(x).setToolTipText(cPMxsTTT);

	    if (!p1Active.getMoveSet().get(x).useable())
	    {
	      currPokemonMoves.get(x).setEnabled(false);
	    }
	  }

	  for (int x = 0; x < p1Pokemon.size(); x ++)
	  {
	    currSwitchablePokemon.get(x).setEnabled(true);
	    currSwitchablePokemon.get(x).setText(p1Pokemon.get(x).getName());
	    currSwitchablePokemon.get(x).setIcon(p1Pokemon.get(x).getImg());
	    currSwitchablePokemon.get(x).setToolTipText("<html>" + p1Pokemon.get(x).getTypeAsString() + "type<br>"
	    		+ p1Pokemon.get(x).getStatsAsString() + "<br>"
	    		+ p1Pokemon.get(x).getMoveSetAsString());
	    if (p1Pokemon.get(x).equals(p1Active) || p1Pokemon.get(x).getHP() < 1)
	    {
	      currSwitchablePokemon.get(x).setEnabled(false);
	    }
	  }
	}
	
	public void p2Turn()
	{
	  String i = "It is now " + p2Name + "'s Turn";
    new InstructionsGui(this, i);
    
	  currPlayerAndAllPokemon.setText(p2Name);
	  opPlayerAndAllPokemon.setText(p1Name);
	  currPlayerActiveImg.setText("<html>" + p2Active.getName() + "<br>" + p2Active.getTypeAsString());
	  opPlayerActiveImg.setText("<html>" + p1Active.getName() + "<br>" + p1Active.getTypeAsString());
	  currPlayerActiveImg.setIcon(p2Active.getImg());  //get img of p2activepokemon
      opPlayerActiveImg.setIcon(p1Active.getImg());  //get img of p1activepokemon
	  
	  currPlayerPokemonHP.setValue((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()));
	  currPlayerPokemonHP.setString((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()) + "%");
	  
	  //buff and status effect data
	  currPlayerPokemonStatusEffects.setText(p2Active.getStatusEffect());
	  for (int x = 0; x < p2Active.getBuffs().size(); x ++)
	  {
		  currPlayerPokemonStatusEffects.setText(currPlayerPokemonStatusEffects.getText() + " " + p2Active.getBuffs().get(x));
	  }
	  
	  opPlayerPokemonHP.setValue((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()));
	  opPlayerPokemonHP.setString((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()) + "%");
	  opPlayerPokemonStatusEffects.setText(p1Active.getStatusEffect());
	  for (int x = 0; x < p1Active.getBuffs().size(); x ++)
	  {
		  opPlayerPokemonStatusEffects.setText(opPlayerPokemonStatusEffects.getText() + " " + p1Active.getBuffs().get(x));
	  }
    
    for (int x = 0; x < 4; x ++)
    {
      currPokemonMoves.get(x).setEnabled(true);
      currPokemonMoves.get(x).setText("<html>" + p2Active.getMoveSet().get(x).getName());
      
      String cPMxsTTT = "<html>" + p2Active.getMoveSet().get(x).getType() + " attack / "
          + p2Active.getMoveSet().get(x).getDmgType() + " type <br>"
          + p2Active.getMoveSet().get(x).getPower() + " power / ";
      if (p2Active.getMoveSet().get(x).getHitChance() == 0)
      {
        cPMxsTTT += "cannot miss";
      }
      else
      {
        cPMxsTTT += p2Active.getMoveSet().get(x).getHitChance() + " % hitchance";
      }
      currPokemonMoves.get(x).setToolTipText(cPMxsTTT);
      
      if (!p2Active.getMoveSet().get(x).useable())
      {
        currPokemonMoves.get(x).setEnabled(false);
      }
    }
    for (int x = 0; x < p2Pokemon.size(); x ++)
    {
      currSwitchablePokemon.get(x).setEnabled(true);
      currSwitchablePokemon.get(x).setText(p2Pokemon.get(x).getName());
      currSwitchablePokemon.get(x).setIcon(p2Pokemon.get(x).getImg());
      currSwitchablePokemon.get(x).setToolTipText("<html>" + p2Pokemon.get(x).getTypeAsString() + "type<br>"
	    		+ p2Pokemon.get(x).getStatsAsString() + "<br>"
	    		+ p2Pokemon.get(x).getMoveSetAsString());
      if (p2Pokemon.get(x).equals(p2Active) || p2Pokemon.get(x).getHP() < 1)
      {
        currSwitchablePokemon.get(x).setEnabled(false);
      }
    }
  }


	//TODO check status effects and item effects after both pokemon move
	public void turnMove()
	{
		currTurnEvents.setText("<html>Turn " + turnNum + " -------");
		previousMovesLog.setText(previousMovesLog.getText() + "<br>Turn " + turnNum + " -------");

		if (p1Switch != -1)  //P1 switches out
		{
			currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p1Name + " withdrew " + p1Active.getName());
			previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p1Name + " withdrew  " + p1Active.getName());
			p1Active = p1Pokemon.get(p1Switch);
			currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p1Name + " sent out " + p1Active.getName());
			previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p1Name + " sent out " + p1Active.getName());
			if (p2Move != -1)
			{
			  Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
			}
		}
		
		if (p2Switch != -1)  //p2 Switches out
		{
			currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p2Name + " withdrew " + p2Active.getName());
			previousMovesLog.setText(previousMovesLog.getText() +"<br>" + p2Name + " withdrew " + p2Active.getName());
			p2Active = p2Pokemon.get(p2Switch);
			currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p2Name + " sent out " + p2Active.getName());
			previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p2Name + " sent out " + p2Active.getName());
			if (p1Move != -1)
			{
				Pokemon.setHP(p2Active, p2Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
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
		    	Pokemon.setHP(p2Active, p2Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
		    	if (p2Active.getHP() > 0)
		    	{
		    		Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
		    	}
		    }
		    //both players same spd
		    else if (p1Active.getSpeed() == p2Active.getSpeed())
	      	{
	        //p1 wins roll goes first
		    	if (Math.random() > .5)
		    	{
		    		Pokemon.setHP(p2Active, p2Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
		    		if (p2Active.getHP() > 0)
		    		{
		    			Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
		    		}
		    	}
	        //p2 wins roll goes first
		    	else
		    	{
		    		Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
		    		{
		    			if (p1Active.getHP() > 0)
		    			{
		    				Pokemon.setHP(p2Active, p2Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
		    			}
		    		}
		    	}
	      	}
		    //p2 faster
		    else
		    {
		    	Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
		    	if (p1Active.getHP() > 0)
		    	{
		    		Pokemon.setHP(p2Active, p2Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
		    	}
		    }
		  }
		  //priorities unequal
		  else
		  {
		    //p1 higher priority
		    if (p1Active.getMoveSet().get(p1Move).getPriority() > p2Active.getMoveSet().get(p2Move).getPriority())
		    {
		      Pokemon.setHP(p2Active, p2Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
		      if (p2Active.getHP() > 0)
		      {
		    	  Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
		      }
		    }
		    else
		    {
		        Pokemon.setHP(p1Active, p1Active.getHP() - performAttack(p2Active, p1Active, p2Active.getMoveSet().get(p2Move)));
		        if (p1Active.getHP() > 0)
		        {
		      	  Pokemon.setHP(p2Active, p1Active.getHP() - performAttack(p1Active, p2Active, p1Active.getMoveSet().get(p1Move)));
		        }
		    }
		  }	  
		}
		
		currPlayerPokemonHP.setValue((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()));
    currPlayerPokemonHP.setString((int)(p2Active.getHP() * 100 / p2Active.getMaxHP()) + "%");
    opPlayerPokemonHP.setValue((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()));
    opPlayerPokemonHP.setString((int)(p1Active.getHP() * 100 / p1Active.getMaxHP()) + "%");
    
		  battleOver();
		  
		  if (battleInProgress)
		  {
			  if (p1Active.getHP() < 1)
			  {
				  currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p1Name + "'s " + 
						  p1Active.getName() + " fainted!");
				  previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p1Name + "'s " + 
						  p1Active.getName() + " fainted!");
				  freeSwap(1);
			  }
			  if (p2Active.getHP() < 1)
			  {
				  currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p2Name + "'s " + 
						  p2Active.getName() + " fainted!");
				  previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p2Name + "'s " + 
						  p2Active.getName() + " fainted!");
				  freeSwap(2); 
			  }
			  //Damage from status conditions
			  if (p1Active.getStatusEffect().equals("PSN") && p1Active.getTurnsStatus() > 0)
			  {
				  int dmg = (int)(p1Active.getMaxHP() * .06 * p1Active.getTurnsStatus());
				  Pokemon.setHP(p1Active, p1Active.getHP() - dmg);
			  }
			  else if (p1Active.getStatusEffect().equals("BRN") && p1Active.getTurnsStatus() > 0)
			  {
				  int dmg = (int)(p1Active.getMaxHP() * .12);
				  Pokemon.setHP(p1Active, p1Active.getHP() - dmg);
			  }
			  
			  battleOver();
		  }
		  
		//resets variables in preparation for next round
		if (battleInProgress)
		{
			p1Move = -1;
			p2Move = -1;
			p1Switch = -1;
			p2Switch = -1;
			turnNum ++;
			p1Turn();
		}
		
	}
	
	public void freeSwap (int player)
	{
		battleInProgress = false;
	  for (int x = 0; x < 4; x ++)
	  {
	    currPokemonMoves.get(x).setText("     ");
	    currPokemonMoves.get(x).setEnabled(false);
	  }
	  if (player == 1)
	  {
	    String i = p1Name + " Must Switch Pokemon";
	    new InstructionsGui(this, i);
	    
	    switchFaint = 1;
	    for (int x = 0; x < p1Pokemon.size(); x ++)
	    {
	      currSwitchablePokemon.get(x).setEnabled(true);
	      currSwitchablePokemon.get(x).setText(p1Pokemon.get(x).getName());
	      currSwitchablePokemon.get(x).setIcon(p1Pokemon.get(x).getImg());
	      currSwitchablePokemon.get(x).setToolTipText("<html>" + p1Pokemon.get(x).getTypeAsString() + "type<br>"
	          + p1Pokemon.get(x).getStatsAsString() + "<br>"
	          + p1Pokemon.get(x).getMoveSetAsString());
	      if (x == p1ActiveIndex || p1Pokemon.get(x).getHP() < 1)
	      {
	        currSwitchablePokemon.get(x).setEnabled(false);
	      }
	    }
	  }
	  else
	  {
	    String i = p2Name + " Must Switch Pokemon";
      new InstructionsGui(this, i);
      
	    switchFaint = 2;
	    for (int x = 0; x < p2Pokemon.size(); x ++)
	    {
	      currSwitchablePokemon.get(x).setEnabled(true);
	      currSwitchablePokemon.get(x).setText(p2Pokemon.get(x).getName());
	      currSwitchablePokemon.get(x).setIcon(p2Pokemon.get(x).getImg());
	      if (x == p2ActiveIndex || p2Pokemon.get(x).getHP() < 1)
	      {
	        currSwitchablePokemon.get(x).setEnabled(false);
	      }
	    }
	  }
	}
	
	public int performAttack(Pokemon attacker, Pokemon defender, Move attack)
	{
		//Check attack inhibiting status
		if (attacker.getStatusEffect().equals("FRZ"))
		{
			if (Math.random() < .8)
			{
				currTurnEvents.setText(currTurnEvents.getText() + "<br>"
						+ attacker.getName() + " is frozen! It can't move!");
				previousMovesLog.setText(previousMovesLog.getText() + "<br>"
						+ attacker.getName() + " is frozen! It can't move!");
				return 0;
			}
			else
			{
				currTurnEvents.setText(currTurnEvents.getText() + "<br>"
						+ attacker.getName() + " thawed out!");
				previousMovesLog.setText(previousMovesLog.getText() + "<br>"
						+ attacker.getName() + " thawed out!");
			}
		}
		else if (attacker.getStatusEffect().equals("PAR"))
		{
			if (Math.random() < .25)
			{
				currTurnEvents.setText(currTurnEvents.getText() + "<br>"
						+ attacker.getName() + " is paralyzed! It can't move!");
				previousMovesLog.setText(previousMovesLog.getText() + "<br>"
						+ attacker.getName() + " is paralyzed! It can't move!");
				return 0;
			}
		}
		else if (attacker.getStatusEffect().equals("SLP"))
		{
			if (Math.random() < .66)
			{
				currTurnEvents.setText(currTurnEvents.getText() + "<br>"
						+ attacker.getName() + " is still asleep!");
				previousMovesLog.setText(previousMovesLog.getText() + "<br>"
						+ attacker.getName() + " is still asleep!");
				return 0;
			}
			else
			{
				currTurnEvents.setText(currTurnEvents.getText() + "<br>"
						+ attacker.getName() + " woke up!");
				previousMovesLog.setText(previousMovesLog.getText() + "<br>"
						+ attacker.getName() + " woke up!");
			}
		}
			
		currTurnEvents.setText(currTurnEvents.getText() + "<br>" +
				attacker.getName() + " used " + attack.getName() + "!");
		previousMovesLog.setText(previousMovesLog.getText() + "<br>" +
				attacker.getName() + " used " + attack.getName() + "!");
		
		
		//dmg calcs
		if (Math.random() < (attack.getHitChance()/100.0) || attack.getHitChance() == 00)
		{
		  int dmg = 0;
		  if (attack.getType().equals("p"))
		  {
			  dmg = (int)((((2 *  attacker.getLevel() / 5 + 2 ) * attacker.getAttack() * attack.getPower() 
					  / defender.getDefense()) / 50 + 2) * stabResult(attacker, attack) * 
					  weakResist(defender, attack) * ((int)(Math.random() * 16) + 85) / 100 );
			  if (attacker.getStatusEffect().equals("BRN"))
			  {
				  dmg *= .5;
			  }
		  }
		  else if (attack.getType().equals("s"))
		  {
			  dmg = (int)((((2 *  attacker.getLevel() / 5 + 2 ) * attacker.getSpAttack() * attack.getPower() 
					  / defender.getSpDefense()) / 50) + 2 * stabResult(attacker, attack) *
					  weakResist(defender, attack) * ((int)(Math.random() * 16) + 85) / 100 );
		  }
		  //Status effect
		  else if (attack.getType().equals("e"))
		  {
			  System.out.println("Status Effect Used");
		  }

		  currTurnEvents.setText(currTurnEvents.getText() + "<br>The opponent's " + defender.getName() + " lost "
		      + (int)(dmg * 100 / defender.getMaxHP()) + "% of its HP!");
		  previousMovesLog.setText(previousMovesLog.getText() +"<br>The opponent's " + defender.getName() + " lost "
		      + (int)(dmg * 100 / defender.getMaxHP()) + "% of its HP!" );
		  
		  return dmg;
		}
		else
		{
		  currTurnEvents.setText(currTurnEvents.getText() + "<br>The opponent's " + defender.getName() +
		      " dodged the attack!");
		  previousMovesLog.setText(previousMovesLog.getText() + "<br>The opponent's " + defender.getName() +
			      " dodged the attack!");
		  return 0;
		}	
		
		//TODO status effect moves	
		
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
	  Connection con;
	  ResultSet r;
	  Statement stmt;
	  double avgMultiplier = 1.0;
	  try
	  {
	      Driver d = (Driver)Class.forName("org.sqlite.JDBC").newInstance();
	      DriverManager.registerDriver(d);
	      
	      String url = "jdbc:sqlite:resources/pokemon-database.sqlite";
	      con = DriverManager.getConnection(url);
	      
	      String query_multiplier = "select * from type_efficacy";
	      
	      stmt = con.createStatement();
	      
	      r = stmt.executeQuery(query_multiplier);
	      
	      
	      while(r.next())
	      {
	        String dmgType = r.getString("damage_type_id");
	        String targetType = r.getString("target_type_id");
	        for(String type : defender.getType())
	        {
	        	if(type.equals(getType(Integer.parseInt(targetType))) && ability.getDmgType().equals(getType(Integer.parseInt(dmgType))))
	        	{
	        		avgMultiplier *= r.getDouble("damage_factor")/100;
		        }
	        }
	        
	      }
    
	      
	    }catch(Exception e)
	    {
	      e.printStackTrace();
	    }
	  
	  if (avgMultiplier == 0)
	  {
		  currTurnEvents.setText(currTurnEvents.getText() + "<br>the opposing pokemon is immune!");
	  }
	  else if(avgMultiplier > 1)
	  {
		  currTurnEvents.setText(currTurnEvents.getText() + "<br>it's super effective!");
	  }
	  else if (avgMultiplier < 1)
	  {
		  currTurnEvents.setText(currTurnEvents.getText() + "<br>it's not very effective!");
	  }
	   return avgMultiplier;
	}
	
	public void battleOver ()
	{
		boolean p1Alive = false;
		boolean p2Alive = false;
		for (int x = 0; x < p1Pokemon.size(); x ++)
		{
			if (p1Pokemon.get(x).getHP() > 0)
			{
				p1Alive = true;
			}
		}
		for (int x = 0; x < p2Pokemon.size(); x ++)
		{
			if (p2Pokemon.get(x).getHP() > 0)
			{
				p2Alive = true;
			}
		}
		
		if (!p1Alive || !p2Alive)
		{
			battleInProgress = false;
			new SetupGui(this);
		}
		if (!p1Alive)
		{
			currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p2Name + " has won the battle!");
			previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p2Name + " has won the battle!");
			
		}
		else if (!p2Alive)
		{
			currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p1Name + " has won the battle!");
			previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p1Name + " has won the battle!");
		}
		
	}
	
  public void actionPerformed(ActionEvent ae)
  {
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
    for (int x = 0; x < currSwitchablePokemon.size(); x ++)
    {
      if (ae.getSource().equals(currSwitchablePokemon.get(x)))
      {
        if (switchFaint != -1)
        {
        	
          if (switchFaint == 1)
          {
            p1Active = p1Pokemon.get(x);
            currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p1Name + " sent out " + p1Active.getName());
            previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p1Name + " sent out " + p1Active.getName());
          }
          else
          {
            p2Active = p2Pokemon.get(x);
            currTurnEvents.setText(currTurnEvents.getText() + "<br>" + p2Name + " sent out " + p2Active.getName());
            previousMovesLog.setText(previousMovesLog.getText() + "<br>" + p2Name + " sent out " + p2Active.getName());
          }
          switchFaint = -1;
          battleInProgress = true;
          whoseTurn = true;
          p1Move = -1;
		  p2Move = -1;
		  p1Switch = -1;
		  p2Switch = -1;
		  turnNum ++;
		  p1Turn();
          return;
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

	

  
  public void experimentalPokemonDBLoader()
  {
    Statement stmt;
    ResultSet resName;
    ResultSet resStats;
    ResultSet restTypes;
    ResultSet resMoves;
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
              Pokemon.setHP(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 110 + statValue * 2);
              Pokemon.setMaxHP(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 110 + statValue * 2);
              break;
            case 2:
             Pokemon.setAttack(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 5 + 2 *statValue);
             break;
            case 3:
             Pokemon.setDefense(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 5 + 2 *statValue);
             break;
            case 4:
             Pokemon.setSpAttack(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 5 + 2 *statValue);
             break;
            case 5:
             Pokemon.setSpDefense(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 5 + 2 *statValue);
             break;
            case 6:
             Pokemon.setSpeed(Pokemon.getPokemonByID(Integer.parseInt(resStats.getString("pokemon_id")),pokemonPool) , 5 + 2 *statValue);
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

    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    
        
  }
  
  
  public static String getType(int type)
  {
	  String ret = "";
	  switch(type) 
      {
        case 1:
      	  ret = "normal";
      	  break;
        case 2:
      	  ret = "fighting";
      	  break;
        case 3:
        	ret = "flying";
	        break;
        case 4:
        	ret = "poison";
	          break;
        case 5:
        	ret = "ground";
	     	  break;
        case 6:
        	ret = "rock";
	        break;
        case 7:
        	ret = "bug";
	        break;
        case 8:
        	ret = "ghost";
        	break;
        case 9:
        	ret = "steel";
        	break;
        case 10:
        	ret = "fire";
      	  break;
        case 11:
      	  ret = "water";
      	  break;
        case 12:
      	  ret = "grass";
      	  break;
        case 13:
      	  ret = "electric";
      	  break;
        case 14:
      	  ret = "psychic";
      	  break;
        case 15:
      	  ret = "ice";
      	  break;
        case 16:
      	  ret = "dragon";
      	  break;
        case 17:
      	  ret = "dark";
      	  break;
        case 18:
      	  ret = "fairy";
      	   break;
        case 10001:
      	  ret = "unknown";
      	  break;
        case 10002:
      	  ret = "shadow";
      	  break;
      }
	  return ret;
  }

  public void adjustmentValueChanged(AdjustmentEvent e)
  {
    if(!e.getValueIsAdjusting())
    {
      e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
    }
    
  }

}

