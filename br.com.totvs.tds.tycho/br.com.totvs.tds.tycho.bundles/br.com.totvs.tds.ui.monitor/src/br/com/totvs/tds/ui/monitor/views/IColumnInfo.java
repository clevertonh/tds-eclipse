package br.com.totvs.tds.ui.monitor.views;

/**
 * Interface de colunas.
 *
 * @author eriky.kashivagui
 *
 */
public interface IColumnInfo {

	/**
	 * Tamanho padr�o das colunas.
	 */
	int DEFAULT_COLUMN_SIZE = 100;

	/**
	 * Chave de propriedade que indica se a coluna pode ser configurada.
	 */
	String CAN_BE_CONFIGURED = new String("CanBeConfigured"); //$NON-NLS-1$

	/**
	 * Chave de propriedade que indica o tamanho padr�o da coluna.
	 */
	String DEFAULT_WIDTH = new String("DefaultWidth"); //$NON-NLS-1$

	/**
	 * Chave de propriedade que indica o �ndice da coluna.
	 */
	String INDEX = new String("Index"); //$NON-NLS-1$

	/**
	 * Chave de propriedade que indica a posi��o da coluna.
	 */
	String POSITION = new String("Position"); //$NON-NLS-1$

	/**
	 * Chave de propriedade que indica se a coluna � vis�vel.
	 */
	String VISIBLE = new String("Visible"); //$NON-NLS-1$

	/**
	 * Chave de propriedade que indica o tamanho da coluna.
	 */
	String WIDTH = new String("Width"); //$NON-NLS-1$

	/**
	 * Retorna o texto da coluna.
	 *
	 * @return Retorna o texto da coluna.
	 */
	String getText();

	/**
	 * Retorna se a coluna pode ser movida.
	 *
	 * @return Retorna se a coluna pode ser movida.
	 */
	boolean isColumnMovable();

	/**
	 * Retorna se a coluna pode ser redimensionada.
	 *
	 * @return Retorna se a coluna pode ser redimensionada.
	 */
	boolean isColumnResizable();

	/**
	 * Retorna o tamanho da coluna.
	 *
	 * @return Retorna o tamanho da coluna.
	 */
	int getColumnWidth();

	/**
	 * Insere o �ndice da coluna.
	 *
	 * @param index - �ndice da coluna
	 */
	void setColumnIndex(final int index);

	/**
	 * Insere o tamanho da coluna.
	 *
	 * @param width - Tamanho da coluna.
	 */
	void setColumnWidth(final int width);

	/**
	 * Retorna o objeto interno da coluna.
	 *
	 * @return Retorna o objeto interno da coluna.
	 */
	Object innerObject();

	/**
	 * Retorna o �ndice da coluna.
	 *
	 * @return Retorna o �ndice da coluna.
	 */
	int getColumnIndex();

	/**
	 * Insere a visibilidade da coluna.
	 *
	 * @param visibility - Informa se � vis�vel.
	 */
	void setColumnVisible(boolean visibility);

	/**
	 * Retorna se a coluna � vis�vel.
	 *
	 * @return Retorna se a coluna � vis�vel.
	 */
	boolean isColumnVisible();

	/**
	 * Insere se a coluna pode ser configurada.
	 *
	 * @param canBeConfigured - Informa se a coluna pode ser configurada.
	 */
	void setColumnCanBeConfigured(boolean canBeConfigured);

	/**
	 * Informa se a coluna pode ser configurada.
	 *
	 * @return Informa se a coluna pode ser configurada.
	 */
	boolean canColumnBeConfigured();

	/**
	 * Retorna o tamanho padr�o da coluna.
	 *
	 * @return Retorna o tamanho padr�o da coluna.
	 */
	Integer getColumnWidthDefault();

}
