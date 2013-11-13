package HashTables;

/**
 * Project 4 for Data Structures class.
 * @author Jinwei Yuan
 * 11/10/2013
 * 
 * Complete the basic requirements: implement three functions: insert(), contains(), and main().
 * 
 * CALL FOR BONUS:
 * This work also implements additional functions: 
 * 1. supply a remove() function;
 * 2. it can handle rehashing problem. 
 */


public class HopscotchHashTable<Type> {

	private static final int HOP_DISTANCE = 8;
	private static final int DEFAULT_TABLE_SIZE = 101;
	
	private HashEntry<Type>[] array;
	private int size;                         // size of hash table
	private int occupied;                     // current number of elements
	
	/**
	 * constructors
	 */
	public HopscotchHashTable(){
		this(DEFAULT_TABLE_SIZE);
	}
	
	public HopscotchHashTable(int size){
		array = new HashEntry[nextPrime(size)];
		this.size = array.length;
		occupied = 0;
	}
	
	/**
	 * insert()
	 * insert an item into the hashtable. The project does not require to consider rehashing problem, 
	 * however, I finished this part. When the inserted item can not find empty cell within hop distance, 
	 * rehashing will be called.
	 */
	public boolean insert(Type e){
		if(contains(e))
			return false;
		
		boolean isSuccess = insertData(e);
		
		HashEntry<Type>[] oldArray = array;
		while(!isSuccess){
			// rehash...
			array = new HashEntry[nextPrime(size*2)];
			occupied = 0;
			size = array.length;
			
			for(HashEntry<Type> entry:oldArray){
				if(entry!=null && entry.isActive){
					isSuccess = insertData(entry.elem);
					if(!isSuccess)
						break;
				}
			}
			if(isSuccess)
				isSuccess = insertData(e);
		}
		
		return true;
	}
	
	/**
	 * contains()
	 * returns true if the item is in the hashtable, else returns false.
	 */
	public boolean contains(Type e){
		int location = myHash(e);
		
		for(int i=0; i<HOP_DISTANCE; i++){
			int curr = location+i;
			if(curr > size-1)
				curr -= size;
			
			if(array[curr] != null && array[curr].isActive && array[curr].elem.equals(e)){
				return true;
			}
		}
		
		return false;
	}
	
	
	public void remove(Type e){
		int location = myHash(e);
		
		for(int i=0; i<HOP_DISTANCE; i++){
			int curr = location+i;
			if(curr > size-1)
				curr -= size;
			
			if(array[curr] != null && array[curr].isActive && array[curr].elem.equals(e)){
				array[curr].isActive = false;
				array[myHash(e)].hop[i] = 0;
				occupied--;
				return;
			}
		}
	}

	
	private boolean insertData(Type e){
		int currLocation = findLocation(e);
		if(currLocation < 0)
			return false;
		
		int orgLocation = myHash(e);
		
		while(!isValid(currLocation, orgLocation)){
			int upLocation = swapUp(currLocation);
			if(upLocation < 0){
				return false;
			}
			currLocation = upLocation;
		}
		
		addData(e, currLocation);
		occupied++;
		return true;
	}
	
	private int findLocation(Type e){
		int location = myHash(e);
		int count = 0;
		while(array[location] != null && array[location].isActive){
			if(count++ > size)
				return -1;
			location++;
			if(location > size-1)
				location -= size;
		}
		return location;
	}
	
	private int swapUp(int curr){
		int location;
		int rst;
		for(int i=HOP_DISTANCE-1; i>=1; i--){
			location = curr-i;
			if(location < 0)
				location += size;
			
			for(int j=0; j<i; j++){
				if(array[location].hop[j] == 1){
					array[location].hop[j] = 0;
					array[location].hop[i] = 1;
					rst = location + j;
					if(rst>size-1)
						rst -= size;
					addData(array[rst].elem, curr);
					array[rst].isActive = false;
					
					return rst;
				}
			}
		}
		return -1;
	}
	
