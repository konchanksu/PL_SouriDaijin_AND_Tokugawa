#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""CSV2HTML：総理大臣と徳川幕府の情報「CSV」からWebページ「HTML」を生成。"""
__author__ = 'Hidemasa Kondo (Created by AOKI Atsushi)'
__version__ = '1.3.0'
__date__ = '2021/12/15 (Created: 2016/01/01)'

import csv

# pylint: disable=R0201
# R0201: Method could be a function (no-self-use)


class IO:
	"""入出力：リーダ・ダウンローダ・ライタを抽象する。"""

	def __init__(self, a_table):
		"""入出力のコンストラクタ。"""

		super().__init__()
		self._table = a_table

	def attributes(self):
		"""属性リストを応答する。"""

		return self.table().attributes()

	def read_csv(self, filename):
		"""指定されたファイルをCSVとして読み込み、行リストを応答する。"""

		row_list = []
		with open(filename, "r", encoding="utf-8") as a_file:
			row_list = list(csv.reader(a_file))

		return row_list

	@classmethod
	def html_canonical_string(cls, a_string: str):
		"""指定された文字列をHTML内に記述できる正式な文字列に変換して応答する。"""

		table = {
			'&': '&amp;',
			'>': '&gt;',
			'<': '&lt;',
			'"': '&quot;',
			' ': '&nbsp;',
			'\t': '',
			'\r': '',
			'\n': '<br>',
			'\f': '',
		}

		for key, value in table.items():
			a_string = a_string.replace(key, value)

		return a_string

	def table(self):
		"""テーブルを応答する。"""

		return self._table

	def tuples(self):
		"""タプル群を応答する。"""

		return self.table().tuples()

	def write_csv(self, filename, rows):
		"""指定されたファイルにCSVとして行たち(rows)を書き出す。"""

		with open(filename, "w", encoding="utf-8") as a_file:
			writer = csv.writer(a_file)
			writer.writerows(rows)
