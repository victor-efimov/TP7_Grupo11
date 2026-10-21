package ar.edu.unju.escmi.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.dao.imp.ClienteDaoImp;
import ar.edu.unju.escmi.dao.imp.FacturaDaoImp;
import ar.edu.unju.escmi.dao.imp.ProductoDaoImp;
import ar.edu.unju.escmi.entities.Cliente;
import ar.edu.unju.escmi.entities.DetalleFactura;
import ar.edu.unju.escmi.entities.Factura;
import ar.edu.unju.escmi.entities.Producto;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static IClienteDao clienteDao = new ClienteDaoImp();
    private static IProductoDao productoDao = new ProductoDaoImp();
    private static IFacturaDao facturaDao = new FacturaDaoImp();
    public static void main(String[] args) {
        int opcion = -1;

        try {
            do {
                mostrarMenu();
                try {
                    System.out.print("Seleccione una opción: ");
                    if (!scanner.hasNextLine()) {
                        break;
                    }
                    opcion = Integer.parseInt(scanner.nextLine().trim());

                    switch (opcion) {
                        case 1:
                            altaCliente();
                            break;
                        case 2:
                            altaProducto();
                            break;
                        case 3:
                            realizarVenta();
                            break;
                        case 4:
                            buscarFacturaPorId();
                            break;
                        case 5:

                            System.out.println("Opción 5 pendiente de implementación.");
                            break;
                        case 6:

                            System.out.println("Opción 6 pendiente de implementación.");
                            break;
                        case 7:

                            System.out.println("Opción 7 pendiente de implementación.");
                            break;
                        case 8:

                            System.out.println("Opción 8 pendiente de implementación.");
                            break;
                        case 9:

                            System.out.println("Opción 9 pendiente de implementación.");
                            break;
                        case 10:

                            System.out.println("Opción 10 pendiente de implementación.");
                            break;
                        case 11:

                            System.out.println("Opción 11 pendiente de implementación.");
                            break;
                        case 12:

                            System.out.println("Opción 12 pendiente de implementación.");
                            break;
                        case 0:
                            System.out.println("Saliendo del sistema...");
                            break;
                        default:
                            System.out.println("Opción inválida. Ingrese un número entre 0 y 12.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debe ingresar un número entero válido.");
                } catch (Exception e) {
                    System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                }
            } while (opcion != 0);

        } finally {
            try {
                EmfSingleton.getInstance().cerrar();
            } finally {
                scanner.close();
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n========= MENU PRINCIPAL - TP7 =========");
        System.out.println("1 - Alta de cliente");
        System.out.println("2 - Alta de producto");
        System.out.println("3 - Realizar la venta de productos (Alta de factura)");
        System.out.println("4 - Buscar una factura por su número y mostrar datos");
        System.out.println("5 - Eliminar una factura (baja lógica)");
        System.out.println("6 - Eliminar un producto (baja lógica)");
        System.out.println("7 - Modificar datos de cliente");
        System.out.println("8 - Modificar precio de producto");
        System.out.println("9 - Eliminar cliente (baja lógica)");
        System.out.println("10 - Mostrar todas las facturas");
        System.out.println("11 - Mostrar todos los clientes");
        System.out.println("12 - Mostrar las facturas que superen $900.000");
        System.out.println("0 - Salir");
        System.out.println("========================================");
    }


    private static void altaCliente() {
        try {
            System.out.println("\n--- ALTA DE CLIENTE ---");
            Cliente cliente = new Cliente();

            System.out.print("Ingrese nombre: ");
            cliente.setNombre(scanner.nextLine().trim());

            System.out.print("Ingrese apellido: ");
            cliente.setApellido(scanner.nextLine().trim());

            System.out.print("Ingrese domicilio: ");
            cliente.setDomicilio(scanner.nextLine().trim());

            System.out.print("Ingrese DNI (solo números): ");
            cliente.setDni(Integer.parseInt(scanner.nextLine().trim()));

            cliente.setEstado(true);

            clienteDao.guardarCliente(cliente);
            System.out.println("Cliente registrado con éxito.");
        } catch (NumberFormatException e) {
            System.out.println("Error en el alta: El DNI debe ser un número entero válido.");
        } catch (Exception e) {
            System.out.println("Error al registrar el cliente: " + e.getMessage());
        }
    }

    private static void altaProducto() {
        try {
            System.out.println("\n--- ALTA DE PRODUCTO ---");
            Producto producto = new Producto();

            System.out.print("Ingrese descripción del producto: ");
            producto.setDescripcion(scanner.nextLine().trim());

            System.out.print("Ingrese precio unitario: ");
            double precio = Double.parseDouble(scanner.nextLine().trim());
            if (!Double.isFinite(precio) || precio <= 0) {
                System.out.println("El precio unitario debe ser mayor a 0.");
                return;
            }
            producto.setPrecioUnitario(precio);
            producto.setEstado(true);

            productoDao.guardarProducto(producto);
            System.out.println("Producto registrado con éxito.");
        } catch (NumberFormatException e) {
            System.out.println("Error en el alta: El precio debe ser un valor numérico válido.");
        } catch (Exception e) {
            System.out.println("Error al registrar el producto: " + e.getMessage());
        }
    }


    private static void realizarVenta() {
        try {
            System.out.println("\n--- REALIZAR VENTA (NUEVA FACTURA) ---");
            System.out.print("Ingrese el ID del cliente: ");
            Long idCliente = Long.parseLong(scanner.nextLine().trim());

            Cliente cliente = clienteDao.obtenerClientes().stream()
                    .filter(c -> idCliente.equals(c.getId()))
                    .findFirst()
                    .orElse(null);
            if (cliente == null || !cliente.isEstado()) {
                System.out.println("El cliente no existe o se encuentra dado de baja.");
                return;
            }

            Factura factura = new Factura();
            factura.setFecha(LocalDate.now());
            factura.setCliente(cliente);
            factura.setDomicilio(cliente.getDomicilio());
            factura.setEstado(true);

            List<DetalleFactura> detalles = new ArrayList<>();
            String continuar = "S";

            do {
                System.out.print("Ingrese el ID del producto a vender: ");
                Long idProducto = Long.parseLong(scanner.nextLine().trim());

                Producto producto = productoDao.obtenerProductos().stream()
                        .filter(p -> idProducto.equals(p.getId()))
                        .findFirst()
                        .orElse(null);
                if (producto == null || !producto.isEstado()) {
                    System.out.println("El producto no existe o está dado de baja.");
                } else {
                    System.out.print("Ingrese la cantidad de '" + producto.getDescripcion() + "': ");
                    int cantidad = Integer.parseInt(scanner.nextLine().trim());

                    if (cantidad <= 0) {
                        System.out.println("La cantidad debe ser mayor a cero.");
                    } else {
                        DetalleFactura detalle = new DetalleFactura();
                        detalle.setProducto(producto);
                        detalle.setCantidad(cantidad);
                        double subtotal = detalle.calcularSubTotal();


                        detalles.add(detalle);
                        System.out.println("Producto agregado. Subtotal: $" + subtotal);
                    }
                }

                System.out.print("¿Desea agregar otro producto a la factura? (S/N): ");
                continuar = scanner.nextLine().trim();
            } while (continuar.equalsIgnoreCase("S"));

            if (detalles.isEmpty()) {
                System.out.println("Venta cancelada: no se agregaron productos válidos a la factura.");
                return;
            }

            factura.setDetalles(detalles);
            factura.calcularTotal();

            facturaDao.guardarFactura(factura);
            System.out.println("Venta registrada con éxito. Total de la factura: $" + factura.getTotal());

        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresó un formato numérico inválido para ID o cantidad.");
        } catch (Exception e) {
            System.out.println("Error al procesar la venta: " + e.getMessage());
        }
    }

    private static void buscarFacturaPorId() {
        try {
            System.out.println("\n--- BUSCAR FACTURA POR NÚMERO (ID) ---");
            System.out.print("Ingrese el número (ID) de la factura: ");
            Long idFactura = Long.parseLong(scanner.nextLine().trim());

            Factura factura = facturaDao.buscarFactura(idFactura);
            if (factura == null || !factura.isEstado()) {
                System.out.println("No se encontró una factura activa con el ID: " + idFactura);
            } else {
                System.out.println("\n========================================");
                System.out.println("Factura N°: " + factura.getId());
                System.out.println("Fecha: " + factura.getFecha());
                System.out.println("Cliente: " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido()
                        + " (DNI: " + factura.getCliente().getDni() + ")");
                System.out.println("Domicilio: " + factura.getDomicilio());
                System.out.println("--- Detalles de la Factura ---");
                if (factura.getDetalles() != null) {
                    for (DetalleFactura det : factura.getDetalles()) {
                        System.out.println(" * Producto: " + det.getProducto().getDescripcion()
                                + " | Precio Unit.: $" + det.getProducto().getPrecioUnitario()
                                + " | Cant.: " + det.getCantidad()
                                + " | Subtotal: $" + det.getSubtotal());
                    }
                }
                System.out.println("TOTAL FACTURA: $" + factura.getTotal());
                System.out.println("========================================");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El número de factura debe ser un entero válido.");
        } catch (Exception e) {
            System.out.println("Error al buscar la factura: " + e.getMessage());
        }
    }
}
