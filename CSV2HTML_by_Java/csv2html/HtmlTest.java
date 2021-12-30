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
	 * "&amp;"を"&amp;amp;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforAmpersandTest() {
		assertEquals("&amp;", IO.htmlCanonicalString("&"));
	}

	/**
	 * "&gt;"を"&amp;gt;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforBiggerthanTest() {
		assertEquals("&gt;", IO.htmlCanonicalString(">"));
	}

	/**
	 * "&lt;"を"&amp;lt;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforLessthanTest() {
		assertEquals("&lt;", IO.htmlCanonicalString("<"));
	}

	/**
	 * "&quot;"を"&amp;quot;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforBackslashTest() {
		assertEquals("&quot;", IO.htmlCanonicalString("\""));
	}

	/**
	 * "&nbsp;"を"&amp;nbsp;"に変換できているかのテスト
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
	 * "\n"を"&lt;br&gt;"に変換できているかのテスト
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
