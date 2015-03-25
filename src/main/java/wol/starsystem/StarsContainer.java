package wol.starsystem;

import wol.dom.iEvent;
import wol.physics.starsystem.SolarSystemPhisycs;
import wol.starsystem.planets.Cosmos;
import wol.dom.WorldContainer;
import wol.dom.space.Position;
import wol.dom.space.iCoordinate;
import wol.dom.space.iPlanetEntity;
import wol.dom.space.iPlanetoid;
import wol.dom.space.iSpace;
import wol.dom.time.TimeQueque;
import wol.dom.time.iTimeManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 31-gen-2010
 * Time: 23.50.16
 * To change this template use File | Settings | File Templates.
 */
public class StarsContainer implements WorldContainer<iPlanetoid,Position> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8786030279582189151L;
	private iTimeManager<iPlanetoid> timeManager;
    private Cosmos space;
    private SolarSystemPhisycs phisycs;
    private Collection<WorldContainer<iPlanetEntity,Position>> subWorlds=new ArrayList<WorldContainer<iPlanetEntity,Position>>();

    public StarsContainer() {
	}
    
	@Override
	public void init(float spacePrecision, float timePrecision){
    	if(space==null){
    		space=new Cosmos();
    	}
    	if(timeManager==null){
    		timeManager = new TimeQueque<iPlanetoid>();
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
        for(WorldContainer<iPlanetEntity,Position> subWold:subWorlds){
        	subWold.run();
        }
    }
    
    public void setSpace(Cosmos space){
    	this.space=space;
    }
    
    public void insertEntity(iCoordinate coordinate,iPlanetoid entity){
    		phisycs.insert(entity,(Position) coordinate);
    		if(entity instanceof WorldContainer){
    			subWorlds.add((WorldContainer<iPlanetEntity,Position>) entity);
    		}
    }
    
    public Collection<iPlanetoid> getAllEntities(){
		return space.getAllEntities();
    }

    @Override
    public void processEvent(iEvent event) {
    	
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
	public void setPhisycs(SolarSystemPhisycs phisycs) {
		this.phisycs=phisycs;
	}
	
	public void setTimeManager(iTimeManager<iPlanetoid> tm) {
		this.timeManager=tm;
	}
	
	public iTimeManager<iPlanetoid> getTimeManager() {
		return timeManager;
	}
	
	@Override
	public iSpace<iPlanetoid,Position> getSpace() {
		return space;
	}
	
	public SolarSystemPhisycs getPhisycs() {
		return phisycs;
	}
	
}
