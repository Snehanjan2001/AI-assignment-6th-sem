import java.util.*;
import java.util.stream.*;
public class Maze {
	class Graph{
		int m, n, s, B, D;	byte[][] a, b;	double T;
		final String[] M = {"L", "R", "U", "D"};
		public Graph(int m, int n, int si, int sj, byte[][] a) {
			if(valid(m, n, si, sj, a)) {
				this.m = m;
				this.n = n;
				this.s = si * n + sj;
				this.a = a;
				this.b = new byte[m][n];
				this.B = 4;
				this.D = m * n;
				display();
			}
			else
				System.out.println("Invalid inputs");
		}
		boolean valid(int m, int n, int si, int sj, byte[][] a) {
			if(m <= 0 || n <= 0 || si*(si - m + 1) > 0 || sj*(sj - n + 1) > 0)
				return false;
			if(a.length != m || Arrays.stream(a).reduce(false, (acc, x) -> acc || x.length != n, (x, y) -> x||y))
				return false;
			if(a[si][sj] == 0)
				return false;
			return Arrays.stream(a).reduce(0, (acc, x) -> acc + Arrays.asList(x).stream()
					.reduce(0, (ac, y) -> {for(byte b : y) if(b*(b - 2) > 0) ++ac; return ac;}, 
							(a1, a2) -> a1 + a2), (u, v) -> u + v) == 0;
		}
		void copy() {
			for(int i = 0 ; i < m * n ; b[i / n][i % n] = a[i / n][i++ % n]);
		}
		void display() {
			System.out.println("Maze problem on grid :- ");
			Arrays.stream(a).map(x -> {String s = "";for(byte b : x) s += b + "\t"; return s;})
							.forEach(System.out::println);
			System.out.println("with start index = " + s / n + ", " + s % n);
		}
		int dist(int x, int y) {
			return (x/n > y/n ? x/n - y/n : y/n - x/n) + (x%n > y%n ? x%n - y%n : y%n - x%n);
		}
		int heuristic(int p) {
			switch(b[p / n][p % n]) {
				case 0 : return -1;
				case 2 : return 0;
				default: return 1;
			}
		}
		// Moves = {0 = L, 1 = R, 2 = U, 3 = D};
		int[] tran(int p){
			int[] z =		{	p % n > 0		&& b[p / n][p % n - 1] > 0 ? p - 1 : -1, 
								p % n < n - 1	&& b[p / n][p % n + 1] > 0 ? p + 1 : -1, 
								p / n > 0		&& b[p / n - 1][p % n] > 0 ? p - n : -1, 
								p / n < m - 1	&& b[p / n + 1][p % n] > 0 ? p + n : -1};
			System.out.println(Arrays.asList(0, 1, 2, 3).stream().filter(i -> z[i] > -1)
					.reduce("\nMoves = ", (a, i) -> a + M[i] + ", ", (a, b) -> a + b));
			return z;
		}
		void display(String s1, String s2) {
			Stream.iterate(0, i -> i + 1).limit(m).map(i -> Stream.iterate(0, j -> j + 1).limit(n)
					.reduce(i == 0 ? s1 : s2, (a, j) -> a + b[i][j] + "\t", (x, y) -> x + y))
			.forEach(System.out::println);
		}
		void path(boolean c, List<Integer> p) {
			T = System.nanoTime() - T;	T *= 0.000001;
			System.out.println((c ? "\nPath = " + p.stream().map(i -> "[" + i / n + ", " + i % n + "]")
					.collect(Collectors.toList()) : "\nNo path found") + "\n\nTime = " + T + "ms");
		}
		private boolean bfs(Queue<Integer> l, List<Integer> v, List<Integer> p) {
			Map<Integer, Integer> q = new HashMap<>();	int z;
			for(l.add(s) , z = 1 ; z > 0 ; ) {
				display("\nCurrent Node =\t", "\t\t");
				v.add(0, l.peek());
				if(b[l.peek() / n][l.peek() % n] == 2)
					break;
				b[l.peek() / n][l.peek() % n] = -1;
				z += Arrays.stream(tran(l.peek())).filter(i -> i > -1)
						.filter(i -> !v.stream().anyMatch(j -> j == i))
						.filter(i -> !l.stream().anyMatch(j -> j == i))
						.reduce(-1, (a, i) -> {l.add(i); q.put(i, l.peek()); return ++a;});
				l.remove();
			}
			if(z == 0)
				return false;
			for(p.add(0, l.peek()) ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		private boolean bls(int s, List<Integer> v, List<Integer> p, int c) {
			display("\nCurrent Node =\t", "\t\t");
			v.add(0, s);
			p.add(0, s);
			if(b[s / n][s % n] == 2) {
				Collections.reverse(p);
				return true;
			}
			b[s / n][s % n] = -1;
			if(Arrays.stream(tran(s)).filter(i -> i > -1)
					.filter(i -> !v.stream().anyMatch(j -> i == j)).limit(c)
					.reduce(-1, (a, i) -> a > -1 ? a : bls(i, v, p, c) ? 0 : -1) > -1)
				return true;
			p.remove(0);
			return false;
		}
		private boolean ibs(int s, List<Integer> v, List<Integer> p) {
			for(int i = 0 ; i < B ; i++) {
				System.out.println("\nBreadth-limit = " + i);
				if(bls(s, v, p, i))
					return true;
				v = new LinkedList<>();
				copy();
			}
			return false;
		}
		private boolean dfs(int s, List<Integer> v, List<Integer> p) {
			display("\nCurrent Node =\t", "\t\t");
			v.add(0, s);
			p.add(0, s);
			if(b[s / n][s % n] == 2) {
				Collections.reverse(p);
				return true;
			}
			b[s / n][s % n] = -1;
			if(Arrays.stream(tran(s)).filter(i -> i > -1)
					.filter(i -> !v.stream().anyMatch(j -> i == j))
					.reduce(-1, (a, i) -> a > -1 ? a : dfs(i, v, p) ? 0 : -1) > -1)
				return true;
			p.remove(0);
			return false;
		}
		private boolean dls(int s, List<Integer> v, List<Integer> p, int c, int d) {
			display("\nCurrent Node =\t", "\t\t");
			v.add(0, s);
			p.add(0, s);
			if(b[s / n][s % n] == 2) {
				Collections.reverse(p);
				return true;
			}
			if(d == c) {
				p.remove(0);
				return false;
			}
			b[s / n][s % n] = -1;
			if(Arrays.stream(tran(s)).filter(i -> i > -1)
					.filter(i -> !v.stream().anyMatch(j -> i == j))
					.reduce(-1, (a, i) -> a > -1 ? a : dls(i, v, p, c, d + 1) ? 0 : -1) > -1)
				return true;
			p.remove(0);
			return false;
		}
		private boolean ids(int s, List<Integer> v, List<Integer> p) {
			for(int i = 0 ; i < D ; i++) {
				System.out.println("\nDepth-limit = " + i);
				if(dls(s, v, p, i, 0))
					return true;
				v = new LinkedList<>();
				copy();
			}
			return false;
		}
		private boolean ucs(Queue<Integer> l, List<Integer> v, List<Integer> p) {
			Map<Integer, Integer> d = new HashMap<>(), q = new HashMap<>();
			int z;	l.add(s);	d.put(s, 0);	q.put(s, null);
			for( z = 1 ; z > 0 ; ) {
				int x = d.keySet().stream()
						.filter(i -> !v.stream().anyMatch(j -> i == j))
						.filter(i -> l.stream().anyMatch(j -> i == j))
						.reduce((i, j) -> d.get(i) < d.get(j) ? i : j).orElse(null);
				display("\nCurrent Node =\t", "\t\t");
				v.add(0, x);
				if(b[x / n][x % n] == 2) {
					p.add(0, x);
					break;
				}
				b[x / n][x % n] = -1;
				z += Arrays.stream(tran(x)).filter(i -> i > -1)
						.filter(i -> !v.stream().anyMatch(j -> i == j))
						.filter(i -> !l.stream().anyMatch(j -> i == j))
						.reduce(-1, (ac, i) -> {
							if(!d.containsKey(i) || d.get(x) + dist(x, i) < d.get(i)) 
							{l.add(i);	d.put(i, d.get(x) + dist(x, i));	q.put(i, x);}
							return ++ac;});
			}
			if(z == 0)
				return false;
			for( ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		private boolean astar(Queue<Integer> l, List<Integer> v, List<Integer> p) {
			Map<Integer, Integer> d = new HashMap<>(), q = new HashMap<>(), a_ = new HashMap<>();
			int z;	l.add(s);	d.put(s, 0);	a_.put(s, heuristic(s));	q.put(s, null);
			for( z = 1 ; z > 0 ; ) {
				int x = d.keySet().stream()
						.filter(i -> !v.stream().anyMatch(j -> i == j))
						.filter(i -> l.stream().anyMatch(j -> i == j))
						.reduce((i, j) -> a_.get(i) < a_.get(j) ? i : j).orElse(null);
				display("\nCurrent Node =\t", "\t\t");
				v.add(0, x);
				if(a_.get(x) == d.get(x)) {
					p.add(0, x);
					break;
				}
				b[x / n][x % n] = -1;
				z += Arrays.stream(tran(x)).filter(i -> i > -1)
						.filter(i -> !v.stream().anyMatch(j -> i == j))
						.filter(i -> !l.stream().anyMatch(j -> i == j))
						.reduce(-1, (ac, i) -> {
							if(!d.containsKey(i) || d.get(x) + dist(x, i) + heuristic(i) < d.get(i)) 
							{d.put(i, d.get(x) + dist(x, i));	a_.put(i, d.get(i) + heuristic(i));
							l.add(i);	q.put(i, x);}	return ++ac;});
			}
			if(z == 0)
				return false;
			for( ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		private boolean Bfs(Queue<Integer> l, List<Integer> v, List<Integer> p) {
			List<Integer> d = new LinkedList<>();	Map<Integer, Integer> q = new HashMap<>();
			int z;	l.add(s);	d.add(0, s);	q.put(s, null);
			for( z = 1 ; z > 0 ; ) {
				int x = d.stream()
						.filter(i -> !v.stream().anyMatch(j -> i == j))
						.filter(i -> l.stream().anyMatch(j -> i == j))
						.reduce((i, j) -> heuristic(i) < heuristic(j) ? i : j).orElse(null);
				display("\nCurrent Node =\t", "\t\t");
				v.add(0, x);
				if(heuristic(x) == 0) {
					p.add(0, x);
					break;
				}
				b[x / n][x % n] = -1;
				z += Arrays.stream(tran(x)).filter(i -> i > -1)
						.filter(i -> !v.stream().anyMatch(j -> i == j))
						.filter(i -> !l.stream().anyMatch(j -> i == j))
						.reduce(-1, (ac, i) -> {
							if(!d.stream().anyMatch(j -> i == j)) 
							{l.add(i);	d.add(0, i);	q.put(i, x);}
							return ++ac;});
			}
			if(z == 0)
				return false;
			for( ; p.get(0) != s ; p.add(0, q.get(p.get(0))));
			return true;
		}
		void search(boolean c[], int bl, int dl) {
			System.out.println("\nRunning search algorithms with start postion = " + s/n + ", " + s%n + "...");
			List<Integer> p, v;	Queue<Integer> l;
			if(c[0]) {
				p = new LinkedList<>();	v = new LinkedList<>();	l = new LinkedList<>();
				System.out.println("\n\nRunning bfs :- ");
				copy();	T = System.nanoTime();	path(bfs(l, v, p), p);
			}
			if(c[1] && bl > -1) {
				p = new LinkedList<>();	v = new LinkedList<>();
				System.out.println("\n\nRunning bls with Breadth-Limit = " + bl + " :- ");
				copy();	T = System.nanoTime();	path(bls(s, v, p, bl), p);
			}
			if(c[2]) {
				p = new LinkedList<>();	v = new LinkedList<>();	l = new LinkedList<>();
				System.out.println("\n\nRunning ibs :- ");
				copy();	T = System.nanoTime();	path(ibs(s, v, p), p);
			}
			if(c[3]) {
				p = new LinkedList<>();	v = new LinkedList<>();
				System.out.println("\n\nRunning dfs :- ");
				copy();	T = System.nanoTime();	path(dfs(s, v, p), p);
			}
			if(c[4] && dl > -1) {
				p = new LinkedList<>();	v = new LinkedList<>();
				System.out.println("\n\nRunning dls with Depth-Limit = " + dl + " :- ");
				copy();	T = System.nanoTime();	path(dls(s, v, p, dl, 0), p);
			}
			if(c[5]) {
				p = new LinkedList<>();	v = new LinkedList<>();	l = new LinkedList<>();
				System.out.println("\n\nRunning ids :- ");
				copy();	T = System.nanoTime();	path(ids(s, v, p), p);
			}
			if(c[6]) {
				p = new LinkedList<>();	v = new LinkedList<>();	l = new LinkedList<>();
				System.out.println("\n\nRunning ucs :- ");
				copy();	T = System.nanoTime();	path(ucs(l, v, p), p);
			}
			if(c[7]) {
				p = new LinkedList<>();	v = new LinkedList<>();	l = new LinkedList<>();
				System.out.println("\n\nRunning A* :- ");
				copy();	T = System.nanoTime();	path(astar(l, v, p), p);
			}
			if(c[8]) {
				p = new LinkedList<>();	v = new LinkedList<>();	l = new LinkedList<>();
				System.out.println("\n\nRunning Best first :- ");
				copy();	T = System.nanoTime();	path(Bfs(l, v, p), p);
			}
		}
	}
	public static void main(String[] args) {
		int m = 4, n = 4, si = 0, sj = 0;
		byte[][] b = {	{1, 1, 1, 2}, 
						{0, 1, 0, 1}, 
						{1, 1, 1, 1}, 
						{1, 0, 0, 0}};
		
		int bl = 1, dl = 1;
		boolean c[] = {true, false, false, false, false, false, false, false, false};
		Maze ob = new Maze();
		Graph G = ob.new Graph(m, n, si, sj, b);
		G.search(c, bl, dl);
	}
}