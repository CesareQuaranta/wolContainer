package wol.dom.space.geometric;

import java.util.Collection;

import wol.dom.Entity;
import wol.dom.iEvent;
import wol.dom.Phenomen;
import wol.dom.iEventObserver;
import wol.dom.phisycs.Force;
import wol.dom.phisycs.ForceFactory;
import wol.dom.space.Vector;
import wol.dom.space.Movement;
import wol.dom.space.iSpace;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 26-mag-2010
 * Time: 0.24.30
 * Simple Flat Space.
 * Only an Object at time can have e slot.
 * Moving out the limit cause return to the specular opposit place.
 */
public class FlatSphere implements iSpace<Entity,Vector>{
   /* protected List<List<C>> matrix;
    protected Integer radius;
    protected Integer maxY;
    protected Integer[] maxX;

    public FlatSphere(int radius){
             //Calculate slot
        //FirstSlot 4 entry
        //y=sqrt(r^2-4)
        this.radius=radius;
        this.maxY=(int)sqrt((radius^2)-4)*2;
        this.maxX=new Integer[maxY];
        int i=0;
        matrix = new ArrayList<List<C>>(maxY);
        for (int y=0;y<radius;y++){
            int curMaxX=(int)sqrt((radius^2)-(y^2));
            if (curMaxX>=4){
                maxX[i]=curMaxX;
                maxX[maxY-i]=curMaxX;
                matrix.add(i,new ArrayList<C>(curMaxX));
                matrix.add(maxY-i,new ArrayList<C>(curMaxX));
                i++;
            }
        }
        /*
        y = sqrt(r^2 -( x - x0)^2) + y0
        void imel_draw_circle (ImelImage *image, size x, size y, size radius, ImelPixel pxl)
{
size sx = x - radius - 1, ex = x + radius;
double py;

return_if_fail (image);

while ( ++sx < ex ) {
         py = ((double) sqrt (pow (((double) radius), 2) - pow (((double) sx - x), 2)) + ((double) y));

         if ( py > -1 && py < image->height )
              imel_pixel_copy (&(image->pixel[(size) py][sx]), pxl);
}
}

double tmp;
while ( ++sx <= ex ) {
         tmp = (double) sqrt (pow (((double) radius), 2) - pow (((double) sx - x), 2));
         py1 = ((double) y) + tmp;
         py2 = ((double) y) - tmp;

         //e poi li disegni entrambi
}


void imel_draw_circle (ImelImage *image, size x, size y, size radius, ImelPixel pxl)
{
size sx = x - radius - 1, ex = x + radius + 1, py[2];
int po[2] = { -1, -1 };
double p;

return_if_fail (image);

while ( ++sx < ex ) {
         p = sqrt (pow (((double) radius), 2) - pow (((double) sx - x), 2));

         py[0] = y + ((size) p);
         if ( po[0] == -1 && py[0] < image->height ) {
              imel_pixel_copy (&(image->pixel[py[0]][sx]), pxl);
         }
         else imel_draw_line (image, sx - 1, (po[0] < py[0]) ? po[0] : py[0], sx,
                              (po[0] > py[0]) ? po[0] : py[0], pxl);
         po[0] = py[0];

         py[1] = y - ((size) p);
         if ( po[1] == -1 && py[1] < image->height ) {
              imel_pixel_copy (&(image->pixel[py[1]][sx]), pxl);
         }
         else imel_draw_line (image, sx - 1, (po[1] < py[1]) ? po[1] : py[1], sx,
                              (po[1] > py[1]) ? po[1] : py[1], pxl);
         po[1] = py[1];
}
}
         */
   /* }

    public boolean insert(int x,int y,C o){
        if (y<maxY&&x<maxX[y]){
                matrix.get(y).add(x,o);
            return true;
        }else{
            return false;
        }
    }           */


    public Entity getEntity(Vector position) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Vector getPosition(Entity tEntity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

   /* @Override
    public void process(Entity tEntity, Movement movement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addObserver(iEventObserver observer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }*/


    public void processEvent(Phenomen phenomen) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public boolean insertEntity(Vector coordinate, Entity entity) {
		return false;
	}

	@Override
	public void processEvent(iEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Entity> getAllEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean process(Movement<Entity> movement) {
		return false;
		
	}

	@Override
	public void addObserver(iEventObserver<Entity> observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertForceFactory(ForceFactory f) {
		// TODO Auto-generated method stub
		
	}
}
