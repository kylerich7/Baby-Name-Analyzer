package ui;

import model.YearAnalyzer;
import model.YearRangeAnalyzer;

import java.util.Scanner;

public class BabyNamesUI {
  private Scanner input;
  private YearAnalyzer yearAnalyzer;
  private int birthyear;
  private String babyName;
  private String gender;

  public BabyNamesUI() {
    initialize();
    askForBirthInfo();
    runMainMenu();
  }

  public static void main(String[] args) {
    new BabyNamesUI();
  }

  private void askForBirthInfo() {
    System.out.println("What is your name?");
    String name = getInput();
    System.out.println("What year were you born? Please type YYYY in the range [1880,2014]");
    String birthYear = getInput();
    System.out.println("What is your gender? (Please type \"Male\" or \"Female\")");
    String gender = getInput();
    yearAnalyzer = new YearAnalyzer(Integer.parseInt(birthYear), name, gender);
    this.babyName = name;
    this.gender = gender;
    this.birthyear = Integer.parseInt(birthYear);
  }

  private void initialize() {
    input = new Scanner(System.in);
  }

  private void runMainMenu() {
    boolean keepGoing = true;

    while (keepGoing) {
      displayMainMenu();
      String command = getInput();
      if (command.equals("9")) {
        goodbyeMsg();
        keepGoing = false;
      } else {
        processMainMenu(command);
      }
    }
  }

  private void goodbyeMsg() {
    System.out.println("\nGoodbye!\n");
  }

  private void displayMainMenu() {
    System.out.println("\nAll Statistics Are Based on US Data \n");
    System.out.println("Would You Like To:");
    System.out.println("\t[0] --> See your birth year name statistics");
    System.out.println(
        "\t[1] --> See what your name would be if you were born in a different year (by rank)");
    System.out.println("\t[2] --> See birth name statistics for your name across a range of years");
    System.out.println("\n\t[9] --> Quit");
  }

  private void processMainMenu(String command) {
    if (command.equals("0")) {
      printBirthYearStatistics();
    } else if (command.equals("1")) {
      printNewNameForTargetYear();
    } else if (command.equals("2")) {
      printYearRangeStatistics();
    } else {
      invalidMenuInputMsg();
    }
  }

  private String getInput() {
    String inputString = input.nextLine();
    while (inputString.length() <= 0) {
      inputString = input.nextLine();
    }
    return inputString;
  }

  private void invalidMenuInputMsg() {
    System.out.println(
        "\nYour input was not valid! Please Try Again By Pressing Only Number Keys.\n");
  }

  private void printYearRangeStatistics() {
    System.out.println(
        "What is the lower bound year of the range you are interested in? (YYYY >= 1880)");
    int lowerBound = Integer.parseInt(getInput());
    System.out.println(
        "What is the upper bound year of the range you are interested in? (YYYY <= 2014)");
    int upperBound = Integer.parseInt(getInput());
    YearRangeAnalyzer yearRangeAnalyzer =
        new YearRangeAnalyzer(babyName, gender, lowerBound, upperBound);
    int topRank = yearRangeAnalyzer.getTopRank();
    int topRankYr = yearRangeAnalyzer.getTopRankYr();
    double avRank = yearRangeAnalyzer.getAverageRank();
    System.out.println(
        "\n\nTop Name Popularity Rank for \""
            + babyName
            + "\" From The Year "
            + lowerBound
            + " to "
            + upperBound
            + " is: "
            + topRank
            + " In The Year "
            + topRankYr);
    System.out.println(
        "Average Rank for \""
            + babyName
            + "\" From Years "
            + lowerBound
            + " to "
            + upperBound
            + " is: "
            + avRank);
  }

  private void printNewNameForTargetYear() {
    System.out.println(
        "\n\nWhat year would you like to compare to? Please type YYYY in the range [1880,2014]");
    int targetYear = Integer.parseInt(getInput());
    int currRank = yearAnalyzer.getNameRankForBirthYear();
    String newName = yearAnalyzer.getNewName(targetYear);
    System.out.println(
        "\nIn "
            + targetYear
            + ", The Name At Your Name Popularity Rank "
            + currRank
            + " Is: "
            + newName);
  }

  private void printBirthYearStatistics() {
    int maleBirthCount = yearAnalyzer.countBirths("M");
    int femaleBirthCount = yearAnalyzer.countBirths("F");
    int totalBirthCount = maleBirthCount + femaleBirthCount;
    int maleNameCount = yearAnalyzer.countNames("M");
    int femaleNameCount = yearAnalyzer.countNames("F");
    int totalNameCount = maleNameCount + femaleNameCount;
    int rank = yearAnalyzer.getNameRankForBirthYear();
    int birthCountOfHigherRanks = yearAnalyzer.getTotalBirthsRankedHigher();

    System.out.println("\n\nSummary For The Birth Year Of " + birthyear + ":\n");

    System.out.println("Total Births: " + totalBirthCount);
    System.out.println("Female Births: " + femaleBirthCount);
    System.out.println("Male Births: " + maleBirthCount + "\n");

    System.out.println("Total Names: " + totalNameCount);
    System.out.println("Female Names: " + femaleNameCount);
    System.out.println("Male Names: " + maleNameCount + "\n");

    System.out.println(gender + " Name \"" + babyName + "\" Popularity Rank: " + rank);
    System.out.println(
        "Total "
            + gender
            + " Births Above Rank "
            + rank
            + " In "
            + birthyear
            + ": "
            + birthCountOfHigherRanks);
  }
}
