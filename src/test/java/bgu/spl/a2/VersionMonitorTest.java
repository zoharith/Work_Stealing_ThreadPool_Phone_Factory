package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionMonitorTest {

	private VersionMonitor ver1;
	private VersionMonitor ver2;
	
	@Before
	public void setUp() throws Exception {
		this.ver1=new VersionMonitor();
		this.ver2=new VersionMonitor();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetVersion() {;
		assertEquals( ver1.getVersion(), 0);	
	}

	@Test
	public void testInc() {
		int beforeInc=ver1.getVersion();
		ver1.inc();
		int afterInc=ver1.getVersion();
		assertNotSame(beforeInc, afterInc);
		beforeInc++;
		assertEquals("the version has been change" , beforeInc, afterInc); 	
	}

	@Test
	public void testAwait() throws InterruptedException {
		
		Thread t1=new Thread(()->{
			try {
				ver1.await(ver1.getVersion());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ver1");
		});
			t1.start();
			while(!t1.isAlive()){}
			ver1.inc();
			try{
				t1.join();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			assertFalse(t1.isAlive());

		};
	
}