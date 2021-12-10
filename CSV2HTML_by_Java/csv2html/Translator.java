package csv2html;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
//import java.util.stream.Collectors;

import utility.ImageUtility;
import utility.StringUtility;

/**
 * トランスレータ：CSVファイルをHTMLページへと変換するプログラム。
 */
public class Translator extends Object {
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
	public Translator(Class<? extends Attributes> classOfAttributes) {
		super();

		Attributes.flushBaseDirectory();

		try {
			Constructor<? extends Attributes> aConstructor = classOfAttributes.getConstructor(String.class);

			this.inputTable = new Table(aConstructor.newInstance("input"));
			this.outputTable = new Table(aConstructor.newInstance("output"));
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException
				| InvocationTargetException anException) {
			anException.printStackTrace();
		}

		return;
	}

	/**
	 * 在位日数を計算して、それを文字列にして応答する。
	 * @param periodString 在位期間の文字列
	 * @return 在位日数の文字列
	 */
	public String computeNumberOfDays(String periodString) {
		String periodSeparator = "〜";
		String dateSeparator = "年月日";

		Function<String, Calendar> dateToCalender = (date) -> {
			List<String> splitDateString = StringUtility.splitString(date, dateSeparator);
			if (splitDateString.size() != 3) {
				throw new IllegalArgumentException("日付の文字列に誤りがあります。");
			}

			List<Integer> splitDateInteger = splitDateString.stream().map((aString) -> Integer.valueOf(aString))
					.toList();
			//Java8~でも使用するならこっち
			//List<Integer> splitDateInteger = splitDateString.stream().map((aString) -> Integer.valueOf(aString)).collect(Collectors.toList());

			Calendar aCalendar = Calendar.getInstance();
			aCalendar.set(splitDateInteger.get(0), splitDateInteger.get(1) - 1, splitDateInteger.get(2));
			return aCalendar;
		};

		List<String> splitPeriod = StringUtility.splitString(periodString, periodSeparator);
		Calendar start = dateToCalender.apply(splitPeriod.get(0));
		Calendar end = (splitPeriod.size() == 2) ? dateToCalender.apply(splitPeriod.get(1)) : Calendar.getInstance();
		Long diffMillis = end.getTimeInMillis() - start.getTimeInMillis();
		Long diffDay = diffMillis / (1000 * 60 * 60 * 24) + 1;

		return String.format("%,d", diffDay);
	}

	/**
	 * サムネイル画像から画像へ飛ぶためのHTML文字列を作成して、それを応答する。
	 * @param aString 画像の文字列
	 * @param aTuple タプル
	 * @param no 番号
	 * @return サムネイル画像から画像へ飛ぶためのHTML文字列
	 */
	public String computeStringOfImage(String aString, Tuple aTuple, int no) {
		StringBuilder aBuilder = new StringBuilder();
		String imagePath = aTuple.values().get(aTuple.attributes().indexOfImage());
		String thumbnailPath = aTuple.values().get(aTuple.attributes().indexOfThumbnail());
		BufferedImage anImage = ImageUtility
				.readImageFromFile(new File(this.inputTable.attributes().baseDirectory(), thumbnailPath));
		Integer imageWidth = anImage.getWidth();
		Integer imageHeight = anImage.getHeight();
		aBuilder.append(String.format("<a name=\"%d\" href=\"%s\">", no, imagePath));
		aBuilder.append(String.format("<img class=\"borderless\" src=\"%s\" width=\"%d\" height=\"%d\" alt=\"%s\">",
				thumbnailPath, imageWidth, imageHeight, aString));
		aBuilder.append("</a>");
		return aBuilder.toString();
	}

	/**
	 * CSVファイルをHTMLページへ変換する。
	 */
	public void execute() {
		try {
			// 必要な情報をダウンロードする。
			Downloader aDownloader = new Downloader(this.inputTable);
			aDownloader.start();
			aDownloader.join();

			// CSVに由来するテーブルをHTMLに由来するテーブルへと変換する。
			System.out.println(this.inputTable);
			this.translate();
			System.out.println(this.outputTable);

			// HTMLに由来するテーブルから書き出す。
			Writer aWriter = new Writer(this.outputTable);
			aWriter.perform();
		} catch (InterruptedException exception) {

		}

		// ブラウザを立ち上げて閲覧する。
		try {
			Attributes attributes = this.outputTable.attributes();
			String fileStringOfHTML = attributes.baseDirectory() + attributes.indexHTML();
			ProcessBuilder aProcessBuilder = new ProcessBuilder("open", "-a", "Safari", fileStringOfHTML);
			aProcessBuilder.start();
		} catch (Exception anException) {
			anException.printStackTrace();
		}

		return;
	}

	/**
	 * 属性リストのクラスを受け取って、CSVファイルをHTMLページへと変換するクラスメソッド。
	 * @param classOfAttributes 属性リストのクラス
	 */
	public static void perform(Class<? extends Attributes> classOfAttributes) {
		// トランスレータのインスタンスを生成する。
		Translator aTranslator = new Translator(classOfAttributes);
		// トランスレータにCSVファイルをHTMLページへ変換するように依頼する。
		aTranslator.execute();

		return;
	}

	/**
	 * CSVファイルを基にしたテーブルから、HTMLページを基にするテーブルに変換する。
	 */
	public void translate() {

		//新たに設定する属性リストのキーと名前
		Map<String, String> replacementNames = new HashMap<>();
		replacementNames.put("days", "在位日数");

		//outputTableの属性リストの名前を設定
		this.outputTable.attributes().keys().forEach((key) -> {
			Attributes outputAttributes = this.outputTable.attributes();
			Attributes inputAttributes = this.inputTable.attributes();
			Integer indexOfOutputKey = outputAttributes.indexOf(key);
			Integer indexOfInputKey = inputAttributes.indexOf(key);

			String aName = "Undefined"; // inputTableにもreplacementNamesにも定義されていないなら未定義
			if (replacementNames.containsKey(key)) {
				aName = replacementNames.get(key);
			} else if (indexOfInputKey != -1) {
				aName = inputAttributes.nameAt(indexOfInputKey);
			}
			outputAttributes.names().set(indexOfOutputKey, IO.htmlCanonicalString(aName));
		});

		//outputTableにタプルの要素を追加
		this.inputTable.tuples().forEach((aTuple) -> {
			Attributes inputAttributes = this.inputTable.attributes();
			List<String> tupleValues = new ArrayList<>();

			this.outputTable.attributes().keys().forEach((key) -> {
				String aValue = "";
				if ("days".equals(key)) {
					//在位日数を入れる
					aValue = this.computeNumberOfDays(aTuple.values().get(inputAttributes.indexOf("period")));
				} else if ("image".equals(key)) {
					//画像のHTML文を入れる
					File imageFile = new File(aTuple.values().get(inputAttributes.indexOfImage()));
					String no = aTuple.values().get(inputAttributes.indexOfNo());
					aValue = this.computeStringOfImage(imageFile.getName(), aTuple, Integer.valueOf(no));
				} else if (inputAttributes.indexOf(key) != -1) {
					aValue = IO.htmlCanonicalString(aTuple.values().get(inputAttributes.indexOf(key)));
				}
				tupleValues.add(aValue);
			});

			this.outputTable.add(new Tuple(this.outputTable.attributes(), tupleValues));
		});

		return;
	}
}
