package br.com.imc.bean;

import br.com.imc.DAO.AlunoDAO;
import br.com.imc.DAO.ImcDAO;
import br.com.imc.domain.Aluno;
import br.com.imc.domain.Imc;
import br.com.imc.model.ImcModel;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Messages;
import org.primefaces.component.linechart.LineChart;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

@Named
@ViewScoped
public class ImcBean implements Serializable {

    @Param
    private Integer aluno_id;

    private Aluno aluno;
    private Imc imc;

    private AlunoDAO alunoDAO;
    private ImcDAO imcDAO;

    private LineChartModel lineModel;

    @PostConstruct
    public void init() {
        aluno = new Aluno();
        imc = new Imc();
        alunoDAO = new AlunoDAO();
        imcDAO = new ImcDAO();

        try {
            aluno = alunoDAO.buscar(aluno_id);
        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar carregar os dados");
        }

        createLineModel();

    }

    public void calcular() {
        try {
            //calcula o imc
            float resultado = imc.getPeso()
                    / (imc.getAltura() / 100 * imc.getAltura() / 100);
            //armazenar o valor na variável imc
            imc.setImc(resultado);
            //obter o texto do imc
            String texto = interpretarResultado(imc.getImc());
            //salvar o valor na variável imc.resultado
            imc.setResultado(texto);
            //salvar o registro no banco de dados
            imc.setAluno(aluno);
            imc.setDataImc(LocalDateTime.now());
            imc = imcDAO.merge(imc);
            //listar os imsc do aluno

            Messages.addGlobalInfo("Dados salvos com sucesso");
        } catch (RuntimeException e) {
            Logger.getLogger(ImcBean.class.getName()).log(Level.SEVERE, null, e);
            Messages.addGlobalError("Erro ao tentar calcular o IMC");
        }
    }

    public String interpretarResultado(float imc) {
        if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc < 25) {
            return "Peso normal";
        } else if (imc < 30) {
            return "Sobrepeso";
        } else if (imc < 35) {
            return "Obesidade grau 1";
        } else if (imc < 40) {
            return "Obesidade grau 2 (severa)";
        } else {
            return "Obesidade grau 3 (mórbida)";
        }
    }

    public void limpar() {
        imc = new Imc();
    }

    public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        LineChartDataSet dataSet2 = new LineChartDataSet();
        LineChartDataSet dataSet3 = new LineChartDataSet();
        //dados da vertical, imc
        List<Object> values = new ArrayList<>();
        List<Object> values2 = new ArrayList<>();
        List<Object> values3 = new ArrayList<>();
        //dados da horizontal, meses
        List<String> labels = new ArrayList<>();

        //lista dos dados no banco de dados
        List<ImcModel> imcModels = imcDAO.listarImcsPorAluno(aluno);
        for (ImcModel imcModel : imcModels) {
            values.add(imcModel.getImc_media()); //add o imc
            labels.add(StringUtils.capitalize(imcModel.getMes_nome())); //adiciona o mês
            values2.add(18.6);
            values3.add(24.9);
        }

        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("IMC");
        dataSet.setBorderColor("rgb(255, 165, 0)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);

        dataSet2.setData(values2);
        dataSet2.setFill(false);
        dataSet2.setLabel("Mínimo");
        dataSet2.setBorderColor("rgb(27, 155, 0)");
        dataSet2.setBorderWidth(2);
        dataSet2.setBorderDash(Arrays.asList(3, 6));
        dataSet2.setPointRadius(0);
        data.addChartDataSet(dataSet2);

        dataSet3.setData(values3);
        dataSet3.setFill(false);
        dataSet3.setLabel("Máximo");
        dataSet3.setBorderColor("rgb(27, 155, 0)");
        dataSet3.setBorderWidth(2);
        dataSet3.setBorderDash(Arrays.asList(3, 6));
        dataSet3.setPointRadius(0);
        data.addChartDataSet(dataSet3);

        data.setLabels(labels);

        //opções
        LineChartOptions options = new LineChartOptions();
        options.setMaintainAspectRatio(false);
        options.setResponsive(true);
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico do IMC");
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(data);

    }

    public Integer getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(Integer aluno_id) {
        this.aluno_id = aluno_id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Imc getImc() {
        return imc;
    }

    public void setImc(Imc imc) {
        this.imc = imc;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

}
