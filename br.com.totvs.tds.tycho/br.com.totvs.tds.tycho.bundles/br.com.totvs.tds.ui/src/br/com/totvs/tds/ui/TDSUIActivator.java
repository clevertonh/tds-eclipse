package br.com.totvs.tds.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import br.com.totvs.tds.ui.console.MainConsole;

/**
 * The activator class controls the plug-in life cycle.
 */
public final class TDSUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "br.com.totvs.tds.ui"; //$NON-NLS-1$

	// The shared instance
	private static TDSUIActivator plugin;

	private BundleContext context;

	/**
	 * The constructor.
	 */
	public TDSUIActivator() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.context = context;

		MainConsole.getDefault().getMainConsole(); // inicia o console TDS
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static TDSUIActivator getDefault() {
		return plugin;
	}

	public BundleContext getContext() {
		return context;
	}

	/**
	 * Utility method to create status.
	 *
	 * @param level
	 * @param message
	 * @param thr
	 * @return status
	 */
	public static IStatus showStatus(final int level, final String message, final Object... args) {
		final IStatus status = TDSMessageHandler.createStatus(level, PLUGIN_ID, message, args);
		TDSMessageHandler.showMessage(status);

		getDefault().getLog().log(status);

		return status;
	}

	public static IStatus logStatus(final int level, final String message, final Object... args) {
		final IStatus status = TDSMessageHandler.createStatus(level, PLUGIN_ID, message, args);
		TDSMessageHandler.logMessage(status);

		getDefault().getLog().log(status);

		return status;
	}
}
