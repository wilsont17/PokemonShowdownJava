import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SetupGui implements ActionListener
{
	JFrame j;
	JLabel p1NameDesc, p2NameDesc;
	JButton start;
	JComboBox gameModes;
	JTextField p1NameInput, p2NameInput;
	PokemonShowdownMainGui h;
	
	public SetupGui(PokemonShowdownMainGui host)
	{
		h = host;
		j = new JFrame("Setup Game");
		j.setLayout(new GridLayout(0, 1));
		j.setSize(600,400);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);
		
		p1NameDesc = new JLabel("Enter P1 Name Below");
		p1NameInput = new JTextField(15);
		p2NameDesc = new JLabel("Enter P2 Name Below");
		p2NameInput = new JTextField(15);
		j.add(p1NameDesc);
		j.add(p1NameInput);
		j.add(p2NameDesc);
		j.add(p2NameInput);
		
		//gameModes = new JComboBox();
		//String onevsone = "1 vs 1";
		//gameModes.addItem(onevsone);
		//j.add(gameModes);
		
		start = new JButton("Start Game");
		start.addActionListener(this);
		j.add(start);
		
	}

	public void actionPerformed(ActionEvent ae) 
	{
		if (ae.getSource() == start)
		{
			System.out.println(p1NameInput.getText());
			h.addNames(p1NameInput.getText(), p2NameInput.getText());
			/*
			if ()  //JComboBox selects 1v1
			{
				h.createOneVsOne();
			}
			else if ()  //JComboBox selects 6v6
			{
				h.createSixVsSix();
			}
			*/
			j.dispose();
		}
		
	}
}
