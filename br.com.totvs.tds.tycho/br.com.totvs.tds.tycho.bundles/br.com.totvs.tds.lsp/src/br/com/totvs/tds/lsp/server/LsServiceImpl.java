package br.com.totvs.tds.lsp.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import br.com.totvs.tds.lsp.server.model.node.ApplyPatchNode;
import br.com.totvs.tds.lsp.server.model.node.AuthenticationNode;
import br.com.totvs.tds.lsp.server.model.node.DisconnectReturnInfo;
import br.com.totvs.tds.lsp.server.model.node.IdNode;
import br.com.totvs.tds.lsp.server.model.node.InspectorFunctionsNode;
import br.com.totvs.tds.lsp.server.model.node.InspectorObjectNode;
import br.com.totvs.tds.lsp.server.model.node.KillUserNode;
import br.com.totvs.tds.lsp.server.model.node.NodeInfo;
import br.com.totvs.tds.lsp.server.model.node.PatchDirListNode;
import br.com.totvs.tds.lsp.server.model.node.PatchGenerateNode;
import br.com.totvs.tds.lsp.server.model.node.SendMessageToUserNode;
import br.com.totvs.tds.lsp.server.model.node.ServerPermissionsNode;
import br.com.totvs.tds.lsp.server.model.node.SlaveDataNode;
import br.com.totvs.tds.lsp.server.model.node.SlaveNode;
import br.com.totvs.tds.lsp.server.model.node.UsersInfoDataNode;
import br.com.totvs.tds.lsp.server.model.node.ValidKeyNode;
import br.com.totvs.tds.lsp.server.model.protocol.AppKillUserData;
import br.com.totvs.tds.lsp.server.model.protocol.AppKillUserInfo;
import br.com.totvs.tds.lsp.server.model.protocol.AuthenticationData;
import br.com.totvs.tds.lsp.server.model.protocol.AuthenticationInfo;
import br.com.totvs.tds.lsp.server.model.protocol.ClientImpl;
import br.com.totvs.tds.lsp.server.model.protocol.CompilationData;
import br.com.totvs.tds.lsp.server.model.protocol.CompilationInfo;
import br.com.totvs.tds.lsp.server.model.protocol.CompileOptions;
import br.com.totvs.tds.lsp.server.model.protocol.DefragRpoData;
import br.com.totvs.tds.lsp.server.model.protocol.DefragRpoInfo;
import br.com.totvs.tds.lsp.server.model.protocol.DisconnectData;
import br.com.totvs.tds.lsp.server.model.protocol.DisconnectInfo;
import br.com.totvs.tds.lsp.server.model.protocol.InspectorFunctionsData;
import br.com.totvs.tds.lsp.server.model.protocol.InspectorFunctionsInfo;
import br.com.totvs.tds.lsp.server.model.protocol.InspectorObjectsData;
import br.com.totvs.tds.lsp.server.model.protocol.InspectorObjectsInfo;
import br.com.totvs.tds.lsp.server.model.protocol.KeyInfo;
import br.com.totvs.tds.lsp.server.model.protocol.KillUserData;
import br.com.totvs.tds.lsp.server.model.protocol.KillUserInfo;
import br.com.totvs.tds.lsp.server.model.protocol.PatchApplyData;
import br.com.totvs.tds.lsp.server.model.protocol.PatchApplyInfo;
import br.com.totvs.tds.lsp.server.model.protocol.PatchGenerateData;
import br.com.totvs.tds.lsp.server.model.protocol.PatchGenerateInfo;
import br.com.totvs.tds.lsp.server.model.protocol.PathDirListData;
import br.com.totvs.tds.lsp.server.model.protocol.PathDirListInfo;
import br.com.totvs.tds.lsp.server.model.protocol.SendMessageToUserData;
import br.com.totvs.tds.lsp.server.model.protocol.SendMessageToUserInfo;
import br.com.totvs.tds.lsp.server.model.protocol.ServerPermissionsData;
import br.com.totvs.tds.lsp.server.model.protocol.ServerPermissionsInfo;
import br.com.totvs.tds.lsp.server.model.protocol.SlaveData;
import br.com.totvs.tds.lsp.server.model.protocol.SlaveInfo;
import br.com.totvs.tds.lsp.server.model.protocol.UsersInfo;
import br.com.totvs.tds.lsp.server.model.protocol.UsersInfoData;
import br.com.totvs.tds.lsp.server.model.protocol.ValidKeyData;
import br.com.totvs.tds.lsp.server.model.protocol.ValidationData;
import br.com.totvs.tds.lsp.server.model.protocol.ValidationInfo;

