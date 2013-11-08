/**
 * By Jinwei Yuan
 * 10/29/2013
 */

package HashTables;

import java.util.LinkedList;
import java.util.List;

public class SeperateChainingHashTable<T> {
	private static final int DEFAULT_TABLE_SIZE = 101;
	private List<T>[] list;
	private int currSize;
	
	public SeperateChainingHashTable(){
		this(DEFAULT_TABLE_SIZE);
	}
	
	public SeperateChainingHashTable(int size){
		list = new LinkedList[nextPrime(size)];
		
		for(int i=0; i<list.length; i++){
			list[i] = new LinkedList<T>();
		}
	}
	
	public void insert(T x){
		List<T> thisList = list[myhash(x)];
		
		if(!thisList.contains(x)){
			thisList.add(x);
			if(++currSize > list.length){
				rehash();
			}
		}
		
	}
	
	public void remove(T x){
		List<T> thisList = list[myhash(x)];
		
		if(thisList.contains(x)){
			thisList.remove(x);
			--currSize;
		}
	}
	
	public boolean contains(T x){
		return list[myhash(x)].contains(x);
	}
	
	public void makeEmpty(){
		for(int i=0; i<list.length; i++){
			list[i].clear();
		}
		currSize = 0;
	}
	
	private void rehash(){
		List<T>[] oldList = list;
		
		list = new LinkedList[nextPrime(2*oldList.length)];
		for(int i=0; i<list.length; i++){
			list[i] = new LinkedList<T>();
		}
		currSize = 0;
		for(int i=0; i<oldList.length; i++){
			for(T x:oldList[i]){
				insert(x);
			}
		}
	}
	
	private int myhash(T x){
		int hashVal = x.hashCode();
		hashVal %= list.length;
		
		if(hashVal<0)
			hashVal += list.length;
		
		return hashVal;
	}
	
	private static int nextPrime(int n){
		while(!isPrime(n)){
			n++;
		}
		return n;
	}
	
	private static boolean isPrime(int n){
		if(n == 2 || n == 3)
			return true;
		if(n == 1 || n%2 == 0)
			return false;
		
		for(int i = 3; i*i <= n; i += 2){
			if(n%i == 0)
				return false;
		}
		
		return true;
	}
	
}
