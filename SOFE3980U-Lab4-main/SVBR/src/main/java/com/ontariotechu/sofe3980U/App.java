package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import java.util.HashMap;
import com.opencsv.*;

/**
 * Evaluate Binary Classification Metrics
 *
 */
public class App
{
	public static void main( String[] args )
	{
		String[] modelFiles = {"model_1.csv", "model_2.csv", "model_3.csv"};

		for (String filePath : modelFiles) {
			evaluateModel(filePath);
		}
	}

	public static void evaluateModel(String filePath) {
		FileReader filereader;
		List<String[]> allData;
		try{
			filereader = new FileReader(filePath);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			allData = csvReader.readAll();
		}
		catch(Exception e){
			System.out.println("Error reading the CSV file: " + filePath);
			return;
		}

		int tp = 0, tn = 0, fp = 0, fn = 0;
		double bce = 0.0;
		int totalSamples = allData.size();

		for (String[] row : allData) {
			int y_true = Integer.parseInt(row[0]);
			int y_pred = Integer.parseInt(row[1]);
			double prob = Double.parseDouble(row[2]);

			// Compute Binary Cross-Entropy (BCE)
			if (y_true == 1) {
				bce -= Math.log(prob);
			} else {
				bce -= Math.log(1 - prob);
			}

			// Compute Confusion Matrix
			if (y_true == 1 && y_pred == 1) tp++;
			else if (y_true == 0 && y_pred == 0) tn++;
			else if (y_true == 0 && y_pred == 1) fp++;
			else if (y_true == 1 && y_pred == 0) fn++;
		}
		bce /= totalSamples;

		double accuracy = (double)(tp + tn) / totalSamples;
		double precision = tp + fp > 0 ? (double) tp / (tp + fp) : 0;
		double recall = tp + fn > 0 ? (double) tp / (tp + fn) : 0;
		double f1_score = (precision + recall) > 0 ? 2 * (precision * recall) / (precision + recall) : 0;
		double auc_roc = 0.5 * ((double)tp / (tp + fn) + (double)tn / (tn + fp));

		System.out.println("Results for " + filePath + ":");
		System.out.println("BCE = " + bce);
		System.out.println("Confusion Matrix:");
		System.out.println("\ty=1\t  y=0");
		System.out.println("y^=1\t" + tp + "\t" + fp);
		System.out.println("y^=0\t" + fn + "\t" + tn);
		System.out.println("Accuracy = " + accuracy);
		System.out.println("Precision = " + precision);
		System.out.println("Recall = " + recall);
		System.out.println("F1 Score = " + f1_score);
		System.out.println("AUC-ROC = " + auc_roc);
		System.out.println();
	}
}
