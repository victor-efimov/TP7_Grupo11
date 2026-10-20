package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ar.edu.unju.escmi.entities.Factura;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IFacturaDao;

public class FacturaDaoImp implements IFacturaDao {
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

@Override
public void guardarFactura(Factura factura) {
	try {
		manager.getTransaction().begin();
		manager.persist(factura);
		manager.getTransaction().commit();
	}catch(Exception e) {
		if (manager.getTransaction()!=null) {
			manager.getTransaction().rollback();
		}
		System.out.println("No se pudo guardar la factura");
	} finally {
		manager.close();
	}
}
@Override
public void borrarFactura(Factura factura) {
	try {
		manager.getTransaction().begin();
		factura.setEstado(false);
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
public Factura buscarFactura(Long id) {
return manager.find(Factura.class, id);
}
@Override
public List <Factura> obtenerFacturas(){
	TypedQuery<Factura> query = manager.createQuery("SELECT f FROM Factura f AND f.estado = true", Factura.class);
	List<Factura> facturas = query.getResultList();
	return facturas;
}

@Override
public List <Factura> obtenerFacturas900(){
	TypedQuery<Factura> query = manager.createQuery("SELECT f FROM Factura f WHERE f.total > 900000 AND f.estado = true", Factura.class);
	List<Factura> facturas = query.getResultList();
	return facturas;
}
}