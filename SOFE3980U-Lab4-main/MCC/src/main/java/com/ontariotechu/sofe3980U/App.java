package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;
import java.util.HashMap;

/**
 * Evaluate Categorical Cross-Entropy and Confusion Matrix
 *
 */
public class App
{
	public static void main( String[] args )
	{
		String filePath = "model.csv";
		List<String[]> allData;
		try {
			FileReader filereader = new FileReader(filePath);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			allData = csvReader.readAll();
		} catch (Exception e) {
			System.out.println("Error reading the CSV file: " + filePath);
			return;
		}

		double ce = 0;
		int numClasses = 5;
		int[][] confusionMatrix = new int[numClasses][numClasses];
		int count = allData.size();

		for (String[] row : allData) {
			int y_true = Integer.parseInt(row[0]) - 1;
			float[] y_predicted = new float[numClasses];

			for (int i = 0; i < numClasses; i++) {
				y_predicted[i] = Float.parseFloat(row[i + 1]);
			}

			int y_pred = getPredictedClass(y_predicted);
			ce += -Math.log(y_predicted[y_true]);
			confusionMatrix[y_pred][y_true]++;
		}

		ce /= count;

		System.out.println("CE = " + ce);
		System.out.println("Confusion Matrix");
		System.out.println("\ty=1\ty=2\ty=3\ty=4\ty=5");

		for (int i = 0; i < numClasses; i++) {
			System.out.print("y^=" + (i + 1) + "\t");
			for (int j = 0; j < numClasses; j++) {
				System.out.print(confusionMatrix[i][j] + "\t");
			}
			System.out.println();
		}
	}

	private static int getPredictedClass(float[] probabilities) {
		int maxIndex = 0;
		for (int i = 1; i < probabilities.length; i++) {
			if (probabilities[i] > probabilities[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
}
