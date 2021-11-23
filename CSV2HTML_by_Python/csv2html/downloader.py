#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""CSV2HTML：総理大臣と徳川幕府の情報「CSV」からWebページ「HTML」を生成。"""
__author__ = 'AOKI Atsushi'
__version__ = '1.0.7'
__date__ = '2021/01/10 (Created: 2016/01/01)'

import os
# import shutil
import urllib.request

from csv2html.io import IO
from csv2html.reader import Reader


class Downloader(IO):
	"""ダウンローダ：CSVファイル・画像ファイル・サムネイル画像ファイルをダウンロードする。"""

	def __init__(self, input_table):
		"""TODO ダウンローダのコンストラクタ

		Parameters
		----------
		input_table : Table
			入力テーブル
		"""

		super().__init__(input_table)
		(lambda x: x)(input_table)  # NOP

	def download_csv(self):
		"""情報を記したCSVファイルをダウンロードする。"""

		url = super().attributes().csv_url()
		local_csv_file = super().attributes().base_directory() + os.sep + os.path.basename(url)

		# csvをダウンロード
		with urllib.request.urlopen(url) as a_file:
			csv_text = a_file.read().decode('utf-8')

		# 文字列をcsvに書き出し
		with open(local_csv_file, "w") as a_file:
			a_file.write(csv_text)


	def download_images(self, image_filenames):
		"""画像ファイル群または縮小画像ファイル群をダウンロードする。"""

		base_url = super().attributes().base_url()
		base_dir = super().attributes().base_directory()

		# 画像ファイルをダウンロード
		for image_filename in image_filenames:
			with urllib.request.urlopen(base_url + image_filename) as a_file:
				image_data = a_file.read()

			# ファイルが存在しない場合は生成
			a_directory = os.path.dirname(base_dir + os.sep + image_filename)
			os.makedirs(a_directory, exist_ok=True)

			# 書き出し
			with open(base_dir + os.sep + image_filename, mode="wb") as a_image_file:
				a_image_file.write(image_data)

	def perform(self):
		"""すべて（情報を記したCSVファイル・画像ファイル群・縮小画像ファイル群）をダウンロードする。"""
		self.download_csv()
		Reader(super().table()).perform()
		self.download_images(super().table().thumbnail_filenames())
		self.download_images(super().table().image_filenames())
