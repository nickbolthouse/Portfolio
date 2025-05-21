IF YOU ENCOUNTER PROBLEMS BUILDING/COMPILING


I couldn't quite get Maven or java's built-in packaging working, so it doesn't build right (for some reason). However, I was able to compile it on my macOS machine with "javac -d . {filename}" by compiling the files in this order: 

HousingUnit, Lot, GameState, Event, InputParser, Game, and Driver. 

This might just be a unix thing, but for some reason compiling in a subdirectory worked. If not, try concatenating them into a single file then running that or let me know so we can work out a solution.