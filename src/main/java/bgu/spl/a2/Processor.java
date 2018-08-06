package bgu.spl.a2;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool wstp;
    private final int id;
    boolean isInterapted;
    
    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
    	isInterapted=false;
        this.id = id;
        this.wstp = pool;
    }

    @Override
    public void run() 
    {
    	boolean trySteal;
    	while(!isInterapted)
    	{	
    		trySteal=false;
    		if(wstp.myVec.get(this.id).isEmpty())
    		{
    		while(wstp.myVec.get(this.id).isEmpty() && (!isInterapted) && (!trySteal))
    		{	
				trySteal=steal();
					if(trySteal==false)  
					{	
						try {	
							wstp.vm.await(wstp.vm.getVersion());
					}
						catch (InterruptedException e) { }
					}}}
    	else
    	{		
    		ConcurrentLinkedDeque<Task<?>> queue=wstp.myVec.get(this.id); 			
    		Task<?> tmptask=queue.pollFirst();
    		if(tmptask!=null)
    			tmptask.handle(this); 		
    		}
    	}
    	
    }
    
    
    WorkStealingThreadPool getPool()
    {
    	return wstp;
    }

    int getId()
    {
    	return id;
    }
  
    private boolean steal(){
    	
    	boolean stealing = false;     //if we steal 
    	boolean findVictim=false;     //if there is a victim
    	Vector<ConcurrentLinkedDeque<Task<?>>> myVec=wstp.getQVector();	
    	int fromWhoToSteal=(id+1)%myVec.size();				
    	for(int i=fromWhoToSteal; i<myVec.size() && (i!=fromWhoToSteal) && (!findVictim) ; i=(i+1)%myVec.size())
    	{
    		if(myVec.get(i).size()>1)
    		{
    			fromWhoToSteal=i;					//	apply the victim		
    			findVictim=true;							
    		}		
    	}
    	if(findVictim)
    	{
    		int numToSteal=myVec.get(fromWhoToSteal).size()/2;
    		for(int i=0;i<numToSteal ;i++)        //transfer the tasks
    		{   
    			Task<?> victimTask=myVec.get(fromWhoToSteal).pollLast();
    			if(victimTask!=null)
    				myVec.get(id).add(victimTask);			
    		}
    		stealing = true;
    	}
    	return stealing;
    }
    		 
  
}
