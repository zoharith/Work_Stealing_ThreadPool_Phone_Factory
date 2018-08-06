package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;

public class GcdScrewDriver implements Tool {

	String type;
	
	public GcdScrewDriver()
	{
		this.type="gs-driver";
	}
	
	@Override
	public String getType()
	{
		return type;
	}

	@Override
	/**@useOn calculate the final id **/
	public long useOn(Product p) {
		long value=0;
		for(int i=0;i<p.getParts().size();i++){
			value+=Math.abs(sum(p.getParts().get(i).getFinalId()));
		}
        return value;
	}
	 public long sum(long val){
		 BigInteger b1 = BigInteger.valueOf(val);
	        BigInteger b2 = BigInteger.valueOf(reverse(val));
	        long value= (b1.gcd(b2)).longValue();
	        return value;
	 }
	
	public long reverse(long n)
	{
	    long reverse=0;
	    while( n != 0 )
	    {
	        reverse = reverse * 10;
	        reverse = reverse + n%10;
	        n = n/10;
	    }
	    return reverse;
	  }

}