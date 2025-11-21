import java.util.*;

// =====================
// CLASE ENTRADA (Producto) - PATRÓN BUILDER
// =====================
// Esta clase representa la "base" de nuestra entrada de cine. 
// Contiene todos los atributos de una entrada (película, sala, asiento, horario, tipo y extras).
// Builder se utiliza para construir instancias complejas de Entrada paso a paso.
class Entrada {
    private String pelicula;
    private String sala;
    private String asiento;
    private String horario;
    private String tipoEntrada;
    private double precioBase;
    private List<String> extras;

    public Entrada() {
        extras = new ArrayList<>();
    }

    // Setters para Builder
    public void setPelicula(String pelicula) { this.pelicula = pelicula; }
    public void setSala(String sala) { this.sala = sala; }
    public void setAsiento(String asiento) { this.asiento = asiento; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setTipo(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }
    public void setPrecio(double precioBase) { this.precioBase = precioBase; }
    public void agregarExtra(String extra) { extras.add(extra); }

    // Getters
    public double getPrecioBase() { return precioBase; }
    public List<String> getExtras() { return extras; }

    // Mostrar detalles de la entrada
    public String mostrarDetalles() {
        String detalles = "";
        detalles += "Película: " + pelicula + "\n";
        detalles += "Sala: " + sala + "\n";
        detalles += "Asiento: " + asiento + "\n";
        detalles += "Horario: " + horario + "\n";
        detalles += "Tipo: " + tipoEntrada + "\n";
        if (!extras.isEmpty()) {
            detalles += "Extras: " + String.join(", ", extras) + "\n";
        }
        return detalles;
    }
}

// =====================
// BUILDER - PATRÓN DE CREACIÓN
// =====================
// Permite construir objetos complejos (Entrada) paso a paso sin necesidad de tener un constructor gigante.
class EntradaBuilder {
    private Entrada entrada;

    public EntradaBuilder() { entrada = new Entrada(); }

    public EntradaBuilder setPelicula(String pelicula) { entrada.setPelicula(pelicula); return this; }
    public EntradaBuilder setSala(String sala) { entrada.setSala(sala); return this; }
    public EntradaBuilder setAsiento(String asiento) { entrada.setAsiento(asiento); return this; }
    public EntradaBuilder setHorario(String horario) { entrada.setHorario(horario); return this; }
    public EntradaBuilder setTipo(String tipo) { entrada.setTipo(tipo); return this; }
    public EntradaBuilder setPrecio(double precio) { entrada.setPrecio(precio); return this; }

    public Entrada build() { return entrada; }
}

// =====================
// DECORATOR - PATRÓN ESTRUCTURAL
// =====================
// Permite añadir funcionalidades adicionales (extras) a la entrada sin modificar su clase.
// Se envuelve la entrada base y se le suma el precio del extra automáticamente.
abstract class EntradaDecorator extends Entrada {
    protected Entrada entrada;

    public EntradaDecorator(Entrada entrada) { this.entrada = entrada; }

    @Override
    public double getPrecioBase() { return entrada.getPrecioBase(); }

    @Override
    public String mostrarDetalles() { return entrada.mostrarDetalles(); }
}

class Gafas3D extends EntradaDecorator {
    private final double precioExtra = 3.0;

    public Gafas3D(Entrada entrada) {
        super(entrada);
        this.entrada.agregarExtra("Gafas 3D");
    }

    @Override
    public double getPrecioBase() { return entrada.getPrecioBase() + precioExtra; }

    @Override
    public String mostrarDetalles() {
        return entrada.mostrarDetalles() + "Precio final con extras: Bs." + getPrecioBase() + "\n";
    }
}

class Combo extends EntradaDecorator {
    private final double precioExtra = 10.0;

    public Combo(Entrada entrada) {
        super(entrada);
        this.entrada.agregarExtra("Combo Palomitas/Bebida");
    }

    @Override
    public double getPrecioBase() { return entrada.getPrecioBase() + precioExtra; }

    @Override
    public String mostrarDetalles() {
        return entrada.mostrarDetalles() + "Precio final con extras: Bs." + getPrecioBase() + "\n";
    }
}

// =====================
// STRATEGY - PATRÓN DE COMPORTAMIENTO
// =====================
// Permite definir distintos métodos de pago de manera intercambiable.
// La clase Cliente (Main) no necesita conocer la implementación concreta del pago.
interface PagoStrategy {
    void pagar(double monto);
}

class PagoEfectivo implements PagoStrategy {
    public void pagar(double monto) { System.out.println("Pagando Bs." + monto + " en efectivo."); }
}

class PagoTarjeta implements PagoStrategy {
    private String numero, titular, cvv;

