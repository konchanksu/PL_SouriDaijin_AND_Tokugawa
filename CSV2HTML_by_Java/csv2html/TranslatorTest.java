package csv2html;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Translatorに対するテスト
 */
public class TranslatorTest{

	private Translator aTranslator = new Translator(ExtendsAttributesUsedTest.class);

	/**
	 * 1日が正しく計算できているかのテスト
	 */
	@Test
	public void caluculateOneDay() {
        assertEquals("2", this.aTranslator.computeNumberOfDays("2020年1月1日〜2020年1月2日"));
    }

    /**
	 * 1週間が正しく計算できているかのテスト
	 */
    @Test
	public void caluculateOneWeek() {
        assertEquals("8", this.aTranslator.computeNumberOfDays("2020年1月1日〜2020年1月8日"));
    }

    /**
	 * 1年が正しく計算できているかのテスト
	 */
    @Test
	public void caluculateOneYear() {
		assertEquals("366", this.aTranslator.computeNumberOfDays("2019年1月1日〜2020年1月1日"));
    }

	/**
	 * 1年(うるう年)が正しく計算できているかのテスト
	 */
    @Test
	public void caluculateLeapYear() {
		assertEquals("367", this.aTranslator.computeNumberOfDays("2020年1月1日〜2021年1月1日"));
    }
	
    /**
	 * 1752年の9月2日と14日の間が正しく計算できているかのテスト
	 */
    @Test
	public void caluculateGregorian() {
		assertEquals("2", this.aTranslator.computeNumberOfDays("1752年9月2日〜1752年9月14日"));
    }
}