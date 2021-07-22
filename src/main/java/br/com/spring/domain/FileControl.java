package br.com.spring.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_CONTEST_CONTROLE")
public class FileControl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ID_CONTROLE_SEQUENCE")
    @SequenceGenerator(name = "ID_CONTROLE_SEQUENCE", sequenceName = "ID_CONTROLE_SEQUENCE", allocationSize = 1)
    @Column(name = "ID_CONTROLE")
    private Integer id;

    @Column(name = "DT_EXECUCAO")
    private Date executionTime;

    @Column(name = "TAMANHO_ARQUIVO", precision = 19)
    private Long fileSize;

    @Column(name = "NOM_ARQUIVO")
    private String name;

    @Column(name = "MSG_SAIDA")
    private String exitMessage;

    @Column(name = "PATH")
    private String path;

    @Column(name = "STATUS_STEP")
    private String statusStep;

    @Column(name = "STATUS_JOB")
    private String statusJob;

    @Column(name = "STEP_EXECUTION_ID")
    private Long stepExecutionId;

    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

}
