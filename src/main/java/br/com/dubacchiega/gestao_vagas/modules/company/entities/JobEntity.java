package br.com.dubacchiega.gestao_vagas.modules.company.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "job")
@Builder // permite que eu construa um JobEntity a partir de outro objeto. Precisa ter os construtores
@AllArgsConstructor
@NoArgsConstructor
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(example = "Vaga para designer")
    private String description;

    @Schema(example = "GYMPass, Plano de saúde")
    private String benefits;

    @NotBlank(message = "Esse campo é obrigatório")
    @Schema(example = "SÊNIOR")
    private String level;

    @ManyToOne()
    @JoinColumn(name = "company_id", insertable = false, updatable = false) // o JPA não vai usar esse campo para inserir ou atualizar o valor de company_id
    private CompanyEntity companyEntity;

    @Column(name = "company_id", nullable = false) // essa coluna que vai controlar o valor e lógica.
    private UUID companyId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
