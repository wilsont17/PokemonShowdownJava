import java.io.FileNotFoundException;

import javax.swing.*;

public class RunThis 
{
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try
                {
                  new PokemonShowdownMainGui();
                }
                catch (FileNotFoundException e)
                {
                  e.printStackTrace();
                }
            }
        });
	}	

}
