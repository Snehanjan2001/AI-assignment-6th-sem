import java.util.*;
import java.util.stream.*;
public class miscan {
	static class Node{
		int m1, c1, m2, c2;	boolean b;
		Node(int m1, int c1, int m2, int c2, boolean b){
			this.m1 = m1;
			this.c1 = c1;
			this.m2 = m2;
			this.c2 = c2;
			this.b = b;
		}
		Node(int a[]){
			this.m1 = a[0];
			this.c1 = a[1];
			this.m2 = a[2];
			this.c2 = a[3];
			this.b = a[4] % 2 == 1;
		}
		boolean equal(int m1, int c1, int m2, int c2, boolean b) {
			return (this.m1 == m1 && this.c1 == c1 && this.m2 == m2 && this.c2 == c2 && this.b == b);
		}
		boolean equal(int[] a) {
		return (this.m1==a[0] && this.c1==a[1] && this.m2==a[2] && this.c2==a[3] && this.b==(a[4]%2==1));
		}
		static boolean equal(Node x, Node y) {
			return ((x.m1 == y.m1) && (x.m2 == y.m2) && (x.c1 == y.c1) && (x.c2 == y.c2) && (x.b == y.b));
		}
		public String toString() {
		return "[Left-bank = [M = " + m1 + ", C = " + c1 + "], Boat = " + (b ? "Right" : "Left ") + 
				", Right-bank = [M = " + m2 + ", C = " + c2 + "]]";
		}
		String getString() {
			return "[" + m1 + ", " + c1 + (b ? " __B " : " B__ ") + m2 + ", " + c2 + "]";
		}
		void display(String s) {
			System.out.println(s + this);
		}
		void display2(String s) {
			System.out.println(s + m1 + ", " + c1 + (b ? ", Right" : ", Left"));
		}
	}
	class Graph{
		List<Node> G;	Node s, e;
		int m, c, k, B, D;	Double T;
		Graph(int m, int c, boolean sb, boolean eb, int k, int sm, int sc, int em, int ec, int[][] G){
			if(valid(m, c, k) && valid(m, c, sm, sc, em, ec, sb, eb)) {
				this.m = m;
				this.c = c;
				this.k = k;
				this.B = (int)(Stream.iterate(0, i -> i + 1).limit(m + 1).flatMap(i -> Stream
						.iterate(0, j -> j + 1).limit(c + 1).filter(j -> i + j > 0 && i + j <= k && 
						(i >= j || i == 0) && (m - i >= c - j || m == i))).count());
				this.D = (int)(Stream.iterate(0, i -> i + 1).limit(m + 1).flatMap(i -> Stream
						.iterate(0, j -> j + 1).limit(c + 1).filter(j -> (i >= j || i == 0) && 
								(m - i >= c - j || m == i))).count());
				this.s = new Node(sm, sc, m - sm, c - sc, sb);
				this.e = new Node(em, ec, m - em, c - ec, eb);
				this.G = (G == null) ? Arrays.asList(this.e) : Arrays.stream(G)
						.filter(x -> (x[0]*(x[1]) <= 0) && (x[1]*(x[1] - c) <= 0))
						.map(x -> new Node(x[0], x[1], m - x[0], c - x[1], x[2] % 2 == 1))
						.collect(Collectors.toList());
				display();
			}
			else
				System.out.println("Invalid Inputs");
		}
		boolean valid(int m, int c, int k) {
			return (m > 0) && (c > 0) && (m >= c) && (k > 0);
		}
		boolean valid(int m, int c, int sm, int sc, int em, int ec, boolean sb, boolean eb) {
			return ((sm*(sm - m) <= 0) && (em*(em - m) <= 0) && (sc*(sc - c) <= 0) && (ec*(ec - c) <= 0) && 
					(sm >= sc || sm == 0) && (m - sm >= c - sc || sm == m) && 
					(em >= ec || em == 0) && (m - em >= c - ec || em == m) && 
					(sb ? sm + sc < m + c : sm + sc > 0));
		}
		void display() {
			System.out.println("Missionary-Cannibal Problem with Missionaries = " + m 
					+ ", Cannibals = " + c + ", Boat-Capacity = " + k);
		}
		int dist(Node x, Node y) {
			return 1;
		}
		int heuristic(Node x) {
			return G.stream().anyMatch(i -> Node.equal(i, x)) ? 0 : 1;
		}
		Stream<int[]> tran(Node x){
			return Stream.iterate(0, i -> i + 1).limit(k + 1)
					.flatMap(i -> Stream.iterate(0, j -> j + 1).limit(k + 1)
							.filter(j -> i + j > 0 && i + j <= k && (i >= j || i == 0))
							.map(j -> new int[] {i, j}))
					.filter(a -> x.b ?	x.m2 >= a[0] && x.c2 >= a[1] && 
								(x.m2 - a[0] >= x.c2 - a[1] || x.m2 == a[0]) && 
								(x.m1 + a[0] >= x.c1 + a[1] || x.m1 + a[0] == 0): 
										x.m1 >= a[0] && x.c1 >= a[1] && 
								(x.m1 - a[0] >= x.c1 - a[1] || x.m1 == a[0]) && 
								(x.m2 + a[0] >= x.c2 + a[1] || x.m2 + a[0] == 0));
		}
		Stream<int[]> moves(Node x){
			return tran(x).map(i -> x.b ? 
					new int[] {x.m1 + i[0], x.c1 + i[1], x.m2 - i[0], x.c2 - i[1], 0} : 
					new int[] {x.m1 - i[0], x.c1 - i[1], x.m2 + i[0], x.c2 + i[1], 1});
		}
		void showMoves(Node x) {
			System.out.println(tran(x).reduce("Moves = ", (a, i) -> a + conv(i) + ", ", (a, b) -> a + b));
		}
		String conv(int[] m) {
			return "[M = " + m[0] + ", C = " + m[1] + "]";
		}
		void path(boolean c, List<Node> p) {
			T = System.nanoTime() - T;	T *= 0.000001;
			System.out.println((c ? "Path = " + p.stream().map(x -> x.getString())
					.collect(Collectors.toList()) : "No path Found") + "\nTime = " + T + "ms");
		}
		private boolean bfs(Queue<Node> l, List<Node> v, List<Node> p) {
			Map<Node, Node> q = new HashMap<>();	int z;
			for(l.add(s) , z = 1 ; z > 0 ; ){
				l.peek().display("Current node = ");
				v.add(0, l.peek());
				if(Node.equal(l.peek(), e))
					break;
				showMoves(l.peek());
				z += moves(l.peek())
					.filter(i -> !v.stream().anyMatch(j -> j.equal(i)))
					.filter(i -> !l.stream().anyMatch(j -> j.equal(i)))
					.reduce(-1, (y, i) -> {Node k = new Node(i);
							l.add(k); q.put(k, l.peek()); return ++y;}, (u, y) -> u + y);
				l.remove();
			}
			if(z == 0)
				return false;
			for(p.add(0, l.peek()) ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		private boolean bls(Node s, List<Node> v, List<Node> p, int c){
			s.display("Current node = ");
			v.add(0, s);
			p.add(0, s);
			if(Node.equal(s, e)) {
				Collections.reverse(p);
				return true;
			}
			showMoves(s);
			if(moves(s).filter(i -> !v.stream().anyMatch(j -> j.equal(i))).limit(c)
				.reduce(false, (y, i) -> y ? y : bls(new Node(i), v, p, c), (y, b) -> y || b))
					return true;
			p.remove(0);
			return false;
		}
		private boolean ibs(Node s, List<Node> v, List<Node> p) {
			for(int i = 0 ; i <= B ; i++) {
				System.out.println("Breadth-limit = " + i);
				if(bls(s, v, p, i))
					return true;
				v = new LinkedList<>();
			}
			return false;
		}
		private boolean dfs(Node s, List<Node> v, List<Node> p) {
			s.display("Current node = ");
			v.add(0, s);
			p.add(0, s);
			if(Node.equal(s, e)) {
				Collections.reverse(p);
				return true;
			}
			showMoves(s);
			if(moves(s).filter(i -> !v.stream().anyMatch(j -> j.equal(i)))
			.reduce(false, (y, i) -> y ? y : dfs(new Node(i), v, p), (y, b) -> y || b))
				return true;
			p.remove(0);
			return false;
		}
		private boolean dls(Node s, List<Node> v, List<Node> p, int c, int d) {
			s.display("Current node = ");
			v.add(0, s);
			p.add(0, s);
			if(Node.equal(s, e)) {
				Collections.reverse(p);
				return true;
			}
			if(d == c) {
				p.remove(0);
				return false;
			}
			showMoves(s);
			if(moves(s).filter(i -> !v.stream().anyMatch(j -> j.equal(i)))
			.reduce(false, (y, i) -> y ? y : dls(new Node(i),v,p,c,d+1), (y, b) -> y || b)) {
				return true;
			}
			p.remove(0);
			return false;
		}
		private boolean ids(Node s, List<Node> v, List<Node> p) {
			for(int i = 0 ; i <= D ; i++) {
				System.out.println("Depth-limit = " + i);
				if(dls(s, v, p, i, 0))
					return true;
				v = new LinkedList<>();
			}
			return false;
		}
		private boolean ucs(Queue<Node> l, List<Node> v, List<Node> p) {
			Map<Node, Integer> d = new HashMap<>();
			Map<Node, Node> q = new HashMap<>();
			int z;	l.add(s);	d.put(s, 0);	q.put(s, null);
			for(z = 1 ; z > 0 ; ) {
				Node x = d.keySet().stream()
						.filter(i -> !v.stream().anyMatch(j -> Node.equal(i, j)))
						.filter(i -> l.stream().anyMatch(j -> Node.equal(i, j)))
						.reduce((a, b) -> d.get(a) < d.get(b) ? a : b).orElse(null);
				x.display("Current node = ");
				v.add(0, x);
				if(G.stream().anyMatch(i -> Node.equal(i, x))) {
					p.add(0, x);
					break;
				}
				z += moves(x)
						.filter(i -> !v.stream().anyMatch(j -> j.equal(i)))
						.filter(i -> !l.stream().anyMatch(j -> j.equal(i)))
						.map(i -> d.keySet().stream().filter(j -> j.equal(i))
								.findAny().orElse(new Node(i)))
						.reduce(-1, (a, i) -> {
							if(!d.containsKey(i) || (d.get(x) + dist(x, i) < d.get(i)))
							{l.add(i);	d.put(i, d.get(x) + dist(x, i));	q.put(i, x);}
							return ++a;
				}, (a, b) -> a + b);
			}
			if(z == 0)
				return false;
			for( ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		private boolean astar(Queue<Node> l, List<Node> v, List<Node> p) {
			Map<Node, Integer> d = new HashMap<>(), a_ = new HashMap<>();
			Map<Node, Node> q = new HashMap<>();
			int z;	l.add(s);	d.put(s, 0);	a_.put(s, heuristic(s));	q.put(s, null);
			for(z = 1 ; z > 0 ; ) {
				Node x = d.keySet().stream()
						.filter(i -> !v.stream().anyMatch(j -> Node.equal(i, j)))
						.filter(i -> l.stream().anyMatch(j -> Node.equal(i, j)))
						.reduce((a, b) -> a_.get(a) < a_.get(b) ? a : b).orElse(null);
				x.display("Current node = ");
				v.add(0, x);
				if(a_.get(x) == d.get(x)) {
					p.add(0, x);
					break;
				}
				z += moves(x)
						.filter(i -> !v.stream().anyMatch(j -> j.equal(i)))
						.filter(i -> !l.stream().anyMatch(j -> j.equal(i)))
						.map(i -> d.keySet().stream().filter(j -> j.equal(i))
								.findAny().orElse(new Node(i)))
						.reduce(-1, (a, i) -> {
							if(!d.containsKey(i) || (d.get(x) + dist(x, i) + heuristic(i) < d.get(i)))
							{d.put(i, d.get(x) + dist(x, i));	a_.put(i, d.get(i) + heuristic(i));
							l.add(i);	q.put(i, x);}	return ++a;
				}, (a, b) -> a + b);
			}
			if(z == 0)
				return false;
			for( ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		private boolean Bfs(Queue<Node> l, List<Node> v, List<Node> p) {
			List<Node> d = new LinkedList<>();
			Map<Node, Node> q = new HashMap<>();
			int z;	l.add(s);	d.add(0, s);	q.put(s, null);
			for(z = 1 ; z > 0 ; ) {
				Node x = d.stream()
						.filter(i -> !v.stream().anyMatch(j -> Node.equal(i, j)))
						.filter(i -> l.stream().anyMatch(j -> Node.equal(i, j)))
						.reduce((a, b) -> heuristic(a) < heuristic(b) ? a : b).orElse(null);
				x.display("Current node = ");
				v.add(0, x);
				if(heuristic(x) == 0) {
					p.add(0, x);
					break;
				}
				z += moves(x)
						.filter(i -> !v.stream().anyMatch(j -> j.equal(i)))
						.filter(i -> !l.stream().anyMatch(j -> j.equal(i)))
						.map(i -> d.stream().filter(j -> j.equal(i))
								.findAny().orElse(new Node(i)))
						.reduce(-1, (a, i) -> {
							if(!d.stream().anyMatch(j -> Node.equal(i, j)))
							{d.add(0, i);	l.add(i);	q.put(i, x);}	return ++a;
				}, (a, b) -> a + b);
			}
			if(z == 0)
				return false;
			for( ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		void search(boolean[] c, int bl, int dl) {
			System.out.println("Running search techniques with start = " + s + 
					"\nend = " + e + " ...");
			List<Node> p, v;	Queue<Node> l;
			if(c[0]) {
				p = new LinkedList<>(); v = new LinkedList<>(); l = new LinkedList<>();
				System.out.println("\nRunning bfs :-");	T = (double) System.nanoTime();
				path(bfs(l, v, p), p);
			}
			if(c[1] && (bl > -1)) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning bls with breadth-limit = " + bl + " :-");
				path(bls(s, v, p, bl) , p);
			}
			if(c[2]) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning ibs :-");
				path(ibs(s, v, p) , p);
			}
			if(c[3]) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning dfs :-");
				path(dfs(s, v, p) , p);
			}
			if(c[4] && (dl > -1)) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning dls with depth-limit = " + dl + " :-");
				path(dls(s, v, p, dl, 0) , p);
			}
			if(c[5]) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning ids :-");
				path(ids(s, v, p) , p);
			}
			if(c[6]) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning ucs :-");
				path(ucs(new LinkedList<>(), v, p) , p);
			}
			if(c[7]) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning A* :-");
				path(astar(new LinkedList<>(), v, p) , p);
			}
			if(c[8]) {
				p = new LinkedList<>(); v = new LinkedList<>();	T = (double) System.nanoTime();
				System.out.println("\nRunning Best first :-");
				path(Bfs(new LinkedList<>(), v, p) , p);
			}
		}
	}
	public static void main(String[] args) {
		int m = 3, c = 3, k = 2, sm = 3, sc = 3, em = 0, ec = 0, bl = 1, dl = 1;
		boolean sb = false, eb = true;
		boolean ch[] = {true, false, false, false, false, false, false, false, false};
		int g[][] = null;
		miscan ob = new miscan();
		Graph G = ob.new Graph(m, c, sb, eb, k, sm, sc, em, ec, g);
		G.search(ch, bl, dl);
	}
}