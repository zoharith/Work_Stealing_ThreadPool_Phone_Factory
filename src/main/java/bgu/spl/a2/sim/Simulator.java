/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.partTask;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * A class describing the simulator for part 2 of the assignment
 */

public class Simulator 
{
	static WorkStealingThreadPool wstp;
	private static Warehouse house;
	private static initialData data;

	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start()
    {
    	
      	ConcurrentLinkedQueue<Product> output=new ConcurrentLinkedQueue<Product>();  //output need to return
	    wstp.start();  
	    int numsOfWave=data.waves.length;
		for(int i=0;i<numsOfWave;i++)
		{
			int productsByWave=data.waves[i].length;
			for(int j=0;j<productsByWave;j++){
				long prodId=data.waves[i][j].startId;
				String prodName=data.waves[i][j].product;
				int productQty=data.waves[i][j].qty;
				CountDownLatch cdl=new CountDownLatch(productQty);    
				ArrayList<partTask> tasks=new ArrayList<partTask>();
				for (int k=0;k<productQty;k++){
					partTask tmpTask=new partTask(prodId+k,prodName, house);   
					tasks.add(tmpTask);
					wstp.submit(tmpTask);       //submit the task
					tmpTask.getResult().whenResolved(()->{
						cdl.countDown();
					});
				}
				try{
				cdl.await();	  
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				for (partTask task: tasks){      //
					output.add(task.getResult().get());
				}}
			}
		try{
			wstp.shutdown();
		} catch (InterruptedException e) {}
    	return output;
    }
	

	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool)
	{
		wstp=myWorkStealingThreadPool;
	}
	
	public static void main(String [] args) 
	{
		Gson gson=new Gson();      //open the file and throw exception hav'nt file   
		JsonReader jsonReader = null;
		try{
			jsonReader = new JsonReader(new FileReader(args[0]));}
		catch (FileNotFoundException e)
		{
			System.out.println("file not found");
		} 
		initialData data = gson.fromJson(jsonReader, initialData.class);
		if(data!=null){
		initialization(data);     //initial the house
		}
		
			ConcurrentLinkedQueue<Product> SimulationResult=start();
			
			try{
	   	System.out.println("stop");
	   	FileOutputStream fout = new FileOutputStream("result.ser");
	   	ObjectOutputStream oos = new ObjectOutputStream(fout);
	   	oos.writeObject(SimulationResult);
			oos.close();
			fout.close();
		} catch (FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			catch (IOException e) {

				e.printStackTrace();
			}
		
		//return 0;
	}
	/**@initialization initial the house by the data from the file **/
	private static void initialization(initialData data) {
		int nthreads=data.threads;
		WorkStealingThreadPool jsonPool = new WorkStealingThreadPool (nthreads);     //creating the pool
		Simulator.attachWorkStealingThreadPool(jsonPool);
		house = new Warehouse();
		for(int i=0;i<data.tools.length;i++){      //creating the tools
			int qty=data.tools[i].qty;
			Tool tool=null;
			tool=createTool(data.tools[i].tool);
				house.addTool(tool, qty);}
			
		
		for(int i=0;i<data.plans.length;i++){       //creating the plan and add to the house
			house.addPlan(new ManufactoringPlan(data.plans[i].product,data.plans[i].parts,data.plans[i].tools ));
		}
		
		setWarehouse(house);
		setData(data);
	}
	

	/** @setData set the data **/
	private static void setData(initialData data1) {
		data=data1;		
	}


	/**@setWarehouse set the warehouse **/
	private static void setWarehouse(Warehouse house2) {
		house=house2;
	}
	 
	/**@createTool  create tool by the type **/
	private static Tool createTool(String toolName) {
		if(toolName.equals("rs-pliers")){
			return new RandomSumPliers();}
		if(toolName.equals("gs-driver")){
			return new GcdScrewDriver();}
		if(toolName.equals("np-hammer")){
		return	new NextPrimeHammer();}
		return null;
	}

}