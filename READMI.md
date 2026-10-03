# Design and Analysis of Algorithms: Assignment 2 (Data Structures)

Alikhan Zhambayev, SE-2517

In-memory workload engine with three data structures written from scratch (no `java.util` collections, `int` values only):

| Structure | Description |
|---|---|
| `DynamicArray` | resizable `int[]`, grows 2x when full |
| `MyLinkedList` | singly linked list with `head` and `tail` pointers |
| `MinHeap` | array-based binary min-heap |

The project counts **steps**, **moves** and **comparisons** inside the operations themselves, benchmarks the structures on four workloads and exports everything to `results/results.csv`.




## Run the benchmark

From IntelliJ IDEA: run `daa.Main`.

From the command line:




## Workloads

All structures are filled with the same data from `new Random(42)`. Sizes: `n = 100, 1 000, 10 000, 100 000`.

| Workload | What is measured | Structures |
|---|---|---|
| W1 random access | fill with `n` values, then 10 000 `get(index)` with a random index | DynamicArray, MyLinkedList |
| W2 search | 1 000 `contains(x)` queries, half of the values present, half absent | DynamicArray, MyLinkedList |
| W3 insert & remove | 1 000 insertions followed by 1 000 removals at index 0 (`head`) or at index `n / 2` (`middle`) | DynamicArray, MyLinkedList |
| W4 priority processing | insert `n` values, then `extractMin()` `n` times; the output is checked to be non-decreasing | MinHeap |

Measurement method: every case is run 3 + 5 times after a global JVM warm-up pass. The first 3 runs of each case are discarded, and the **median** time of the remaining 5 runs is saved. Filling the structure is not part of the measured time (counters are reset before the timer starts). Counter values come from the last run (the data is identical in every run).

