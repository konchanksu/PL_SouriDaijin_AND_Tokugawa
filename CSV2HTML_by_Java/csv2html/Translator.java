package csv2html;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

import utility.StringUtility;

/**
 * トランスレータ：CSVファイルをHTMLページへと変換するプログラム。
 */
public class Translator extends Object
{
	/**
	 * CSVに由来するテーブルを記憶するフィールド。
	 */
	private Table inputTable;

	/**
	 * HTMLに由来するテーブルを記憶するフィールド。
	 */
	private Table outputTable;

	/**
	 * 属性リストのクラスを指定したトランスレータのコンストラクタ。
	 * @param classOfAttributes 属性リストのクラス
	 */
	public Translator(Class<? extends Attributes> classOfAttributes)
	{
		super();

		Attributes.flushBaseDirectory();

		try
		{
			Constructor<? extends Attributes> aConstructor = classOfAttributes.getConstructor(String.class);

			this.inputTable = new Table(aConstructor.newInstance("input"));
			this.outputTable = new Table(aConstructor.newInstance("output"));
		}
		catch (NoSuchMethodException |
			   InstantiationException |
			   IllegalAccessException |
			   InvocationTargetException anException) { anException.printStackTrace(); }

		return;
	}

	/**
	 * 在位日数を計算して、それを文字列にして応答する。
	 * @param periodString 在位期間の文字列
	 * @return 在位日数の文字列
	 */
	public String computeNumberOfDays(String periodString)
	{
		String periodSeparator = "〜";
		String dateSeparator = "年月日";

		Function <String,Calendar>  dateToCalender =(date)->{
			List<String> splitDateString = StringUtility.splitString(date, dateSeparator);
			List<Integer> splitDateInteger = splitDateString.stream().map((aString)->Integer.valueOf(aString)).toList();
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.set(splitDateInteger.get(0),splitDateInteger.get(1)-1,splitDateInteger.get(2));
			return aCalendar;
		};

		List<String> splitPeriod = StringUtility.splitString(periodString, periodSeparator);
		Calendar start = dateToCalender.apply(splitPeriod.get(0));
		Calendar end = (splitPeriod.size() == 2) ? dateToCalender.apply(splitPeriod.get(1)) : Calendar.getInstance();
		Long diffMillis = end.getTimeInMillis()-start.getTimeInMillis();
		Long diffDay = diffMillis/(60*60*24*1000)+1;

		return String.format("%,d", diffDay);
	}

	/**
	 * TODO
	 * サムネイル画像から画像へ飛ぶためのHTML文字列を作成して、それを応答する。
	 * @param aString 画像の文字列
	 * @param aTuple タプル
	 * @param no 番号
	 * @return サムネイル画像から画像へ飛ぶためのHTML文字列
	 */
	public String computeStringOfImage(String aString, Tuple aTuple, int no)
	{
		return null;
	}

	/**
	 * CSVファイルをHTMLページへ変換する。
	 */
	public void execute()
	{
		// 必要な情報をダウンロードする。
		Downloader aDownloader = new Downloader(this.inputTable);
		aDownloader.perform();

		// CSVに由来するテーブルをHTMLに由来するテーブルへと変換する。
		System.out.println(this.inputTable);
		this.translate();
		System.out.println(this.outputTable);

		// HTMLに由来するテーブルから書き出す。
		Writer aWriter = new Writer(this.outputTable);
		aWriter.perform();

		// ブラウザを立ち上げて閲覧する。
		try
		{
			Attributes attributes = this.outputTable.attributes();
			String fileStringOfHTML = attributes.baseDirectory() + attributes.indexHTML();
			ProcessBuilder aProcessBuilder = new ProcessBuilder("open", "-a", "Safari", fileStringOfHTML);
			aProcessBuilder.start();
		}
		catch (Exception anException) { anException.printStackTrace(); }

		return;
	}

	/**
	 * 属性リストのクラスを受け取って、CSVファイルをHTMLページへと変換するクラスメソッド。
	 * @param classOfAttributes 属性リストのクラス
	 */
	public static void perform(Class<? extends Attributes> classOfAttributes)
	{
		// トランスレータのインスタンスを生成する。
		Translator aTranslator = new Translator(classOfAttributes);
		// トランスレータにCSVファイルをHTMLページへ変換するように依頼する。
		aTranslator.execute();

		return;
	}

	/**
	 * TODO
	 * CSVファイルを基にしたテーブルから、HTMLページを基にするテーブルに変換する。
	 */
	public void translate()
	{
		
		return;
	}
}
