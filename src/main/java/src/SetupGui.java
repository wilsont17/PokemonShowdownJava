/* Sam Ginzburg and Lang Gao
 * 
 * This class shows the initial setup screen for a game, it allows the user to select their names and the game mode desired.
*/

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SetupGui implements ActionListener
{
	JFrame j;
	JLabel p1NameDesc, p2NameDesc;
	JButton start;
	JComboBox<String> gameModes;
	JTextField p1NameInput, p2NameInput;
	PokemonShowdownMainGui h;
	
	public SetupGui(PokemonShowdownMainGui host)
	{
		h = host;
		j = new JFrame("Setup Game");
		j.setLayout(new GridLayout(0, 1));
		j.setSize(250,350);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);
		
		p1NameDesc = new JLabel("Enter P1 Name Below");
		p1NameInput = new JTextField(15);
		p2NameDesc = new JLabel("Enter P2 Name Below");
		p2NameInput = new JTextField(15);
		p2NameInput.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	            	start.doClick();
	            }}});
		j.add(p1NameDesc);
		j.add(p1NameInput);
		j.add(p2NameDesc);
		j.add(p2NameInput);
		
		String[] gameOptions = {"1v1", "6v6"};	
		gameModes = new JComboBox<String>(gameOptions);
		gameModes.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	            	start.doClick();
	            }}});
		j.add(gameModes);
	
		start = new JButton("Start Game");
		start.addActionListener(this);
		j.add(start);
	}

	public void actionPerformed(ActionEvent ae) 
	{
		if (ae.getSource() == start)
		{
			h.addNames(p1NameInput.getText(), p2NameInput.getText());
			if (gameModes.getSelectedIndex() == 0)  //JComboBox selects 1v1
			{
				h.createOneVsOne();
			}
			else if (gameModes.getSelectedIndex() == 1)  //JComboBox selects 6v6
			{
				h.createSixVsSix();
			}
			h.p1Turn();
			j.dispose();
		}
		
	}
}
