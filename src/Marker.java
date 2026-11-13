import java.util.ArrayList;
import java.util.HashMap;

public class Marker {
    private static final long BASE = 31;
    private static final long MOD = 1_000_000_007L;

    // Mission 3a. Prefix function
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];

        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];

            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }

    // Mission 3b. KMP search
    public static int[] findAll(String text, String p) {
        if (p.isEmpty()) {
            return new int[0];
        }

        int[] pi = prefixFunction(p);
        ArrayList<Integer> hits = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }

            if (j == p.length()) {
                hits.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }

        int[] result = new int[hits.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = hits.get(i);
        }

        return result;
    }

    // Mission 4. Rabin–Karp with a rolling hash
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || k > a.length() || k > b.length()) {
            return 0;
        }

        long power = 1;
        long hashA = 0;
        long hashB = 0;

        for (int i = 0; i < k; i++) {
            hashA = (hashA * BASE + a.charAt(i)) % MOD;
            hashB = (hashB * BASE + b.charAt(i)) % MOD;

            if (i < k - 1) {
                power = power * BASE % MOD;
            }
        }

        HashMap<Long, ArrayList<Integer>> positionsByHash = new HashMap<>();

        for (int i = 0; i <= b.length() - k; i++) {
            positionsByHash.computeIfAbsent(hashB, key -> new ArrayList<>()).add(i);

            if (i < b.length() - k) {
                hashB = slideHash(hashB, b.charAt(i), b.charAt(i + k), power);
            }
        }

        int count = 0;

        for (int i = 0; i <= a.length() - k; i++) {
            ArrayList<Integer> candidates = positionsByHash.get(hashA);

            if (candidates != null) {
                for (int j : candidates) {
                    int matched = 0;

                    while (matched < k && a.charAt(i + matched) == b.charAt(j + matched)) {
                        matched++;
                    }

                    if (matched == k) {
                        count++;
                        break;
                    }
                }
            }

            if (i < a.length() - k) {
                hashA = slideHash(hashA, a.charAt(i), a.charAt(i + k), power);
            }
        }

        return count;
    }

    private static long slideHash(long hash, char outgoing, char incoming, long power) {
        long removed = outgoing * power % MOD;
        long remaining = (hash - removed + MOD) % MOD;
        return (remaining * BASE + incoming) % MOD;
    }
}