package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.entities.*;

public class ProductoDaoImp implements IProductoDao {
    @Override
    public void guardarProducto(Producto producto) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(producto);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo guardar producto.", e);
        } finally {
            manager.close();
        }
    }

    @Override
    public void borrarProducto(Producto producto) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            if (producto == null || producto.getId() == null) {
                throw new IllegalArgumentException("Debe seleccionar un registro existente.");
            }
            Producto existente = manager.find(Producto.class, producto.getId());
            if (existente == null) {
                throw new IllegalArgumentException("No se encontró el registro.");
            }
            existente.setEstado(false);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo borrar producto.", e);
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Producto> obtenerProductos() {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            return manager.createQuery("SELECT e FROM Producto e WHERE e.estado = true", Producto.class).getResultList();
        } finally {
            manager.close();
        }
    }

    @Override
    public void modificarPrecio(Long idProducto, double nuevoPrecio) {
        // Pendiente: implementar la modificación del precio del producto.
        throw new UnsupportedOperationException("Modificación de precio pendiente de implementación.");
    }
}
