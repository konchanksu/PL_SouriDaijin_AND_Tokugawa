package csv2html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utility.StringUtility;

/**
 * ライタ：情報のテーブルをHTMLページとして書き出す。
 *
 * HACK: 全体的に汚いのでいい案があれば直してください...
 */
public class Writer extends IO {
	/**
	 * ライタのコンストラクタ。
	 * @param aTable テーブル
	 */
	public Writer(Table aTable) {
		super(aTable);

		return;
	}

	/**
	 * HTMLページを基にするテーブルからインデックスファイル(index.html)に書き出す。
	 */
	public void perform() {
		Attributes attributes = this.attributes();
		String fileStringOfHTML = attributes.baseDirectory() + attributes.indexHTML();
		File aFile = new File(fileStringOfHTML);
		try (FileOutputStream outputStream = new FileOutputStream(aFile);
				OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream, StringUtility.encodingSymbol());
				BufferedWriter aWriter = new BufferedWriter(outputWriter);) {

			this.writeHeaderOn(aWriter);
			this.writeTableBodyOn(aWriter);
			this.writeFooterOn(aWriter);

		} catch (UnsupportedEncodingException | FileNotFoundException anException) {
			anException.printStackTrace();
		} catch (IOException anException) {
			anException.printStackTrace();
		}

		return;
	}

	/**
	 * 属性リストを書き出す。
	 * @param aWriter ライタ
	 */
	public void writeAttributesOn(BufferedWriter aWriter) throws IOException {

		aWriter.write("<tr>");
		aWriter.newLine();
		this.attributes().names().forEach((aName) -> {
			try {
				aWriter.write("<td class=\"center-pink\">");
				aWriter.write("<strong>");
				aWriter.write(aName);
				aWriter.write("</strong>");
				aWriter.write("</td>");
				aWriter.newLine();
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
		});
		aWriter.write("</tr>");
		aWriter.flush();

		return;
	}

	/**
	 * フッタを書き出す。
	 * @param aWriter ライタ
	 */
	public void writeFooterOn(BufferedWriter aWriter) throws IOException {
		Calendar aCalendar = Calendar.getInstance();
		String dateString = String.format("%d/%d/%d %d:%d:%d", aCalendar.get(Calendar.YEAR),
				aCalendar.get(Calendar.MONTH) + 1, aCalendar.get(Calendar.DAY_OF_MONTH),
				aCalendar.get(Calendar.HOUR_OF_DAY), aCalendar.get(Calendar.MINUTE), aCalendar.get(Calendar.SECOND));
		aWriter.write("<div class=\"right-small\">");
		aWriter.write("Created by Okayama Kodai (CSV2HTML written by Java) ");
		aWriter.write(IO.htmlCanonicalString(dateString));
		aWriter.write("</div>");
		aWriter.newLine();
		aWriter.write("</body>");
		aWriter.newLine();
		aWriter.write("</html>");
		return;
	}

	/**
	 * ヘッダを書き出す。
	 * @param aWriter ライタ
	 */
	public void writeHeaderOn(BufferedWriter aWriter) throws IOException {
		aWriter.write(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		aWriter.newLine();
		aWriter.write("<html lang=\"ja\">");
		aWriter.newLine();
		String header = """
					<head>
						<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">
						<meta http-equiv=\"Content-Style-Type\" content=\"text/css\">
						<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\">
						<meta name=\"keywords\" content=\"Smalltalk,Oriented,Programming\">
						<meta name=\"description\" content=\"Prime Ministers\">
						<meta name=\"author\" content=\"AOKI Atsushi\">
						<link rev=\"made\" href=\"http://www.cc.kyoto-su.ac.jp/~atsushi/\">
						<link rel=\"index\" href=\"index.html\">
						<style type=\"text/css\">
						<!--
						body {
							background-color : #ffffff;
							margin : 20px;
							padding : 10px;
							font-family : serif;
							font-size : 10pt;
						}
						a {
							text-decoration : underline;
							color : #000000;
						}
						a:link {
							background-color : #ffddbb;
						}
						a:visited {
							background-color : #ccffcc;
						}
						a:hover {
							background-color : #dddddd;
						}
						a:active {
							background-color : #dddddd;
						}
						div.belt {
							background-color : #eeeeee;
							padding : 0px 4px;
						}
						div.right-small {
							text-align : right;
							font-size : 8pt;
						}
						img.borderless {
							border-width : 0px;
							vertical-align : middle;
						}
						table.belt {
							border-style : solid;
							border-width : 0px;
							border-color : #000000;
							background-color : #ffffff;
							padding : 0px 0px;
							width : 100%;
						}
						table.content {
							border-style : solid;
							border-width : 0px;
							border-color : #000000;
							padding : 2px 2px;
						}
						td.center-blue {
							padding : 2px 2px;
							text-align : center;
							background-color : #ddeeff;
						}
						td.center-pink {
							padding : 2px 2px;
							text-align : center;
							background-color : #ffddee;
						}
						td.center-yellow {
							padding : 2px 2px;
							text-align : center;
							background-color : #ffffcc;
						}
						-->
						</style>
						<title>Prime Ministers</title>
					</head>
				""";
		aWriter.write(header);
		aWriter.newLine();
		aWriter.flush();
		return;
	}

	/**
	 * ボディを書き出す。
	 * @param aWriter ライタ
	 */
	public void writeTableBodyOn(BufferedWriter aWriter) throws IOException {
		aWriter.write("<body cz-shortcut-listen=\"true\">");
		aWriter.newLine();
		aWriter.write("<div class=\"belt\">");
		aWriter.newLine();
		aWriter.write("<h2>");
		aWriter.write(super.attributes().captionString());
		aWriter.write("</h2>");
		aWriter.newLine();
		aWriter.write("</div>");
		aWriter.write("<table class=\"belt\" summary=\"table\">");
		aWriter.write("<tbody>");
		aWriter.write("<tr>");
		aWriter.write("<td>");
		aWriter.write("<table class=\"content\" summary=\"table\">");
		aWriter.write("<tbody>");
		aWriter.flush();

		this.writeAttributesOn(aWriter);
		this.writeTuplesOn(aWriter);

		aWriter.write("</tbody>");
		aWriter.write("</table>");
		aWriter.write("</td>");
		aWriter.write("</tr>");
		aWriter.write("</tbody>");
		aWriter.write("</table>");
		aWriter.write("</hr>");

		return;
	}

	/**
	 * タプル群を書き出す。
	 * 
	 * HACK: インデックス番号を外に出すのが気持ち悪いので、良い書き方あったら教えてください。
	 * 
	 * @param aWriter ライタ
	 */
	public void writeTuplesOn(BufferedWriter aWriter) throws IOException {
		//タプルの色を指定するHTMLのクラス属性の名前
		List<String> classNames = new ArrayList<>();
		classNames.add("center-blue");
		classNames.add("center-yellow");

		Integer index = 0;
		Integer numberOfClasses = classNames.size();
		for (Tuple aTuple : this.table().tuples()) {
			String className = classNames.get(index);
			aWriter.write("<tr>");
			aWriter.newLine();
			aTuple.values().forEach((aValue) -> {
				try {
					aWriter.write("<td class=\"");
					aWriter.write(className);
					aWriter.write("\">");
					aWriter.write(aValue);
					aWriter.write("</td>");
					aWriter.newLine();
				} catch (IOException exception) {
					throw new RuntimeException(exception);
				}
			});
			aWriter.write("</tr>");
			index = (index + 1) % numberOfClasses;
		}
		return;
	}
}
