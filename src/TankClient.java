import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JOptionPane;



public class TankClient extends Frame implements MouseListener,MouseMotionListener {
	int x = 100;

	int y = 100;

	int GAME_WIDTH = 800;

	int GAME_HEIGHT = 600;

	int offx;

	int offy;

	Image off;
	Tank player;
	Image background;
	Image background2;
	public objController objmanager=new objController();
	public int mousepx=0,mousepy=0;
	public int SPEED = 2;
	Random rand = new Random();
	@Override
	public void update(Graphics g) {
		// double buffering
		// 1. 得到缓冲图象
		if (this.off == null) {
			this.off = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		// 2. 得到缓冲图象的画笔
		Graphics _g = this.off.getGraphics();
		// 3. 绘制缓冲图象

		_g.setColor(Color.BLACK);
		_g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		if(objmanager.shakingtimer!=0)
			_g.translate(rand.nextInt(objmanager.shakingtimer), rand.nextInt(objmanager.shakingtimer));
		_g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		// 4. 调用paint(),将缓冲图象的画笔传入
		//paint(_g);
		if(player.blood<=0&&player.blood!=-128)
		{
			_g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
			_g.setColor(Color.RED);
			_g.drawString("GameOver", 350, 100);
			_g.drawString("YourScore:"+objmanager.Score, 350, 130);
			g.drawImage(this.off, 0, 0, null);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			player.blood=-128;
			

			return;
		}
		if(player.blood==-128)
		{
			_g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
			_g.drawImage(background2, -20,0, 975,611, null);
			_g.setColor(Color.RED);
			_g.drawString("GameOver", 350, 100);
			_g.drawString("YourScore:"+objmanager.Score, 350, 130);
			_g.drawString("Press Your Mouse To ReStart", 350, 160);
			g.drawImage(this.off, 0, 0, null);
			if(objmanager.shoot)
			{
				objmanager.enermies.clear();
				objmanager.bullets.clear();
				player.blood=100;
				player.x=400;
				player.y=300;
				objmanager.Score=0;
				objmanager.energy=50;
			}
			objmanager.shakingtimer=20;
			return;
		}
		_g.setColor(Color.RED);
		_g.drawString("Score:"+objmanager.Score, 600, 70);
		_g.setColor(Color.GRAY);
		_g.fillRect(20, 60, 500, 20);
		_g.setColor(Color.RED);
		_g.fillRect(20, 60, 500*player.blood/100, 20);
		_g.setColor(Color.GRAY);
		_g.fillRect(20, 80, 500, 20);
		_g.setColor(Color.BLUE);
		_g.fillRect(20, 80, 500*objmanager.energy/100, 20);
		
		objmanager.updateAnimation(_g);

		if(objmanager.Score<3)
		{
			if (rand.nextInt(1000)<20)
				new Tank(rand.nextInt(800),rand.nextInt(600),objmanager,false,rand.nextInt(1)+1,rand.nextInt(2)+1);
		}
		else if(objmanager.Score<5)
		{
			if (rand.nextInt(1000)<20)
				new Tank(rand.nextInt(800),rand.nextInt(600),objmanager,false,rand.nextInt(2)+1,rand.nextInt(3)+1);
		}
		else if(objmanager.Score<8)
		{
			if (rand.nextInt(1000)<30)
				new Tank(rand.nextInt(800),rand.nextInt(600),objmanager,false,rand.nextInt(2)+2,rand.nextInt(4)+1);
		}
		else {
			if (rand.nextInt(1000)<12)
				new Tank(rand.nextInt(800),rand.nextInt(600),objmanager,false,rand.nextInt(2)+3,rand.nextInt(4)+1);
		}
		player.addv(offx,offy);
		player.Aimposition(mousepx, mousepy);
		player.update(_g);
		
		objmanager.updateEnermies(_g);
		objmanager.updateBullets(_g);
		
		objmanager.shaking();
		
		// 5. 再将此缓冲图像一次性绘到代表屏幕的Graphics对象，即该方法传入的“g”上.
		g.drawImage(this.off, 0, 0, null);
	}

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
		// new Thread(tc.new PaintThread()).start();
	}

	//@Override
//	public void paint(Graphics g) {
//		Color c = g.getColor();
//		g.setColor(Color.red);
//		g.fillOval(x += offx, y += offy, 50, 50);
//		g.setColor(c);
//		// y+=1;
//	}

	private void launchFrame() {
		this.setBounds(100, 100, GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.setBackground(Color.BLACK);
		this.setResizable(false);
		this.addKeyListener(new KeyMoniter());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				TankClient.this.setVisible(false);
				System.exit(0);
			}
		});
		this.setVisible(true);
		// new Thread(this.new PaintThread()).start();
		new Thread(new PaintThread()).start();
	}

	class PaintThread implements Runnable {
		
		public void run() {
			player=new Tank(400,300,objmanager,true,100,0);
			objmanager.player=player;
			objmanager.Score=0;
			objmanager.energy=50;
			background=Toolkit.getDefaultToolkit().getImage("background.png");
			background2=Toolkit.getDefaultToolkit().getImage("timg.jpg");
			while (true) {
				TankClient.this.repaint();
				
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class KeyMoniter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			//System.out.println(key);
			if(key==KeyEvent.VK_LEFT)
				offx = -SPEED;
			if(key==KeyEvent.VK_UP)
				offy = -SPEED;
			if(key==KeyEvent.VK_RIGHT)
				offx = SPEED;
			if(key==KeyEvent.VK_DOWN)
				offy = SPEED;
			/*switch (key) {
			case KeyEvent.VK_LEFT:
				offx = -SPEED;
				break;
			case KeyEvent.VK_UP:
				offy = -SPEED;
				break;
			case KeyEvent.VK_RIGHT:
				offx = SPEED;
				break;
			case KeyEvent.VK_DOWN:
				offy = SPEED;
				break;

			default:
				break;
			}*/
		} 

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			//System.out.println(key + ".....");
			if(key==KeyEvent.VK_LEFT)
				offx = 0;
			if(key==KeyEvent.VK_UP)
				offy = 0;
			if(key==KeyEvent.VK_RIGHT)
				offx = 0;
			if(key==KeyEvent.VK_DOWN)
				offy = 0;
			/*switch (key) {
			case KeyEvent.VK_LEFT:
				offx = 0;
				break;
			case KeyEvent.VK_UP:
				offy = 0;
				break;
			case KeyEvent.VK_RIGHT:
				offx = 0;
				break;
			case KeyEvent.VK_DOWN:
				offy = 0;
				break;

			default:
				break;
			}*/
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.mousepx=arg0.getX();
		this.mousepy=arg0.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.mousepx=arg0.getX();
		this.mousepy=arg0.getY();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getButton()==MouseEvent.BUTTON1)
			objmanager.shoot=true;
		if(arg0.getButton()==MouseEvent.BUTTON3)
			objmanager.shootEX=true;
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		objmanager.shoot=false;
		
	}
}