/**
 * Implementação do serviço Language Service.<br>
 *
 * @author acandido
 */
public final class LsServiceImpl implements ILanguageServerService {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static LsServiceImpl instance;

	/**
	 * Construtor.
	 */
	private LsServiceImpl() {

	}

	@Override
	public String authentication(final String id, final URI address, final String buildVersion,
			final String environment, final String user, final String password, final int serverType,
			final boolean secureConnection) {
		final AuthenticationInfo authenticationInfo = new AuthenticationInfo();

		authenticationInfo.setConnType(1);
		authenticationInfo.setIdentification(id);
		authenticationInfo.setServer(address.getHost());
		authenticationInfo.setPort(address.getPort());
		authenticationInfo.setBuildVersion(buildVersion);
		authenticationInfo.setEnvironment(environment);
		authenticationInfo.setUser(user);
		authenticationInfo.setPassword(password);
		authenticationInfo.setAutoReconnect(true);
		authenticationInfo.setServerType(serverType);
		authenticationInfo.setbSecure(secureConnection ? 1 : 0);

		AuthenticationNode result = null;
		try {
			final AuthenticationData authenticationData = new AuthenticationData(authenticationInfo);
			result = ClientImpl.getInstance().authentication(authenticationData);
		} catch (final Exception e) {
			result = null;
		}
		return result == null ? null : result.getConnectionToken();
	}

	@Override
	public boolean disconnect(final String name, final String token) {
		final DisconnectInfo disconnectInfo = new DisconnectInfo();

		disconnectInfo.setServerName(name);
		disconnectInfo.setConnectionToken(token);

		final DisconnectData disconnectData = new DisconnectData(disconnectInfo);
		final DisconnectReturnInfo result = ClientImpl.getInstance().disconnect(disconnectData);

		return result != null;
	}

	@Override
	public String validation(final URI address, final boolean secureConnection) {
		final ValidationInfo validationInfo = new ValidationInfo();
		validationInfo.setServer(address.getHost());
		validationInfo.setPort(address.getPort());
		validationInfo.setbSecure(secureConnection ? 1 : 0);

		final ValidationData validationData = new ValidationData(validationInfo);
		final NodeInfo result = ClientImpl.getInstance().validation(validationData);

		if (result != null) {
			return result.getBuildVersion();
		}

		return null;
	}

	@Override
	public void buidlFile(final String token, final String permimissionToken, final String environment,
			final List<String> files, final CompileOptions compileOptions, final List<String> includePaths) {
		final CompilationInfo compilationInfo = new CompilationInfo();

		compilationInfo.setConnectionToken(token);
		compilationInfo.setAuthorizationToken(permimissionToken);
		compilationInfo.setEnvironment(environment);
		compilationInfo.setIncludeUris(includePaths.toArray(new String[includePaths.size()]));
		compilationInfo.setFileUris(files.toArray(new String[files.size()]));
		compilationInfo.setCompileOptions(compileOptions);

		final CompilationData compilationData = new CompilationData(compilationInfo);
		ClientImpl.getInstance().compilation(compilationData);
	}

	@Override
	public SlaveDataNode[] getSlaveList(final String token) {
		final SlaveInfo slaveInfo = new SlaveInfo();

		slaveInfo.setConnectionToken(token);

		final SlaveData slaveData = new SlaveData(slaveInfo);
		final SlaveNode slaveNode = ClientImpl.getInstance().slave(slaveData);

		return slaveNode.getSlaves();
	}

