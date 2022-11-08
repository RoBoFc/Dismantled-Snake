import java.awt.*;

import java.awt.event.*;
import java.io.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener
{
	
	static final int UNIT_SIZE = 30;
	static final int PANEL_WIDTH = 720;
	static final int PANEL_HEIGHT = 510;
	static final int GAME_UNITS = (PANEL_WIDTH*PANEL_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static int DELAY = 100;
	
	int i;
	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	int bodyparts;
	int appleX,appleY;
	int goodAppleX,goodAppleY;
	int goodAppleTime;
	int badAppleX,badAppleY;
	int badAppleTime;
	Random random;
	int appleseaten;
	int score;
	
	Timer timer = new Timer(DELAY,this);
	Timer goodAppleTimer = new Timer(6000,this);
	Timer badAppleTimer = new Timer(6000,this);
	boolean running  = false;
	boolean dismantle;
	boolean randomize;
	boolean goodApplePresent = false;
	boolean badApplePresent = false;
	
	char direction = 'R';
	
	File file = new File(".\\highscore.txt");
	FileInputStream fin;
	FileOutputStream fout;
	
	
	int highscore = 0;
	
	GamePanel()
	{
		this.setBounds(280,15,PANEL_WIDTH,PANEL_HEIGHT);
		this.setBackground(Color.black);
		this.setLayout(new BorderLayout());
		this.addKeyListener(new MyKeyAdapter());
		this.setFocusable(true);
		
		startGame();
	}
	
	public void startGame()
	{
		appleseaten=0;
		score = 0;
		bodyparts=3;
		x[0] = 0;
		y[0] = UNIT_SIZE*8;
		direction = 'R';
		random = new Random();
		running = true;
		dismantle = false;
		randomize = false;
		newApple();
		newGoodApple();
		newBadApple();
		timer.start();
		highscore = 0;
		try
		{
			fin = new FileInputStream(file);
			i = fin.read();
			while(i!=-1)
			{
				highscore = highscore*10 + ((int)i);
				i=fin.read();
			}
			fin.close();
		}
		catch (Exception e)
		{
			highscore = 0;
		}
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g)
	{
		if(running)
		{
			g.setColor(Color.red);
			g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
			
			for(i=0;i<bodyparts;i++)
			{
				if(i==0)
				{
					g.setColor(new Color(0,120,212));
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
				else
				{
					g.setColor(new Color(80,200,255));
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
			}
			
			if(goodAppleTime==appleseaten%10)
			{
				goodApplePresent = true;
				
				for(i=0;i<bodyparts;i++)
				{
					while(x[i]==goodAppleX && y[i]==goodAppleY)
					{
						goodAppleX = random.nextInt(PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
						goodAppleY = random.nextInt(PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
					}
				}
			}
			if(goodApplePresent)
			{
				g.setColor(Color.white);
				g.fillOval(goodAppleX,goodAppleY,UNIT_SIZE,UNIT_SIZE);
				goodAppleTimer.start();
			}
			
			if(badAppleTime==appleseaten%10)
			{
				badApplePresent=true;
				
				for(i=0;i<bodyparts;i++)
				{
					while(x[i]==badAppleX && y[i]==badAppleY)
					{
						badAppleX = random.nextInt(PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
						badAppleY = random.nextInt(PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
					}
				}
			}
			if(badApplePresent)
			{
				g.setColor(new Color(255,70,70));
				g.fillOval(badAppleX,badAppleY,UNIT_SIZE,UNIT_SIZE);
				badAppleTimer.start();
			}
		}
		else
		{
			gameOver(g);
		}
	}
	
	public void newApple()
	{
		appleX = random.nextInt(PANEL_WIDTH/UNIT_SIZE) * UNIT_SIZE;
		appleY = random.nextInt(PANEL_HEIGHT/UNIT_SIZE) * UNIT_SIZE;
		
		for(i=0;i<bodyparts;i++)
		{
			if(x[i]==appleX && y[i]==appleY)
				newApple();
		}
	}
	
	public void newGoodApple()
	{
		goodAppleTime = random.nextInt(10);
		
		goodAppleX = random.nextInt(PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		goodAppleY = random.nextInt(PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void newBadApple()
	{
		badAppleTime = random.nextInt(10);
		
		badAppleX = random.nextInt(PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		badAppleY = random.nextInt(PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void checkGoodApple()
	{
		if(x[0]==goodAppleX && y[0]==goodAppleY)
		{
			dismantle = false;
			randomize = false;
			goodApplePresent = false;
			score += 5;
			goodAppleTimer.stop();
			newGoodApple();
		}
	}
	
	public void checkBadApple()
	{
		if(x[0]==badAppleX && y[0]==badAppleY)
		{
			badApplePresent = false;
			dismantle = true;
			randomize = true;
			badAppleTimer.stop();
			newBadApple();
		}
	}
	
	public void move()
	{
		if(dismantle)
		{
			randomize = true;
			dismantle = false;
			for(i=bodyparts;i>0;i--)
			{
				x[i]=random.nextInt(PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
				y[i]=random.nextInt(PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
				
				if((x[i]==appleX && y[i]==appleY) || (x[i]==goodAppleX && y[i]==goodAppleY))
				{
					x[i]=random.nextInt(PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
					y[i]=random.nextInt(PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
				}
			}
		}
		else
		{
			if(!randomize)
			{
				for(i=bodyparts;i>0;i--)
				{
					x[i]=x[i-1];
					y[i]=y[i-1];
				}
			}
		}
		
		if(direction=='R')
		{
			x[0]=x[0]+UNIT_SIZE;
			if(x[0]==PANEL_WIDTH)
			{
				x[0]=0;
			}
		}
		if(direction=='L')
		{
			x[0]=x[0]-UNIT_SIZE;
			if(x[0]<0)
			{
				x[0]=PANEL_WIDTH-UNIT_SIZE;
			}
		}
		if(direction=='U')
		{
			y[0]=y[0]-UNIT_SIZE;
			if(y[0]<0)
			{
				y[0]=PANEL_HEIGHT-UNIT_SIZE;
			}
		}
		if(direction=='D')
		{
			y[0]=y[0]+UNIT_SIZE;
			if(y[0]==PANEL_HEIGHT)
			{
				y[0]=0;
			}
		}
	}

	public void checkApple()
	{
		if(x[0]==appleX && y[0]==appleY)
		{
			dismantle = false;
			randomize = false;
			appleseaten++;
			score++;
			bodyparts++;
			newApple();
		}
	}
	
	public void checkCollision()
	{
		for(i=1;i<bodyparts;i++)
		{
			if(x[0]==x[i] && y[0]==y[i])
			{
				running = false;
			}
		}
		if(!running)
			timer.stop();
	}
	
	public void gameOver(Graphics g)
	{
		g.setColor(Color.red);
		g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
		
		for(i=0;i<bodyparts;i++)
		{
			if(i==0)
			{
				g.setColor(new Color(0,120,212));
				g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
			}
			else
			{
				g.setColor(new Color(80,200,255));
				g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
			}
		}
		
		g.setColor(Color.yellow);
		g.setFont(new Font(null,Font.PLAIN,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("SCORE: "+score, (PANEL_WIDTH-metrics1.stringWidth("SCORE: "+score))/2, PANEL_HEIGHT*3/4);
		
		g.setColor(Color.red);
		g.setFont(new Font(null,Font.PLAIN,60));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (PANEL_WIDTH-metrics2.stringWidth("GAME OVER"))/2, PANEL_HEIGHT*2/5);
		
	}
	
	public void newGame()
	{
		if(score>highscore)
		{
			try
			{
				fout = new FileOutputStream(file);
				fout.write((int)score);
				fout.close();
			}
			catch (Exception e1)
			{
				
			}
		}
		startGame();
	}
	
	public void quitGame()
	{
		if(score>highscore)
		{
			try
			{
				fout = new FileOutputStream(file);
				fout.write((int)score);
				fout.close();
			}
			catch (Exception e1)
			{
				
			}
		}
		System.exit(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==timer)
		{
			move();
			checkApple();
			checkCollision();
			repaint();
			if(goodApplePresent)
			{
				checkGoodApple();
			}
			if(badApplePresent)
			{
				checkBadApple();
			}
		}
		if(e.getSource()==goodAppleTimer)
		{
			goodApplePresent = false; 
			goodAppleTimer.stop();
			newGoodApple();
		}
		if(e.getSource()==badAppleTimer)
		{
			badApplePresent = false; 
			badAppleTimer.stop();
			newBadApple();
		}
	}
	
	class MyKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			int z = e.getKeyCode();
			if(z==KeyEvent.VK_LEFT)
			{
				if(direction!='R' && timer.isRunning())
				{
					direction = 'L';
				}
			}
			if(z==KeyEvent.VK_RIGHT)
			{
				if(direction != 'L' && timer.isRunning())
				{
					direction = 'R';
				}
			}
			if(z==KeyEvent.VK_UP)
			{
				if(direction != 'D' && timer.isRunning())
				{
					direction = 'U';
				}
			}
			if(z==KeyEvent.VK_DOWN)
			{
				if(direction != 'U' && timer.isRunning())
				{
					direction = 'D';
				}
			}
			if(z==KeyEvent.VK_SPACE)
			{
				if(running)
				{
					if(timer.isRunning())
						timer.stop();
					else
						timer.start();

					if(goodAppleTimer.isRunning())
						goodAppleTimer.stop();
					else
						goodAppleTimer.start();
					
					if(badAppleTimer.isRunning())
						badAppleTimer.stop();
					else
						badAppleTimer.start();
				}
				else
				{
					newGame();
				}
			}
		}
	}
}
