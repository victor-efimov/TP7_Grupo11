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
                            eliminarFactura();
                            break;
                        case 6:
                            eliminarProducto();
                            break;
                        case 7:
                            modificarCliente();
                            break;
                        case 8:
                            modificarPrecioProducto();
                            break;
                        case 9:
                            eliminarCliente();
                            break;
                        case 10:
                            mostrarFacturas();
                            break;
                        case 11:
                            mostrarClientes();
                            break;
                        case 12:
                            mostrarFacturasMayores900();
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
            System.out.println("Error: El numero de factura debe ser un entero valido.");
        } catch (Exception e) {
            System.out.println("Error al buscar la factura: " + e.getMessage());
        }
    }
        private static void eliminarFactura() {
        try {
            System.out.println("\n--- ELIMINAR FACTURA (BAJA LOGICA) ---");
            System.out.print("Ingrese el ID de la factura: ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            Factura factura = facturaDao.buscarFactura(id);
            if (factura == null || !factura.isEstado()) {
                System.out.println("No existe una factura activa con ese ID.");
                return;
            }
            facturaDao.borrarFactura(factura);
            System.out.println("Factura eliminada con exito.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un núuero entero valido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar la factura: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        try {
            System.out.println("\n--- ELIMINAR PRODUCTO (BAJA LOGICA) ---");
            System.out.print("Ingrese el ID del producto: ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            Producto producto = productoDao.obtenerProductos().stream()
                    .filter(p -> id.equals(p.getId()))
                    .findFirst()
                    .orElse(null);
            if (producto == null || !producto.isEstado()) {
                System.out.println("No existe un producto activo con ese ID.");
                return;
            }
            productoDao.borrarProducto(producto);
            System.out.println("Producto eliminado.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un numero entero valido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    private static void modificarCliente() {
        try {
            System.out.println("\n--- MODIFICAR CLIENTE ---");
            System.out.print("Ingrese el ID del cliente: ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            Cliente cliente = clienteDao.obtenerClientes().stream()
                    .filter(c -> id.equals(c.getId()))
                    .findFirst()
                    .orElse(null);
            if (cliente == null || !cliente.isEstado()) {
                System.out.println("No existe un cliente activo con ese ID.");
                return;
            }

            System.out.print("Nuevo nombre (" + cliente.getNombre() + "): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) cliente.setNombre(nombre);

            System.out.print("Nuevo apellido (" + cliente.getApellido() + "): ");
            String apellido = scanner.nextLine().trim();
            if (!apellido.isEmpty()) cliente.setApellido(apellido);

            System.out.print("Nuevo domicilio (" + cliente.getDomicilio() + "): ");
            String domicilio = scanner.nextLine().trim();
            if (!domicilio.isEmpty()) cliente.setDomicilio(domicilio);

            System.out.print("Nuevo DNI (" + cliente.getDni() + "): ");
            String dni = scanner.nextLine().trim();
            if (!dni.isEmpty()) cliente.setDni(Integer.parseInt(dni));

            clienteDao.modificarCliente(cliente);
            System.out.println("Cliente modificado con exito.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingreso un número invalido (ID o DNI).");
        } catch (Exception e) {
            System.out.println("Error al modificar el cliente: " + e.getMessage());
        }
    }

    private static void modificarPrecioProducto() {
        try {
            System.out.println("\n--- MODIFICAR PRECIO DE PRODUCTO ---");
            System.out.print("Ingrese el ID del producto: ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            Producto producto = productoDao.obtenerProductos().stream()
                    .filter(p -> id.equals(p.getId()))
                    .findFirst()
                    .orElse(null);
            if (producto == null || !producto.isEstado()) {
                System.out.println("No existe un producto activo con ese ID.");
                return;
            }

            System.out.print("Nuevo precio (actual $" + producto.getPrecioUnitario() + "): ");
            double precio = Double.parseDouble(scanner.nextLine().trim());
            if (!Double.isFinite(precio) || precio <= 0) {
                System.out.println("El precio debe ser mayor a 0.");
                return;
            }
            productoDao.modificarPrecio(id, precio);
            System.out.println("Precio modificado con exito.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingreso un nimero invalido.");
        } catch (Exception e) {
            System.out.println("Error al modificar el precio: " + e.getMessage());
        }
    }

    private static void eliminarCliente() {
        try {
            System.out.println("\n--- ELIMINAR CLIENTE (BAJA LOGICA) ---");
            System.out.print("Ingrese el ID del cliente: ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            Cliente cliente = clienteDao.obtenerClientes().stream()
                    .filter(c -> id.equals(c.getId()))
                    .findFirst()
                    .orElse(null);
            if (cliente == null || !cliente.isEstado()) {
                System.out.println("No existe un cliente activo con ese ID.");
                return;
            }
            clienteDao.borrarCliente(cliente);
            System.out.println("Cliente eliminado con exito.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un numero entero valido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    private static void mostrarFacturas() {
        try {
            System.out.println("\n--- TODAS LAS FACTURAS ---");
            imprimirFacturas(facturaDao.obtenerFacturas());
        } catch (Exception e) {
            System.out.println("Error al mostrar las facturas: " + e.getMessage());
        }
    }

    private static void mostrarClientes() {
        try {
            System.out.println("\n--- TODOS LOS CLIENTES ---");
            boolean hay = false;
            for (Cliente c : clienteDao.obtenerClientes()) {
                if (c.isEstado()) {
                    hay = true;
                    System.out.println("ID: " + c.getId() + " | " + c.getNombre() + " " + c.getApellido()
                            + " | DNI: " + c.getDni() + " | Domicilio: " + c.getDomicilio());
                }
            }
            if (!hay) System.out.println("No hay clientes activos.");
        } catch (Exception e) {
            System.out.println("Error al mostrar los clientes: " + e.getMessage());
        }
    }

    private static void mostrarFacturasMayores900() {
        try {
            System.out.println("\n--- FACTURAS CON TOTAL MAYOR A $900.000 ---");
            imprimirFacturas(facturaDao.obtenerFacturas900());
        } catch (Exception e) {
            System.out.println("Error al mostrar las facturas: " + e.getMessage());
        }
    }

    private static void imprimirFacturas(List<Factura> facturas) {
        boolean hay = false;
        for (Factura f : facturas) {
            if (f.isEstado()) {
                hay = true;
                System.out.println("Factura N " + f.getId() + " | Fecha: " + f.getFecha()
                        + " | Cliente: " + f.getCliente().getNombre() + " " + f.getCliente().getApellido()
                        + " | Total: $" + f.getTotal());
            }
        }
        if (!hay) System.out.println("No hay facturas para mostrar.");
    }
}
