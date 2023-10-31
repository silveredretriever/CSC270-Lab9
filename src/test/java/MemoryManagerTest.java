import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryManagerTest {

	MemoryManager memMan = new MemoryManager(100);
	
	String bill = "Bill";
	String mary = "Mary";
	String jax = "Jax";
	
	@BeforeEach
	void setUp() throws Exception {
		
	}

	public void testMem(String owner, int pos, int len, MemoryAllocation mem)
	{
		assertEquals(owner, mem.getOwner());
		assertEquals(pos, mem.getPosition());
		assertEquals(len, mem.getLength());
	}
	
	@Test
	void testInitialization() {
		MemoryAllocation checkFree = memMan.requestMemory(100, "TestFree");
		testMem("TestFree", 0, 100, checkFree);
	}
	
	@Test
	void testRequestReturnMemory() {
		// Testing by requesting Bill
		MemoryAllocation billMem = memMan.requestMemory(20, bill);
		testMem("Bill", 0, 20, billMem);

		// Testing that the rest is free
		MemoryAllocation checkFree = memMan.requestMemory(80, "TestFree");
		testMem("TestFree", 20, 80, checkFree);
		memMan.returnMemory(checkFree);

		// Testing by requesting Mary
		MemoryAllocation maryMem = memMan.requestMemory(10, mary);
		testMem("Bill", 0, 20, billMem);
		testMem("Mary", 20, 10, maryMem);
		
		// hi jax
		MemoryAllocation jaxMem = memMan.requestMemory(15, jax);
		
		// Testing that the rest is free
		checkFree = memMan.requestMemory(55, "TestFree");
		testMem("TestFree", 45, 55, checkFree);
		memMan.returnMemory(checkFree);
		
		// Testing by returning Mary and Jax
		memMan.returnMemory(maryMem);
		memMan.returnMemory(jaxMem);
		
		// Testing that the rest is free
		checkFree = memMan.requestMemory(80, "TestFree");
		testMem("TestFree", 20, 80, checkFree);
		// But we don't pull it out, so it should be full
		
		maryMem = memMan.requestMemory(10, mary);
		assertNull(maryMem);
	}

}
