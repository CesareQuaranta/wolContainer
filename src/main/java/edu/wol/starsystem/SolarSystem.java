package edu.wol.starsystem;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.wol.TimeQueque;
import edu.wol.dom.WolEntity;
import edu.wol.dom.WorldContainer;
import edu.wol.dom.iEvent;
import edu.wol.dom.iEventObserver;
import edu.wol.dom.space.NewPosition;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Space;
import edu.wol.dom.space.iCoordinate;
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
public class SolarSystem extends WorldContainer<Planetoid,Position> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8786030279582189151L;
	private double radius;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "tmId", referencedColumnName = "ID")
	private TimeQueque<Planetoid> timeManager;
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "spId", referencedColumnName = "ID")
    private Orbital space;
    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "phId", referencedColumnName = "ID")
    private SolarSystemPhisycs phisycs;
    
    //@OneToMany
   // private Collection<WorldContainer<Planetoid,Position>> subWorlds=new ArrayList<WorldContainer<Planetoid,Position>>();

    public SolarSystem() {
	}
    
	@Override
	public void init(float spacePrecision, float timePrecision){
    	if(space==null){
    		space=new Orbital();
    	}
    	if(timeManager==null){
    		timeManager = new TimeQueque<Planetoid>();
    	}
    	if(phisycs==null){
    		phisycs = new SolarSystemPhisycs(space, timeManager, spacePrecision,timePrecision);
    	}
        phisycs.addObserver(this);
        space.addObserver(phisycs);
        timeManager.addObserver(phisycs);
        System.out.println("StarsContainer successfull Initialized");
    }
    public void run() {
        phisycs.run();
       /* for(WorldContainer<Planetoid,Position> subWold:subWorlds){
        	subWold.run();
        }*/
    }
    
    public void setSpace(Orbital space){
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
	public Space<Planetoid,Position> getSpace() {
		return space;
	}
	
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