	@Override
	public List<String> serverPermissions(final String token) {
		final ServerPermissionsInfo serverPermissionsInfo = new ServerPermissionsInfo();

		serverPermissionsInfo.setConnectionToken(token);

		final ServerPermissionsData serverPermissionsData = new ServerPermissionsData(serverPermissionsInfo);
		final ServerPermissionsNode serverPermissionNode = ClientImpl.getInstance()
				.serverPermissions(serverPermissionsData);

		final String[] permission = serverPermissionNode.getServerPermissions().getOperation();

		return Arrays.asList(permission);
	}

	@Override
	public boolean isReady() {

		return ClientImpl.getInstance() != null;
	}

	@Override
	public List<String> getProgramMap(final String token, final String environment, final boolean includeTres) {
		List<String> result = Collections.emptyList();

		final InspectorObjectsInfo inspectorObjectsInfo = new InspectorObjectsInfo();
		inspectorObjectsInfo.setConnectionToken(token);
		inspectorObjectsInfo.setEnvironment(environment);
		inspectorObjectsInfo.setIncludeTres(includeTres);

		final InspectorObjectsData inspectorObjectsData = new InspectorObjectsData(inspectorObjectsInfo);
		final InspectorObjectNode inspectorObjectNode = ClientImpl.getInstance().inspectorObjects(inspectorObjectsData);

		if (inspectorObjectNode.getMessage().equals("Success")) { //$NON-NLS-1$
			result = inspectorObjectNode.getObjects();
		}

		return result;
	}

	@Override
	public int patchGenerate(final String token, final String authorizationToken, final String environment,
			final boolean isLocal, final String name, final String patchDest, final String[] patchFiles,
			final String patchMaster, final int patchType) {
		final PatchGenerateInfo patchGenerateInfo = new PatchGenerateInfo();
		patchGenerateInfo.setConnectionToken(token);
		patchGenerateInfo.setAuthorizationToken(authorizationToken);
		patchGenerateInfo.setEnvironment(environment);
		patchGenerateInfo.setLocal(isLocal);
		patchGenerateInfo.setName(name);
		patchGenerateInfo.setPatchDest(patchDest);
		patchGenerateInfo.setPatchFiles(patchFiles);
		patchGenerateInfo.setPatchMaster(patchMaster);
		patchGenerateInfo.setPatchType(patchType);

		final PatchGenerateData patchGenerateData = new PatchGenerateData(patchGenerateInfo);
		final PatchGenerateNode patchGenerateNode = ClientImpl.getInstance().patchGenerate(patchGenerateData);

		if (patchGenerateNode != null) {
			return patchGenerateNode.getReturnCode();
		}

		return -1;
	}

	@Override
	public String[] getPathDirList(final String token, final String environment, final String folder,
			final boolean includeDir) {
		final PathDirListInfo pathDirListInfo = new PathDirListInfo();

		pathDirListInfo.setConnectionToken(token);
		pathDirListInfo.setEnvironment(environment);
		pathDirListInfo.setFolder(folder);
		pathDirListInfo.setIncludeDir(includeDir);

		final PathDirListData pathDirListData = new PathDirListData(pathDirListInfo);
		final PatchDirListNode patchDirListNode = ClientImpl.getInstance().getPathDirList(pathDirListData);

		if (patchDirListNode != null) {
			return patchDirListNode.getDirectory();
		}

		return EMPTY_STRING_ARRAY;
	}

	public static Object getInstance() {
		if (instance == null) {
			instance = new LsServiceImpl();
		}

		return instance;
	}

