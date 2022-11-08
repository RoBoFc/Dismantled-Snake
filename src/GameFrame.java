import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.*;

public class GameFrame extends JFrame implements ActionListener
{
	static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
	static final int WIDTH = (int) (SCREENSIZE.getWidth()*4/5); //1024
	static final int HEIGHT = (int) (SCREENSIZE.getHeight()*4/5); //576
	
	GamePanel gp = new GamePanel();
	
	JLabel l1 = new JLabel("Score: "+gp.score);
	JLabel l2 = new JLabel();
	
	JButton b1 = new JButton("PAUSE");
	JButton b2 = new JButton("NEW GAME");
	JButton b3 = new JButton("QUIT");
	
	Timer timer = new Timer(100,this);
	
	GameFrame()
	{
		l1.setBounds(20,30,220,80);
		l1.setFont(new Font(null,Font.BOLD,40));
		l1.setHorizontalAlignment(JLabel.CENTER);
		l1.setForeground(Color.black);
		
		b1.setBounds(50,150,150,60);
		b1.setFont(new Font(null,Font.BOLD,20));
		b1.setFocusable(false);
		b1.setBackground(Color.black);
		b1.setBorderPainted(false);
		b1.addActionListener(this);
		b1.setForeground(Color.green);
		
		b2.setBounds(50,250,150,60);
		b2.setFont(new Font(null,Font.BOLD,20));
		b2.setFocusable(false);
		b2.setBackground(Color.black);
		b2.setBorderPainted(false);
		b2.addActionListener(this);
		b2.setForeground(Color.green);
		
		b3.setBounds(50,350,150,60);
		b3.setFont(new Font(null,Font.BOLD,20));
		b3.setFocusable(false);
		b3.setBackground(Color.black);
		b3.setBorderPainted(false);
		b3.addActionListener(this);
		b3.setForeground(Color.green);
		
		l2.setBounds(20,420,220,80);
		l2.setFont(new Font(null,Font.BOLD,25));
		l2.setHorizontalAlignment(JLabel.CENTER);
		l2.setForeground(Color.black);
		
		this.setTitle("Dismantle Snake Game");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(WIDTH,HEIGHT);
		this.setResizable(false);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.green);
		this.add(gp);
		this.add(l1);
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(l2);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		l1.setText("Score: "+gp.score);
		l2.setText("Best Score: "+gp.highscore);
		
		if(gp.running)
		{
			b1.setEnabled(true);
			if(gp.timer.isRunning())
				b1.setText("PAUSE");
			else
				b1.setText("RESUME");
		}
		else
		{
			b1.setEnabled(false);
		}
		
		if(e.getSource()==b1)
		{
			if(b1.getText()=="PAUSE")
			{
				gp.timer.stop();
			}
			else
			{
				if(b1.getText()=="RESUME")
				{
					gp.timer.start();
				}
			}
		}
		if(e.getSource()==b2)
		{
			gp.timer.stop();
			if(JOptionPane.showConfirmDialog(getParent(), "Start a new game?", "Confirm", JOptionPane.YES_NO_OPTION)==0)
			{
				gp.newGame();
			}
			else
			{
				if(b1.getText()=="PAUSE")
					gp.timer.start();
			}
		}
		if(e.getSource()==b3)
		{
			gp.timer.stop();
			if(JOptionPane.showConfirmDialog(getParent(), "Quit this application?", "Confirm", JOptionPane.YES_NO_OPTION)==0)
			{
				gp.quitGame();
			}
			else
			{
				if(b1.getText()=="PAUSE")
					gp.timer.start();
			}
		}
	}
}
