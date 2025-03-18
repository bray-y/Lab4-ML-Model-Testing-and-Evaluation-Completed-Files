package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression with Classification Metrics
 */
public class App
{
	public static void main( String[] args )
	{
		String[] modelFiles = {"model_1.csv", "model_2.csv", "model_3.csv"};
		String bestBCEModel = "", bestAccuracyModel = "", bestPrecisionModel = "", bestRecallModel = "", bestF1Model = "", bestAUCROCModel = "";
		double minBCE = Double.MAX_VALUE, maxAccuracy = 0, maxPrecision = 0, maxRecall = 0, maxF1 = 0, maxAUCROC = 0;

		for (String filePath : modelFiles) {
			List<Double> actual = new ArrayList<>();
			List<Double> predicted = new ArrayList<>();

			try {
				FileReader filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				List<String[]> allData = csvReader.readAll();

				for (String[] row : allData) {
					actual.add(Double.parseDouble(row[0]));
					predicted.add(Double.parseDouble(row[1]));
				}
			} catch (Exception e) {
				System.out.println("Error reading the CSV file: " + filePath);
				continue;
			}

			double bce = calculateBCE(actual, predicted);
			int[][] confusionMatrix = calculateConfusionMatrix(actual, predicted);
			double accuracy = calculateAccuracy(confusionMatrix);
			double precision = calculatePrecision(confusionMatrix);
			double recall = calculateRecall(confusionMatrix);
			double f1Score = calculateF1Score(precision, recall);
			double aucRoc = calculateAUCROC(actual, predicted);

			System.out.println("For " + filePath);
			System.out.println("    BCE = " + bce);
			System.out.println("    Confusion matrix");
			System.out.println("            y=1      y=0");
			System.out.println("    y^=1    " + confusionMatrix[0][0] + "    " + confusionMatrix[0][1]);
			System.out.println("    y^=0    " + confusionMatrix[1][0] + "    " + confusionMatrix[1][1]);
			System.out.println("    Accuracy = " + accuracy);
			System.out.println("    Precision = " + precision);
			System.out.println("    Recall = " + recall);
			System.out.println("    F1 Score = " + f1Score);
			System.out.println("    AUC ROC = " + aucRoc);
			System.out.println();

			if (bce < minBCE) { minBCE = bce; bestBCEModel = filePath; }
			if (accuracy > maxAccuracy) { maxAccuracy = accuracy; bestAccuracyModel = filePath; }
			if (precision > maxPrecision) { maxPrecision = precision; bestPrecisionModel = filePath; }
			if (recall > maxRecall) { maxRecall = recall; bestRecallModel = filePath; }
			if (f1Score > maxF1) { maxF1 = f1Score; bestF1Model = filePath; }
			if (aucRoc > maxAUCROC) { maxAUCROC = aucRoc; bestAUCROCModel = filePath; }
		}

		System.out.println("According to BCE, The best model is " + bestBCEModel);
		System.out.println("According to Accuracy, The best model is " + bestAccuracyModel);
		System.out.println("According to Precision, The best model is " + bestPrecisionModel);
		System.out.println("According to Recall, The best model is " + bestRecallModel);
		System.out.println("According to F1 Score, The best model is " + bestF1Model);
		System.out.println("According to AUC ROC, The best model is " + bestAUCROCModel);
	}

	private static double calculateBCE(List<Double> actual, List<Double> predicted) {
		double sum = 0;
		for (int i = 0; i < actual.size(); i++) {
			sum += -(actual.get(i) * Math.log(predicted.get(i)) + (1 - actual.get(i)) * Math.log(1 - predicted.get(i)));
		}
		return sum / actual.size();
	}

	private static int[][] calculateConfusionMatrix(List<Double> actual, List<Double> predicted) {
		int[][] matrix = new int[2][2];
		for (int i = 0; i < actual.size(); i++) {
			int yTrue = (actual.get(i) >= 0.5) ? 1 : 0;
			int yPred = (predicted.get(i) >= 0.5) ? 1 : 0;
			matrix[yPred][yTrue]++;
		}
		return matrix;
	}

	private static double calculateAccuracy(int[][] matrix) {
		return (double) (matrix[0][0] + matrix[1][1]) / (matrix[0][0] + matrix[0][1] + matrix[1][0] + matrix[1][1]);
	}

	private static double calculatePrecision(int[][] matrix) {
		return (double) matrix[0][0] / (matrix[0][0] + matrix[0][1]);
	}

	private static double calculateRecall(int[][] matrix) {
		return (double) matrix[0][0] / (matrix[0][0] + matrix[1][0]);
	}

	private static double calculateF1Score(double precision, double recall) {
		return 2 * (precision * recall) / (precision + recall);
	}

	private static double calculateAUCROC(List<Double> actual, List<Double> predicted) {
		return 0.5 + (calculateAccuracy(calculateConfusionMatrix(actual, predicted)) - 0.5) * 2;
	}
}

