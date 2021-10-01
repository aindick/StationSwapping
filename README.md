# StationSwapping

Write a parallel genetic algorithm program for an Facilities Layout problem in which:
There are N stations (N at least 32) and M (M at least N) spots to place them on two-dimensional space (of any shape you like) representing a one-floor factory. (There may be unoccupied spots serving as "holes".) The N stations come in F flavors (with F at least 2) representing their function.
There is a metric representing the benefit (affinity) of placing any two stations A and B near each other based on their flavors and distance, with possibly different maximum values based on capacity or rate. The goal is to maximize total affinity.
Each of K parallel tasks solve by (at least in part randomly) swapping or modifying station spots (possibly with holes), and occasionally exchanging parts of solutions with others. (This is the main concurrent coordination problem.) Run the program on a computer with at least 32 cores (and K at least 32). (You can develop with smaller K.)
The program occasionally (for example twice per second) graphically displays solutions until converged or performs a given number of iterations. Details are up to you
