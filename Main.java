public class Main {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        //while loop to select cities coninuously
        while(true){
            //displaying choice of cities
            System.out.println("Choice 1: Istanbul to Trabzon\nChoice 2: Izmir to Diyarbakir\nChoice 3: Gaziantep to Istanbul\nChoice 4: Antalya to Malatya\nChoice 5: Mersin to Urfa\nChoice 6: Samsun to Denizli\nChoice 7: Trabzon to Izmir\nChoice 8: Batman to Ankara\nEnter your choice: ");
            int choice=scanner.nextInt();
            if(choice==1){
                displayResults("Istanbul","Trabzon");
            }else if (choice==2) {
                displayResults("Izmir","Diyarbakir");
            }else if(choice==3){
                displayResults("Gaziantep","Istanbul");
            }else if(choice==4){
                displayResults("Antalya","Malatya");
            }else if(choice==5){
                displayResults("Mersin","Urfa");
            }else if(choice==6){
                displayResults("Samsun","Denizli");
            }else if(choice==7){
                displayResults("Trabzon","Izmir");
            }else if(choice==8){
                displayResults("Batman","Ankara");
            }else
                System.out.println("invalid choice\n");
        }
    }
    //method for calculating distances and displaying results
    public static void displayResults(String startCity, String endCity){
        String filePath = "src/resources/Turkish_cities.csv";
        //reading the file
        CSVReader reader = new CSVReader(filePath);
        //cities array
        String[] cities = reader.getCities();
        //distances array (adjacency matrix)
        int[][] distances = reader.getDistances();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int startIndex = reader.getCityIndex(startCity);
        int endIndex = reader.getCityIndex(endCity);
        //starting the timer to calculate the execotion time for BFS
        long startTime1 = System.nanoTime();
        BFS bfs = new BFS(distances, cities);
        Result bfsResult = bfs.findShortestPath(startIndex, endIndex);
        if (bfsResult.getDistance() != -1) {
            System.out.println("BFS Shortest Path: " + bfsResult.getPath());
            System.out.println("BFS Total Distance: " + bfsResult.getDistance());
            //stopping the timer
            long endTime = System.nanoTime();
            //calculating the time
            long duration = endTime - startTime1;
            System.out.println("BFS Time: "+duration+" nanoseconds\n");
        } else {
            System.out.println("No path found using BFS.");
        }
        //starting the timer to calculate the execution time
        long startTime2 = System.nanoTime();
        DFS dfs = new DFS(distances, cities);
        Result dfsResult = dfs.findShortestPath(startIndex, endIndex);
        if (dfsResult.getDistance() != -1) {
            System.out.println("DFS Shortest Path: " + dfsResult.getPath());
            System.out.println("DFS Total Distance: " + dfsResult.getDistance()+"\n");
            //stopping the timer
            long endTime = System.nanoTime();
            //calculating the time
            long duration = endTime - startTime2;
            System.out.println("DFS Time: "+duration+" nanoseconds\n");
        } else
            System.out.println("No path found using DFS.");
    }
}