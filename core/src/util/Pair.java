package util;



public class Pair {
	public static int pairsMade=0;
	public float x,y;
	public Pair(float x, float y){
		this.x=x;
		this.y=y;
		pairsMade++;
	}
	public Pair(double x, double y){
		this.x=(float) x;
		this.y=(float) y;
		pairsMade++;
	}
	public Pair(float x, double y){
		this.x=(float) x;
		this.y=(float) y;
		pairsMade++;
	}
	public Pair(double x, float y){
		this.x=(float) x;
		this.y=(float) y;
		pairsMade++;
	}
	public Pair(){
		pairsMade++;
	}
	public static Pair getUnitVector(float radians){
		return new Pair(Math.cos(radians), Math.sin(radians));
	}
	public static Pair getVector(Pair from, Pair to){
		return new Pair(to.x-from.x,to.y-from.y);
	}
	public float getDistance(){
		return (float) Math.sqrt(x*x+y*y);
	}
	public float getDistance(Pair p){
		float xDist=Math.abs(x-p.x);
		float yDist=Math.abs(y-p.y);
		return (float) Math.sqrt(xDist*xDist+yDist*yDist);
	}
	public float getFastDistanceRatio(Pair p){
		float xDist=Math.abs(x-p.x);
		float yDist=Math.abs(y-p.y);
		return xDist*xDist+yDist*yDist;
	}
	public Pair normalise(){
		float dist = getDistance();
		return new Pair(x=x/dist,y=y/dist);
	}
	public void rotate(){
		float tempy=y;
		y=-x;
		x=tempy;
	}
	
	public Pair rotate(double radians){
		float newX=(float) (x*Math.cos(radians)-y*Math.sin(radians));
		float newY=(float) (x*Math.sin(radians)+y*Math.cos(radians));
		return new Pair(newX, newY);
	}
	public Pair multiply(float amount){
		return new Pair(x*amount,y*amount);
	}
	public Pair multiply(Pair s){
		return new Pair(x*s.x,y*s.y);
	}
	public Pair subtract(Pair s){
		return new Pair(x-s.x,y-s.y);
	}
	public Pair add(float x, float y){
		return new Pair(this.x+x,this.y+y);
	}
	public Pair add(Pair s){
		return new Pair(x+s.x,y+s.y);
	}
	public String toString(){
		return x+":"+y;
	}
	public static Pair randomUnitVector(){
		float dx=(float) (Math.random()*2-1);
		float dy=(float) Math.sqrt(1-dx*dx);
		if(Math.random()>.5){
			dy=-dy;
		}
		return new Pair(dx,dy);
	}
	public static Pair randomAnyVector(){
		float dx=(float) (Math.random()*2-1);
		float dy=(float) (Math.random()*2-1);
		return new Pair(dx,dy);
	}
	public static Pair randomLocation(float startX, float startY, float width, float height){
		return new Pair(startX+(float)Math.random()*width,startY+(float)Math.random()*height);
	}
	public Pair copy() {
		return new Pair(x,y);
	}
	public float getAngle(Pair to){
		float result=(float) Math.atan2(to.y-y, to.x-x);
		while(result<0)result+=Math.PI*2;
		return result;
	}
	public float getAngle(){
		float result= (float) Math.atan2(y, x);
		while(result<0)result+=Math.PI*2;
		return result;
	}
	public Pair floor() {
		return new Pair((int)x,(int)y);
	}
	public Pair squared() {
		return new Pair(x*x,y*y);
	}
	public Pair absolute() {
		return new Pair(Math.abs(x),Math.abs(y));
	}
	public Pair ceil() {
		return new Pair((int)Math.ceil(x),(int)Math.ceil(y));
	
	}
	public Pair round() {
		return new Pair((int)Math.round(x),(int)Math.round(y));
	}
	
	public float dotProduct(Pair n) {
		return x*n.x+y*n.y;
	}
	
	public float distanceToLine(Line line){

		
		Pair p=this;
		Pair a=line.point;
		Pair n=line.direction;
		Pair aMinusP=a.subtract(p);
		
		if(aMinusP.dotProduct(n)>0){
			//System.out.println("it's backwards");
			return 100000;
		}
		
		//(a-p).n is -t. t must be pos;
		//(a-p)-n((a-p).n)
		Pair distance  = aMinusP.subtract(
				n.multiply(aMinusP.dotProduct(n)));
		distance=new Pair(Math.abs(distance.x),Math.abs(distance.y));

		/*System.out.println("Line starting at "+a+" and going "+n+" and "+p);
		System.out.println("thie distance vector is "+distance);*/

		return (float) Math.sqrt(distance.x*distance.x+distance.y*distance.y);
	}
	
}