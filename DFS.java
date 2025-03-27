class DFS {
    private int[][] distances;
    private String[] cities;

    public DFS(int[][] distances, String[] cities) {
        this.distances = distances;
        this.cities = cities;
    }

    public Result findShortestPath(int startIndex, int endIndex) {
        boolean[] visited = new boolean[distances.length];
        CustomStack<Integer> stack = new CustomStack<>();
        int[] distanceFromStart = new int[distances.length];
        int[] parents = new int[distances.length];

        // Başlangıç değerlerini ayarla
        for (int i = 0; i < distances.length; i++) {
            distanceFromStart[i] = Integer.MAX_VALUE;
            parents[i] = -1;
        }
        distanceFromStart[startIndex] = 0;

        stack.push(startIndex);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (visited[current]) continue;
            visited[current] = true;

            for (int i = 0; i < distances.length; i++) {
                if (distances[current][i] != 99999 && !visited[i]) {
                    int newDistance = distanceFromStart[current] + distances[current][i];
                    if (newDistance < distanceFromStart[i]) {
                        distanceFromStart[i] = newDistance;
                        parents[i] = current;
                    }
                    stack.push(i);
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

class Result {
    private String path;
    private int distance;

    public Result(String path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public String getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
