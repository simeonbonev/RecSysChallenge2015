import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

public class CSVProcessor {
	static int sessionId = 0;
	static int clicksCount = 1;
	static int sessionStart = 2;
	static int sessionEnd = 3;
	static int distinctCategories = 4;
	static int numCat = 5;
	static int numItems = 6;
	static int bought = 7;
	static int boughtNumber = 8;

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		Calendar calendar = Calendar.getInstance();
		try {
			bufferedReader = new BufferedReader(
					new FileReader(
							"/Users/monkeybusiness/Documents/RecSys/workspace/mongoProcessedData.csv"));
			File fout = new File(
					"/Users/monkeybusiness/Documents/RecSys/workspace/testNotBroken.csv");
			FileOutputStream fos = new FileOutputStream(fout);

			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));

			String line = "";
			String splitter = ",";
			String header = "";
			boolean skipOne = true;
			while ((line = bufferedReader.readLine()) != null) {
				if (skipOne) {
					header = line;
					skipOne = false;
					bufferedWriter
							.write(line
									+ ",duration,dayOfWeek,month,hour,isWeekend,isDuringWork,isAfterWork,isAtNight,hasNoCat,hasGroupCat,hasSpecificCat,hasPromoCat");
					bufferedWriter.newLine();
					continue;
				}
				// use comma as separator
				String[] entity = line.split(splitter);
				// System.out.println("sessionId" + " " + entity[sessionId]);
				// System.out.println("clicksCount" + " " +
				// entity[clicksCount]);
				// System.out.println("sessionStart" + " "
				// + entity[sessionStart].replaceAll("^\"|\"$", ""));
				// System.out.println("sessionEnd" + " " + entity[sessionEnd]);
				// System.out.println("distinctCategories" + " "
				// + entity[distinctCategories]);
				// System.out.println("numCat" + " " + entity[numCat]);
				// System.out.println("numItems" + " " + entity[numItems]);
				// System.out.println("bought" + " " + entity[bought]);
				// System.out.println("boughtNumber" + " " +
				// entity[boughtNumber]);

				Date sStart = DatatypeConverter.parseDateTime(
						entity[sessionStart].replaceAll("^\"|\"$", ""))
						.getTime();
				Date sEnd = DatatypeConverter.parseDateTime(
						entity[sessionEnd].replaceAll("^\"|\"$", "")).getTime();
				long duration = (sEnd.getTime() - sStart.getTime()) / 1000;

				calendar.setTime(sStart);
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				int month = calendar.get(Calendar.MONTH);
				int hour = calendar.get(Calendar.HOUR);
				boolean isWeekend = dayOfWeek == 1 || dayOfWeek == 7
						|| (dayOfWeek == 6 && hour > 18);
				boolean isDuringWork = dayOfWeek > 1 && dayOfWeek < 7
						&& hour > 9 && hour < 17;
				boolean isAfterWork = dayOfWeek > 1 && dayOfWeek < 7
						&& hour > 17 && hour < 23;
				boolean isAtNight = hour > 0 && hour < 7;

				boolean hasNoCat = false;
				boolean hasGroupCat = false;
				boolean hasSpecificCat = false;
				boolean hasPromoCat = false;

				JSONArray jsonArray = null;
				String jaString = "";
				try {
					jaString = entity[distinctCategories]
							.replaceAll("^\"|\"$", "").replaceAll("\"\"S\"\"",
									"-1").replaceAll("\"\"S\"", "-1").replaceAll("\"S\"\"", "-1").replaceAll("\"S\"", "-1");
					jsonArray = new JSONArray(jaString);
				} catch (JSONException je) {
					System.out.println("Broken line: "
							+ line
							+ "###"
							+ entity[distinctCategories] + "&&&" + entity[distinctCategories].replaceAll("^\"|\"$", "") + "%%%" + entity[distinctCategories].replaceAll("^\"|\"$", ""));
					continue;
				}
				int[] categories = new int[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {

					categories[i] = jsonArray.optInt(i);
					if (categories[i] < 0) {
						hasPromoCat = true;
					}
					if (categories[i] == 0) {
						hasNoCat = true;
					}

					if (categories[i] >= 1 && categories[i] <= 12) {
						hasGroupCat = true;
					}

					if (categories[i] > 12) {
						hasSpecificCat = true;
					}
				}

				bufferedWriter.write(line + "," + duration + "," + dayOfWeek
						+ "," + month + "," + hour + "," + isWeekend + ","
						+ isDuringWork + "," + isAfterWork + "," + isAtNight
						+ "," + hasNoCat + "," + hasGroupCat + ","
						+ hasSpecificCat + "," + hasPromoCat);
				bufferedWriter.newLine();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}

			if (bufferedWriter != null) {
				bufferedWriter.close();
			}

			System.out.println("Done");
		}
	}
}