    public PagoTarjeta(String numero, String titular, String cvv) {
        this.numero = numero;
        this.titular = titular;
        this.cvv = cvv;
    }

    public void pagar(double monto) {
        System.out.println("Pagando Bs." + monto + " con tarjeta " + numero + " titular: " + titular);
    }
}

class PagoQR implements PagoStrategy {
    public void pagar(double monto) { System.out.println("Pagando Bs." + monto + " mediante QR."); }
}

// =====================
// MAIN
// =====================
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Precios base de cada tipo de entrada
        Map<String, Double> precios = new HashMap<>();
        precios.put("2D", 20.0);
        precios.put("3D", 30.0);
        precios.put("VIP", 50.0);

        List<Entrada> entradas = new ArrayList<>();
        String opcion;

        do {
            System.out.println("\n=== Venta de Entradas de Cine ===");
            System.out.print("Ingrese la película: ");
            String pelicula = sc.nextLine();
            System.out.print("Ingrese la sala: ");
            String sala = sc.nextLine();
            System.out.print("Ingrese el asiento: ");
            String asiento = sc.nextLine();
            System.out.print("Ingrese el horario: ");
            String horario = sc.nextLine();

            // Mostrar tipos de entrada y precios
            System.out.println("=== Tipos de entradas y precios ===");
            int idx = 1;
            for (String tipo : precios.keySet()) {
                System.out.println(idx + " - " + tipo + ": Bs." + precios.get(tipo));
                idx++;
            }

            // Selección tipo de entrada
            String tipoEntrada = "";
            while (true) {
                System.out.print("Ingrese el tipo de entrada (2D/3D/VIP): ");
                tipoEntrada = sc.nextLine().toUpperCase();
                if (precios.containsKey(tipoEntrada)) break;
                System.out.println("Tipo inválido.");
            }

            double precioBase = precios.get(tipoEntrada);

            // Crear entrada con Builder
            Entrada entrada = new EntradaBuilder()
                    .setPelicula(pelicula)
                    .setSala(sala)
                    .setAsiento(asiento)
                    .setHorario(horario)
                    .setTipo(tipoEntrada)
                    .setPrecio(precioBase)
                    .build();

            // Selección de extras (Decorator)
            int opcionExtra;
            do {
                System.out.println("\nSeleccione extras:");
                System.out.println("1 - Gafas 3D (+Bs.3)");
                System.out.println("2 - Combo palomitas/bebida (+Bs.10)");
                System.out.println("0 - Terminar selección");
                System.out.print("Opción: ");
                opcionExtra = Integer.parseInt(sc.nextLine());

                switch (opcionExtra) {
                    case 1:
                        if (!tipoEntrada.equals("2D")) {
                            entrada = new Gafas3D(entrada);
                        } else {
                            System.out.println("No se pueden agregar gafas 3D a una entrada 2D.");
                        }
                        break;
                    case 2:
                        entrada = new Combo(entrada);
                        break;
                    case 0:
                        System.out.println("Selección de extras finalizada.");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } while (opcionExtra != 0);

            entradas.add(entrada);
            System.out.println("\nEntrada agregada:");
            System.out.println(entrada.mostrarDetalles());

            System.out.print("¿Desea agregar otra entrada? (s/n): ");
            opcion = sc.nextLine();
        } while (opcion.equalsIgnoreCase("s"));

        // Calcular total
        double total = 0;
        for (Entrada e : entradas) total += e.getPrecioBase();
        System.out.println("\nTotal a pagar: Bs." + total);

        // Pago (Strategy)
        System.out.println("Métodos de pago: 1-Efectivo, 2-Tarjeta, 3-QR");
        System.out.print("Seleccione: ");
        int metodo = Integer.parseInt(sc.nextLine());

        PagoStrategy pago;
        switch (metodo) {
            case 1: pago = new PagoEfectivo(); break;
            case 2:
                System.out.print("Número de tarjeta: ");
                String num = sc.nextLine();
                System.out.print("Titular: ");
                String titular = sc.nextLine();
                System.out.print("CVV: ");
                String cvv = sc.nextLine();
                pago = new PagoTarjeta(num, titular, cvv);
                break;
            case 3: pago = new PagoQR(); break;
            default: pago = new PagoEfectivo();
        }

        pago.pagar(total);
        System.out.println("¡Gracias por su compra!");
        sc.close();
    }
}
