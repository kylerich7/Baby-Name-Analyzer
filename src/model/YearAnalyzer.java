package model;

import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;

public class YearAnalyzer {
  private final int year;
  private final String babyName;
  private final String gender;
  private FileResource yearFile;
  private CSVParser yearFileParser;

  public YearAnalyzer(int year, String babyName, String gender) {
    this.year = year;
    setYearFileRes();
    resetYearFileParser();
    this.babyName = babyName;
    this.gender = gender;
  }

  public int countBirths(String targetGender) {
    resetYearFileParser();
    int totalBirths = 0;

    for (CSVRecord record : yearFileParser) {
      String currentGender = record.get(1);
      if (targetGender.startsWith(currentGender)) {
        int currentBirths = Integer.parseInt(record.get(2));
        totalBirths += currentBirths;
      }
    }
    return totalBirths;
  }

  public int countNames(String targetgender) {
    resetYearFileParser();
    int totalNames = 0;

    for (CSVRecord record : yearFileParser) {
      String currentGender = record.get(1);
      if (targetgender.startsWith(currentGender)) {
        totalNames += 1;
      }
    }

    return totalNames;
  }

  public int getNameRankForBirthYear() {
    resetYearFileParser();
    int rankSoFar = 0;

    for (CSVRecord record : yearFileParser) {
      String currentGender = record.get(1);
      if (gender.startsWith(currentGender)) {
        String currentName = record.get(0);
        rankSoFar += 1;
        if (babyName.equalsIgnoreCase(currentName)) {
          return rankSoFar;
        }
      }
    }
    return -1;
  }

  public int getTotalBirthsRankedHigher() {
    int sumBirthsSoFar = 0;
    int nameRank = getNameRankForBirthYear();
    int rankSoFar = 0;
    resetYearFileParser();

    for (CSVRecord record : yearFileParser) {
      String currentGender = record.get(1);
      if (gender.startsWith(currentGender)) {
        int currentBirths = Integer.parseInt(record.get(2));
        rankSoFar += 1;
        if (rankSoFar == nameRank) {
          return sumBirthsSoFar;
        }
        sumBirthsSoFar += currentBirths;
      }
    }
    return sumBirthsSoFar;
  }

  public String getNewName(int targetYear) {
    String filename = "yob" + year + ".csv";
    File yearFile = new File("./resources/us_babynames/" + filename);
    CSVParser targetYearFileParser = new FileResource(yearFile).getCSVParser(false);
    int rankSoFar = 0;
    int nameRank = getNameRankForBirthYear();

    for (CSVRecord record : targetYearFileParser) {
      String currentGender = record.get(1);
      if (gender.startsWith(currentGender)) {
        String currentName = record.get(0);
        rankSoFar += 1;
        if (rankSoFar == nameRank) {
          return currentName;
        }
      }
    }

    return "That name wasnt found in " + targetYear + " baby name data";
  }

  public int getYear() {
    return year;
  }

  public String getBabyName() {
    return babyName;
  }

  public String getGender() {
    return gender;
  }

  private void setYearFileRes() {
    String filename = "yob" + year + ".csv";
    File yearFile = new File("./resources/us_babynames/" + filename);
    this.yearFile = new FileResource(yearFile);
  }

  private void resetYearFileParser() {
    yearFileParser = yearFile.getCSVParser(false);
  }
}
