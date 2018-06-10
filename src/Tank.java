import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Tank  {
	float vx,vy;
	public int x,y;
	int mx,my;
	double angle=0;
	double gangle=0;
	public Image tank;
	public Image gun;
	public objController om;
	public boolean isplayer;
	public int blood=100;
	
	Random rand = new Random();
	public int ai_mx,ai_my;
	public int AItype=0;
	
	public Tank(int ix,int iy,objController _m,boolean _isplayer,int _blood, int ai){
		AItype=ai;
		vx=vy=0;
		x=ix;
		y=iy;
		om=_m;
		isplayer=_isplayer;
		blood=_blood;
		if(!isplayer)
		{
			om.enermies.add(this);
			ai_mx=rand.nextInt(800);
			ai_my=rand.nextInt(600);
			tank=Toolkit.getDefaultToolkit().getImage("tank_"+AItype+".png");
			gun=Toolkit.getDefaultToolkit().getImage("gun_"+AItype+".png");
			
		}
		else
		{
			tank=Toolkit.getDefaultToolkit().getImage("tank.png");
			gun=Toolkit.getDefaultToolkit().getImage("gun.png");
		}
	}
	public void addv(int dx,int dy){
		vx+=dx;
		vy+=dy;
	}
	public void update(Graphics g){
		
		boolean col=false;
		if(x<25){
			x=25;vx*=-1;
			col=true;
		}
		if(x>800-25){
			x=800-25;vx*=-1;
			col=true;
		}
		if(y<25){
			y=25;vy*=-1;
			col=true;
		}
		if(y>600-25){
			y=600-25;vy*=-1;
			col=true;
		}
		if(isplayer)
		{
			for (Tank t:om.enermies) {
				float dis=(float) Math.pow(t.x-x,2)+(float) Math.pow(t.y-y,2);
				if(dis<50*50)
				{
					vx=-vx*1.4f;
					vy=-vy*1.4f;
					col=true;
				}
			}
		}
		else
		{
			for (Tank t:om.enermies) {
				if(t==this) continue;
				float dis=(float) Math.pow(t.x-x,2)+(float) Math.pow(t.y-y,2);
				if(dis<50*50)
				{
					vx=-vx*1.4f;
					vy=-vy*1.4f;
					col=true;
				}
			}
			
		}
		
		
		x+=vx;
		y+=vy;
		vx*=0.8;
		vy*=0.8;
		
		if(vx*vx+vy*vy>50 && rand.nextInt(100)<60)
		{
			int r=rand.nextInt(50);
			new Animation("smoke", 4, 50+r, 50+r, 5, x, y, om);
		}
			
		
		if(isplayer)
		{
			if(om.shoot) 
			{
				om.shoot=false;
				new bullet(x, y, mx, my, om,true,Color.YELLOW,25);
				om.shakingtimer+=5;
						
			}
			if(om.shootEX)
			{
				om.shootEX=false;
				if(om.energy<10)
				{
					return;
				}
				om.energy-=10;
				for(int i=0;i<10;i++)
				{
					new bullet(x, y, mx+rand.nextInt(70)-35, my+rand.nextInt(70)-35, om,true,Color.YELLOW,20+rand.nextInt(10));
				}
				om.shakingtimer+=15;
			}
		}
		else {

			float dis=(float) Math.pow(ai_mx-x,2)+(float) Math.pow(ai_my-y,2);
			dis=(float) Math.sqrt(dis);
			int SPEED=2;
			int dvx=(int) ((ai_mx-x)*SPEED/dis);
			int dvy=(int) ((ai_my-y)*SPEED/dis);
			addv(dvx, dvy);
			if(col || dis<80)
			{
				ai_mx=rand.nextInt(800);
				ai_my=rand.nextInt(600);
			}
			Tank t=om.player;
			Aimposition(t.x, t.y);
			if(AItype==1)
				if (rand.nextInt(100)<3)
					new bullet(x, y, mx, my, om,false,Color.red,25);
			if(AItype==2)
			{
				if (rand.nextInt(100)<2)
				{
					for(int i=0;i<7;i++)
						new bullet(x, y, mx+rand.nextInt(100)-50, my+rand.nextInt(100)-50, om,false,Color.red,20);
					
				}
			}
			if(AItype==3)
			{
				if (rand.nextInt(100)<5)
				{
					
					new bullet(x, y, mx+rand.nextInt(100)-50, my+rand.nextInt(100)-50, om,false,Color.red,40);
					
				}
			}
			if(AItype==4)
			{
				if (rand.nextInt(100)<1)
				{
					for(int i=0;i<20;i++)
						new bullet(x, y, mx+rand.nextInt(100)-50, my+rand.nextInt(100)-50, om,false,Color.red,15+rand.nextInt(10));
					
				}
			}
			
				
		}
 
		draw(g);
		
	}
	public void Aimposition(int _mx,int _my) {
		mx=(int) (mx+(float)(_mx-mx)*0.25);
		my=(int) (my+(float)(_my-my)*0.25);
		
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillOval(x-10, y-10, 20, 20);
		g.setColor(c);
		
		//float r=(float) Math.sqrt(vx*vx+vy*vy);
		//float angle=(float) Math.asin(vy/r);
		if(vx!=0)
			angle=Math.atan((float)vy/vx);
		else {
			if(vy>0)
				angle=3.14/2;
			else
				angle=-3.14/2;
		}
		if(vx<0)
		{
			angle=-3.14+angle;
		}
		int dx,dy;
		dx=mx-x;
		dy=my-y;
		if(dx!=0)
			gangle=Math.atan((float)dy/dx);
		else {
			if(dy>0)
				gangle=3.14/2;
			else
				gangle=-3.14/2;
		}
		if(dx<0)
		{
			gangle=-3.14+gangle;
		}
		
		
			
		Graphics2D g2 = (Graphics2D) g;
        g2.translate(x, y);
        g2.rotate(angle);
        g2.drawImage(tank, 0-25,0-25,50,50,null);
        g2.rotate(-angle);
        g2.rotate(gangle);
        g2.drawImage(gun, 0-25,0-25,50,50,null);
        g2.rotate(-gangle);
        g2.translate(-x,-y);
        //g2.drawImage(image,imageX,imageY,imageW,imageH,null);
		
		
		
	}
	

}
