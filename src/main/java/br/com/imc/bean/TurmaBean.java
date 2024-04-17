package br.com.imc.bean;

import br.com.imc.DAO.TurmaDAO;
import br.com.imc.domain.Turma;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class TurmaBean implements Serializable {

    @Param
    private Integer id;

    private Turma turma;
    private TurmaDAO turmaDAO;

    @PostConstruct
    public void init() {
        turma = new Turma();
        turmaDAO = new TurmaDAO();

        if (id != null) {
            turma = turmaDAO.buscar(id);
        }
    }

    public void novo() {
        turma = new Turma();
    }

    public void salvar() {
        try {
            System.out.println(turma.toString());
            TurmaDAO turmaDAO = new TurmaDAO();
            turma = turmaDAO.merge(turma);

            Messages.addGlobalInfo("Dados salvos com sucesso");
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar salvar os dados");
        }
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
