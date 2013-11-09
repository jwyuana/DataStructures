package Trees;


import java.util.Stack;

/**
 * Data Structures
 * Project 3
 * @author Jinwei Yuan
 *
 */

public class SplayTree {
	/* tree constructor */
	public SplayTree(){
		root = null;
		stack = new Stack<>();
	}
	
	/**
	 * add()
	 * add a node to the Splay tree and rotates the node to the top.
	 * @param value the element to add
	 */
	public void add(int value){
		stack.clear();
		root = add(value, root);
		rotate();
	}
	
	/* the internal method of add() */
	private SplayNode add(int value, SplayNode node){
		if(node == null){
			SplayNode newNode = new SplayNode(value);
			stack.push(newNode);
			return newNode;
		}
		stack.push(node);
		if(value < node.element)
			node.left =  add(value, node.left);
		else if(value > node.element)
			node.right = add(value, node.right);
		else
			;
		return node;
	}
	
	/**
	 * remove()
	 * Remove a node like a binary search tree does, then rotate the removed node's parent to top.
	 * PLEASE NOTE: In this implementation, if the removed value is not in the tree, then 
	 * the last accessed node will be rotated to top.
	 * @param value the value needed to be removed
	 */
	public void remove(int value){
		stack.clear();
		root = remove(value, root);
		rotate();
	}
	
	/* The internal method of remove */
	private SplayNode remove(int value, SplayNode node){
		if(node == null)
			return node;
		
		if(value < node.element){
			stack.push(node);
			node.left = remove(value, node.left);
		}
		else if(value > node.element){
			stack.push(node);
			node.right = remove(value, node.right);
		}
		else if(node.left != null && node.right != null){
			node.element = findMin(node.right);
			node.right = removeMin(node.right);
		}
		else
			node = (node.left!=null) ? node.left : node.right;
		
		return node;
	}
	
	private int findMin(SplayNode node){
		if(node.left == null)
			return node.element;
		
		return findMin(node.left);
	}
	
	private SplayNode removeMin(SplayNode node){
		if(node.left == null)
			return null;
		node.left = removeMin(node.left);
		
		return node;
	}
	
	/**
	 * find()
	 * Return true if the value is in the tree. Rotate the node to top if found or the last node 
	 * accessed if not found.
	 * @param value the item to be found
	 * @return boolean
	 */
	public boolean find(int value){
		stack.clear();
		boolean rst =  find(value, root);
		rotate();
		return rst;
	}
	
	/* The internal method of find */
	private boolean find(int value, SplayNode node){
		if(node == null)
			return false;
		
		stack.push(node);
		
		if(value < node.element)
			return find(value, node.left);
		else if(value > node.element)
			return find(value, node.right);
		else
			return true;
	}
	
	/**
	 * leafCount()
	 * Return the count of all the leaves in the tree.
	 * @return number of leaves
	 */
	public int leafCount(){
		return leafCount(root);
	}
	
	private int leafCount(SplayNode node){
		if(node == null)
			return 0;
		
		if(node.left == null && node.right == null)
			return 1;
		else
			return leafCount(node.left)+leafCount(node.right);
	}
	
	/**
	 * leafSum()
	 * Return the sum of all the leaves in the tree.
	 * @return sum of leaves
	 */
	public int leafSum(){
		return leafSum(root);
	}
	
	private int leafSum(SplayNode node){
		if(node == null)
			return 0;
		
		if(node.left == null && node.right == null)
			return node.element;
		else 
			return leafSum(node.left) + leafSum(node.right);
	}
	
