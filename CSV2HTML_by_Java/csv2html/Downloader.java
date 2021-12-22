package csv2html;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utility.ImageUtility;

/**
 * ダウンローダ：CSVファイル・画像ファイル・サムネイル画像ファイルをダウンロードする。
 */
public class Downloader extends IO {
	/**
	 * ダウンローダのコンストラクタ。
	 * @param aTable テーブル
	 */
	public Downloader(Table aTable) {
		super(aTable);

		return;
	}

	@Override
	public void run() {
		this.perform();

		return;
	}

	/**
	 * 総理大臣の情報を記したCSVファイルをダウンロードする。
	 */
	public void downloadCSV() {
		String csvUrl = super.attributes().csvUrl();
		List<String> splitUrl = IO.splitString(csvUrl, "/");
		File csvFile = new File(super.attributes().baseDirectory(), splitUrl.get(splitUrl.size() - 1));
		List<String> csvText = IO.readTextFromURL(csvUrl);
		IO.writeText(csvText, csvFile);
		return;
	}

	/**
	 * 総理大臣の画像群をダウンロードする。
	 */
	public void downloadImages() {
		int indexOfImage = this.attributes().indexOfImage();
		this.downloadPictures(indexOfImage);

		return;
	}

	/**
	 * 総理大臣の画像群またはサムネイル画像群をダウンロードする。
	 * @param indexOfPicture 画像のインデックス
	 */
	private void downloadPictures(int indexOfPicture) {

		List<Thread> processList = new ArrayList<>();
		super.table().tuples().forEach((aTuple) -> {
			processList.add(new Thread(() -> {
				String pictureName = aTuple.values().get(indexOfPicture);
				BufferedImage anImage = ImageUtility.readImageFromURL(super.attributes().baseUrl() + pictureName);
				File saveImageFile = new File(super.attributes().baseDirectory(), pictureName);
				saveImageFile.getParentFile().mkdirs();
				ImageUtility.writeImage(anImage, saveImageFile);
			}));
		});

		processList.forEach((aProcess) -> {
			aProcess.start();
		});

		processList.forEach((aProcess) -> {
			try {
				aProcess.join();
			} catch (InterruptedException exception) {

			}
		});
		return;
	}

	/**
	 * 総理大臣の画像群をダウンロードする。
	 */
	public void downloadThumbnails() {
		int indexOfThumbnail = this.attributes().indexOfThumbnail();
		this.downloadPictures(indexOfThumbnail);

		return;
	}

	/**
	 * 総理大臣の情報を記したCSVファイルをダウンロードして、画像群やサムネイル画像群もダウロードする。
	 */
	public void perform() {
		this.downloadCSV();

		new Reader(super.table()).perform();

		Thread downloadImageThread = new Thread(() -> {
			this.downloadImages();
			return;
		});

		Thread downloadThumbnailsThread = new Thread(() -> {
			this.downloadThumbnails();
			return;
		});

		downloadImageThread.start();
		downloadThumbnailsThread.start();

		try {
			downloadImageThread.join();
			downloadThumbnailsThread.join();
		} catch (InterruptedException exception) {

		}

		return;
	}
}
