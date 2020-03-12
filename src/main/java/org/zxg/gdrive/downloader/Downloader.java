/*
 * Copyright (c) 2020, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.zxg.gdrive.downloader;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import me.tongfei.progressbar.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Downloader {

	private static final long STEPS = 1000;

	private String fileId;
	private File localFile;

	private Drive driveService;

	public Downloader(Client client, String fileId, File localFile) {
		this.fileId = fileId;
		this.localFile = localFile;
		this.driveService = client.getDriveService();
	}

	public void run() throws IOException {
		Get get = this.driveService.files().get(this.fileId);
		MediaHttpDownloader downloader = get.getMediaHttpDownloader();
		try (OutputStream output = new FileOutputStream(localFile)) {
			try (ProgressBar progressBar = new ProgressBar("Downloading...", STEPS)) {
				while (downloader.getDownloadState() != DownloadState.MEDIA_COMPLETE) {
					downloader.download(get.buildHttpRequestUrl(),
							get.getRequestHeaders(), output);
					progressBar.stepTo((long) (downloader.getProgress() * STEPS));
				}
			}
		}
	}
}
