package br.com.imc.bean;

import br.com.imc.DAO.AlunoDAO;
import br.com.imc.DAO.TurmaDAO;
import br.com.imc.domain.Aluno;
import br.com.imc.domain.Turma;
import java.io.Serializable;
import java.util.List;
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
    private TurmaDAO turmaDAO;
    private List<Turma> turmas;

    @PostConstruct
    public void init() {
        aluno = new Aluno();
        alunoDAO = new AlunoDAO();
        turmaDAO = new TurmaDAO();

        if (id != null) {
            aluno = alunoDAO.buscar(id);
        }
        //vamos preencher a variável turmas com a lista das turmas
        turmas = turmaDAO.listar();
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

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }
}
