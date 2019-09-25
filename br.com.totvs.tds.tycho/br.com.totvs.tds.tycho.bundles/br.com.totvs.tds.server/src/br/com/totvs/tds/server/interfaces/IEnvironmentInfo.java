package br.com.totvs.tds.server.interfaces;

/**
 * Interface b�sica de ambientes.
 * 
 * @author acandido
 */
public interface IEnvironmentInfo extends IItemInfo {

	boolean isCredentialValidated();

	void setCredentialValidated(boolean credentialValidated);
}