	private void addData(Type e, int location){
		if(array[location] == null){
			array[location] = new HashEntry<Type>(e);
		}
		else{
			array[location].elem = e;
			array[location].isActive = true;
		}
		
		int orgLocation = myHash(e);
		if(orgLocation > location)
			location += size;
		int dist = location-orgLocation;
		array[orgLocation].hop[dist] = 1;
	}
	
	private boolean isValid(int location, int orgLocation){
		if(orgLocation > location)
			location += size;
		
		int dist = location-orgLocation;
		if(dist < HOP_DISTANCE && dist >= 0)
			return true;
		return false;
	}
	
	private int myHash(Type e){
		int hashVal = e.hashCode();
		hashVal %= size;
		if(hashVal < 0)
			hashVal += size;
		
		return hashVal;
	}
	
	private static class HashEntry<Type>{
		public Type elem;                // element saved in current cell.
		public boolean isActive;         // false when elem is removed and no new elem is added to this cell
		public int[] hop;                // Hop information
		
		public HashEntry(Type e){
			this(e, true);
		}
		
		public HashEntry(Type e, boolean b){
			elem = e;
			isActive = b;
			hop = new int[HOP_DISTANCE];
		}
	}
	
	private int nextPrime(int x){
		if(x%2 == 0)
			x += 1;
		
		while(!isPrime(x))
			x += 2;
		
		return x;
	}
	
	private boolean isPrime(int x){
		if(x == 2 || x == 3)
			return true;
		
		if(x ==1 || x%2 ==0)
			return false;
		
		for(int i=3; i*i<=x; i+=2)
			if(x%i == 0)
				return false;
		
		return true;
	}
	
	public int getTableSize(){
		return size;
	}
	
	public int getOccupied(){
		return occupied;
	}
	
	
	public static void main(String args[]){
		System.out.println( "-------------------------" );
		
		System.out.println( "Test Case 1:" );
		HopscotchHashTable<Integer> hash = new HopscotchHashTable<Integer>(9);
		System.out.println( "Create a empty hashtable with table size: " + hash.getTableSize());
		System.out.println( "Does the table contain number 11?  " + hash.contains(11));
		System.out.println( "Insert numbers 1 to 15 into the hashtable." );
		for(int i=1; i<=15; i++)
			hash.insert(i);
		System.out.println( "Does the table contain number 11?  " + hash.contains(11));
		System.out.println( "Remove number 11." );
		hash.remove(11);
		System.out.println( "Does the table contain number 11?  " + hash.contains(11));
		System.out.println( "The table size changes to: " + hash.getTableSize() + " because of rehashing.");
		
		System.out.println( "-------------------------" );
		
		System.out.println( "Test Case 2:" );
		System.out.println( "This is the test case used by the author of the text book." );
		HopscotchHashTable<String> H = new HopscotchHashTable<String>();
		
		long startTime = System.currentTimeMillis( );
        
	    final int NUMS = 20000;
	    final int GAP  =   37;

	    System.out.println( "Checking... (no more output means success)" );


	    for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
	        H.insert( ""+i );
	    
	    for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
	        if( H.insert( ""+i ) )
	            System.out.println( "OOPS!!! " + i );
	    for( int i = 1; i < NUMS; i+= 2 )
	        H.remove( ""+i );

	    for( int i = 2; i < NUMS; i+=2 )
	        if( !H.contains( ""+i ) )
	            System.out.println( "Find fails " + i );

	    for( int i = 1; i < NUMS; i+=2 )
	    {
	        if( H.contains( ""+i ) )
	            System.out.println( "FOOPS!!! " +  i  );
	    }
	        
	    long endTime = System.currentTimeMillis( );
	        
	    System.out.println( "Elapsed time: " + (endTime - startTime) );
	    System.out.println( "H size is: " + H.getTableSize() );
	    System.out.println( "Array size is: " + (H.getOccupied()));
	}
	
	
}
