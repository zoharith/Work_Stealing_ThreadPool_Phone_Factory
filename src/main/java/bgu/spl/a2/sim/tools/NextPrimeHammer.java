package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

public class NextPrimeHammer implements Tool {

	String type;
	
	public NextPrimeHammer()
	{
		this.type="np-hammer";
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

			long v =val + 1;
	        while (!isPrime(v)) 
	        {
	            v++;
	        }

	        return v;
	 }
	
	
	private boolean isPrime(long value) 
	{
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i < sq; i++) 
        {
            if (value % i == 0) 
            {
                return false;
            }
        }
        return true;
    }
}