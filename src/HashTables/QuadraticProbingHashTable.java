package HashTables;


public class QuadraticProbingHashTable<Type> {
	
	private static final int DEFAULT_TABLE_SIZE = 101;
	private int occupied;           // current number of elements
	private int size;               // table size;
	private HashEntry<Type>[] array;
	
	public QuadraticProbingHashTable(){
		this(DEFAULT_TABLE_SIZE);
	}
	
	public QuadraticProbingHashTable(int size){
		array = new HashEntry[nextPrime(size)];
		this.size = array.length;
		occupied = 0;
	}
	
	public boolean insert(Type e){
		if(contains(e))
			return false;
		
		int location = findLocation(e);
		array[location] = new HashEntry<Type>(e);
		occupied++;
		if(occupied > size/2)
			rehash();
		
		return true;
	}
	
	public boolean contains(Type e){
		int curr = myHash(e);
		int offset = 1;
		
		while(array[curr] != null){
			if(array[curr].isActive && array[curr].element.equals(e))
				return true;
			
			curr += offset;
			offset += 2;
			if(curr >= size)
				curr -= size;
		}
		return false;
	}
	
	public void remove(Type e){
		int curr = myHash(e);
		int offset = 1;
		
		while(array[curr] != null){
			if(array[curr].isActive && array[curr].element.equals(e)){
				array[curr].isActive = false;
				occupied--;
				return;
			}
			curr += offset;
			offset += 2;
			if(curr >= size)
				curr -= size;
		}
	}
	
	public int getSize(){
		return size;
	}
	
	public int getOccupied(){
		return occupied;
	}
	
	private void rehash(){
		HashEntry<Type>[] oldArray = array;
		array = new HashEntry[nextPrime(size*2)];
		size = array.length;
		occupied = 0;
		
		for(HashEntry<Type> entry : oldArray)
			if(entry != null && entry.isActive)
				insert(entry.element);
	}
	
	private int findLocation(Type e){
		int curr = myHash(e);
		int offset = 1;
		
		while(array[curr] != null && array[curr].isActive){
			curr += offset;
			offset += 2;
			if(curr >= size)
				curr -= size;
		}
		
		return curr;
	}
	
	private int myHash(Type e){
		int hashVal = e.hashCode();
		hashVal %= size;
		if(hashVal < 0)
			hashVal += size;
		
		return hashVal;
	}
	
	
	private static class HashEntry<Type>{
		public Type element;
		public boolean isActive; 
		
		public HashEntry(Type e){
			this(e, true);
		}
		
		public HashEntry(Type e, boolean b){
			element = e;
			isActive = b;
		}
	}
	
	private int nextPrime(int x){
		if(x%2 == 0)
			x++;
		
		while(!isPrime(x)){
			x += 2;
		}
		
		return x;
	}
	
	private boolean isPrime(int x){
		if(x == 2 || x == 3)
			return true;
		if(x == 1 || x%2 == 0)
			return false;
		
		for(int i=0; i*i<=x; i += 2){
			if(x%i == 0)
				return false;
		}
		
		return true;
	}
	
	
	// Sample main
	public static void main( String [ ] args )
	{
		LinearProbingHashTable<String> H = new LinearProbingHashTable<>( );

	        
	    long startTime = System.currentTimeMillis( );
	        
	    final int NUMS = 200000;
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
	            System.out.println( "OOPS!!! " +  i  );
	    }
	        
	    long endTime = System.currentTimeMillis( );
	        
	    System.out.println( "Elapsed time: " + (endTime - startTime) );
	    System.out.println( "H size is: " + H.getSize() );
	    System.out.println( "Array size is: " + (H.getOccupied()));
	}
	
	
	
}
