package ponto;

//import ClassePonto;

public aspect Assertions {
	
	private boolean ClassePonto.assertX(double x) {
		if (x <= 100.0 && x >= 0.0)
			return (true);
		else return(false);
	}
	
	private boolean ClassePonto.assertY(double y) {
		if (y <= 100.0 && y >= 0.0)
			return (true);
		else return(false);
	}
	
	before(ClassePonto p, double x): target(p) && args(x) && call(void setX(double)) {
		if (!p.assertX(x)) {
			System.out.println("Valor ilegal para x");
			return;
		}
	}
	
	after(ClassePonto p, double x): target(p) && args(x) && call(void setX(double)) {
		if (!p.assertX(x)) {
			p.setX(0);
			return;
		}
	}
	
	before(ClassePonto p, double y): target(p) && args(y) && call(void setY(double)) {
		if (!p.assertY(y)) {
			System.out.println("Valor ilegal para y");
			return;
		}
	}
	
	after(ClassePonto p, double y): target(p) && args(y) && call(void setY(double)) {
		if (!p.assertY(y)) {
			p.setY(0);
			return;
		}
	}


}