	@Override
	public Properties validKey(final InputStream inputStream) throws IOException {
		final Properties props = new Properties();
		props.load(inputStream);
		inputStream.close();

		final KeyInfo keyInfo = new KeyInfo();
		keyInfo.setId(props.getOrDefault("ID", Messages.LsServiceImpl_EMPTY_STRING).toString()); //$NON-NLS-1$
		keyInfo.setIssued(props.getOrDefault("GENERATION", Messages.LsServiceImpl_EMPTY_STRING).toString()); //$NON-NLS-1$
		keyInfo.setExpiry(props.getOrDefault("VALIDATION", Messages.LsServiceImpl_EMPTY_STRING).toString()); //$NON-NLS-1$
		keyInfo.setToken(props.getOrDefault("KEY", Messages.LsServiceImpl_EMPTY_STRING).toString()); //$NON-NLS-1$
		keyInfo.setCanOverride(props.getOrDefault("PERMISSION", "0").toString()); //$NON-NLS-1$ //$NON-NLS-2$

		final ValidKeyData validKey = new ValidKeyData(keyInfo);
		final ValidKeyNode validKeyNode = ClientImpl.getInstance().validKey(validKey);
		final int buildType = validKeyNode.getBuildType();

		props.clear();
		if ((buildType == 0) || (buildType == 1) || (buildType == 2)) {
			props.put("ID", validKeyNode.getMachineId()); //$NON-NLS-1$
			props.put("GENERATION", validKeyNode.getIssued()); //$NON-NLS-1$
			props.put("VALIDATION", validKeyNode.getExpiry()); //$NON-NLS-1$
			props.put("KEY", validKeyNode.getAuthorizationToken()); //$NON-NLS-1$
			props.put("PERMISSION", keyInfo.getCanOverride()); //$NON-NLS-1$
			props.put("USER_ID", validKeyNode.getUserId()); //$NON-NLS-1$
		}

		return props;
	}

	@Override
	public String getMachineId() {
		String machineId = null;

		if (ClientImpl.getInstance() != null) {
			final IdNode idNode = ClientImpl.getInstance().getId();

			if (idNode != null) {
				machineId = idNode.getId();
			}
		}

		return machineId;
	}

	@Override
	public IStatus validPatch(final String token, final String authorizationCode, final String environment,
			final URI patchFile, final boolean local) {
		final String[] patchArray = new String[1];
		patchArray[0] = convertURI(patchFile);

		final PatchApplyInfo patchApplyInfo = new PatchApplyInfo();
		patchApplyInfo.setConnectionToken(token);
		patchApplyInfo.setAuthenticateToken(authorizationCode);
		patchApplyInfo.setEnvironment(environment);
		patchApplyInfo.setPatchUris(patchArray);
		patchApplyInfo.setLocal(local);
		patchApplyInfo.setValidatePatch(true);
		patchApplyInfo.setApplyOldProgram(false);

		final PatchApplyData patchApplyData = new PatchApplyData(patchApplyInfo);
		final ApplyPatchNode applyPatchNode = ClientImpl.getInstance().patchApply(patchApplyData);

		IStatus status = Status.OK_STATUS;
		if (applyPatchNode == null) {
			status = new Status(IStatus.ERROR, LspActivator.PLUG_IN, "Erro desconhecido. Verifique o log de console.");
		} else if (applyPatchNode.getReturnCode() != 0) {
			status = new Status(IStatus.ERROR, LspActivator.PLUG_IN, String.format("Código: %d\n\tArquivos: %s",
					applyPatchNode.getReturnCode(), applyPatchNode.getFiles()));
		}

		return status;
	}

	private String convertURI(final URI uri) {
		String convertedUri = uri.toString();

		convertedUri = convertedUri.replace("file:///", ""); //$NON-NLS-1$
		convertedUri = convertedUri.replace("file://", ""); //$NON-NLS-1$
		convertedUri = convertedUri.replace("file:/", ""); //$NON-NLS-1$

		return convertedUri;
	}

	@Override
	public IStatus applyPatch(final String token, final String authorizationCode, final String environment,
			final URI patchFile, final boolean local, final boolean oldPrograms) {
		final String[] patchArray = new String[1];
		patchArray[0] = convertURI(patchFile);

		final PatchApplyInfo patchApplyInfo = new PatchApplyInfo();

		patchApplyInfo.setConnectionToken(token);
		patchApplyInfo.setAuthenticateToken(authorizationCode);
		patchApplyInfo.setEnvironment(environment);
		patchApplyInfo.setPatchUris(patchArray);
		patchApplyInfo.setLocal(local);
		patchApplyInfo.setValidatePatch(false);
		patchApplyInfo.setApplyOldProgram(oldPrograms);

		final PatchApplyData patchApplyData = new PatchApplyData(patchApplyInfo);
		final ApplyPatchNode applyPatchNode = ClientImpl.getInstance().patchApply(patchApplyData);

		IStatus status = Status.OK_STATUS;
		if (applyPatchNode == null) {
			status = new Status(IStatus.ERROR, LspActivator.PLUG_IN, "Erro desconhecido. Verifique o log de console.");
		} else if (applyPatchNode.getReturnCode() != 0) {
			status = new Status(IStatus.ERROR, LspActivator.PLUG_IN, String.format("Código: %d\n\tArquivos: %s",
					applyPatchNode.getReturnCode(), applyPatchNode.getFiles()));
		}

		return status;
	}

