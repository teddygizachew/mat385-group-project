import java.util.*;
import java.io.*;

public class BookKnapSack {
    public static void main(String[] args) throws FileNotFoundException {
        // Takes input from the user
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the total budget: ");
        int budget = input.nextInt();
        System.out.print("Enter the name of input file: ");
        String inputFileName = input.next();
        System.out.print("Enter the name of output file: ");
        String outputFileName = input.next();

        // Open a reader for the input file
        Scanner inputFile = new Scanner(new File(inputFileName));
        // Create an array list for courses
        ArrayList<Course> coursesList = new ArrayList<>();
        // Add each course from the input file into the course array list
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine();
            String[] split = line.split(" ");
            Course course = new Course(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            coursesList.add(course);
        }
        // Close reader
        inputFile.close();
        // Output the number of courses read from the file
        System.out.println("Number of courses = " + coursesList.size());
        System.out.println("Done");

        // Create an empty Knapsack (table) with budget and course list size
        int[][] knapsack = new int[budget + 1][coursesList.size() + 1];
        // Iterate over each row within the budget range
        for (int i = 1; i <= budget; i++) {
            // Iterate over each item in the row
            for (int j = 1; j <= coursesList.size(); j++) {
                Course course = coursesList.get(j - 1);
                if (course.bookPrice > i) {
                    knapsack[i][j] = knapsack[i][j - 1];
                } else {
                    knapsack[i][j] = Math.max(knapsack[i - course.bookPrice][j - 1] + course.percent, knapsack[i][j - 1]);
                }
            }
        }
        
        // Create array list to hold the select results from the knapsack created abover
        ArrayList<Integer> selectList = new ArrayList<>();
        int totalPercent = 0;
        for (int i = knapsack.length - 1; i > 0; i--) {
            for (int j = knapsack[i].length - 1; j > 0; j--) {
                Course course = coursesList.get(j - 1);
                int itemProfit = course.percent;
                if (course.bookPrice <= i) {
                    if (knapsack[i][j] == knapsack[i - course.bookPrice][j - 1] + itemProfit) {
                        selectList.add(j - 1);
                        i = i - course.bookPrice;
                        totalPercent += course.percent;
                    }
                }
            }
        }

        // Create output file to write the selected results
        PrintWriter outputFile = new PrintWriter(outputFileName);
        outputFile.println("Courses available: " + coursesList.size());
        outputFile.println("Budget available: " + budget);
        outputFile.println("Books chosen from courses: " + selectList.size());
        outputFile.println("Most percentage gained: " + totalPercent);
        // Iterate over the select array to write the output into the output file
        for (int i = selectList.size() - 1; i >= 0; i--) {
            int index = selectList.get(i);
            outputFile.println(coursesList.get(index).bookName + " " + coursesList.get(index).bookPrice + " " + coursesList.get(index).percent);
        }
        outputFile.close();
    }

    // Course
    public static class Course {
        private final String bookName;
        private final int bookPrice;
        private final int percent;

        public Course(String bookName, int bookPrice, int percent) {
            this.bookName = bookName;
            this.bookPrice = bookPrice;
            this.percent = percent;
        }
    }
}
