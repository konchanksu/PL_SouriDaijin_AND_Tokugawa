#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""CSV2HTML：総理大臣と徳川幕府の情報「CSV」からWebページ「HTML」を生成。"""
__author__ = 'Hidemasa Kondo, Ryoga Yamauchi (Created by AOKI Atsushi)'
__version__ = '1.3.0'
__date__ = '2021/12/23 (Created: 2016/01/01)'

import os
# import shutil
import urllib.request
import threading

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
		with open(local_csv_file, "w", encoding="utf-8") as a_file:
			a_file.write(csv_text)


	def download_images(self, image_filenames):
		"""画像ファイル群または縮小画像ファイル群をダウンロードする。"""

		base_url = super().attributes().base_url()
		base_dir = super().attributes().base_directory()
		thread_list = []

		# 画像ファイルをダウンロード
		for image_filename in image_filenames:
			thread = threading.Thread(target=Downloader.download_image, args=(base_url, base_dir, image_filename,))
			thread_list.append(thread)
			thread.start()
		for thread in thread_list:
			thread.join()

	@staticmethod
	def download_image(base_url, base_dir, image_filename):
		""" 画像一つをダウンロードする"""
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
		thumbnail_thread = threading.Thread(target=self.download_images, args=(super().table().thumbnail_filenames(),))
		image_thread = threading.Thread(target=self.download_images, args=(super().table().image_filenames(),))
		thumbnail_thread.start()
		image_thread.start()
		thumbnail_thread.join()
		image_thread.join()