	@Override
	public InspectorFunctionsNode inspectorFunctions(final String token, final String environment) {
		final InspectorFunctionsInfo inspectorFunctionsInfo = new InspectorFunctionsInfo();

		inspectorFunctionsInfo.setConnectionToken(token);
		inspectorFunctionsInfo.setEnvironment(environment);

		final InspectorFunctionsData inspectorFunctionsData = new InspectorFunctionsData(inspectorFunctionsInfo);
		final InspectorFunctionsNode inspectorFunctionsNode = ClientImpl.getInstance()
				.inspectorFunctions(inspectorFunctionsData);

		return inspectorFunctionsNode;
	}

	@Override
	public void defragRPO(final String token, final String environment) {
		final DefragRpoInfo defragRPOInfo = new DefragRpoInfo();

		defragRPOInfo.setConnectionToken(token);
		defragRPOInfo.setEnvironment(environment);

		final DefragRpoData defragRPOData = new DefragRpoData(defragRPOInfo);
		ClientImpl.getInstance().defragRpo(defragRPOData);
	}

	@Override
	public UsersInfoDataNode[] getUsersInfo(final String token) {
		final UsersInfo usersInfo = new UsersInfo();
		usersInfo.setConnectionToken(token);

		final UsersInfoData usersInfoData = new UsersInfoData(usersInfo);
		final UsersInfoDataNode[] usersInfoNodes = ClientImpl.getInstance().usersInfo(usersInfoData);

		return usersInfoNodes;
	}

	@Override
	public SendMessageToUserNode sendMessageUser(final String token, final String userName, final String computerName,
			final long threadId, final String serverName, final String messageText) {
		final SendMessageToUserInfo sendMessageToUserInfo = new SendMessageToUserInfo();

		sendMessageToUserInfo.setConnectionToken(token);
		sendMessageToUserInfo.setComputerName(computerName);
		sendMessageToUserInfo.setServerName(serverName);
		sendMessageToUserInfo.setUserName(userName);
		sendMessageToUserInfo.setThreadId(threadId);
		sendMessageToUserInfo.setMessage(messageText);

		final SendMessageToUserData sendMessageToUser = new SendMessageToUserData(sendMessageToUserInfo);

		return ClientImpl.getInstance().sendMessageToUser(sendMessageToUser);
	}

	@Override
	public KillUserNode killUser(final boolean immediately, final String token, final String userName,
			final String computerName, final long threadId, final String serverName) {

		if (immediately) {
			final KillUserInfo killUserInfo = new KillUserInfo();

			killUserInfo.setConnectionToken(token);
			killUserInfo.setComputerName(computerName);
			killUserInfo.setServerName(serverName);
			killUserInfo.setUserName(userName);
			killUserInfo.setThreadId(threadId);

			final KillUserData killUserData = new KillUserData(killUserInfo);

			return ClientImpl.getInstance().killUser(killUserData);
		}

		final AppKillUserInfo killUserInfo = new AppKillUserInfo();

		killUserInfo.setConnectionToken(token);
		killUserInfo.setComputerName(computerName);
		killUserInfo.setServerName(serverName);
		killUserInfo.setUserName(userName);
		killUserInfo.setThreadId(threadId);

		final AppKillUserData killUserData = new AppKillUserData(killUserInfo);

		return ClientImpl.getInstance().appKillUser(killUserData);

	}

}
