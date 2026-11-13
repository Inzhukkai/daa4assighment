public class Strains {
    // Mission 1. Edit distance
    public static int editDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] d = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int delete = d[i - 1][j] + 1;
                int insert = d[i][j - 1] + 1;
                int change = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                int diagonal = d[i - 1][j - 1] + change;

                d[i][j] = Math.min(diagonal, Math.min(delete, insert));
            }
        }

        return d[n][m];
    }

    // Mission 2. Longest common subsequence
    public static int lcsLength(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[n][m];
    }
}