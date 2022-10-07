import java.util.Arrays;

public class BusquedaLocal {
    public static void main(String[] args){

        System.out.println("Starting being uwu...");

        EnergiaBoard uwu = new EnergiaBoard(
                new int[]{5, 10, 15},
                1,
                1000,
                new double[]{0.25,0.30,0.45},
                0.75,
                0 );
        uwu.generarEstadoInicial(0);
        uwu.printPanicMethod();
        System.out.println(uwu.calculaBeneficios());

        System.out.println("Not uwu anymore...");

    }
}
