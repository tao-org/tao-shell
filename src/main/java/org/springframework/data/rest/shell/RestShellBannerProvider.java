package org.springframework.data.rest.shell;

import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.stereotype.Component;

/**
 * Banner provider for the REST shell.
 *
 * @author Jon Brisbin
 */
@Component
@Order(Integer.MIN_VALUE)
public class RestShellBannerProvider implements BannerProvider {

	private static final String VERSION = "1.0";
	private static final String BANNER  = "TAO REST Shell\n==============\n";
	private static final String WELCOME = "Welcome to the REST shell. For assistance hit TAB or type \"help\".";

	@Override public String getBanner() {
		return BANNER + getVersion() + "\n";
	}

	@Override public String getVersion() {
		return VERSION;
	}

	@Override public String getWelcomeMessage() {
		return WELCOME;
	}

	@Override public String getProviderName() {
		return "tao-shell";
	}

}
