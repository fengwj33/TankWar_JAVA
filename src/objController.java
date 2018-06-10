import java.awt.Graphics;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class objController {
	public boolean shoot=false;
	public boolean shootEX=false;
	public Tank player;
	Set<bullet> bullets = new HashSet<bullet>();
	Set<bullet> removelist = new HashSet<bullet>();
	Set<Tank> enermies =new HashSet<Tank>();
	Set<Tank> rm_enermies =new HashSet<Tank>();
	//Set<Animation> animations=new HashSet<Animation>();
	ArrayList<Animation> animations=new ArrayList<Animation>();
	Set<Animation> rm_animations=new HashSet<Animation>();
	public int shakingtimer=0;
	public int Score;
	public int energy;
	public void updateBullets(Graphics g)
	{
		removelist.clear();
		boolean flag;
		for (bullet b : bullets) {  
		      flag=b.update(g);
		      if(flag)
		    	  removelist.add(b);
		}  
		for (bullet b : removelist) {  
			bullets.remove(b);
		}
	}
	public void updateAnimation(Graphics g)
	{
		rm_animations.clear();
		boolean flag;
		for (Animation b : animations) {  
		      flag=b.update(g);
		      if(flag)
		    	  rm_animations.add(b);
		}  
		for (Animation b : rm_animations) {  
			animations.remove(b);
		}
	}
	public void updateEnermies(Graphics g)
	{
		rm_enermies.clear();
		for (Tank t : enermies) {  
		      if(t.blood<=0)
		      {
		    	  rm_enermies.add(t);
		    	  new Animation("ash", 2, 300, 300, 200, t.x, t.y, this);
		    	  new Animation("boom", 13, 200, 200, 2, t.x, t.y, this);
		    	  
		    	  shakingtimer+=20;
		    	  energy+=2;
		    	  if(energy>100)
		    		  energy=100;
		    	  if(player.blood!=100)
		    		  player.blood++;
		    	  Score++;
		      }
		      else
		      {
		    	  t.update(g);
		      }
		}  
		for (Tank t : rm_enermies)
		{
			enermies.remove(t);
		}
		
	}
	public void shaking()
	{
		if (shakingtimer>0)
			shakingtimer-=1;
		if(shakingtimer>20)
			shakingtimer=20;
	}
}
