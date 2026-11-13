public class Outbreak {
    // Mission 5a. Choose whom to test
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] tested = new boolean[n];

        if (n == 0) {
            return tested;
        }

        long[] take = new long[n];
        long[] skip = new long[n];

        fillCosts(0, -1, nb, cost, take, skip);
        restoreTests(0, -1, false, nb, take, skip, tested);

        return tested;
    }

    private static void fillCosts(int u, int parent, int[][] nb, int[] cost,
                                  long[] take, long[] skip) {
        take[u] = cost[u];

        for (int v : nb[u]) {
            if (v == parent) {
                continue;
            }

            fillCosts(v, u, nb, cost, take, skip);

            take[u] += Math.min(take[v], skip[v]);
            skip[u] += take[v];
        }
    }

    private static void restoreTests(int u, int parent, boolean forced, int[][] nb,
                                     long[] take, long[] skip, boolean[] tested) {
        tested[u] = forced || take[u] <= skip[u];

        for (int v : nb[u]) {
            if (v != parent) {
                restoreTests(v, u, !tested[u], nb, take, skip, tested);
            }
        }
    }

    // Mission 5b. Find the longest chain
    public static int longestChain(int n, int[][] nb) {
        if (n == 0) {
            return 0;
        }

        int[] best = {0};
        depth(0, -1, nb, best);
        return best[0];
    }

    private static int depth(int u, int parent, int[][] nb, int[] best) {
        int first = 0;
        int second = 0;

        for (int v : nb[u]) {
            if (v == parent) {
                continue;
            }

            int length = 1 + depth(v, u, nb, best);

            if (length > first) {
                second = first;
                first = length;
            } else if (length > second) {
                second = length;
            }
        }

        best[0] = Math.max(best[0], first + second);
        return first;
    }
}