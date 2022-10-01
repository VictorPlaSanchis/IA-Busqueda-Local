
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;

/**
 *
 * @author Maria Montalvo y Victor Pla
 */


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaBoard {
    //Numero de centrales electricas
    private static int nCentrales;

    //Numero de clientes garantizados
    private static int nGarantizados;

    //Numero de clientes no garantizados
    private static int nNoGarantizados;

    //ArrayList para almacenar las centrales
    private static Centrales centrales;

    //ArrayList para almacenar los clientes
    private static Clientes clientes;

    private Random random;


    /** Crea una nueva instancia de EnergiaBoard */
    public EnergiaBoard() {
        centrales = (Centrales) new ArrayList<Central>();
        clientes = (Clientes) new ArrayList<Cliente>();

        

    }


    public EnergiaBoard(EnergiaBoard e) {
        int i, j;
        boolean aux[][]=e.getConnexions();
        connexions = new boolean[aux.length][aux.length];
        for (i=0; i<ncentrals+nrepetidors; i++)
            for (j=0; j<ncentrals+nrepetidors; j++)
                connexions[i][j]=aux[i][j];

        int aux2[]=c.getGrau();
        grau = new int[aux2.length];

        for (i=0; i<ncentrals+nrepetidors; i++)
            grau[i]=aux2[i];

        error_total = c.getErrorTotal();
        num_repetidors = c.getNumRepetidors();


        myRandom = new Random();
    }

}

