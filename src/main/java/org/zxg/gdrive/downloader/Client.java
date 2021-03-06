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
import com.google.api.client.http.javanet.ConnectionFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Client implements ConnectionFactory {

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_READONLY);

	private String applicationName;
	private int localServerPort;

	private Proxy.Type proxyType;
	private String proxyHost;
	private Integer proxyPort;
	private Proxy proxy;

	private int readTimeout;
	private int connectTimeout;

	private File tokensDir;
	private File credentialsFile;

	private Drive driveService;

	public Client(String applicationName, int localServerPort, Proxy.Type proxyType, String proxyHost,
				  Integer proxyPort, int readTimeout, int connectTimeout, File tokensDir, File credentialsFile) {
		this.applicationName = applicationName;
		this.localServerPort = localServerPort;
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.readTimeout = readTimeout;
		this.connectTimeout = connectTimeout;
		this.tokensDir = tokensDir;
		this.credentialsFile = credentialsFile;
	}

	public void login() throws GeneralSecurityException, IOException {
		if (Proxy.Type.DIRECT != this.proxyType) {
			this.proxy = new Proxy(this.proxyType,
					new InetSocketAddress(
							InetAddress.getByName(this.proxyHost),
							this.proxyPort));
		}
		NetHttpTransport httpTransport = new NetHttpTransport.Builder()
				.trustCertificates(GoogleUtils.getCertificateTrustStore())
				.setConnectionFactory(this).build();
		this.driveService = new Drive.Builder(httpTransport, JSON_FACTORY,
				getCredentials(httpTransport))
				.setApplicationName(this.applicationName).build();
	}

	public Drive getDriveService() {
		return driveService;
	}

	private Credential getCredentials(NetHttpTransport httpTransport) throws IOException {
		try (Reader reader = new FileReader(this.credentialsFile)) {
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
					clientSecrets, SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(this.tokensDir))
					.setAccessType("offline").build();
			LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(localServerPort).build();
			return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		}
	}

	@Override
	public HttpURLConnection openConnection(URL url) throws IOException, ClassCastException {
		HttpURLConnection connection = (HttpURLConnection) (
				null != this.proxy
						? url.openConnection(this.proxy)
						: url.openConnection());
		connection.setReadTimeout(this.readTimeout);
		connection.setConnectTimeout(this.connectTimeout);
		return connection;
	}
}
