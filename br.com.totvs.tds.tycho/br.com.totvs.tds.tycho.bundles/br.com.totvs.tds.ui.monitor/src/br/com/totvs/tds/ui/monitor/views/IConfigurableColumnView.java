package br.com.totvs.tds.ui.monitor.views;

import java.util.List;

/**
 * Interface para vis�es com configura��o de colunas.
 *
 * @author eriky.kashivagui
 *
 */
public interface IConfigurableColumnView {

	/**
	 * Retorna as colunas vis�veis.
	 *
	 * @return Retorna as colunas vis�veis.
	 */
	List<IColumnInfo> getVisibleColumns();

	/**
	 * Retorna as colunas n�o vis�veis.
	 *
	 * @return Retorna as colunas n�o vis�veis.
	 */
	List<IColumnInfo> getNonVisibleColumns();

	/**
	 * Retorna todas as colunas.
	 *
	 * @return Retorna todas as colunas.
	 */
	List<IColumnInfo> getAllColumns();

	/**
	 * Atualiza as colunas.
	 */
	void refreshColumns();

}
