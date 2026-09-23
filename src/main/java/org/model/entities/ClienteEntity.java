package org.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClienteEntity {
	private Integer id;
	private String nomeCliente;
	private LocalDate dataNascimento;
	private LocalDateTime dataCadastroCliente;
	
	public ClienteEntity() {
		
	}
	
	public ClienteEntity(Integer id , String nome, LocalDate data, LocalDateTime dataCadastro){
		this.id = id;
		this.nomeCliente = nome;
		this.dataNascimento = data;
		this.dataCadastroCliente = dataCadastro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	
	
}
