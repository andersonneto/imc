package br.com.imc.bean;

import br.com.imc.DAO.TurmaDAO;
import br.com.imc.domain.Turma;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class TurmasBean implements Serializable {

    private List<Turma> turmas;
    private TurmaDAO turmaDAO;
    

    /**
     * Método a ser chamado no inicio do carregamento da página
     */
    @PostConstruct
    public void init() {
        try {
            turmaDAO = new TurmaDAO();
            turmas = turmaDAO.listar();
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar listar os dados");
        }
    }

    public void novo() {
    }

    public void excluir(Turma turma) {
        try {
            turmaDAO.excluir(turma);
            turmas = turmaDAO.listar();
            Messages.addGlobalInfo("Turma excluída com sucesso");
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar excluir a turma");
        }
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }
}
