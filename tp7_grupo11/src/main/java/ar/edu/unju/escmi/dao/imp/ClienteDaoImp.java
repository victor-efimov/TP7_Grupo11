package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ar.edu.unju.escmi.entities.Cliente;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;

public class ClienteDaoImp implements IClienteDao {
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

@Override
public void guardarCliente(Cliente cliente) {
	try {
		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.getTransaction().commit();
	}catch(Exception e) {
		if (manager.getTransaction()!=null) {
			manager.getTransaction().rollback();
		}
		System.out.println("No se pudo guardar el detalle");
	} finally {
		manager.close();
	}
}
@Override
public void borrarCliente(Cliente cliente) {
	try {
		manager.getTransaction().begin();
		cliente.setEstado(false);
		manager.getTransaction().commit();
	}catch (Exception e) {
		if (manager.getTransaction()!=null) {
			manager.getTransaction().rollback();
		}
		System.out.println("No se pudo borrar la factura");
	}finally {
		manager.close();
	}
}
@Override
public List <Cliente> obtenerClientes(){
	TypedQuery<Cliente> query = manager.createQuery("SELECT c FROM Cliente c AND c.estado = true", Cliente.class);
	List<Cliente> clientes = query.getResultList();
	return clientes;
}
}