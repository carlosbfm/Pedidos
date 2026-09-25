package org.model.entities;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteEntity {
	private Long id;
	private String nomeCliente;
	private LocalDate dataNascimento;
	private LocalDateTime dataCadastroCliente;
	private static final AtomicLong GERADOR_ID = new AtomicLong(0);
	
	private static final DateTimeFormatter FMT_DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_TIMESTAMP_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public String getDataNascimentoFormatada() {
        if (this.dataNascimento == null) {
            return "";
        }
        return this.dataNascimento.format(FMT_DATA_BR);
    }

    public String getDataCadastroFormatada() {
        if (this.dataCadastroCliente == null) {
            return "";
        }
        return this.dataCadastroCliente.format(FMT_TIMESTAMP_BR);
    }

	public ClienteEntity() {

	}

	public ClienteEntity( String nome, LocalDate data){
		
		this.nomeCliente = nome;
		this.dataNascimento = data;
		this.id = GERADOR_ID.incrementAndGet();
		this.dataCadastroCliente = LocalDateTime.now();
	}

	public LocalDateTime getDataCadastroCliente() {
		return dataCadastroCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
