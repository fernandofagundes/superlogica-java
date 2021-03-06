package br.com.jpb.superlogica.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import br.com.jpb.JsonUtil;
import br.com.jpb.jackson.databind.CustomLocalDateDeserializer;
import br.com.jpb.jackson.databind.CustomLocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Assinatura implements Serializable {

	@JsonProperty("planos[0][id_sacado_sac]")
	private int idCliente;
	@JsonProperty("planos[0][id_plano_pla]")
	private int idPlano;
	@JsonProperty("planos[0][dt_contrato_plc]")
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	private Date dataContratacao;

	protected Assinatura() {
	}

	private Assinatura(int idCliente, int idPlano, Date dataContratacao) {
		this.idCliente = idCliente;
		this.idPlano = idPlano;
		this.dataContratacao = dataContratacao;
	}

	public static Assinatura assinaturaHoje(int idCliente, int idPlano) {
		return new Assinatura(idCliente, idPlano, new Date());
	}

	public static Assinatura assinaturaComData(int idCliente, int idPlano,
			Date data) {
		return new Assinatura(idCliente, idPlano, data);
	}

	protected int getIdCliente() {
		return idCliente;
	}

	protected void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	protected int getIdPlano() {
		return idPlano;
	}

	protected void setIdPlano(int idPlano) {
		this.idPlano = idPlano;
	}

	protected Date getDataContratacao() {
		return dataContratacao;
	}

	protected void setDataContratacao(Date dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	@Override
	public String toString() {
		return JsonUtil.serialize(this);
	}
}