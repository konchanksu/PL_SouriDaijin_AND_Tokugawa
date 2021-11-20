"""table.pyの単体テストを行う"""
import unittest

from csv2html.attributes import AttributesForPrimeMinisters
from csv2html.table import Table
from csv2html.tuple import Tuple

class PythonTableTest(unittest.TestCase):
	"""Tableクラスの単体テストを行うクラス"""

	def setUp(self):
		self._attributes = AttributesForPrimeMinisters
		self._classes = [self._attributes]
		self._tuples = []
		for class_attributes in self._classes:
			self._table = Table('test', class_attributes)

	def test_return_tuple(self):
		"""テーブルがタプルの中身を正しく返してくれるか確認するテスト"""
		print('テーブルがタプルの中身を正しく返してくれるか確認するテスト')
		value = 'test'
		a_tuple = Tuple(self._attributes, value)
		a_tuple2 = Tuple(self._attributes, None)

		self._table.add(a_tuple)
		self._table.add(a_tuple2)
		testtuples = self._table.tuples()
		testtuple = testtuples[0]
		testtuple2 = testtuples[1]
		print('TestCase1: test')
		self.assertEqual(testtuple.values(), 'test')
		if self.assertEqual(testtuple.values(), 'test') is None:
			print('Test OK')
		print('TestCase2: None')
		self.assertEqual(testtuple2.values(), None)
		if self.assertEqual(testtuple2.values(), None) is None:
			print('Test OK')
