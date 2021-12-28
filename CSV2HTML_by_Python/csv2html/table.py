#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""CSV2HTML：総理大臣と徳川幕府の情報「CSV」からWebページ「HTML」を生成。"""
__author__ = 'Hidemasa Kondo (Created by AOKI Atsushi)'
__version__ = '1.3.0'
__date__ = '2021/11/05 (Created: 2016/01/01)'

class Table:
	"""表：情報テーブル。"""

	def __init__(self, kind_string, class_attributes):
		"""テーブルのコンストラクタ。"""

		super().__init__()
		self._attributes = class_attributes(kind_string)
		self._tuples = []

	def __str__(self):
		"""自分自身を文字列にして、それを応答する。"""

		string = '0: ' + str(self.attributes())
		countup_number = 1
		for a_tuple in self._tuples:
			string += '\n' + str(countup_number) + ': ' + str(a_tuple)
			countup_number += 1

		return string

	def add(self, a_tuple):
		"""タプルを追加する。"""

		self._tuples.append(a_tuple)

	def attributes(self):
		"""属性リストを応答する。"""

		return self._attributes

	def image_filenames(self):
		"""画像ファイル群をリストにして応答する。"""

		return_image_filenames = []
		for a_tuple in self._tuples:
			return_image_filenames.append(a_tuple.values()[self._attributes.keys().index("image")])
		return return_image_filenames

	def thumbnail_filenames(self):
		"""縮小画像ファイル群をリストにして応答する。"""

		return_thumbnail_filenames = []
		for a_tuple in self._tuples:
			return_thumbnail_filenames.append(a_tuple.values()[self._attributes.keys().index("thumbnail")])
		return return_thumbnail_filenames


	def tuples(self):
		"""タプル群を応答する。"""

		return self._tuples
