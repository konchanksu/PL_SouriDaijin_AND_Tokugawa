#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""CSV2HTML：総理大臣と徳川幕府の情報「CSV」からWebページ「HTML」を生成。"""
__author__ = 'AOKI Atsushi'
__version__ = '1.0.7'
__date__ = '2021/01/10 (Created: 2016/01/01)'

import os

from csv2html.io import IO
from csv2html.tuple import Tuple

class Reader(IO):
	"""リーダ：情報を記したCSVファイルを読み込んでテーブルに仕立て上げる。"""

	def __init__(self, input_table):
		"""リーダのコンストラクタ。"""

		super().__init__(input_table)
		(lambda x: x)(input_table) # NOP

	def perform(self):
		"""ダウンロードしたCSVファイルを読み込む。"""

		url = super().attributes().csv_url()
		local_csv_file = super().attributes().base_directory() + os.sep + os.path.basename(url)
		rows = super().read_csv(local_csv_file)

		for index, name in enumerate(rows[0]):
			super().attributes().names()[index] = name

		for row in rows[1:]:
			a_tuple = Tuple(attributes=super().attributes() ,values=row)
			super().table().add(a_tuple)
