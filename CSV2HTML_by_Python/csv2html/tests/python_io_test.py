"""Io.pyの単体テストを行う"""
import unittest

from csv2html.attributes import AttributesForPrimeMinisters
from csv2html.table import Table
from csv2html.io import IO

class PythonIOTest(unittest.TestCase):
	"""IOクラスの単体テストを行うクラス"""

	def setUp(self):
		self._attributes = AttributesForPrimeMinisters
		self._classes = [self._attributes]
		for class_attributes in self._classes:
			self._table = Table('test', class_attributes)
		self._io = IO(self._table)

		self.testfilepath = 'csv2html/tests/testdata.csv'

	def test_readcsv(self):
		"""csvファイルを正しく読み込めているか確認する"""
		print('csvファイルを正しく読み込めているか確認する')
		a_list = self._io.read_csv(self.testfilepath)
		print("TestCase1: [1,2,3,4,5,6]")
		self.assertEqual(a_list[0], ['1', '2', '3', '4', '5', '6'])
		if self.assertEqual(a_list[0], ['1', '2', '3', '4', '5', '6']) is None:
			print('Test OK')
		print("TestCase2: [あ,お,き,あ,つ,し]")
		self.assertEqual(a_list[1], ['あ', 'お', 'き', 'あ', 'つ', 'し'])
		if self.assertEqual(a_list[1], ['あ', 'お', 'き', 'あ', 'つ', 'し']) is None:
			print('Test OK')

	def test_html_canonical_string(self):
		"""htmlへの文字変換が正しく出来ているか確認するテスト"""
		testcontext = '&><" '
		print('TestCase1:&><" ')
		self.assertEqual(self._io.html_canonical_string(testcontext), '&amp;&gt;&lt;&quot;&nbsp;')
		if self.assertEqual(self._io.html_canonical_string(testcontext), '&amp;&gt;&lt;&quot;&nbsp;')is None:
			print('Test OK')
		print('TestCase2: \\t\\r\\n\\f')
		testcontext = '\t\r\n\f'
		self.assertEqual(self._io.html_canonical_string(testcontext), '<br>')
		if self.assertEqual(self._io.html_canonical_string(testcontext), '<br>')is None:
			print('Test OK')
		print('TestCase3: あおきあつし')
		testcontext = "あおきあつし"
		self.assertEqual(self._io.html_canonical_string(testcontext), 'あおきあつし')
		if self.assertEqual(self._io.html_canonical_string(testcontext), 'あおきあつし') is None:
			print('Test OK')
