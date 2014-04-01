import java.awt.*;
import java.util.*;

import javax.swing.*;

public class PokemonShowdownMainGui 
{
	boolean p1Switch, p2Switch, p1Moved, p2Moved;
	Pokemon p1Active, p2Active;
	JLabel p1AndAllPokemon, p2AndAllPokemon, p1ActiveImg, p2ActiveImg,
	  p1StatusEffects, p2StatusEffects, previousMovesLog, currentTurnEvents;
	JProgressBar p1PokemonHP, p2PokemonHP;
	JScrollBar movesLogScroll;
	ArrayList<Pokemon> p1Pokemon, p2Pokemon, currentPokemonMoves, pokemonPool;

	JFrame jfrm;
	GridBagLayout gb;
	GridBagConstraints gbc;
	
	
	PokemonShowdownMainGui()
	{
		jfrm = new JFrame("Pokemon Showdown");
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		jfrm.setLayout(gb);
		jfrm.setSize(1280,720);
		
		jfrm.setVisible(true);
		
		
		for (int x = 0; x < 6; x ++)
		{
		  
		  p1Pokemon.add();
		  p2Pokemon.add();
		}
		
		p1Active = p1Pokemon.get(0);
		p2Active = p2Pokemon.get(0);
		
		previousMovesLog = new JLabel("<html>");
		gbc.gridx = 8; gbc.gridy = 0;
		gbc.gridheight = 9; gbc.gridwidth = 3;
		jfrm.add(previousMovesLog, gbc);
		
		
	}
	
	
	//Mechanics
	
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
	

}