	/**
	 * toString()
	 * Return a string of the values of a preorder traversal.
	 */
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("[ ");
		printTree(root, string);
		string.append("]");
		return string.toString();
	}
	
	private void printTree(SplayNode nd, StringBuilder string){
		if(nd != null){
			string.append(nd.element + " ");
			printTree(nd.left, string);
			printTree(nd.right, string);
		}
		
	}
	
	/**
	 * A method to rotate the specified node.
	 * @param node the node need to be rotated
	 */
	private void rotate(){
		if(stack.size() <= 1)
			return;
		else{
			SplayNode n = stack.pop();
			
			while(stack.size()>1){
				SplayNode np = stack.pop();
				SplayNode gp = stack.pop();
				
				if((n == np.left && np == gp.left) || (n == np.right && np == gp.right))
					n = zigzig(n, np, gp);
				else
					n = zigzag(n, np, gp);
				if(stack.size() != 0){
					SplayNode nd = stack.pop();
					if(nd.left == gp)
						nd.left = n;
					else
						nd.right = n;
					stack.push(nd);
				}
			}
			
			if(stack.size() == 1){
				SplayNode np = stack.pop();
				rotateTwoNodes(n, np);
			}
				
			root = n;
		}
	}
	
	private void rotateTwoNodes(SplayNode node, SplayNode nodeP){
		if(node == nodeP.left){
			nodeP.left = node.right;
			node.right = nodeP;
		}
		else{
			nodeP.right = node.left;
			node.left = nodeP;
		}
	}
	
	private SplayNode zigzig(SplayNode node, SplayNode nodeP, SplayNode nodeGP){
		SplayNode n = new SplayNode(node.element);
		SplayNode p = new SplayNode(nodeP.element);
		SplayNode gp = new SplayNode(nodeGP.element);
		
		if(node == nodeP.left){
			n.left = node.left;
			n.right = p;
			p.left = node.right;
			p.right = gp;
			gp.right = nodeGP.right;
			gp.left = nodeP.right;
		}
		else{
			n.left = p;
			n.right = node.right;
			p.left = gp;
			p.right = node.left;
			gp.left = nodeGP.left;
			gp.right = nodeP.left;
		}	
		return n;
	}
	
	private SplayNode zigzag(SplayNode node, SplayNode nodeP, SplayNode nodeGP){
		SplayNode n = new SplayNode(node.element);
		SplayNode p = new SplayNode(nodeP.element);
		SplayNode gp = new SplayNode(nodeGP.element);
		
		if(node == nodeP.right){
			n.left = p;
			n.right = gp;
			p.left = nodeP.left;
			p.right = node.left;
			gp.left = node.right;
			gp.right = nodeGP.right;
		}
		else{
			n.left = gp;
			n.right = p;
			gp.left = nodeGP.left;
			gp.right = node.left;
			p.left = node.right;
			p.right = nodeP.right;
		}	
		return n;
	}
	
	/**
	 * Tree Node class
	 * element, left child, right child and its parent.
	 */
	private static class SplayNode{
		int element;
		SplayNode left;
		SplayNode right;
		
		SplayNode(int value){
			element = value;
			left = null;
			right = null;
		}
	}
	
	/* root of the tree */
	private SplayNode root; 
	/* stack */
	private Stack<SplayNode> stack;
	
	/**
	 * main()
	 * All the test cases show that the program works correctly.
	 */
	public static void main(String args[]){
		SplayTree sptree1 = new SplayTree();
		SplayTree sptree2 = new SplayTree();
		
		/**
		 * Test Case 1:
		 * Construct a splaytree as in Assignment 4 - Question 5
		 * Test the result of find(45), mainly shows the correctness of the node rotation.
		 */
		System.out.println("Test Case 1: ");
		
		SplayNode node10 = new SplayNode(10);
		SplayNode node20 = new SplayNode(20);
		SplayNode node30 = new SplayNode(30);
		SplayNode node40 = new SplayNode(40);
		SplayNode node45 = new SplayNode(45);
		SplayNode node50 = new SplayNode(50);
		SplayNode node60 = new SplayNode(60);
		SplayNode node70 = new SplayNode(70);
		SplayNode node80 = new SplayNode(80);
		SplayNode node120 = new SplayNode(120);
		
		sptree1.root = node80;
		node80.left = node40;
		node80.right = node120;
		node40.left = node20;
		node40.right = node60;
		node20.left = node10;
		node20.right = node30;
		node60.left = node50;
		node60.right = node70;
		node50.left = node45;
		
		System.out.println("The input tree: " + sptree1.toString());
		System.out.println("Is 45 in the tree? " + sptree1.find(45));
		System.out.println("The output tree: " + sptree1.toString());
		
		/**
		 * Test Case 2
		 * Test remaining methods.
		 */
		System.out.println("Test Case 2: ");
		System.out.println("The resulted trees adding nodes in the order: "
				+ "4, 3, 2, 1, 5, 0, 7, 6.");
		sptree2.add(4);
		System.out.println(sptree2.toString());
		sptree2.add(3);
		System.out.println(sptree2.toString());
		sptree2.add(2);
		System.out.println(sptree2.toString());
		sptree2.add(1);
		System.out.println(sptree2.toString());
		sptree2.add(5);
		System.out.println(sptree2.toString());
		sptree2.add(0);
		System.out.println(sptree2.toString());
		sptree2.add(7);
		System.out.println(sptree2.toString());
		sptree2.add(6);
		System.out.println(sptree2.toString());
		
		sptree2.remove(3);
		System.out.println("The tree after remove node 3: " + sptree2.toString());
		
		System.out.println("The count of leaves in the tree: " + sptree2.leafCount());
		System.out.println("The sum of leaves in the tree: " + sptree2.leafSum());
	}
	
}

