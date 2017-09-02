package edu.wol.starsystem;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
import edu.wol.dom.phisycs.iPhisycs;
import edu.wol.dom.space.Planetoid;
import edu.wol.dom.space.Position;
import edu.wol.dom.space.Space;
import edu.wol.dom.space.iCoordinate;
import edu.wol.dom.time.iTimeManager;
import edu.wol.physics.BasePhisycs;
import edu.wol.physics.starsystem.SolarSystemPhisycs;
import edu.wol.space.Interstellar;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.50.16
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class StarDial extends WorldContainer<SolarSystem,Position,Interstellar,iPhisycs<SolarSystem>> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8786030279582189151L;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "tmId", referencedColumnName = "ID")
	private TimeQueque<Planetoid> timeManager;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "spId", referencedColumnName = "ID")
    private Interstellar space;
    
	//@OneToOne(cascade=CascadeType.ALL)
    //@JoinColumn(name = "phId", referencedColumnName = "ID")
    //TDOD private SolarSystemPhisycs phisycs;
    
    //@OneToMany
   // private Collection<WorldContainer<Planetoid,Position>> subWorlds=new ArrayList<WorldContainer<Planetoid,Position>>();

    public StarDial() {
	}
    
	@Override
	public void init(float spacePrecision, float timePrecision){
    	if(space==null){
    		space=new Interstellar();
    	}
    	if(timeManager==null){
    		timeManager = new TimeQueque<Planetoid>(timePrecision);
    	}
    	/*TODO
    	if(phisycs==null){
    		phisycs = new SolarSystemPhisycs(space, timeManager, spacePrecision,timePrecision);
    	}
        /phisycs.addObserver(this);
        space.addObserver(phisycs);
        timeManager.addObserver(phisycs);
        */
        System.out.println("StarsContainer successfull Initialized");
    }
    public void run() {
        //TODO phisycs.run();
       /* for(WorldContainer<Planetoid,Position> subWold:subWorlds){
        	subWold.run();
        }*/
    }
    
    public void setSpace(Interstellar space){
    	this.space=space;
    }
    
    public void insertEntity(Position position,SolarSystem entity){
    		//TODO phisycs.insert(entity,(Position) coordinate);
    		/*if(entity instanceof WorldContainer){
    			subWorlds.add((WorldContainer<Planetoid,Position>) entity);
    		}*/
    }
    
    public Collection<SolarSystem> getAllEntities(){
		return space.getAllEntities();
    }

    @Override
    public void processEvent(iEvent event) {
    	
        //To change body of implemented methods use File | Settings | File Templates.
    }
    /*TODO
	public void setPhisycs(SolarSystemPhisycs phisycs) {
		this.phisycs=phisycs;
	}
	public SolarSystemPhisycs getPhisycs() {
		return phisycs;
	}
	*/
	
	public void setTimeManager(TimeQueque<Planetoid> tm) {
		this.timeManager=tm;
	}
	
	public iTimeManager<Planetoid> getTimeManager() {
		return timeManager;
	}
	
	@Override
	public Interstellar getSpace() {
		return space;
	}
	
	@Override
	public boolean isEmpty() {
		return space.isEmpty();
	}

	@Override
	public void addEventObserver(iEventObserver<SolarSystem> observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public iPhisycs<SolarSystem> getPhisycs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
