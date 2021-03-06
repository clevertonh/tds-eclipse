package br.com.totvs.tds.ui.server.wizards.server;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import br.com.totvs.tds.lsp.server.ILanguageServerService;
import br.com.totvs.tds.server.ServerActivator;
import br.com.totvs.tds.server.interfaces.IAppServerInfo;
import br.com.totvs.tds.server.interfaces.IServerManager;
import br.com.totvs.tds.ui.server.ServerUIActivator;
import br.com.totvs.tds.ui.server.ServerUIIcons;
import br.com.totvs.tds.ui.server.internal.ServerUtils;
import br.com.totvs.tds.ui.server.nl.Messages;
import br.com.totvs.tds.ui.server.vo.NewServerVO;

/**
 * Assistente para novo servidor Protheus ou Logix.
 *
 * @author acandido
 */
public class NewServerWizardPage extends WizardPage {

	private Button btnDoValidate;

	private NewServerVO newServer;
	private Text txtAddress;
	private Text txtParent;
	private Text txtPort;
	private Text txtServerName;
	private Text txtVersion;
	private Text txtSmartClientPath;
	private Text txtAppServerPath;

	private Button btnConnect;

	private Button btnSelectSmartClientPath;

	private Button btnAppServerPathPath;
	private Button btnLocalServer;
	private Button btnSecureConnection;

	private boolean dialogChanging = false;

	/**
	 * Construtor.
	 *
	 * @param subTitle  Titulo do assistente.
	 * @param newServer VO com informações utilizadas pelo assistente.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter newServer new NewServerVO()
	 *
	 */
	public NewServerWizardPage(final NewServerVO newServer) {
		super("newServerWizardPage", //$NON-NLS-1$
				String.format(Messages.NewServerWizardPage_new_server_title_page,
						newServer.getServer().getServerType().getTitle().toUpperCase()),
				null); // ServerUIIcons.getWizardServer());
		setDescription(Messages.NewServerWizardPage_wizard_descrption);
		this.newServer = newServer;
	}

