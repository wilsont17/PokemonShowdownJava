import java.awt.*;
import java.util.*;

import javax.swing.*;

public class PokemonShowdownMainGui 
{
	Pokemon p1active, p2active;
	ArrayList<Pokemon> p1pokemon, p2pokemon, currentPokemonMoves, pokemonPool;

	JFrame jfrm;
	GridBagLayout gb;
	GridBagConstraints gbc;
	
	
	PokemonShowdownMainGui()
	{
		jfrm = new JFrame("Pokemon Showdown");
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		jfrm.setLayout(gb);
	}
	
	
	//Mechanics
	
	public void turnMove()
	{
		
		if (!p1switch && !p2switch)
		{
			if (p1active.getSpeed() > p2active.getSpeed())
			{
				
			}
			else if (p1active.getSpeed() == p2active.getSpeed())
			{
				
			}
			else
			{
				
			}
		}
	}
	

}

