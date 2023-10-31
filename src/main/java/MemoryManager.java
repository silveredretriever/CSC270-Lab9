public class MemoryManager
{
   protected MemoryAllocation head;
    
   protected final String Free = "Free";


    /* size- how big is the memory space.  
     *  Memory starts at 0
     *
     */
   public MemoryManager(long size)
   {
	   head = new MemoryAllocation("SENTINEL", 0, 0, null, null);
	   head.next = new MemoryAllocation(Free, 0, size, head, null);
   }



    /**
       takes the size of the requested memory and a string of the process requesting the memory
       returns a memory allocation that satisfies that request, or
       returns null if the request cannot be satisfied.
     */
    
   public MemoryAllocation requestMemory(long size, String requester)
   {
      return requestMemory(size, requester, head.next);
   }
   
   private MemoryAllocation requestMemory(long size, String requester, MemoryAllocation node)
   {
	   if (node == null) {return null;}
	   if (node.getOwner() == Free && node.getLength() >= size)
	   {
		   MemoryAllocation newNode = new MemoryAllocation(requester, node.getPosition(), size, node.prev, null);
		   node.prev.next = newNode;
		   if (node.getLength() == size) 
		   {
			   newNode.next = node.next;
			   if (node.next != null)
			   {
				   node.next.prev = newNode;
			   }
		   } 
		   else 
		   {
			   long newPos = node.getPosition() + size;
			   long newLen = node.getLength() - size;
			   MemoryAllocation freeNode = new MemoryAllocation(Free, newPos, newLen, newNode, node.next);
			   newNode.next = freeNode;
			   if (node.next != null)
			   {
				   node.next.prev = freeNode;
			   }
		   }
		   
		   return newNode;
	   }
	   
	   return requestMemory(size, requester, node.next);
   }


   public void combineFree(MemoryAllocation mem)
   {
	   if (mem.getOwner() == Free && mem.next.getOwner() == Free)
	   {
		   mem.len = mem.len + mem.next.len;
		   mem.next = mem.next.next;
	   }
   }
   
    /**
       takes a memoryAllcoation and "returns" it to the system for future allocations.
       Assumes that memory allocations are only returned once.       

     */
   public void returnMemory(MemoryAllocation mem)
   {
	   mem.owner = Free;
	   
	   if (!(mem.next == null)) {combineFree(mem);}
	   if (!(mem.prev == head)) {combineFree(mem.prev);}
   }
    



}
