package HashTables;

public class LinearProbingHashTable<Type> {
	
	private static final int DEFAULT_TABLE_SIZE = 101;
	
	private HashEntry<Type>[] array;
	public int occupied;            // The number of occupied cells 
	public int size;                // Current table size
	
	public LinearProbingHashTable(){
		this(DEFAULT_TABLE_SIZE);
	}
	
	public LinearProbingHashTable(int size){
		array = new HashEntry[nextPrime(size)];
		for(int i=0; i<array.length; i++)
			array[i] = null;
		
		this.size = array.length;
		occupied = 0;
	}
	
	public boolean insert(Type e){
		if(contains(e))
			return false;
		
		int location = myHash(e);
		
		while(array[location] != null && array[location].isActive)
			location = linearProbe(location);
		
		array[location] = new HashEntry<Type>(e);
		occupied++;
		if(occupied > size/2)
			rehash();
		
		return true;
	}
	
	public void remove(Type e){
		int location = myHash(e);
		
		while(array[location] != null){
			if(array[location].isActive && array[location].elem.equals(e)){
				array[location].isActive = false;
				occupied--;
				return;
			}
			location = linearProbe(location);
		}
	}
	
	public boolean contains(Type e){
		int location = myHash(e);
		
		while(array[location] != null){
			if(array[location].isActive && array[location].elem.equals(e)){
				return true;
			}
			location = linearProbe(location);
		}
		
		return false;
	}
	
	private int linearProbe(int location){
		location++;
		if(location > size-1)
			location -= size;
		return location;
	}
	
	private void rehash(){
		HashEntry<Type>[] oldArray = array;
		array = new HashEntry[nextPrime(size*2)];
		for(int i=0; i<array.length; i++)
			array[i] = null;
		
		occupied = 0;
		size = array.length;
		
		for(HashEntry<Type> entry:oldArray){
			if(entry!=null && entry.isActive)
				insert(entry.elem);
		}
	}
	
	private int myHash(Type e){
		int hashVal = e.hashCode();
		hashVal %= size;
		if(hashVal < 0)
			hashVal += size;
		
		return hashVal;
	}
	
	private static class HashEntry<Type>{
		public Type elem;
		public boolean isActive;
		
		public HashEntry(Type e){
			this(e, true);
		}
		
		public HashEntry(Type e, boolean b){
			elem = e;
			isActive = b;
		}
	}

	private int nextPrime(int x){
		if(x%2==0)
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
		
		for(int i=3; i*i<=x; i+=2)
			if(x % i == 0)
				return false;
		
		return true;
	}
	
	
	// Simple main
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
        System.out.println( "H size is: " + H.size );
        System.out.println( "Array size is: " + (H.occupied/H.size));
    }
	
	
	
	
}
