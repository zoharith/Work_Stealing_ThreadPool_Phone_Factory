package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.a2.Deferred;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse 
{
	LinkedList<ManufactoringPlan> plans;
	ConcurrentLinkedQueue<Tool> GCD;
	ConcurrentLinkedQueue<Tool> NPH;
	ConcurrentLinkedQueue<Tool> RSP;
	ConcurrentLinkedQueue<Deferred<Tool>> defGCD;
	ConcurrentLinkedQueue<Deferred<Tool>> defNPH;
	ConcurrentLinkedQueue<Deferred<Tool>> defRSP;
	
	
	/**
	* Constructor
	*/
    public Warehouse()
    {
    	plans = new LinkedList<ManufactoringPlan>();
    	GCD = new ConcurrentLinkedQueue<Tool>();
    	NPH = new ConcurrentLinkedQueue<Tool>();
		RSP = new ConcurrentLinkedQueue<Tool>();
		defGCD = new ConcurrentLinkedQueue<Deferred<Tool>>();
    	defNPH = new ConcurrentLinkedQueue<Deferred<Tool>>();
		defRSP = new ConcurrentLinkedQueue<Deferred<Tool>>();
    }

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
    public synchronized  Deferred<Tool> acquireTool(String type)
    {
    	
    	Deferred<Tool> def = new Deferred<Tool>();
    	Tool tool = null;
    	switch(type){
    	case "rs-pliers":
    	{
    		try 
    		{
    			if(!RSP.isEmpty())
    				tool=(RSP).poll();
    			else
    			{
    				defRSP.add(def);
    			}
    			
			} 
    		catch (Exception e) 
    		{
				e.printStackTrace();
			} 			
    	}
    	break;
    	case "gs-driver":
    		try 
    		{
    			if(!GCD.isEmpty())
    				tool=(GCD).poll();
    			else
    			{
    				defGCD.add(def);
    			}
			} 
    		catch (Exception e)
    		{
				e.printStackTrace();
			}
  
    break;
    	case "np-hammer":
    		try 
    		{
    			if(!NPH.isEmpty())
    				tool=(NPH).poll();
    			else
    			{
    				defNPH.add(def);
    			}
    			
			} 
    		catch (Exception e) 
    		{
				e.printStackTrace();
			}
    	break;
    	}
    	if(tool!=null)
    	    def.resolve(tool);
    	return def;
    	
    }

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/
    public synchronized void releaseTool(Tool tool)
    {
    	Deferred<Tool> def;
    	switch(tool.getType()){
    	case "rs-pliers":
    		Tool nextTool=new RandomSumPliers();
    		if(defRSP.isEmpty())
    			RSP.add(nextTool);   
    		else
    		{
    			def = defRSP.poll();
    			def.resolve(nextTool);
    		}
    		break;
    	case "gs-driver":
    		Tool nextTool1=new GcdScrewDriver();
    		if(defGCD.isEmpty())
    			GCD.add(nextTool1);
    		else
    		{
    			def = defGCD.poll();
    			def.resolve(nextTool1);
    		}
    		break;
    	case "np-hammer":
    		Tool nextTool11=new NextPrimeHammer();
    		if(defNPH.isEmpty())
    			NPH.add(nextTool11);
    		else
    		{
    			def = defNPH.poll();
    			def.resolve(nextTool11);
    		}
    		break;
    	}
    	
    }

	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product)
    {
    	boolean found=false;
    	ManufactoringPlan result = null;
    	for(int i=0; i<plans.size() && !found;i++){
    		if(plans.get(i).getProductName().equals(product)){
    			result = plans.get(i);
    			found=true;
    		}
    	}
    	if(!found){
    		System.out.println("there isnot plan");
    	}
    	return result;
    }
	
	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
    public void addPlan(ManufactoringPlan plan)
    {
    	if(plan!=null)
    		plans.add(plan);
    }
    
	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
    public void addTool(Tool tool, int qty)
    {
    	switch(tool.getType()){
    	case "rs-pliers":
    		while(qty>0)
    		{
    			Tool nextTool = new RandomSumPliers();
    			RSP.add(nextTool);   			
    		qty--;
    		}
    	break;
    	case "gs-driver":
    		while(qty>0)
    		{
    			Tool nextTool = new GcdScrewDriver();
    			GCD.add(nextTool);
    		qty--;
    		}
    		break;
    	case "np-hammer":
    		while(qty>0)
    		{
    			Tool nextTool = new NextPrimeHammer();
    			NPH.add(nextTool);
    			qty--;
    		}
    		break;
    }
    }

}