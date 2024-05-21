import java.util.Scanner;

//Esta clase ya no se usa ya que toda la funcionalidad se paso a CacheLRUGUI como una interfaz

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LRU<Integer, String> LRU = new LRU<>(3);

        while (true) {
            System.out.print("Ingresa un número (o 'salir' para terminar): ");
            String entrada = scanner.nextLine();

            if (entrada.equalsIgnoreCase("salir")) {
                break;
            }

            try {
                int numero = Integer.parseInt(entrada);
                if (LRU.obtener(numero) == null) {
                    if(LRU.memoria.size() < 3){
                        System.out.println("Fallo de pagina");
                    }
                    if (LRU.memoria.size() >= 3) {
                        Integer claveMasAntigua = LRU.getClaveMasAntigua();
                        System.out.println("Fallo de pagina");
                        System.out.println("Reemplazando página: " + claveMasAntigua);
                    }
                    LRU.insertar(numero, "Página " + numero);
                } else {
                    System.out.println("Acierto de pagina");
                    System.out.println("La página " + numero + " ya está en la memoria.");
                }
                LRU.mostrar();
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un número válido.");
            }
        }

        scanner.close();
    }
}
