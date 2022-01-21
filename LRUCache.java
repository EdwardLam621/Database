package driver;
import java.util.HashMap;

class Node {
	int key;
	int value;
	Node ancestor;
	Node successor;

	public Node(int key, int value) {
		this.key = key;
		this.value = value;
	}
}

public class LRUCache {
	private HashMap<Integer, Node> map;
	private int capacity, count;
	private Node head, tail;
	public LRUCache(int capacity) {
		this.capacity = capacity;
		map = new HashMap<>();
		head = new Node(0,0);
		tail = new Node(0,0);
		head.successor = tail;
		head.ancestor = null;
		tail.successor = null;
		tail.ancestor = head;
		count = 0;
	}
	
	public void removeNode(Node n) {
		n.ancestor.successor = n.successor;
		n.successor.ancestor = n.ancestor;
	}
	
	public void insertNode(Node n) {
		n.successor = head.successor;
		n.successor.ancestor = n;
		n.ancestor = head;
		head.successor = n;
	}

	public int get(int key) {
		if (map.get(key) != null) {
			Node n = map.get(key);
			int result = n.value;
			removeNode(n);
			insertNode(n);
			System.out.println("node found -> key: " + key + " value: " + result);
			return result;
		} else {
			System.out.println("node not found -> key: " + key);
			return -1;
		}
		
	}
	
	public void set(int key, int value) {
		System.out.println("Set key = " + key + " and value = " + value);
		if(map.get(key) != null) {				// node exists
			Node n = map.get(key);
			n.value = value;
			removeNode(n);
			insertNode(n);
		}else {									// node does not exist, create new one
			Node n = new Node(key, value);
			map.put(key, n);
			if(count < capacity) {				// if total amount of node is fewer than maximum capacity, simple add new node
				count++;
				insertNode(n);
			}else {								// maximum capacity reached, remove oldest node and add newest node
				map.remove(tail.ancestor,key);
				removeNode(tail.ancestor);
				insertNode(n);
			}
				
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println("initializing LRUCache...");
		LRUCache cache = new LRUCache(2);
		System.out.println("LRUCache initialized.");
		
		cache.set(1,5);
		cache.set(2,10);	//current cache: [(1,5),(2,10)]
		cache.get(2);		//current cache: [(2,10), (1,5)]
		cache.set(3,100);	//current cache: [(1,5),(3,100)]
		cache.get(3);		//since node of key = 2 has been removed, should return -1;
		cache.set(4,99);	//current cache: [(3,100),(4,99)]
		cache.get(1);		//return -1
		cache.get(2);		//return -1
		cache.get(3);
		cache.get(4);
	}

}
