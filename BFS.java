class BFS {
    private int[][] distances;
    private String[] cities;

    public BFS(int[][] distances, String[] cities) {
        this.distances = distances;
        this.cities = cities;
    }

    public Result findShortestPath(int startIndex, int endIndex) {
        boolean[] visited = new boolean[distances.length];
        CustomQueue<Integer> queue = new CustomQueue<>();
        int[] distanceFromStart = new int[distances.length];
        int[] parents = new int[distances.length];

        // Başlangıç değerlerini ayarla
        for (int i = 0; i < distances.length; i++) {
            distanceFromStart[i] = Integer.MAX_VALUE;
            parents[i] = -1;
        }
        distanceFromStart[startIndex] = 0;

        queue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!queue.isEmpty()) {
            int current = queue.dequeue();

            for (int i = 0; i < distances.length; i++) {
                if (distances[current][i] != 99999 && !visited[i]) {
                    int newDistance = distanceFromStart[current] + distances[current][i];
                    if (newDistance < distanceFromStart[i]) {
                        distanceFromStart[i] = newDistance;
                        parents[i] = current;
                    }
                    queue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        return buildResult(startIndex, endIndex, parents, distanceFromStart);
    }

    private Result buildResult(int startIndex, int endIndex, int[] parents, int[] distances) {
        if (distances[endIndex] == Integer.MAX_VALUE) {
            return new Result(null, -1); // Yol bulunamadı
        }

        String[] path = new String[cities.length];
        int pathIndex = 0;
        int current = endIndex;

        while (current != -1) {
            path[pathIndex++] = cities[current];
            current = parents[current];
        }

        // Ters sırayı düzelterek sonucu oluştur
        StringBuilder resultPath = new StringBuilder();
        for (int i = pathIndex - 1; i >= 0; i--) {
            resultPath.append(path[i]);
            if (i != 0) {
                resultPath.append(" -> ");
            }
        }

        return new Result(resultPath.toString(), distances[endIndex]);
    }
}


