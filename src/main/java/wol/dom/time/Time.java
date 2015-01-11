package wol.dom.time;

import java.io.Serializable;

import wol.dom.Entity;
import wol.dom.iExternalCause;

/**
 * Created with IntelliJ IDEA.
 * User: cesare
 * Date: 20/08/12
 * Time: 14.02
 * To change this template use File | Settings | File Templates.
 */
public class Time implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected long time;
	private iTimeManager<?> timeManager;

    Time(iTimeManager<?> tm,long time){
    	this.timeManager=tm;
        this.time = time;
    }

    public long getTime(){
        return time;
    }
}
