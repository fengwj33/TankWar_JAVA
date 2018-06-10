import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.Printable;

public class bullet {
	int x,y;
	float vx,vy;
	objController om;
	boolean isplayer;
	Color color;
	public bullet(int _x,int _y,int _ax,int _ay,objController _om,boolean _isplayer,Color _c,int SPEED) {
		isplayer=_isplayer;
		om=_om;
		om.bullets.add(this);
		x=_x;
		y=_y;
		float dis=(float) Math.pow(_ax-x,2)+(float) Math.pow(_ay-y,2);
		dis=(float) Math.sqrt(dis);
		//System.out.println(dis);
		vx=(int) ((_ax-x)*SPEED/dis);
		vy=(int) ((_ay-y)*SPEED/dis);
		color=_c;
	}
	public boolean update(Graphics g)
	{
		x+=vx;
		y+=vy;
		if(x<0 || x>800 || y<0 || y >600)
		{
			//System.out.println("removed");
			return true;
		}
		else
		{
			if(isplayer)
			{
				for(Tank t:om.enermies)
				{
					float dis=(float) Math.pow(t.x-x,2)+(float) Math.pow(t.y-y,2);
					if(dis<25*25)
					{
						t.blood-=1;
						t.addv((int)vx/3,(int)vy/3);
						return true;
					}
				}
			}
			else
			{
				Tank t=om.player;
				float dis=(float) Math.pow(t.x-x,2)+(float) Math.pow(t.y-y,2);
				if(dis<25*25)
				{
					t.blood-=1;
					t.addv((int)vx/5,(int)vy/5);
					new Animation("effect", 4, 800, 600, 5, 400, 300, om);
					return true;
				}
			}
			
		}
		draw(g);
		return false;
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(color);
	
		//System.out.print(x);
		g.fillOval(x-6, y-6, 12, 12);
		g.setColor(c);
	}
}
