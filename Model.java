import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkException;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

public class Model {
	public static void main(String [] args) {
		glp_prob lp;
		glp_smcp parm;
		SWIGTYPE_p_int ind;
		SWIGTYPE_p_double val;
		int ret;

		//ʻO ke akua wale nō keʻike
		int w,h,x,n;
		double a,b;
		w = 2;
		h = 1;
		x = 2;
		n = 3;
		a = 3.0;
		b = 5.0;

		try {
			// Create problem
			lp = GLPK.glp_create_prob();
			System.out.println("Problem created");
			GLPK.glp_set_prob_name(lp, "myProblem");

			// ** Função Objetiva **
			// Definir colunas (variáveis)
			GLPK.glp_add_cols(lp, 2);
			GLPK.glp_set_col_name(lp, 1, "h");
			GLPK.glp_set_col_kind(lp, 1, GLPKConstants.GLP_CV);
			GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_LO, 0.0, 0.0);
			GLPK.glp_set_col_name(lp, 2, "x");
			GLPK.glp_set_col_kind(lp, 2, GLPKConstants.GLP_CV);
			GLPK.glp_set_col_bnds(lp, 2, GLPKConstants.GLP_LO, 0.0, 0.0);

			//Criar constantes
			//Alocação de memória
			ind = GLPK.new_intArray(2);
			val = GLPK.new_doubleArray(2);


			// ** Restrições **
			//Criar linhas (variáveis artificiais)
			GLPK.glp_add_rows(lp, 2);

			// Restrições
			GLPK.glp_set_row_name(lp, 1, "restricoes");
			GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_DB, 0, w);
			GLPK.intArray_setitem(ind, 1, h);
			GLPK.intArray_setitem(ind, 2, x);
			GLPK.doubleArray_setitem(val, 1, n);
			GLPK.doubleArray_setitem(val, 2, 1);
			GLPK.glp_set_mat_row(lp, 1, 2, ind, val);



			// Desalocar memória
			GLPK.delete_intArray(ind);
			GLPK.delete_doubleArray(val);

			// Coeficientes da função objetiva
			// Função objetivo
			GLPK.glp_set_obj_name(lp, "z");
			GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
			GLPK.glp_set_obj_coef(lp, 1, a);
			GLPK.glp_set_obj_coef(lp, 2, b);

			// Escrever o modelo em um arquivo
			// GLPK.glp_write_lp(lp, null, "model.lp");

			// Resolver o modelo
			parm = new glp_smcp();
			GLPK.glp_init_smcp(parm);
			ret = GLPK.glp_simplex(lp, parm);

			// Restaurar solução
			if (ret == 0) {
				write_lp_solution(lp);
			} else {
				System.out.println("O problema não pôde ser resolvido.");
			}

			// Desalocar memória
			GLPK.glp_delete_prob(lp);
		} catch (GlpkException ex) {
			ex.printStackTrace();
			ret = 1;
		}

		System.exit(ret);
	}

	/**
	 * Escreve a resolução utilizando o método Simplex
	 * @param lp problema
	 */
	static void write_lp_solution(glp_prob lp) {
		int i;
		int n;
		String name;
		double val;

		name = GLPK.glp_get_obj_name(lp);
		val = GLPK.glp_get_obj_val(lp);
		System.out.print(name);
		System.out.print(" = ");
		System.out.println(val);
		n = GLPK.glp_get_num_cols(lp);
		for (i = 1; i <= n; i++) {
			name = GLPK.glp_get_col_name(lp, i);
			val = GLPK.glp_get_col_prim(lp, i);
			System.out.print(name);
			System.out.print(" = ");
			System.out.println(val);
		}
	}
}
