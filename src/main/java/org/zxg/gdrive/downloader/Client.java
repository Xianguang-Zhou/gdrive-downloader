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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Client {

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "credentials.json";

	private String applicationName;
	private int localServerPort;

	private Proxy.Type proxyType;
	private String proxyHost;
	private Integer proxyPort;

	private Drive driveService;

	public Client(String applicationName, int localServerPort, Proxy.Type proxyType, String proxyHost, Integer proxyPort) {
		this.applicationName = applicationName;
		this.localServerPort = localServerPort;
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	public void login() throws GeneralSecurityException, IOException {
		NetHttpTransport.Builder httpTransportBuilder = new NetHttpTransport.Builder();
		if (Proxy.Type.DIRECT != this.proxyType) {
			httpTransportBuilder.setProxy(new Proxy(this.proxyType,
					new InetSocketAddress(InetAddress.getByName(this.proxyHost), this.proxyPort)));
		}
		NetHttpTransport httpTransport = httpTransportBuilder.trustCertificates(GoogleUtils.getCertificateTrustStore())
				.build();
		this.driveService = new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
				.setApplicationName(this.applicationName).build();
	}

	public Drive getDriveService() {
		return driveService;
	}

	private Credential getCredentials(NetHttpTransport httpTransport) throws IOException {
		try (Reader reader = new FileReader(CREDENTIALS_FILE_PATH)) {
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
					clientSecrets, SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
					.setAccessType("offline").build();
			LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(localServerPort).build();
			return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		}
	}
}
