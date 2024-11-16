package com.pedroo722.todolist.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tarefas", uniqueConstraints = @UniqueConstraint(columnNames = "ordemApresentacao"))
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador_tarefa")
    private Long id;

    @Column(name = "nome_tarefa", nullable = false)
    private String nomeTarefa;

    @Column(name = "custo", nullable = false)
    private Double custo;

    @Column(name = "data_limite")
    private Date dataLimite;

    @Column(name = "ordem_apresentacao", nullable = false, unique = true)
    private Integer ordemApresentacao;

    public Long getId() { return id; }
    public String getNomeTarefa() { return nomeTarefa; }
    public Double getCusto() { return custo; }
    public Date getDataLimite() { return dataLimite; }
    public Integer getOrdemApresentacao() { return ordemApresentacao; }

    public void setId(Long id) { this.id = id; }
    public void setNomeTarefa(String nomeTarefa) { this.nomeTarefa = nomeTarefa; }
    public void setCusto(Double custo) { this.custo = custo; }
    public void setDataLimite(Date dataLimite) { this.dataLimite = dataLimite; }
    public void setOrdemApresentacao(Integer ordemApresentacao) { this.ordemApresentacao = ordemApresentacao; }
}