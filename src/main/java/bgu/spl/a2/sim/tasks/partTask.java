package bgu.spl.a2.sim.tasks;



import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;


import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

public class partTask extends Task<Product> 
{
	private long id;
	private String productName;
	private Warehouse house;
	
	/**@partTask constructor **/
public partTask(long prodId, String prodName, Warehouse house) {
	this.id=prodId;
	this.house=house;
	this.productName=prodName;
}

   /** @start a method that implement Task class and it start each task**/
	protected void start() {
		LinkedList<Task<Product>> AllTasks=new LinkedList<>(); //all the tasks 
		AtomicLong afterUseOn=new AtomicLong(this.id);  //for the final id
		ManufactoringPlan plan=house.getPlan(productName); 
		String [] tools=plan.getTools();
		String [] parts=plan.getParts();
		if(parts.length!=0 ){  //if have parts 
		for (int i=0;i<parts.length;i++){
			partTask tmpTask=new partTask(id+1, parts[i], house);
			AllTasks.add(tmpTask);
			spawn(tmpTask);
		}
		}
		if(AllTasks.size()==0){
			Product p=new Product(id, productName);
			p.setFinalId(id);
			complete(p);
		}
		else if(AllTasks.size()>0){    
			
			this.whenResolved(AllTasks, ()->{
				Product p =new Product(id, productName);
				for(int j=0;j<AllTasks.size();j++){
					p.addPart(AllTasks.get(j).getResult().get());
				}
				CountDownLatch cdl=new CountDownLatch(tools.length);
				for (int k=0;k<tools.length;k++){
					Deferred<Tool> tool=house.acquireTool(tools[k]);      
					tool.whenResolved(()->{
						afterUseOn.addAndGet(tool.get().useOn(p));
						house.releaseTool(tool.get());            
					});
					cdl.countDown();
				}
				try{
					cdl.await();
				}
				catch(InterruptedException e){e.printStackTrace();}
				p.setFinalId(afterUseOn.get());
				this.complete(p);
			
			});
		
				
			}
		}
		

	}
	

