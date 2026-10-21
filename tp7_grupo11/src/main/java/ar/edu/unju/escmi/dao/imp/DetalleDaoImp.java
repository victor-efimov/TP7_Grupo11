package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ar.edu.unju.escmi.entities.DetalleFactura;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IDetalleDao;

public class DetalleDaoImp implements IDetalleDao {
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

@Override
public void guardarDetalle(DetalleFactura detalle) {
	try {
		manager.getTransaction().begin();
		manager.persist(detalle);
		manager.getTransaction().commit();
	}catch(Exception e) {
		if (manager.getTransaction()!=null) {
			manager.getTransaction().rollback();
		}
		System.out.println("No se pudo guardar el detalle");
	} finally {
		//manager.close();
	}
}
@Override
public void borrarDetalle(DetalleFactura detalle) {
	try {
		manager.getTransaction().begin();
		manager.remove(detalle);
		manager.getTransaction().commit();
	}catch (Exception e) {
		if (manager.getTransaction()!=null) {
			manager.getTransaction().rollback();
		}
		System.out.println("No se pudo borrar la factura");
	}finally {
		//manager.close();
	}
}
@Override
public List <DetalleFactura> obtenerDetalles(){
	TypedQuery<DetalleFactura> query = manager.createQuery("SELECT e FROM DetalleFactura e", DetalleFactura.class);
	List<DetalleFactura> detalles = query.getResultList();
	return detalles;
}
}