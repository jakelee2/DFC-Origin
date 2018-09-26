package org.dbadmin.model;

public class ValidationStats {
  private String status;
  private int sourceRowCount;
  private int sourceNumNulls;
  private double sourceMin;
  private double sourceMax;
  private double sourceFiftyPercentile;
  private int targetRowCount;
  private int targetNumNulls;
  private double targetMin;
  private double targetMax;
  private double targetFiftyPercentile;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getSourceRowCount() {
    return sourceRowCount;
  }

  public void setSourceRowCount(int sourceRowCount) {
    this.sourceRowCount = sourceRowCount;
  }

  public int getSourceNumNulls() {
    return sourceNumNulls;
  }

  public void setSourceNumNulls(int sourceNumNulls) {
    this.sourceNumNulls = sourceNumNulls;
  }

  public double getSourceMin() {
    return sourceMin;
  }

  public void setSourceMin(double sourceMin) {
    this.sourceMin = sourceMin;
  }

  public double getSourceMax() {
    return sourceMax;
  }

  public void setSourceMax(double sourceMax) {
    this.sourceMax = sourceMax;
  }

  public double getSourceFiftyPercentile() {
    return sourceFiftyPercentile;
  }

  public void setSourceFiftyPercentile(double sourceFiftyPercentile) {
    this.sourceFiftyPercentile = sourceFiftyPercentile;
  }

  public int getTargetRowCount() {
    return targetRowCount;
  }

  public void setTargetRowCount(int targetRowCount) {
    this.targetRowCount = targetRowCount;
  }

  public int getTargetNumNulls() {
    return targetNumNulls;
  }

  public void setTargetNumNulls(int targetNumNulls) {
    this.targetNumNulls = targetNumNulls;
  }

  public double getTargetMin() {
    return targetMin;
  }

  public void setTargetMin(double targetMin) {
    this.targetMin = targetMin;
  }

  public double getTargetMax() {
    return targetMax;
  }

  public void setTargetMax(double targetMax) {
    this.targetMax = targetMax;
  }

  public double getTargetFiftyPercentile() {
    return targetFiftyPercentile;
  }

  public void setTargetFiftyPercentile(double targetFiftyPercentile) {
    this.targetFiftyPercentile = targetFiftyPercentile;
  }
}
