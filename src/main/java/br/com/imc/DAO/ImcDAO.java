package br.com.imc.DAO;

import br.com.imc.domain.Aluno;
import br.com.imc.domain.Imc;
import br.com.imc.model.ImcModel;
import br.com.imc.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

public class ImcDAO extends GenericDAO<Imc> {

    public List<ImcModel> listarImcsPorAluno(Aluno aluno) {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            NativeQuery query = session.createSQLQuery(""
                    + "SELECT \n"
                    + "    a.id AS aluno_id, \n"
                    + "    a.nome,  \n"
                    + "    DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL seq MONTH), '%Y-%m-01') AS data,\n"
                    + "    DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL seq MONTH), '%M') AS mes_extenso,\n"
                    + "    coalesce(avg(i.imc), 0) AS media_imc \n"
                    + "    \n"
                    + "FROM \n"
                    + "    (SELECT 0 AS seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS seqs\n"
                    + "CROSS JOIN \n"
                    + "    aluno a\n"
                    + "LEFT JOIN \n"
                    + "    imc i ON a.id = i.aluno_id \n"
                    + "          AND DATE_FORMAT(i.dataImc, '%Y-%m') = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL seq MONTH), '%Y-%m')\n"
                    + "WHERE \n"
                    + "    a.id = " + aluno.getId() + "\n"
                    + "GROUP BY \n"
                    + "    a.id, data, mes_extenso\n"
                    + "ORDER BY \n"
                    + "    data ;");
            List<Object[]> linhas = query.list();
            List<ImcModel> imcModels = new ArrayList<>();
            for (Object[] linha : linhas) {
                ImcModel imcModel = new ImcModel();
                imcModel.setMes_nome(linha[3].toString());                
                imcModel.setImc_media(Float.parseFloat(linha[4].toString()));

                imcModels.add(imcModel);
            }
            return imcModels;
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            session.close();
        }
    }

}
