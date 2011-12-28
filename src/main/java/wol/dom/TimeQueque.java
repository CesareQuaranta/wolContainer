package wol.dom;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Cesaropode
 * Date: 1-feb-2010
 * Time: 23.45.29
 * To change this template use File | Settings | File Templates.
 */
public class TimeQueque<T> implements iTime<T>{
    private class QuequeElement{
        private List<T> elements;
        private Integer count;

        public List<T> getElements() {
            return elements;
        }

        public void setElements(List<T> elements) {
            this.elements = elements;
        }
    }
    private List<QuequeElement> timeList;
    private Map<T,QuequeElement> index;
    public TimeQueque(){
         timeList=new  LinkedList<QuequeElement>();
        index=new HashMap<T,QuequeElement>();
    }

    public List getPresent(){
        return null;
    }

    public void setFuture(T element,long delay){
        QuequeElement curElement=null;

        long curDelay=delay;
        int curIndex=0;

        do{
           if (timeList.size()>curIndex){
            curElement=timeList.get(curIndex);
            curDelay-=curElement.count;
            }
        }while(curDelay>0&&curElement!=null);

        if (curElement==null&&curDelay>0){//New last element
            if (curDelay<Integer.MAX_VALUE){
                QuequeElement lastElement=new QuequeElement();
                lastElement.count=new Integer((int)curDelay);
                lastElement.elements.add(element);
                timeList.add(lastElement);
            }else{//curDelay>Integer.MAX_VALUE
                long splitDelay=curDelay;
                while(splitDelay>Integer.MAX_VALUE){
                   QuequeElement emptytElement=new QuequeElement();
                   emptytElement.count=new Integer(Integer.MAX_VALUE);
                   timeList.add(emptytElement);
                   splitDelay-=Integer.MAX_VALUE;
                }
                
                QuequeElement lastElement=new QuequeElement();
                lastElement.count=new Integer((int)splitDelay);
                lastElement.elements.add(element);
                timeList.add(lastElement);
            }
        }else if (curDelay==0){//Current position
             curElement.elements.add(element);
        }else{//insert element between
            int insertDelay=(int)curDelay*-1;
            QuequeElement insertElement=new QuequeElement();
            insertElement.count=new Integer(insertDelay);
            insertElement.elements.add(element);
            timeList.add(curIndex,insertElement);
            curElement.count-=insertDelay;//Subtract delay
        }
    }

    public boolean removeFuture(T element){
        return false;
    }
}
