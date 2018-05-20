package br.com.edilsystem.tableviwer.model;

import java.text.NumberFormat;

import javafx.scene.control.CheckBox;

public class ProdutoStatus {

	private Long id;
	private CheckBox checked;
	private String codigo;
	private String descricao;
	private String grade;
	private Integer quantidadeEnviada;
	private Double precoEnviado;
	private Double promocaoEnviado;
	private Integer quantidadeRecebida;
	private Double precoRecebido;
	private Double promocaoRecebido;

	public ProdutoStatus() {
	}

	public ProdutoStatus(long id, String codigo, String descricao, String grade, Integer quantidadeEnviada, Double precoEnviado, Double promocaoEnviado,
			Integer quantidadeRecebida, Double precoRecebido, Double promocaoRecebido, CheckBox checkBox) {
		this.id = id;
		this.codigo = codigo;
		this.descricao = descricao;
		this.grade = grade;
		this.quantidadeEnviada = quantidadeEnviada;
		this.precoEnviado = precoEnviado;
		this.promocaoEnviado = promocaoEnviado;
		this.quantidadeRecebida = quantidadeRecebida;
		this.precoRecebido = precoRecebido;
		this.promocaoRecebido = promocaoRecebido;
		this.setChecked(checkBox);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the quantidadeEnviada
	 */
	public Integer getQuantidadeEnviada() {
		return quantidadeEnviada;
	}

	/**
	 * @param quantidadeEnviada
	 *            the quantidadeEnviada to set
	 */
	public void setQuantidadeEnviada(Integer quantidadeEnviada) {
		this.quantidadeEnviada = quantidadeEnviada;
	}

	/**
	 * @return the precoEnviado
	 */
	public Double getPrecoEnviado() {
		return precoEnviado;
	}

	/**
	 * @param precoEnviado
	 *            the precoEnviado to set
	 */
	public void setPrecoEnviado(Double precoEnviado) {
		this.precoEnviado = precoEnviado;
	}

	/**
	 * @return the promocaoEnviado
	 */
	public Double getPromocaoEnviado() {
		return promocaoEnviado;
	}

	/**
	 * @param promocaoEnviado
	 *            the promocaoEnviado to set
	 */
	public void setPromocaoEnviado(Double promocaoEnviado) {
		this.promocaoEnviado = promocaoEnviado;
	}

	/**
	 * @return the quantidadeRecebida
	 */
	public Integer getQuantidadeRecebida() {
		return quantidadeRecebida;
	}

	/**
	 * @param quantidadeRecebida
	 *            the quantidadeRecebida to set
	 */
	public void setQuantidadeRecebida(Integer quantidadeRecebida) {
		this.quantidadeRecebida = quantidadeRecebida;
	}

	/**
	 * @return the precoRecebido
	 */
	public Double getPrecoRecebido() {
		return precoRecebido;
	}

	/**
	 * @param precoRecebido
	 *            the precoRecebido to set
	 */
	public void setPrecoRecebido(Double precoRecebido) {
		this.precoRecebido = precoRecebido;
	}

	/**
	 * @return the promocaoRecebido
	 */
	public Double getPromocaoRecebido() {
		return promocaoRecebido;
	}

	/**
	 * @param promocaoRecebido
	 *            the promocaoRecebido to set
	 */
	public void setPromocaoRecebido(Double promocaoRecebido) {
		this.promocaoRecebido = promocaoRecebido;
	}
	
	public String getQuantidade() {
		return getQuantidadeEnviada() + "/" + getQuantidadeRecebida();
	}
	
	public String getPreco() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String precoEnviadoString = formatter.format(getPrecoEnviado());
		String precoRecebidoString = formatter.format(getPrecoRecebido());
		return precoEnviadoString + "/" + precoRecebidoString;
	}
	
	public String getPromocao() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String promocaoEnviadoString = formatter.format(getPromocaoEnviado());
		String promocaoRecebidoString = formatter.format(getPromocaoRecebido());
		return promocaoEnviadoString + "/" + promocaoRecebidoString;
	}
	
	public boolean getValid() {
		
		if (!getPrecoEnviado().equals(getPrecoRecebido())) {
			return false;
		}
		
		if (!getPromocaoEnviado().equals(getPromocaoRecebido())) {
			return false;
		}
		
		if (!getQuantidadeEnviada().equals(getQuantidadeRecebida())) {
			return false;
		}
		
		return true;
	}

	public CheckBox getChecked() {
		return checked;
	}

	public void setChecked(CheckBox checked) {
		this.checked = checked;
	}
}
