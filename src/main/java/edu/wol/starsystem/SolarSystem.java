package edu.wol.starsystem;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wol.TimeQueque;
import edu.wol.dom.WorldContainer;
import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.space.NewPosition;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Space;
import edu.wol.dom.time.iTimeManager;
import edu.wol.physics.starsystem.SolarSystemPhisycs;
import edu.wol.space.Orbital;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.50.16
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class SolarSystem extends WorldContainer<Planetoid,Position,Orbital<Planetoid>,SolarSystemPhisycs> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8786030279582189151L;
	final static Logger logger = LoggerFactory.getLogger(SolarSystem.class);
	
	private double radius;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "tmId", referencedColumnName = "ID")
	private TimeQueque<Planetoid> timeManager;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "spId", referencedColumnName = "ID")
    private Orbital<Planetoid> space;
	
    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "phId", referencedColumnName = "ID")
    private SolarSystemPhisycs phisycs;
    
    //@OneToMany
   // private Collection<WorldContainer<Planetoid,Position>> subWorlds=new ArrayList<WorldContainer<Planetoid,Position>>();

    public SolarSystem() {
	}
    
	@Override
	public void init(float spacePrecision, float timePrecision){
		if(timeManager==null){
    		timeManager = new TimeQueque<Planetoid>(timePrecision);
    	}
		
		if(space==null){
    		space=new Orbital<Planetoid>(spacePrecision, timeManager);
    	}
    	
    	if(phisycs==null){
    		phisycs = new SolarSystemPhisycs(space, timeManager);
    	}
        phisycs.addObserver(this);
        space.addObserver(phisycs);
        timeManager.addObserver(phisycs);
        System.out.println("StarsContainer successfull Initialized");
    }
    public void run() {
    	long startExecution = System.currentTimeMillis();
        phisycs.run(); //Implicy run time?
       
       /* for(WorldContainer<Planetoid,Position> subWold:subWorlds){
        	subWold.run();
        }*/
        long executionTime = System.currentTimeMillis() - startExecution;
        long sleepTime = (long) ((timeManager.getPrecision()*1000) - executionTime);
        if(sleepTime < 0){
        	logger.warn("--- Overloading system --- no realtime garantee");
        }else{
        	try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				System.out.println("Gently shutdown while sleeping");
			}
        }
    }
    
    public void setSpace(Orbital<Planetoid> space){
    	this.space=space;
    }
    
    public void insertEntity(Position position,Planetoid entity){
    		phisycs.insert(entity,position);
    		for(iEventObserver<Planetoid> observer : observers){
    			NewPosition<Planetoid> e = new NewPosition<Planetoid>(entity, position);
    			observer.processEvent(e);
    		}
    		/*if(entity instanceof WorldContainer){
    			subWorlds.add((WorldContainer<Planetoid,Position>) entity);
    		}*/
    }
    
    public Collection<Planetoid> getAllEntities(){
		return space.getAllEntities();
    }

    @Override
    public void processEvent(iEvent event) {
    	
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
	public void setPhisycs(SolarSystemPhisycs phisycs) {
		this.phisycs=phisycs;
	}
	
	public void setTimeManager(TimeQueque<Planetoid> tm) {
		this.timeManager=tm;
	}
	
	public iTimeManager<Planetoid> getTimeManager() {
		return timeManager;
	}
	
	@Override
	public Orbital<Planetoid> getSpace() {
		return space;
	}
	
	@Override
	public SolarSystemPhisycs getPhisycs() {
		return phisycs;
	}

	@Override
	public boolean isEmpty() {
		return space.isEmpty();
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public void addEventObserver(iEventObserver<Planetoid> observer) {
		this.observers.add(observer);	
	}
	
}
