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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		CliOptions cliOptions;
		try {
			cliOptions = CommandLine.populateCommand(new CliOptions(), args);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			CommandLine.usage(new CliOptions(), System.err);
			System.exit(1);
			return;
		}

		if (cliOptions.isHelp()) {
			CommandLine.usage(new CliOptions(), System.out);
			return;
		} else {
			if (Proxy.Type.DIRECT != cliOptions.getProxyType()) {
				checkRequiredOption("proxyHost", cliOptions.getProxyHost());
				checkRequiredOption("proxyPort", cliOptions.getProxyPort());
			}
			Client client = new Client(cliOptions.getApplicationName(),
					cliOptions.getLocalServerPort(),
					cliOptions.getProxyType(),
					cliOptions.getProxyHost(),
					cliOptions.getProxyPort());
			try {
				client.login();
				Downloader downloader = new Downloader(client,
						cliOptions.getFileId(), cliOptions.getLocalFile());
				downloader.run();
			} catch (IOException | GeneralSecurityException ex) {
				logger.error(ex.getMessage(), ex);
				System.exit(1);
			}
		}
	}

	private static void checkRequiredOption(String name, Object value) {
		if (null == value) {
			System.err.println(String.format("Missing required option '--%s=<%s>'", name, name));
			CommandLine.usage(new CliOptions(), System.err);
			System.exit(1);
		}
	}
}
