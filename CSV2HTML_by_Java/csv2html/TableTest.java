package csv2html;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Tableに対するテスト
 * @author RyutaroKajiwara
 */
public class TableTest {
	/**
	 * 作成したタプルが追加できているかのテスト
	 */
	@Test
	public void doCreatedTupleAddTest() {
		Attributes anAttributets = null;

		var aTable = new Table(anAttributets);

		List<Tuple> testTuple = new ArrayList<>();

		List<String> testValues = new ArrayList<>();
		testValues.add("test");

		Tuple testCase = new Tuple(anAttributets, testValues);
		testTuple.add(testCase);
		
		aTable.add(testCase);

        assertEquals(aTable.tuples().get(0), testTuple.get(0));
    }
}