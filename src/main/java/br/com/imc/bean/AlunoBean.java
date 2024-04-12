package br.com.imc.bean;

import br.com.imc.DAO.AlunoDAO;
import br.com.imc.domain.Aluno;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class AlunoBean implements Serializable {

    @Param
    private Integer id;

    private Aluno aluno;
    private AlunoDAO alunoDAO;

    @PostConstruct
    public void init() {
        aluno = new Aluno();
        alunoDAO = new AlunoDAO();

        if (id != null) {
            aluno = alunoDAO.buscar(id);
        }
    }

    public void novo() {
    }

    public void salvar() {
        try {
            System.out.println(aluno.toString());
            AlunoDAO alunoDAO = new AlunoDAO();
            aluno = alunoDAO.merge(aluno);

            Messages.addGlobalInfo("Dados salvos com sucesso");
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar salvar os dados");
        }
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
