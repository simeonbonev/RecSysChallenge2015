import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.builder.DefaultTreeBuilder;
import org.apache.mahout.classifier.df.data.Data;
import org.apache.mahout.classifier.df.data.DataLoader;
import org.apache.mahout.classifier.df.data.Instance;
import org.apache.mahout.classifier.df.ref.SequentialBuilder;
import org.apache.mahout.common.RandomUtils;
import org.uncommons.maths.Maths;

public class MahoutRecognizer {
	public static void main(String[] args) throws Exception {
		String descriptor = "I N I I I N N L I N N N N C C C C C C C C";
		String[] trainDataValues = fileAsStringArray("/Users/monkeybusiness/Documents/RecSys/workspace/testNotBroken.csv");

		Data data = DataLoader.loadData(
				DataLoader.generateDataset(descriptor, false, trainDataValues),
				trainDataValues);

		int numberOfTrees = 100;
		DecisionForest forest = buildForest(numberOfTrees, data);

		String[] testDataValues = testFileAsStringArray("data/test.csv");
		Data test = DataLoader.loadData(data.getDataset(), testDataValues);
		Random rng = RandomUtils.getRandom();

		for (int i = 0; i < test.size(); i++) {
			Instance oneSample = test.get(i);

			double classify = forest
					.classify(test.getDataset(), rng, oneSample);
			int label = data.getDataset().valueOf(0,
					String.valueOf((int) classify));

			System.out.println("label: " + label);
		}
	}

	private static String[] testFileAsStringArray(String file) throws Exception {
		ArrayList<String> list = new ArrayList<String>();

		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String strLine;
		br.readLine(); // discard top one (header)
		while ((strLine = br.readLine()) != null) {
			list.add("-," + strLine);
		}

		in.close();
		return list.toArray(new String[list.size()]);
	}

	private static DecisionForest buildForest(int numberOfTrees, Data data) {
		int m = (int) Math
				.floor(Maths.log(2, data.getDataset().nbAttributes()) + 1);

		DefaultTreeBuilder treeBuilder = new DefaultTreeBuilder();
		treeBuilder.setM(m);

		return new SequentialBuilder(RandomUtils.getRandom(), treeBuilder,
				data.clone()).build(numberOfTrees);
	}

	private static String[] fileAsStringArray(String file) throws Exception {
		ArrayList<String> list = new ArrayList<String>();

		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String strLine;
		br.readLine(); // discard top one (header)
		while ((strLine = br.readLine()) != null) {
			list.add(strLine);
		}

		in.close();
		return list.toArray(new String[list.size()]);
	}
}