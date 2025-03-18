package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App
{
	public static void main( String[] args )
	{
		String[] modelFiles = {"model_1.csv", "model_2.csv", "model_3.csv"};
		String bestMSEModel = "", bestMAEModel = "", bestMAREModel = "";
		double minMSE = Double.MAX_VALUE, minMAE = Double.MAX_VALUE, minMARE = Double.MAX_VALUE;

		for (String filePath : modelFiles) {
			List<String[]> allData;
			try{
				FileReader filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				allData = csvReader.readAll();
			}
			catch(Exception e){
				System.out.println("Error reading the CSV file: " + filePath);
				continue;
			}

			double mse = 0, mae = 0, mare = 0;
			int count = allData.size();

			for (String[] row : allData) {
				float y_true = Float.parseFloat(row[0]);
				float y_predicted = Float.parseFloat(row[1]);

				double error = y_true - y_predicted;
				mse += error * error;
				mae += Math.abs(error);
				mare += Math.abs(error / y_true);
			}

			mse /= count;
			mae /= count;
			mare /= count;

			System.out.println("For " + filePath);
			System.out.println("    MSE = " + mse);
			System.out.println("    MAE = " + mae);
			System.out.println("    MARE = " + mare);
			System.out.println();

			if (mse < minMSE) { minMSE = mse; bestMSEModel = filePath; }
			if (mae < minMAE) { minMAE = mae; bestMAEModel = filePath; }
			if (mare < minMARE) { minMARE = mare; bestMAREModel = filePath; }
		}

		System.out.println("According to MSE, The best model is " + bestMSEModel);
		System.out.println("According to MAE, The best model is " + bestMAEModel);
		System.out.println("According to MARE, The best model is " + bestMAREModel);
	}
}