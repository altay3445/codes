class CSVReader {
    private String[] cities;
    private int[][] distances;
    private int cityCount;

    public CSVReader(String filePath) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath));
            String line = reader.readLine(); // İlk satır (şehir isimleri)

            // Şehir isimlerini al
            String[] headers = line.split(",");
            cityCount = headers.length - 1; // İlk sütun başlık dışı
            cities = new String[cityCount];
            distances = new int[cityCount][cityCount];

            for (int i = 1; i <= cityCount; i++) {
                cities[i - 1] = headers[i].trim();
            }

            // Şehirler arası mesafeleri oku
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                for (int col = 1; col <= cityCount; col++) {
                    distances[row][col - 1] = Integer.parseInt(parts[col].trim());
                }
                row++;
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public int getCityIndex(String city) {
        for (int i = 0; i < cityCount; i++) {
            if (cities[i].equals(city)) {
                return i;
            }
        }
        return -1; // Şehir bulunamazsa -1 döndür
    }

    public String[] getCities() {
        return cities;
    }

    public int[][] getDistances() {
        return distances;
    }
}
