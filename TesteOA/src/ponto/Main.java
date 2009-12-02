package ponto;

public class Main {
	
	 
	   public static void main(String[] args){
	      ClassePonto p1 = new ClassePonto();
	      System.out.println("p1 =" + p1);
	      p1.setRectangular(5,2);
	      System.out.println("p1 =" + p1);
	      p1.setPolar( Math.PI / 4.0 , 1.0);
	      System.out.println("p1 =" + p1);
	      p1.setPolar( 0.3805 , 5.385);
	      System.out.println("p1 =" + p1);
	   }
	   

}
