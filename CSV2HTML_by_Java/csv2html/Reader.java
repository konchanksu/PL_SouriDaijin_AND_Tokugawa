package csv2html;

import java.io.File;
import java.util.List;

/**
 * リーダ：情報を記したCSVファイルを読み込んでテーブルに仕立て上げる。
 */
public class Reader extends IO
{
	/**
	 * リーダのコンストラクタ。
	 * @param aTable テーブル
	 */
	public Reader(Table aTable)
	{
		super(aTable);

		return;
	}

	/**
	 * TODO
	 * ダウンロードしたCSVファイルを読み込む。
	 */
	public void perform()
	{
		String csvUrl = super.attributes().csvUrl();
		List<String> splitUrl = IO.splitString(csvUrl, "/");
		File csvFile = new File(super.attributes().baseDirectory() + splitUrl.get(splitUrl.size() - 1));
		List<String> csvText = IO.readTextFromFile(csvFile);

		if(csvText.size() == 0){
			return;
		}

		//Attributesのnamesに属性名を追加
		super.attributes().names(IO.splitString(csvText.get(0), ","));

		if (csvText.size() == 1) {
			return;
		}

		csvText.subList(1, csvText.size()).forEach((aLine)->{
			List<String> valueList = IO.splitString(aLine, ",");
			Tuple aTuple = new Tuple(super.attributes(), valueList);
			super.table().add(aTuple);
		});
		return;
	}
}
