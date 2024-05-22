import java.io.*;
import java.util.*;

public class TextGraphAnalyzer {
    private DirectedGraph graph;
    private Scanner scanner;

    public TextGraphAnalyzer() {
        graph = new DirectedGraph();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        TextGraphAnalyzer analyzer = new TextGraphAnalyzer();
        analyzer.run();
    }

    public void run() {
        System.out.println("Welcome to Text Graph Analyzer!");

        boolean exit = false;
        while (!exit) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Load text file and generate directed graph");
            System.out.println("2. Show directed graph");
            System.out.println("3. Query bridge words");
            System.out.println("4. Generate new text with bridge words");
            System.out.println("5. Calculate shortest path between two words");
            System.out.println("6. Random walk");
            System.out.println("7. Exit");

            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    loadTextFile();
                    break;
                case 2:
                    showDirectedGraph();
                    break;
                case 3:
                    queryBridgeWords();
                    break;
                case 4:
                    generateNewText();
                    break;
                case 5:
                    calculateShortestPath();
                    break;
                case 6:
                    randomWalk();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    private void loadTextFile() {
        System.out.print("Enter the path to the text file: ");
        String filePath = scanner.next();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
                for (int i = 0; i < words.length - 1; i++) {
                    graph.addEdge(words[i], words[i + 1]);
                }
            }
            System.out.println("Text file loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading text file: " + e.getMessage());
        }
    }

    private void showDirectedGraph() {
        System.out.println("Directed Graph:");
        graph.printGraph();
    }

    private void queryBridgeWords() {
        System.out.print("Enter word1: ");
        String word1 = scanner.next(); // 用户输入起始词
        // 添加延迟，等待用户完成输入
        try {
            Thread.sleep(1000); // 这里等待1秒钟，可以根据需要调整时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("Enter word2: ");
        String word2 = scanner.next(); // 用户输入结束词
        Set<String> result = graph.queryBridgeWords(word1, word2);
        String info = "No word1 or word2 in the graph!";
        if (result.isEmpty()) {
            info =  "No bridge words from " + word1 + " to " + word2 + "!";
        } else if(result.contains("NONE")){
            info =  "No word1 or word2 in the graph!";
        }
        else{
            info =  "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", result);
        }
        
        System.out.println(info);
    }

    private void generateNewText() {
        System.out.print("Enter a new text: ");
        Scanner scanner = new Scanner(System.in);
        String inputText = scanner.nextLine(); 
        // 添加延迟，等待用户完成输入
        try {
            Thread.sleep(3000); // 这里等待1秒钟，可以根据需要调整时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String newText = graph.generateNewText(inputText);
        System.out.println("Generated text with bridge words:");
        System.out.println(newText);
    }

    private void calculateShortestPath() {
        System.out.print("Enter start word: ");
        String startWord = scanner.next(); // 用户输入起始词

        // 添加延迟，等待用户完成输入
        try {
            Thread.sleep(1000); // 这里等待1秒钟，可以根据需要调整时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("Enter end word: ");
        String endWord = scanner.next(); // 用户输入结束词
        String shortestPath = graph.calculateShortestPath(startWord, endWord);
        System.out.println(shortestPath);
    }
    

    private void randomWalk() {
        String walkResult = graph.randomWalk();
        System.out.println("Random walk result:");
        System.out.println(walkResult);
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

}

class DirectedGraph {
    private final Map<String, Map<String, Integer>> graph;

    public DirectedGraph() {
        graph = new HashMap<>();
    }

    public void addEdge(String source, String target) {
        if (!graph.containsKey(source)) {
            graph.put(source, new HashMap<>());
        }
        Map<String, Integer> neighbors = graph.get(source);
        neighbors.put(target, neighbors.getOrDefault(target, 0) + 1);
    }

    public void printGraph() {
        for (String source : graph.keySet()) {
            Map<String, Integer> neighbors = graph.get(source);
            for (String target : neighbors.keySet()) {
                int weight = neighbors.get(target);
                System.out.println(source + " -> " + target + ": " + weight);
            }
        }
    }


public Set<String> queryBridgeWords(String word1, String word2) {
    word1 = word1.toLowerCase();
    word2 = word2.toLowerCase();
    Set<String> bridgeWords = new HashSet<>();
    if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
        bridgeWords.add("NONE");
        return bridgeWords;
    }
    Set<String> neighbors1 = graph.getOrDefault(word1.toLowerCase(), new HashMap<>()).keySet();
    /* change the logic of the original code */
    /* by wuooo_339 */
    for (String neighbor : neighbors1) {
        Set<String> nextneighbors = graph.getOrDefault(neighbor.toLowerCase(), new HashMap<>()).keySet();
        for (String nextneighbor : nextneighbors) {
            if(nextneighbor.equals(word2))
            {
                bridgeWords.add(neighbor);
            }
        }
    }
    return bridgeWords;

}



public String generateNewText(String inputText) {
    String[] words = inputText.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
    StringBuilder modifiedSentence = new StringBuilder();
    Set<String> bridgeWords;
    for (int i = 0; i < words.length - 1; i++) {
        modifiedSentence.append(words[i]).append(" ");
        System.out.println(words[i]);
        bridgeWords = queryBridgeWords(words[i], words[i + 1]);
        if(!bridgeWords.isEmpty() && !bridgeWords.contains("NONE"))
        {
        // 将Set转换为List  
                int randomIndex = 0;
                String randomBridgeWord = (String) bridgeWords.toArray()[randomIndex];
                modifiedSentence.append(randomBridgeWord).append(" ");
        }
    }
    modifiedSentence.append(words[words.length - 1]);
    System.out.println("Modified sentence: " + modifiedSentence);
    return "ok";
}


 public String calculateShortestPath(String startWord, String endWord) {
    startWord = startWord.toLowerCase();
    endWord = endWord.toLowerCase();

    if (!graph.containsKey(startWord) || !graph.containsKey(endWord)) {
        return "No path from " + startWord + " to " + endWord + "!";
    }

    Queue<String> queue = new LinkedList<>();
    Map<String, String> parentMap = new HashMap<>();
    Set<String> visited = new HashSet<>();

    queue.offer(startWord);
    visited.add(startWord);

    boolean found = false;
    while (!queue.isEmpty()) {
        String current = queue.poll();
        Set<String> neighbors = graph.getOrDefault(current.toLowerCase(), new HashMap<>()).keySet();
        // System.out.println(current +" has neighbour");
        if(neighbors.isEmpty())
        {
            // System.out.println("is empty");
            current = queue.poll();
            continue;
        }
        // else {
        //     for (String neighbor : neighbors) System.out.print(neighbor+" ");
        // }
        if (current.equals(endWord)||neighbors.isEmpty()) {
            found = true;
            break;
        }

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                queue.offer(neighbor);
                visited.add(neighbor);
                parentMap.put(neighbor, current);
            }
        }
    }

    if (!found) {
        return "No path from " + startWord + " to " + endWord + "!";
    }

    // Reconstruct the path
    List<String> path = new ArrayList<>();
    String current = endWord;
    while (!current.equals(startWord)) {
        path.add(current);
        current = parentMap.get(current);
    }
    path.add(startWord);
    Collections.reverse(path);

    // Print the path
    StringBuilder result = new StringBuilder("Shortest path from " + startWord + " to " + endWord + ": ");
    result.append(String.join(" -> ", path));
    result.append("\nLength: ").append(path.size() - 1); // Length is number of edges, which is number of vertices - 1
    return result.toString();
}

  public String randomWalk() {
    StringBuilder walkResult = new StringBuilder();
    Random random = new Random();
    List<String> vertices = new ArrayList<>(graph.keySet());
    String currentVertex = vertices.get(random.nextInt(vertices.size()));
    walkResult.append(currentVertex).append(" ");
    while (graph.containsKey(currentVertex) && !graph.get(currentVertex).isEmpty()) {
        Map<String, Integer> neighbors = graph.get(currentVertex);
        List<String> neighborList = new ArrayList<>(neighbors.keySet());
        String nextVertex = neighborList.get(random.nextInt(neighborList.size()));
        walkResult.append("-> ").append(nextVertex).append(" ");
        currentVertex = nextVertex;
    }
    return walkResult.toString();
}

}
