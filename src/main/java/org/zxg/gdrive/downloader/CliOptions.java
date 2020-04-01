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
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.net.Proxy;

@Command(showDefaultValues = true,
		sortOptions = false,
		footer = "(c) Copyright Xianguang Zhou (xianguang.zhou@outlook.com) 2020. All rights reserved.",
		description = "Google Drive downloader.")
public class CliOptions {

	@Option(names = {"-an", "--applicationName"},
			defaultValue = "gdrive-downloader")
	private String applicationName;

	@Option(names = {"-lsp", "--localServerPort"}, defaultValue = "-1")
	private Integer localServerPort;

	@Option(names = {"-pt", "--proxyType"}, defaultValue = "DIRECT",
			description = "Supported proxy types: \"DIRECT\", \"HTTP\" or \"SOCKS\".")
	private Proxy.Type proxyType;

	@Option(names = {"-ph", "--proxyHost"})
	private String proxyHost;

	@Option(names = {"-pp", "--proxyPort"})
	private Integer proxyPort;

	@Option(names = {"-fi", "--fileId"}, required = true)
	private String fileId;

	@Option(names = {"-lf", "--localFile"}, required = true)
	private File localFile;

	@Option(names = {"-c", "--continue"})
	private boolean resume = false;

	@Option(names = {"-rt", "--readTimeout"}, defaultValue = "0")
	private Integer readTimeout;

	@Option(names = {"-ct", "--connectTimeout"}, defaultValue = "0")
	private Integer connectTimeout;

	@Option(names = {"-cs", "--chunkSize"})
	private int chunkSize = MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;

	@Option(names = {"-td", "--tokensDir"}, defaultValue = "tokens")
	private File tokensDir;

	@Option(names = {"-cf", "--credentialsFile"}, defaultValue = "credentials.json")
	private File credentialsFile;

	@Option(names = {"--debug"}, description = "Debug flag.")
	private boolean debug = false;

	@Option(names = {"-h", "--help"}, usageHelp = true,
			description = "Display help information.")
	private boolean help = false;

	@Option(names = {"-cn", "--commandName"})
	private String commandName = "java -jar gdrive-downloader.jar";

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Integer getLocalServerPort() {
		return localServerPort;
	}

	public void setLocalServerPort(Integer localServerPort) {
		this.localServerPort = localServerPort;
	}

	public Proxy.Type getProxyType() {
		return proxyType;
	}

	public void setProxyType(Proxy.Type proxyType) {
		this.proxyType = proxyType;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public File getLocalFile() {
		return localFile;
	}

	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}

	public boolean isResume() {
		return resume;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public File getTokensDir() {
		return tokensDir;
	}

	public void setTokensDir(File tokensDir) {
		this.tokensDir = tokensDir;
	}

	public File getCredentialsFile() {
		return credentialsFile;
	}

	public void setCredentialsFile(File credentialsFile) {
		this.credentialsFile = credentialsFile;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
}
