/**
 * Name Ludiana Atnafu
 * CSC 364
 * Professor: Dr. Jeffery Ward
 * Dynamic programming approach to solving a Knapsack problem
 * */
import java.util.*;
import java.io.*;

public class Knapsack {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the total budget: ");
        int budget = input.nextInt();
        System.out.print("Enter the name of input file: ");
        String inputFileName = input.next();
        System.out.print("Enter the name of output file: ");
        String outputFileName = input.next();

        Scanner inputFile = new Scanner(new File(inputFileName));
        ArrayList<Course> coursesList = new ArrayList<>();
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine();
            String[] split = line.split(" ");
            Course course = new Course(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            coursesList.add(course);
        }
        inputFile.close();
        System.out.println("Number of courses = " + coursesList.size());
        System.out.println("Done");

        int[][] knapsack = new int[budget + 1][coursesList.size() + 1];
        for (int i = 1; i <= budget; i++) {
            for (int j = 1; j <= coursesList.size(); j++) {
                Course course = coursesList.get(j - 1);
                if (course.bookPrice > i) {
                    knapsack[i][j] = knapsack[i][j - 1];
                } else {
                    knapsack[i][j] = Math.max(knapsack[i - course.bookPrice][j - 1] + course.percent, knapsack[i][j - 1]);
                }
            }
        }
        ArrayList<Integer> selectList = new ArrayList<>();
        int totalProfit = 0;
        for (int i = knapsack.length - 1; i > 0; i--) {
            for (int j = knapsack[i].length - 1; j > 0; j--) {
                Course projectJ = coursesList.get(j - 1);
                int itemProfit = projectJ.percent;
                if (projectJ.bookPrice <= i) {
                    if (knapsack[i][j] == knapsack[i - projectJ.bookPrice][j - 1] + itemProfit) {
                        selectList.add(j - 1);
                        i = i - projectJ.bookPrice;
                        totalProfit += projectJ.percent;
                    }
                }
            }
        }

        PrintWriter outputFile = new PrintWriter(outputFileName);
        outputFile.println("Courses available: " + coursesList.size());
        outputFile.println("Budget available: " + budget);
        outputFile.println("Books chosen from courses: " + selectList.size());
        outputFile.println("Most percentage gained: " + totalProfit);
        for (int i = selectList.size() - 1; i >= 0; i--) {
            int index = selectList.get(i);
            outputFile.println(coursesList.get(index).bookName + " " + coursesList.get(index).bookPrice + " " + coursesList.get(index).percent);
        }
        outputFile.close();
    }

    public static class Course {
        private String bookName;
        private int bookPrice;
        private int percent;

        public Course(String bookName, int bookPrice, int percent) {
            this.bookName = bookName;
            this.bookPrice = bookPrice;
            this.percent = percent;
        }
    }
}