package br.com.totvs.tds.ui.monitor.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import br.com.totvs.osgi.notificationcenter.INotification;
import br.com.totvs.osgi.notificationcenter.INotificationService;
import br.com.totvs.osgi.notificationcenter.NotificationHandler;
import br.com.totvs.tds.server.ui.jobs.RefreshServerViewJob;

public class RefreshItemsMonitor30Seconds extends ServerMonitorHandler {

	private boolean isEnabled = true;
	RefreshServerViewJob job;

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		sendNotification();
		hookNotification();
		if (job != null) {
			job.stop();
		}
		job = new RefreshServerViewJob("Refresh 30 Seconds", 30000); //$NON-NLS-1$
		job.schedule();
		isEnabled = false;
		return null;
	}

	private void sendNotification() {
		BundleContext bundleContext = FrameworkUtil.getBundle(INotificationService.class).getBundleContext();
		ServiceReference<?> sr = bundleContext.getServiceReference(INotificationService.class.getName());
		INotificationService notificationService = (INotificationService) bundleContext.getService(sr);

		INotification notification = notificationService.createNotification(
				INotification.EventType.MONITOR_REFRESH_30_SEC, this);
		notificationService.send(notification);
	}

	private void hookNotification() {
		BundleContext bundleContext = FrameworkUtil.getBundle(INotificationService.class).getBundleContext();
		ServiceReference<?> sr = bundleContext.getServiceReference(INotificationService.class.getName());
		INotificationService notificationService = (INotificationService) bundleContext.getService(sr);

		NotificationHandler eventHandlerStateConnection = new NotificationHandler() {
			@Override
			public void receive(final INotification notification) {
				if (notification.getEventType() == INotification.EventType.MONITOR_REFRESH_15_SEC
						|| notification.getEventType() == INotification.EventType.MONITOR_REFRESH_05_SEC) {
					job.stop();
					isEnabled = true;
				}
			}
		};

		notificationService.subscribe(INotification.EventType.MONITOR_REFRESH_15_SEC,
				eventHandlerStateConnection);
		notificationService.subscribe(INotification.EventType.MONITOR_REFRESH_05_SEC,
				eventHandlerStateConnection);

	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}