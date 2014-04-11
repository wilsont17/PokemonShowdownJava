import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;


public class InstructionsGui implements ActionListener
{
  PokemonShowdownMainGui host;
  String info;
  JFrame f;
  JButton done;
  
  public InstructionsGui (PokemonShowdownMainGui h, String i)
  {
    host = h;
    info = i;
    
    f = new JFrame("ACHTUNG");
    f.setSize(400,400);
    f.setLayout(new FlowLayout());
    f.setVisible(true);
    
    host.getjfrm().setVisible(false);
    JLabel infomercial = new JLabel(info);
    f.add(infomercial);
    done = new JButton("Continue");
    done.addActionListener(this);
    f.add(done);
  }
  
  
  public void actionPerformed(ActionEvent ae)
  {
    if (ae.getSource() == done)
    {
      host.getjfrm().setVisible(true);
      f.dispose();
    }
    
  }

}
