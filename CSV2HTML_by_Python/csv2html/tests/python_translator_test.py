"""translator.pyの単体テストを行う"""
import unittest

from csv2html.attributes import AttributesForPrimeMinisters
from csv2html.translator import Translator

class PythonTranslatorTest(unittest.TestCase):
	"""Translatorクラスの単体テストを行うクラス"""

	def setUp(self):
		self._attributes = AttributesForPrimeMinisters
		self._classes = [self._attributes]
		for class_attributes in self._classes:
			self._translator = Translator(class_attributes)

		self.testfilepath = 'csv2html/tests/testdata.csv'

	def test_compute_string_of_days(self):
		"""compute_string_of_days内で日数計算が正しく出来ているか確認するテスト"""
		print('compute_string_of_days内で日数計算が正しく出来ているか確認するテスト')

		testdata = "1745年11月2日〜1760年5月13日"
		testdata2 = "3000年1月1日〜4000年12月31日"
		calcresult = self._translator.compute_string_of_days(testdata)
		print("TestCase1: 1745年11月2日〜1760年5月13日")
		self.assertEqual(calcresult, "5307")
		if self.assertEqual(calcresult, "5307") is None:
			print('Test OK')

		calcresult = self._translator.compute_string_of_days(testdata2)
		print("TestCase2: 3000年1月1日〜4000年12月31日")
		self.assertEqual(calcresult, "365608")
		if self.assertEqual(calcresult, "365608") is None:
			print('Test OK')
