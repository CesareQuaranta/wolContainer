package wol.starsystem.physics;

import wol.dom.phisycs.Acceleration;
import wol.dom.phisycs.Force;
import wol.dom.phisycs.ForceFactory;
import wol.dom.phisycs.iForceElements;
import wol.dom.phisycs.iMassEntity;
import wol.dom.space.BigVector;
import wol.dom.space.Position;
import wol.dom.space.Vector;
import wol.starsystem.planets.iPlanetoid;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 17/08/13
 * Time: 15.55
 * To change this template use File | Settings | File Templates.
 */
public class GravityField implements ForceFactory {
	private static final long serialVersionUID = 1L;
	public static final float GRAVITATIONAL_CONSTANT = 6.6742e-11f;
	private Position center;
	private double mass;
	
	public GravityField(Double mass, Position position) {
		this.center=position;
		this.mass=mass;
	}

	 private Vector processGravityVector(iPlanetoid planet){
	        Vector gravityVector=null;
	      /*  Vector planetPosition=index.get(planet);
	        List<Vector> gravityVectors=new ArrayList<Vector>(0);
	        for(Vector curGravityPoint:gravityPoint.keySet()){
	            Long magnitudo=gravityPoint.get(curGravityPoint);
	            if(!curGravityPoint.equals(planetPosition)&&curGravityPoint.getDistance(planetPosition)<magnitudo){
	                double intensity=(magnitudo-curGravityPoint.getDistance(planetPosition))/magnitudo;
	               //TODO processare il gravity vector in maniera che sia significativo dell'effatto di gravità, vicino tutto il vettore di differenza, lontano solo una piccola parte del vettore di differenza
	                gravityVectors.add(curGravityPoint.multiply(intensity));
	            }
	        }
	        if(!gravityVectors.isEmpty()){
	            gravityVector=new Vector();
	            for(Vector curGravityPoint:gravityVectors){
	                //Add Vector
	                //TODO verificare che la somma dei vettori sia quello che ci serve per simulare più forze convergenti divergenti che generano un unica forza/vettore
	                gravityVector.sum(curGravityPoint);
	            }
	        }*/

	        return gravityVector;
	    }

	public double getRadius(double mass) {
		double gravityBaseIntensity=this.mass*mass*GRAVITATIONAL_CONSTANT;
		return Math.sqrt(gravityBaseIntensity);
	}

	public Force getForce(iMassEntity entity,BigVector distance) {
		GravityForceElements elements=new GravityForceElements(entity,distance);
		return getForce(elements);
	}
	
	@Override
	public Force getForce(iForceElements elements) {
			double entityMass=((GravityForceElements)elements).getEntity().getMass();
			BigVector distance=((GravityForceElements)elements).distance;
			double gravityBaseIntensity=mass*GRAVITATIONAL_CONSTANT;
			long distanceX=(long) distance.getX();
			long distanceY=(long) distance.getY();
			long distanceZ=(long) distance.getZ();
			float accelerationX=(distanceX==0?0:Math.copySign((float)(gravityBaseIntensity/Math.pow(distanceX, 2)),distanceX));
			float accelerationY=(distanceY==0?0:Math.copySign((float)(gravityBaseIntensity/Math.pow(distanceY, 2)),distanceY));
			float accelerationZ=(distanceZ==0?0:Math.copySign((float)(gravityBaseIntensity/Math.pow(distanceZ, 2)),distanceZ));
			Acceleration gravityAcceleration=new Acceleration(1,new Vector(accelerationX,accelerationY,accelerationZ));
			return new Force(entityMass,gravityAcceleration);
	}
	
	public class GravityForceElements implements iForceElements<iMassEntity>{
		private iMassEntity entity;
		private BigVector distance;
		public GravityForceElements(iMassEntity entity, BigVector distance) {
			super();
			this.entity = entity;
			this.distance = distance;
		}
		public iMassEntity getEntity() {
			return entity;
		}
		public BigVector getDistance() {
			return distance;
		}
		
	}

	public Position getCenter() {
		return center;
	}

	public void setCenter(Position center) {
		this.center = center;
	}

	@Override
	public String toString() {
		return "G ["
				+ (center != null ? "center=" + center + ", " : "") + "mass="
				+ mass + "]";
	}




	
	
}
