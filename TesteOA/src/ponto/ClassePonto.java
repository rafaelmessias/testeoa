package ponto;

public class ClassePonto {
		
	   protected double x = 0;
	   protected double y = 0;
	   protected double theta = 0;
	   protected double rho = 0;

	   protected boolean polar = true;
	   protected boolean rectangular = true;
	   
	   public void setX(double x){
		   this.x = x;
	   }
	   
	   public void setY(double y){
		   this.y = y;
	   }

	   public double getX(){
	      makeRectangular();
	      return x;
	   }

	   public double getY(){
	      makeRectangular();
	      return y;
	   }

	   public double getTheta(){
	      makePolar();
	      return theta;
	   }

	   public double getRho(){
	      makePolar();
	      return rho;
	   }

	   public void setRectangular(double newX, double newY){
	      x = newX;
	      y = newY;
	      rectangular = true;
	      polar = false;
	   }

	   public void setPolar(double newTheta, double newRho){
	      theta = newTheta;
	      rho = newRho;
	      rectangular = false;
	      polar = true;
	   }

	   public void rotate(double angle){
	      setPolar(theta + angle, rho);
	   }

	  protected void makePolar(){
	      if (!polar){
		 theta = Math.atan2(y,x);
		 rho = y / Math.sin(theta);
		 polar = true;
	      }
	   }

	   protected void makeRectangular(){
	      if (!rectangular) {
		 x = rho * Math.sin(theta);
		 y = rho * Math.cos(theta);
		 rectangular = true;
	      }
	   }

	   public String toString(){
	      return "(" + getX() + ", " + getY() + ")[" 
		 + getTheta() + " : " + getRho() + "]";
	   }

	  
	   
}
