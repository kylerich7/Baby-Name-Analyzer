package model;

import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.HashMap;

public class YearRangeAnalyzer {
  private final int lowerBoundYr;
  private final int upperBoundYr;
  private String babyName;
  private String gender;
  private HashMap<Integer, FileResource> yearFileMap;

  public YearRangeAnalyzer(String babyName, String gender, int lowerBoundYr, int upperBoundYr) {
    this.babyName = babyName;
    this.gender = gender;
    this.lowerBoundYr = lowerBoundYr;
    this.upperBoundYr = upperBoundYr;
    initYearFileMap();
  }

  private void initYearFileMap() {
    this.yearFileMap = new HashMap<>();
    for (int i = lowerBoundYr; i <= upperBoundYr; i++) {
      String filename = "yob" + i + ".csv";
      File yearFile = new File("./resources/us_babynames/" + filename);
      FileResource yearFileRes = new FileResource(yearFile);
      yearFileMap.put(i, yearFileRes);
    }
  }

  public int getTopRank() {
    int topRankSoFar = -1;

    for (int year : yearFileMap.keySet()) {
      int currentRank = getNameRankForYear(year);
      if (((currentRank < topRankSoFar) || (topRankSoFar == -1)) && (currentRank != -1)) {
        topRankSoFar = currentRank;
      }
    }

    return topRankSoFar;
  }

  private int getNameRankForYear(int year) {
    FileResource yearFile = yearFileMap.get(year);
    CSVParser parser = yearFile.getCSVParser(false);
    int rankSoFar = 0;

    for (CSVRecord record : parser) {
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

  public int getTopRankYr() {
    int topRank = getTopRank();

    for (int year : yearFileMap.keySet()) {
      int currentRank = getNameRankForYear(year);
      if (currentRank == topRank) {
        return year;
      }
    }

    return -1;
  }

  public double getAverageRank() {
    double sumRankSoFar = 0.00;
    double countSoFar = 0.00;

    for (int year : yearFileMap.keySet()) {
      int currentRank = getNameRankForYear(year);
      if (currentRank != -1) {
        sumRankSoFar += currentRank;
        countSoFar += 1.00;
      }
    }

    return sumRankSoFar / countSoFar;
  }

  public String getBabyName() {
    return babyName;
  }

  public void setBabyName(String babyName) {
    this.babyName = babyName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
}
