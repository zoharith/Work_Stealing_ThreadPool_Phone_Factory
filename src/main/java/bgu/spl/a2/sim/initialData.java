package bgu.spl.a2.sim;
import com.google.gson.annotations.SerializedName;

public class initialData 
{
	@SerializedName("plans")
	public plan[] plans;

	public class plan 
	{
		@SerializedName("product")
		public String product;

		@SerializedName("tools")
		public String[] tools;

		@SerializedName("parts")
		public String[] parts;

	}

	@SerializedName("threads")
	public int threads;

	@SerializedName("tools")
	public tool [] tools;
	
	public class tool 
	{
	public String tool;
	public int qty;
	}

	@SerializedName("waves")
	public wave[][] waves;

	public class wave
	{
		String product;
		int qty;
		long startId;
	public wave(long id, String pro, int qty){
		this.product=pro;
		this.qty=qty;
		this.startId=id;
		
	}
	
	public int getQty(){
		return this.qty;
	}
	public long getStartId(){
		return this.startId;
	}
	
	public String getProduct(){
		return this.product;
	}

	}

}