package org.distributeme.registry.ui;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import java.util.Arrays;
import java.util.List;

/**
 * Base filter for UI functionality.
 *
 * @author dsilenko
 */
public class UIFilter extends MAFFilter {

	/**
	 * Default logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(UIFilter.class);

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.info("Initializing UIFilter...");
		super.init(config);
	}

	@Override
	protected List<ActionMappingsConfigurator> getConfigurators() {
		return Arrays.asList(new ActionMappingsConfigurator[]{new BaseActionsConfigurator()});
	}
	@Override
	protected String getDefaultActionName() {
		return "registry";
	}

}
