### Final Project for Algorithms and Data Structures II

This project involved analysing data collected from the [Montréal Bus System API](https://www.stm.info/en/about/developers) and given to us by our lecturer, [Dr. Ivana Dusparic](https://www.scss.tcd.ie/Ivana.Dusparic/). We were tasked with using the appropriate data structures and algorithms to solve queries given. Datastructures were given by [Algorithms, 4th Edition](https://algs4.cs.princeton.edu/home/).

#### [Shortest Path](https://github.com/ni-sauvage/Algs-Final-Project/blob/master/src/FinalProject/EdgeWeightedDigraph.java)
In order to calculate the shortest path between stops, we first created a [directed weighted graph](https://en.wikipedia.org/wiki/Directed_graph) object by reading in our file data. We then ran Dijkstra's shortest path algorithm to find the shortest path between bus-stops.

#### [Find Stop](https://github.com/ni-sauvage/Algs-Final-Project/blob/master/src/FinalProject/searchStops.java)
To find each stop by its name, we read our data into a [Ternary Search Tree](https://en.wikipedia.org/wiki/Ternary_search_tree) and used tree traversal in order to return our stop(s).

#### [Arrival Time](https://github.com/ni-sauvage/Algs-Final-Project/blob/master/src/FinalProject/searchTimes.java)
In order to find all buses that stop at a particular time, I used a [hashtable](https://en.wikipedia.org/wiki/Hash_table) which contained [an arraylist](https://en.wikipedia.org/wiki/Dynamic_array). The index of the hashtable was the arrival times, which gave lookup in `O(1)` after initially reading in.

### [TUI](https://github.com/ni-sauvage/Algs-Final-Project/blob/master/src/FinalProject/FinalProject.java)
This project used a fairly simple TUI with scanner objects in order to accept user input and queries. 



