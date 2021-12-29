package csv2html;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * リーダ：情報を記したCSVファイルを読み込んでテーブルに仕立て上げる。
 * 
 * @author Okayama Kodai (Created by Aoki Atsushi)
 * @version 2.0.0
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

		//正規表現を同じ部分で分けようとしましたが、余計に汚くなったのでこうしました。
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
		csvString = IO.uniteLineFeedToUnix(csvString);
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

			//一番後ろの改行、カンマ(,)を削除
			aValue = aValue.substring(0, aValue.length() - 1);
			//ダブルクォートで囲まれていた場合、ダブルクォートを削除
			if (aValue.startsWith("\"") && aValue.endsWith("\"")) {
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
		//URLの長さが足りない場合
		if (splitUrl.isEmpty()) {
			return;
		}
		File csvFile = new File(super.attributes().baseDirectory(), splitUrl.get(splitUrl.size() - 1));
		String csvString = String.join(System.lineSeparator(), IO.readTextFromFile(csvFile));

		//正規表現では最後に改行がある前提なので、無いなら追加
		if (!csvString.endsWith(System.lineSeparator())) {
			csvString += System.lineSeparator();
		}

		List<List<String>> csvValues = new ArrayList<>();
		this.extractRowsFromCsv(csvString).forEach((aRow) -> {
			csvValues.add(this.extractValuesFromRow(aRow));
		});

		Iterator<List<String>> csvIterator = csvValues.iterator();

		//Attributesのnamesに属性名を追加
		if (csvIterator.hasNext()) {
			super.attributes().names(csvIterator.next());
		}

		//要素を追加
		csvIterator.forEachRemaining((values) -> {
			Tuple aTuple = new Tuple(super.attributes(), values);
			super.table().add(aTuple);
		});

		return;
	}
}
