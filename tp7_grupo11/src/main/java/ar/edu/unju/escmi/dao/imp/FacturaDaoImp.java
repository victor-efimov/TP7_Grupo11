package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.entities.*;

public class FacturaDaoImp implements IFacturaDao {
    @Override
    public void guardarFactura(Factura factura) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            if (factura.getId() != null || factura.getCliente() == null
                    || factura.getCliente().getId() == null
                    || factura.getDetalles() == null || factura.getDetalles().isEmpty()) {
                throw new IllegalArgumentException("La factura debe ser nueva y tener cliente y detalles.");
            }
            Cliente cliente = manager.find(Cliente.class, factura.getCliente().getId());
            if (cliente == null || !cliente.isEstado()) {
                throw new IllegalArgumentException("El cliente no existe o está dado de baja.");
            }
            factura.setCliente(cliente);
            factura.setDomicilio(cliente.getDomicilio());
            for (DetalleFactura detalle : factura.getDetalles()) {
                if (detalle == null || detalle.getId() != null || detalle.getProducto() == null
                        || detalle.getProducto().getId() == null) {
                    throw new IllegalArgumentException("Cada detalle debe ser nuevo y tener un producto.");
                }
                Producto producto = manager.find(Producto.class, detalle.getProducto().getId());
                if (producto == null || !producto.isEstado()) {
                    throw new IllegalArgumentException("El producto no existe o está dado de baja.");
                }
                detalle.setProducto(producto);
                detalle.calcularSubTotal();
            }
            factura.calcularTotal();
            // PERSIST en cascada guarda los detalles dentro de esta misma transacción.
            manager.persist(factura);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo guardar factura.", e);
        } finally {
            manager.close();
        }
    }

    @Override
    public void borrarFactura(Factura factura) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            if (factura == null || factura.getId() == null) {
                throw new IllegalArgumentException("Debe seleccionar un registro existente.");
            }
            Factura existente = manager.find(Factura.class, factura.getId());
            if (existente == null) {
                throw new IllegalArgumentException("No se encontró el registro.");
            }
            existente.setEstado(false);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo borrar factura.", e);
        } finally {
            manager.close();
        }
    }

    @Override
    public Factura buscarFactura(Long id) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            List<Factura> facturas = manager.createQuery(
                    "SELECT DISTINCT f FROM Factura f JOIN FETCH f.cliente LEFT JOIN FETCH f.detalles d LEFT JOIN FETCH d.producto WHERE f.id = :id", Factura.class)
                    .setParameter("id", id).getResultList();
            return facturas.isEmpty() ? null : facturas.get(0);
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturas() {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            return manager.createQuery("SELECT DISTINCT f FROM Factura f JOIN FETCH f.cliente LEFT JOIN FETCH f.detalles d LEFT JOIN FETCH d.producto WHERE f.estado = true", Factura.class)
                    .getResultList();
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturas900() {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            return manager.createQuery("SELECT DISTINCT f FROM Factura f JOIN FETCH f.cliente LEFT JOIN FETCH f.detalles d LEFT JOIN FETCH d.producto WHERE f.estado = true AND f.total > 900000", Factura.class)
                    .getResultList();
        } finally {
            manager.close();
        }
    }

}
