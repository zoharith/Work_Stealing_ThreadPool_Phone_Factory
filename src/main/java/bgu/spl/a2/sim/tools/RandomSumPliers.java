package bgu.spl.a2.sim.tools;

import java.util.Random;

import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool {
	
	String type;
	
	public RandomSumPliers()
	{
		this.type="rs-pliers";
	}
	
	@Override
	public String getType()
	{
		return type;
	}

	@Override
	/**@useOn calculate the final id **/
	public long useOn(Product p) 
	{

		long value=0;
		for(int i=0;i<p.getParts().size();i++){
			value+=Math.abs(sum(p.getParts().get(i).getFinalId()));
		}
        return value;
		
	}
	
	 public long sum(long val){
		 Random r = new Random(val);
	        long  sum = 0;
	        for (long i = 0; i < val % 10000; i++)
	        {
	            sum += r.nextInt();
	        }
	        return sum;
	 }

}