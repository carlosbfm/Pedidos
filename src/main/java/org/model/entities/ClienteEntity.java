package org.model.entities;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClienteEntity {

    private Long id;
    private String nomeCliente;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastroCliente;

    private static final DateTimeFormatter FMT_DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_TIMESTAMP_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ClienteEntity() {
    }

    public ClienteEntity(String nomeCliente, LocalDate dataNascimento) {
        this.nomeCliente = nomeCliente;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }
    
    //rever esse method setId
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

    public LocalDateTime getDataCadastroCliente() {
        return dataCadastroCliente;
    }

    public void setDataCadastroCliente(LocalDateTime dataCadastroCliente) {
        this.dataCadastroCliente = dataCadastroCliente;
    }

    public String getDataNascimentoFormatada() {
        return this.dataNascimento != null ? this.dataNascimento.format(FMT_DATA_BR) : "";
    }

    public String getDataCadastroFormatada() {
        return this.dataCadastroCliente != null ? this.dataCadastroCliente.format(FMT_TIMESTAMP_BR) : "";
    }

}