	/**
	 * Create contents of the wizard.
	 *
	 * @param parent
	 * @param btnDoValidate
	 */
	@Override
	public void createControl(final Composite parent) {
		Composite topLevel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.verticalSpacing = 9;
		topLevel.setLayout(layout);
		setControl(topLevel);

		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				dialogChanged();
			}
		};

		SelectionListener selectionListener = new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				dialogChanged();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		};

		ModifyListener modifySmartClientListener = new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (txtAddress.getText().isEmpty()) {
					String text = ((Text) e.widget).getText();
					String[] result = ServerUtils.doProcessSmartClientIni(text);
					txtAddress.setText(result[0]);
					txtPort.setText(result[1]);
					btnSecureConnection.setSelection(result[2].equals("1"));
				}

				doValidateProcess();

				dialogChanged();
			}
		};

		ModifyListener modifyAddressListener = new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				txtVersion.setText(Messages.EMPTY_STRING); // $NON-NLS-1$
				dialogChanged();
			}
		};

		Label lblNewLabel = new Label(topLevel, SWT.NONE);
		lblNewLabel.setText(Messages.NewServerWizardPage_target);
		txtParent = new Text(topLevel, SWT.BORDER);
		txtParent.setEnabled(false);
		txtParent.setEditable(false);
		txtParent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(topLevel, SWT.NONE);
		new Label(topLevel, SWT.NONE);

		btnLocalServer = new Button(topLevel, SWT.CHECK);
		btnLocalServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});
		btnLocalServer.setText(Messages.NewServerWizardPage_btnCheckButton_text);
		new Label(topLevel, SWT.NONE);

		Label lblAppServer = new Label(topLevel, SWT.NONE);
		lblAppServer.setText(Messages.NewServerWizardPage_app_server);
		txtAppServerPath = new Text(topLevel, SWT.BORDER);
		txtAppServerPath.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtAppServerPath.addModifyListener(modifyListener);
		txtAppServerPath.setEnabled(false);

		btnAppServerPathPath = new Button(topLevel, SWT.NONE);
		btnAppServerPathPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAppServerPathPath.setText("..."); //$NON-NLS-1$
		btnAppServerPathPath.setEnabled(false);
		btnAppServerPathPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String result = ServerUtils.doSelectAppServer(getShell());

				if (!result.isEmpty()) {
					txtAppServerPath.setText(result);
				}
			}
		});

		Label lblSmartclient = new Label(topLevel, SWT.NONE);
		lblSmartclient.setText(Messages.NewServerWizardPage_smart_client);

		this.txtSmartClientPath = new Text(topLevel, SWT.BORDER);
		this.txtSmartClientPath.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtSmartClientPath.addModifyListener(modifySmartClientListener);

		btnSelectSmartClientPath = new Button(topLevel, SWT.NONE);
		btnSelectSmartClientPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelectSmartClientPath.setText("..."); //$NON-NLS-1$
		btnSelectSmartClientPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String result = ServerUtils.doSelectSmartClient(getShell());

				if (!result.isEmpty()) {
					txtSmartClientPath.setText(result);
				}
			}
		});

		Label l1 = new Label(topLevel, SWT.NULL);
		l1.setText(Messages.NewServerWizardPage_name);
		txtServerName = new Text(topLevel, SWT.BORDER);
		{
			GridData gd_txtServerName = new GridData(GridData.FILL_HORIZONTAL);
			gd_txtServerName.verticalAlignment = SWT.TOP;
			txtServerName.setLayoutData(gd_txtServerName);
		}
		txtServerName.setText(newServer.getServer().getName());
		if (newServer.getParent() != null) {
			txtParent.setText(newServer.getParent().getName());
		}
		new Label(topLevel, SWT.NONE);
		new Label(topLevel, SWT.NONE);

		btnSecureConnection = new Button(topLevel, SWT.CHECK);
		btnSecureConnection.setText("Conexão segura (SSL)");
		btnSecureConnection.addSelectionListener(selectionListener);
		new Label(topLevel, SWT.NONE);

		Label label_1 = new Label(topLevel, SWT.NONE);
		label_1.setText(Messages.NewServerWizardPage_address);

		Composite composite = new Composite(topLevel, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		this.txtAddress = new Text(composite, SWT.BORDER);
		this.txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.txtAddress.setToolTipText(Messages.NewServerWizardPage_address_warning);

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewServerWizardPage_port);

		this.txtPort = new Text(composite, SWT.BORDER);
		txtPort.addModifyListener(modifyAddressListener);
		txtAddress.addModifyListener(modifyAddressListener);

		btnDoValidate = new Button(topLevel, SWT.NONE);
		btnDoValidate.setText(Messages.NewServerWizardPage_validate);
		btnDoValidate.setEnabled(false);

		btnDoValidate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				dialogChanged();
				doValidateProcess();
			}
		});
		Label lblVersao = new Label(topLevel, SWT.NONE);
		lblVersao.setText(Messages.NewServerWizardPage_version);
		txtVersion = new Text(topLevel, SWT.BORDER | SWT.READ_ONLY);
		this.txtVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		this.txtVersion.setEnabled(false);
		new Label(topLevel, SWT.NONE);

		new Label(topLevel, SWT.NONE);

		btnConnect = new Button(topLevel, SWT.CHECK);
		btnConnect.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		btnConnect.setText(Messages.NewServerWizardPage_immediate_connection);
		btnConnect.setSelection(true);
		new Label(topLevel, SWT.NONE);

		txtServerName.addModifyListener(modifyListener);
		btnConnect.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				dialogChanged();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		setPageComplete(false);
	}

	/**
	 * �a* Processa a mudan�a no diálogo.
	 */
	private void dialogChanged() {
		if (dialogChanging) {
			return;
		}
		dialogChanging = true;

		boolean isProcessValid = false;
		boolean localServer = btnLocalServer.getSelection();
		btnDoValidate.setEnabled(false);

		updateStatus(null);

		//
		newServer.setImmediateConnection(btnConnect.getSelection());
		newServer.getServer().setSecureConnection(btnSecureConnection.getSelection());
		//
		String serverName = txtServerName.getText();
		if (Messages.EMPTY_STRING.equals(serverName)) { // $NON-NLS-1$
			updateStatus(Messages.NewServerWizardPage_server_name_required);
			dialogChanging = false;
			return;
		}

		txtAppServerPath.setEnabled(localServer);
		btnAppServerPathPath.setEnabled(localServer);
		newServer.getServer().setLocalServer(localServer);

		if (localServer) {
			String appServerPath = txtAppServerPath.getText().trim();
			if (Messages.EMPTY_STRING.equals(appServerPath)) { // $NON-NLS-1$
				updateStatus(Messages.NewServerWizardPage_Local_server_executable_required);
				dialogChanging = false;
				return;
			}

			File file = new File(appServerPath);
			if (!file.exists()) {
				updateStatus(Messages.NewServerWizardPage_Local_server_executable_not_found);
				dialogChanging = false;
				return;
			}
			if (!file.canExecute()) {
				updateStatus(Messages.NewServerWizardPage_File_selected_cannot_executed);
				dialogChanging = false;
				return;
			}
			newServer.getServer().setAppServerPath(appServerPath);
		} else {
			newServer.getServer().setAppServerPath(Messages.EMPTY_STRING);
		}

		IServerManager serverManager = ServerActivator.getDefault().getServerManager();
		IAppServerInfo server = serverManager.getServer(serverName);
		if (server != null) {
			updateStatus(Messages.NewServerWizardPage_server_name_invalid);
			dialogChanging = false;
			return;
		}

		newServer.getServer().setName(txtServerName.getText());
		try {
			String serverAddress = txtAddress.getText().trim();
			String serverPort = txtPort.getText().trim();
			if (Messages.EMPTY_STRING.equals(serverAddress)) { // $NON-NLS-1$
				updateStatus(Messages.NewServerWizardPage_address_required);
				dialogChanging = false;
				return;
			} else if (Messages.EMPTY_STRING.equals(serverPort)) { // $NON-NLS-1$
				updateStatus(Messages.NewServerWizardPage_port_required);
				dialogChanging = false;
				return;
			} else {
				isProcessValid = true;
				newServer.getServer().setAddress(URI.create("//" + serverAddress + ":" + serverPort)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (IllegalArgumentException e) {
			updateStatus(e.getMessage());
			dialogChanging = false;
			return;
		}

		btnDoValidate.setEnabled(true);
		//
		if (txtVersion.getText().isEmpty()) {
			isProcessValid = false;
			updateStatus(Messages.NewServerWizardPage_click_validate);
			dialogChanging = false;
			return;
		}

		if (txtSmartClientPath.getText().isEmpty()) {
			updateStatus(Messages.NewServerWizardPage_smart_client_required);
			dialogChanging = false;
			return;
		}

		File file = new File(txtSmartClientPath.getText());
		if (!file.exists()) {
			updateStatus(Messages.NewServerWizardPage_smart_client_not_found);
			dialogChanging = false;
			return;
		}
		if (!file.canExecute()) {
			updateStatus(Messages.NewServerWizardPage_smart_client_unnkonw);
			dialogChanging = false;
			return;
		}

		newServer.getServer().setSmartClientPath(txtSmartClientPath.getText());
		newServer.getServer().setSecureConnection(btnSecureConnection.getSelection());

		try {
			isProcessValid = newServer.getServer().isValid();
		} catch (Exception e) {
			isProcessValid = false;
			updateStatus(e.getMessage());
			dialogChanging = false;
			return;
		}

		if (!isProcessValid) {
			updateStatus(Messages.NewServerWizardPage_21);
			dialogChanging = false;
			return;
		}

	}

	/**
	 * Valida os dados da conex�o.
	 */
	private void doValidateProcess() {

		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			@Override
			public void run() {
				IAppServerInfo server = newServer.getServer();
				IServiceLocator serviceLocator = PlatformUI.getWorkbench();
				ILanguageServerService lsService = serviceLocator.getService(ILanguageServerService.class);

				if (server.isLocalServer()) {
					server.start();
				}

				String version = lsService.validation(server.getAddress(), server.isSecureConnection());

				if (version == null) {
					String msg = String.format(Messages.NewServerWizardPage_connection_error,
							newServer.getServer().getAddress());
					ServerUIActivator.logStatus(IStatus.ERROR, msg);
					updateStatus(msg);
				} else {
					newServer.getServer().setVersion(version);
					txtVersion.setText(version);
					dialogChanged();
				}

				if (server.isLocalServer()) {
					server.stop();
				}
			}
		});

	}

	/**
	 * Atualiza o status do diálogo.
	 *
	 * @param message mensagem a ser apresentada ou null, se estiver tudo ok.
	 */
	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}
