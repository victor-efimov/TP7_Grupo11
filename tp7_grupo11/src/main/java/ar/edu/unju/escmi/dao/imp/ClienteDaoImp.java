package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.entities.*;

public class ClienteDaoImp implements IClienteDao {
    @Override
    public void guardarCliente(Cliente cliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(cliente);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo guardar cliente.", e);
        } finally {
            manager.close();
        }
    }

    @Override
    public void borrarCliente(Cliente cliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            if (cliente == null || cliente.getId() == null) {
                throw new IllegalArgumentException("Debe seleccionar un registro existente.");
            }
            Cliente existente = manager.find(Cliente.class, cliente.getId());
            if (existente == null) {
                throw new IllegalArgumentException("No se encontró el registro.");
            }
            existente.setEstado(false);
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo borrar cliente.", e);
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Cliente> obtenerClientes() {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            return manager.createQuery("SELECT e FROM Cliente e WHERE e.estado = true", Cliente.class).getResultList();
        } finally {
            manager.close();
        }
    }

    @Override
    public void modificarCliente(Cliente cliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
        try {
            manager.getTransaction().begin();
            if (cliente == null || cliente.getId() == null) {
                throw new IllegalArgumentException("Debe seleccionar un cliente existente.");
            }
            Cliente existente = manager.find(Cliente.class, cliente.getId());
            if (existente == null || !existente.isEstado()) {
                throw new IllegalArgumentException("El cliente no existe o está dado de baja.");
            }
            existente.setNombre(cliente.getNombre());
            existente.setApellido(cliente.getApellido());
            existente.setDni(cliente.getDni());
            existente.setDomicilio(cliente.getDomicilio());
            manager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudo modificar el cliente.", e);
        } finally {
            manager.close();
        }
    }

}
