package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeferredTest {
	private Deferred<Integer> in1;
	private Deferred<Integer> in2;
	

	@Before
	public void setUp() throws Exception {
		in1=new Deferred<Integer>();
		in2=new Deferred<Integer>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() {
		in1.resolve(5);
		assertNotNull(in1.get());;
	}

	@Test
	public void testIsResolved() {
		try{
			in1.resolve(5);
			assertTrue(in1.isResolved());}
		catch(Exception e){
			assertFalse(in1.isResolved());
			
	}
		
	}
	
	@Test
	public void testResolve() {
		try{
			in1.resolve(5);
			fail("The above call should have throw an exception!");
		}catch(Exception e){
			assertTrue(true);
		}			
	}
	

	@Test
	public void testWhenResolved() {
		Runnable tmp=null;
		in1.resolve(5);
		try{
			in1.whenResolved(tmp);
			fail("The above call should have throw an exception!");
		}catch(Exception e){
			assertTrue(true);
		}			
	}

}
