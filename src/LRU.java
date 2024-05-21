import java.util.LinkedHashMap;
import java.util.Map;
public class LRU<K, V> {
    private final int capacidad;
    final Map<K, V> memoria;

    public LRU(int capacidad) {
        this.capacidad = capacidad;
        this.memoria = new LinkedHashMap<K, V>(capacidad, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> entradaMasAntigua) {
                return size() > LRU.this.capacidad;
            }
        };
    }

    public V obtener(K clave) {
        return memoria.getOrDefault(clave, null);
    }

    public V insertar(K clave, V valor) {
        return memoria.put(clave, valor);
    }

    public void mostrar() {
        System.out.print("Estado de la memoria: ");
        for (Map.Entry<K, V> entrada : memoria.entrySet()) {
            System.out.print("[" + entrada.getKey() + "=" + entrada.getValue() + "] ");
        }
        System.out.println();
    }

    public K getClaveMasAntigua() {
        return memoria.keySet().iterator().next();
    }
}
