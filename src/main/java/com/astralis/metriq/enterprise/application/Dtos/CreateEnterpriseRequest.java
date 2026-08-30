package com.astralis.metriq.enterprise.application.Dtos;

public record CreateEnterpriseRequest(String razao_social, String cnpj, String email, String telefone, String status,
    String plano) {

}
