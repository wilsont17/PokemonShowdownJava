package src;/* Sam Ginzburg and Lang Gao
 * 
 * This class is the class that runs the entire game on a new thread, if the GUI cannot be initialized the program exits.
*/

import javax.swing.*;
import java.io.FileNotFoundException;

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
