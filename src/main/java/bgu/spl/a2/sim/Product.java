package bgu.spl.a2.sim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	long startId;
	long finalId;
	List<Product> parts;
	/**
	* Constructor 
	* @param startId - Product start id
	* @param name - Product name
	*/
    public Product(long startId, String name)
    {
    	this.name=name;
    	this.startId=startId;
    	parts=new ArrayList<Product>();
    }

    public Product(long startId, String name,List<Product> parts)
    {
    	this.name=name;
    	this.startId=startId;
    	this.parts=parts;
    }
	/**
	* @return The product name as a string
	*/
    public String getName()
    {
    	return name;
    }
    

	/**
	* @return The product start ID as a long. start ID should never be changed.
	*/
    public long getStartId()
    {
    	return startId;
    }
    
	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(); 
	*/
    public long getFinalId()
    {
    	return finalId;
    }
    
    public void setFinalId(long finalId)
    {
    	this.finalId =finalId;//+this.startId;
    }

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts()
    {
    	return parts;
    	
    }
    public boolean hasParts()
    {
    	return parts.size()!=0;
    	
    }

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p)
    {
    	parts.add(p);
    }
   
  
    }

