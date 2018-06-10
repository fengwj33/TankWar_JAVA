import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Image;

public class Animation {
	public int w,h,l,s,x,y;
	public int frame=0;
	public int timer=0;
	public ArrayList<Image> frames=new ArrayList<Image>();
	public Animation(String filename,int length,int width,int height,int speed,int _x,int _y,objController _om)
	{
		x=_x;
		y=_y;
		l=length;
		w=width;
		h=height;
		s=speed;
		timer=s;
		for(int i=0;i<length;i++)
			frames.add(Toolkit.getDefaultToolkit().getImage(filename+i+".png"));
		frame=0;
		_om.animations.add(this);
		
	}
	public boolean update(Graphics g)
	{
		timer=timer-1;
		if(timer<=0)
		{
			frame+=1;
			timer=s;
		}
		if(frame>=l)
			return true;
		draw(g);
		return false;
		
	}
	public void draw(Graphics g){
		g.drawImage(frames.get(frame),x-w/2,y-h/2,w,h,null );
	}
}
