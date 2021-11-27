package csv2html;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * リーダ：情報を記したCSVファイルを読み込んでテーブルに仕立て上げる。
 */
public class Reader extends IO {

	/**
	 * csvファイル全体から行を取り出すための正規表現
	 */
	private final Pattern patternOfExtractRows;

	/**
	 * 1行から要素を取り出すための正規表現
	 */
	private final Pattern patternOfExtractValues;

	/**
	 * リーダのコンストラクタ。
	 * @param aTable テーブル
	 */
	public Reader(Table aTable) {
		super(aTable);
		this.patternOfExtractRows = Pattern.compile("((\"[^\"]*\")|([^\n\",]*),?)*\n");
		this.patternOfExtractValues = Pattern.compile("((\"[^\"]*\")|([^\n\",]*))(,|\n)");

		return;
	}

	/**
	 * CSVファイル全体の文字列から行のリストを取り出す。
	 * @param csvString CSV全体の文字列
	 * @return 1行ごとに分割したリスト
	 */
	private List<String> extractRowsFromCsv(String csvString) {
		Matcher rowMatcher = this.patternOfExtractRows.matcher(csvString);
		List<String> aList = new ArrayList<>();
		while (rowMatcher.find()) {
			String aRow = rowMatcher.group();
			aList.add(aRow);
		}

		return aList;
	}

	/**
	 * CSVの一行の文字列から要素のリストを取り出す。
	 * @param aRow CSVの一行の文字列
	 * @return 要素のリスト
	 */
	private List<String> extractValuesFromRow(String aRow) {
		Matcher valueMatcher = this.patternOfExtractValues.matcher(aRow);
		List<String> aList = new ArrayList<>();
		while (valueMatcher.find()) {
			String aValue = valueMatcher.group();
			aValue = aValue.substring(0, aValue.length() - 1);
			aValue.trim();
			if (aValue.startsWith("\"")) {
				aValue = aValue.substring(1, aValue.length() - 1);
			}
			aList.add(aValue);
		}

		return aList;
	}

	/**
	 * ダウンロードしたCSVファイルを読み込む。
	 */
	public void perform() {
		String csvUrl = super.attributes().csvUrl();
		List<String> splitUrl = IO.splitString(csvUrl, "/");
		File csvFile = new File(super.attributes().baseDirectory(), splitUrl.get(splitUrl.size() - 1));
		String csvString = String.join(System.lineSeparator(), IO.readTextFromFile(csvFile));

		if (csvString.isEmpty()) {
			return;
		}
		if (!csvString.endsWith(System.lineSeparator())) {
			csvString += System.lineSeparator();
		}

		List<String> rows = this.extractRowsFromCsv(csvString);
		List<List<String>> csvValues = new ArrayList<>();
		rows.forEach((aRow) -> {
			csvValues.add(this.extractValuesFromRow(aRow));
		});

		//Attributesのnamesに属性名を追加
		super.attributes().names(csvValues.get(0));

		if (csvValues.size() == 1) {
			return;
		}

		csvValues.subList(1, csvValues.size()).forEach((values) -> {
			Tuple aTuple = new Tuple(super.attributes(), values);
			super.table().add(aTuple);
		});
		return;
	}
}
