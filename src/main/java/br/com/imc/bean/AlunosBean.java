package br.com.imc.bean;

import br.com.imc.DAO.AlunoDAO;
import br.com.imc.domain.Aluno;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class AlunosBean implements Serializable {

    private List<Aluno> alunos;
    private AlunoDAO alunoDAO;

    /**
     * Método a ser chamado no inicio do carregamento da página
     */
    @PostConstruct
    public void init() {
        try {
            alunoDAO = new AlunoDAO();
            alunos = alunoDAO.listar();
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar listar os dados");
        }
    }

    public void novo() {
    }

    public void excluir(Aluno aluno) {
        try {
            alunoDAO.excluir(aluno);
            alunos = alunoDAO.listar();
            Messages.addGlobalInfo("Aluno excluído com sucesso");
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar excluir o aluno");
        }
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
}
