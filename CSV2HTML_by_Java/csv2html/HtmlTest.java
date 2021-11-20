package csv2html;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * 文字列のHTML記述への変換に対するテスト
 */
public class HtmlTest{
	/**
	 * "&"を"&amp;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforAmpersand()
	{
		assertEquals("&amp;", IO.htmlCanonicalString("&"));
	}

	/**
	 * ">"を"&gt;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforBiggerthan()
	{
		assertEquals("&gt;", IO.htmlCanonicalString(">"));
	}

	/**
	 * "<"を"&lt;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforLessthan()
	{
		assertEquals("&lt;", IO.htmlCanonicalString("<"));
	}

	/**
	 * "\"を"&quot;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforBackslash()
	{
		assertEquals("&quot;", IO.htmlCanonicalString("\""));
	}

	/**
	 * " "を"&nbsp;"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforNoBreakSpace()
	{
		assertEquals("&nbsp;", IO.htmlCanonicalString(" "));
	}

	/**
	 * "\t"を""に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforTab()
	{
		assertEquals("", IO.htmlCanonicalString("\t"));
	}

	/**
	 * "\r"を""に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforCarriageReturn()
	{
		assertEquals("", IO.htmlCanonicalString("\r"));
	}

	/**
	 * "\n"を"<br>"に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforLineFeed()
	{
		assertEquals("<br>", IO.htmlCanonicalString("\n"));
	}
	/**
	 * "\f"を""に変換できているかのテスト
	 */
	@Test
	public void compareOriginalWordwithReritedWordforFormFeed()
	{
		assertEquals("", IO.htmlCanonicalString("\f"));
	}
}