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
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            if (idProducto == null) {
                throw new IllegalArgumentException("Debe indicar un producto existente.");
            }
            if (!Double.isFinite(nuevoPrecio) || nuevoPrecio <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a cero.");
            }
            Producto existente = manager.find(Producto.class, idProducto);
            if (existente == null || !existente.isEstado()) {
                throw new IllegalArgumentException("El producto no existe o está dado de baja.");
            }
            existente.setPrecioUnitario(nuevoPrecio);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo modificar el precio.", e);
        } finally {
            manager.close();
        }
    }
}