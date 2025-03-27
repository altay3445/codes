import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Primary Stage Settings
        primaryStage.setTitle("Shortest Path Finder");

        // GridPane for layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Dropdown for city pair selection
        ComboBox<String> cityPairBox = new ComboBox<>();
        cityPairBox.getItems().addAll(
                "1: Istanbul and Trabzon",
                "2: Izmir and Diyarbakir",
                "3: Gaziantep and Istanbul",
                "4: Antalya and Malatya",
                "5: Mersin and Urfa",
                "6: Samsun and Denizli",
                "7: Trabzon and Izmir",
                "8: Batman and Ankara"
        );
        cityPairBox.setPromptText("Select City Pair");

        // Button to find shortest path
        Button findPathButton = new Button("Find Shortest Path");

        // TextArea for output
        TextArea outputArea = new TextArea();
        outputArea.setPrefHeight(300);
        outputArea.setEditable(false);

        // Add components to GridPane
        grid.add(new Label("Select a City Pair:"), 0, 0);
        grid.add(cityPairBox, 1, 0);
        grid.add(findPathButton, 1, 1);
        grid.add(outputArea, 0, 2, 2, 1);

        // Button Action
        findPathButton.setOnAction(e -> {
            String selected = cityPairBox.getValue();
            if (selected == null) {
                outputArea.setText("Please select a city pair.");
                return;
            }

            String[] cities = {
                    "Istanbul", "Trabzon", "Izmir", "Diyarbakir",
                    "Gaziantep", "Istanbul", "Antalya", "Malatya",
                    "Mersin", "Urfa", "Samsun", "Denizli",
                    "Trabzon", "Izmir", "Batman", "Ankara"
            };
            CSVReader reader = new CSVReader("src/resources/Turkish_cities.csv");
            String[] cityArray = reader.getCities();
            int[][] distances = reader.getDistances();
            StringBuilder result = new StringBuilder();

            String startCity, endCity;
            switch (selected.charAt(0)) {
                case '1': startCity = "Istanbul"; endCity = "Trabzon"; break;
                case '2': startCity = "Izmir"; endCity = "Diyarbakir"; break;
                case '3': startCity = "Gaziantep"; endCity = "Istanbul"; break;
                case '4': startCity = "Antalya"; endCity = "Malatya"; break;
                case '5': startCity = "Mersin"; endCity = "Urfa"; break;
                case '6': startCity = "Samsun"; endCity = "Denizli"; break;
                case '7': startCity = "Trabzon"; endCity = "Izmir"; break;
                case '8': startCity = "Batman"; endCity = "Ankara"; break;
                default: outputArea.setText("Invalid choice."); return;
            }

            int startIndex = reader.getCityIndex(startCity);
            int endIndex = reader.getCityIndex(endCity);

            if (startIndex == -1 || endIndex == -1) {
                outputArea.setText("Invalid city selection. Please try again.");
                return;
            }

            // BFS Implementation
            BFS bfs = new BFS(distances, cityArray);
            long bfsStartTime = System.nanoTime();
            Result bfsResult = bfs.findShortestPath(startIndex, endIndex);
            long bfsEndTime = System.nanoTime();
            if (bfsResult.getDistance() != -1) {
                result.append("BFS Shortest Path: ").append(bfsResult.getPath()).append("\n");
                result.append("BFS Total Distance: ").append(bfsResult.getDistance()).append(" km\n");
                result.append("BFS Time: ").append(bfsEndTime - bfsStartTime).append(" nanoseconds\n\n");
            } else {
                result.append("No path found using BFS.\n");
            }

            // DFS Implementation
            DFS dfs = new DFS(distances, cityArray);
            long dfsStartTime = System.nanoTime();
            Result dfsResult = dfs.findShortestPath(startIndex, endIndex);
            long dfsEndTime = System.nanoTime();
            if (dfsResult.getDistance() != -1) {
                result.append("DFS Shortest Path: ").append(dfsResult.getPath()).append("\n");
                result.append("DFS Total Distance: ").append(dfsResult.getDistance()).append(" km\n");
                result.append("DFS Time: ").append(dfsEndTime - dfsStartTime).append(" nanoseconds\n\n");
            } else {
                result.append("No path found using DFS.\n");
            }

            // Display Results
            outputArea.setText(result.toString());
        });

        // Set Scene and Stage
        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}