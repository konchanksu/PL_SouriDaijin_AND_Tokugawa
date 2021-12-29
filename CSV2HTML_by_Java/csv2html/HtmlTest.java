package csv2html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * 文字列のHTML記述への変換に対するテスト
 * 
 * @author Kajiwara Ryutaro
 * @version 1.0.0
 */
public class HtmlTest {
	/**
	 * "&"を"&amp;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforAmpersandTest() {
		assertEquals("&amp;", IO.htmlCanonicalString("&"));
	}

	/**
	 * ">"を"&gt;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforBiggerthanTest() {
		assertEquals("&gt;", IO.htmlCanonicalString(">"));
	}

	/**
	 * "<"を"&lt;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforLessthanTest() {
		assertEquals("&lt;", IO.htmlCanonicalString("<"));
	}

	/**
	 * "\"を"&quot;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforBackslashTest() {
		assertEquals("&quot;", IO.htmlCanonicalString("\""));
	}

	/**
	 * " "を"&nbsp;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforNoBreakSpaceTest() {
		assertEquals("&nbsp;", IO.htmlCanonicalString(" "));
	}

	/**
	 * "\t"を""に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforTabTest() {
		assertEquals("", IO.htmlCanonicalString("\t"));
	}

	/**
	 * "\r"を""に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforCarriageReturnTest() {
		assertEquals("", IO.htmlCanonicalString("\r"));
	}

	/**
	 * "\n"を"<br>"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforLineFeedTest() {
		assertEquals("<br>", IO.htmlCanonicalString("\n"));
	}

	/**
	 * "\f"を""に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforFormFeedTest() {
		assertEquals("", IO.htmlCanonicalString("\f"));
	}
}
