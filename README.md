# Assignment 4. Patient Zero

Student ID: 252678
Name:Inzhu
Group:se-2512

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations:112
    Shared core:877
    Marker hits:7
    First hit:8705
    Border length:4
    Shared words:216
    Tests needed:34
    Cheapest testing:177
    Longest chain:28

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

`d[i][j]` is the minimum number of edits needed to turn the first `i`
letters of `a` into the first `j` letters of `b`. I take the minimum
of deleting a letter, inserting a letter, and using the diagonal cell.
The diagonal adds 0 if the letters match and 1 if they differ.
I used the prefix table from Lecture 08. `d[i][j]` is the minimum
number of edits needed to turn the first `i` letters of strain A into
the first `j` letters of strain B.

The first row counts insertions, and the first column counts deletions.
For every other cell, I compare three options: delete a letter, insert
a letter, or use the diagonal cell. The diagonal costs 0 if the letters
match and 1 if I need to replace a letter.

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A subsequence can skip letters and keep their order. A substring must
use letters next to each other. My shared core is 877, but only 282
letters match at the same index. An insertion or deletion can shift
letters to different positions, and LCS can still match them in order.

I used the LCS table from Lecture 08. A subsequence can skip letters
and keep their order. A substring must use consecutive letters.

If the current letters match, I add 1 to the diagonal cell. If they
differ, I take the larger value from the cell above and the cell to
the left. My strains match at 282 equal indices, while the LCS has
length 877. LCS can still match letters in order after an insertion
or deletion shifts them to different indices.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

`pi[i]` gives the length of the longest proper prefix of `p[0..i]`
that is also a suffix of that part. When letters differ, KMP uses `pi`
to shorten the matched part of the pattern, so the text index keeps
moving forward. My marker is `GCTCGGGCTC`. Its border length is 4
because `GCTC` is both its beginning and its end.

I used the LPS idea from String Algorithms I. `pi[i]` stores the
length of the longest proper prefix of `p[0..i]` that is also its
suffix. A proper prefix is shorter than the whole part.

On a mismatch, KMP uses `pi` to shorten the matched prefix. The genome
index stays in place during that step and moves forward through the
genome overall. After a full match, the fallback also lets the program
find overlapping matches.

My marker is `GCTCGGGCTC`. Its longest matching proper prefix and
suffix are both `GCTC`, so its border length is 4.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Two different windows can have the same hash. I compare their letters
before counting a match. If many windows share one hash, the program
may have to check many candidates, so it takes more time.

I used the polynomial rolling hash from String Algorithms I. When
the window moves, I remove the first letter's contribution, shift the
remaining hash, and add the new letter.

Equal hashes give possible matches. I compare the letters because
different windows can share the same hash. If many windows share a
hash, the program may need to check more candidates, which takes more
time. I count each starting position in strain A at most once.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?
I used the bottom-up tree DP pattern from Lecture 08. `take[u]` is the
minimum cost in the subtree if I test person `u`. `skip[u]` is the
minimum cost if I skip that person.

If I test `u`, I choose the cheaper state for each child. If I skip
`u`, I must test every child to cover its link to `u`. After calculating
the costs, I go from the root to the children to recover the selected
people. The program uses costs of 1 for the minimum number of tests
and the real costs for the cheapest plan.

Choosing the person with the most links can cost more. In the chain
`0–1–2` with costs `1, 9, 1`, testing person 1 costs 9. Testing people
0 and 2 covers both links for 2.

For the longest chain, I use the two longest downward branches at each
node, as shown in Lecture 08. Their sum gives the longest path through
that node. The answer is the maximum of these sums, measured in links.

## Lecture references

| Lecture | Slides | Use in this assignment |
|---|---|---|
| Lecture 08 — Dynamic Programming | 8–10, 15–18 | Edit distance, LCS, bottom-up tree DP, and tree diameter |
| String Algorithms I (`Lecture09` file) | 7–17 | KMP, LPS, rolling hash, and collision checks |
| String Algorithms II (`Lecture10` file) | 3–4, 14 | Compares single-pattern search with multi-pattern search. This project has one marker, so it uses KMP. |

## AI use

I used ChatGPT to connect the missions to the lectures, help write
and check the Java code, and draft this README and the startup guide. 

## Timing (Mission 7)


Reference results from `java -cp out Bench`, in milliseconds.
Each value is the average of three runs.

| Test | Sizes | Times in size order (ms) |
|---|---|---|
| Naive search | 20,000 / 40,000 / 80,000 | 29.2 / 67.1 / 138.6 |
| KMP | 20,000 / 40,000 / 80,000 | 3.1 / 1.5 / 0.5 |
| Edit distance | 500 / 1,000 / 2,000 / 4,000 | 6.9 / 8.6 / 28.9 / 114.9 |

When the text doubled from 40,000 to 80,000 letters, naive search
took about twice as long. KMP stayed below 4 ms in this run, but the
short timings did not show a steady increase. When both strains grew
from 2,000 to 4,000 letters, edit distance took about four times as
long, consistent with the fourfold increase in DP table size.

## Complexity
`a` and `b` are the strain lengths. `n` is the genome length, `m` is
the marker length, and `h` is the number of hits. `k` is the window
length, `wB` is the number of windows in strain B, `c` is the number
of candidate window pairs compared, and `v` is the number of people.

| Algorithm | Time | Extra space |
|---|---|---|
| Edit distance | O((a + 1)(b + 1)) | O((a + 1)(b + 1)) |
| LCS | O((a + 1)(b + 1)) | O((a + 1)(b + 1)) |
| Prefix function and KMP | O(n + m) | O(m + h), including hit positions |
| Rabin–Karp | O(a + b + k·c), assuming expected constant-time hash map operations | O(wB) |
| Tree DP and longest chain | O(v) | O(v), including the recursion stack |

2. START_HERE.md

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
