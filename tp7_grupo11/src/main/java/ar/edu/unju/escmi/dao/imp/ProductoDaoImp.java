package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ar.edu.unju.escmi.entities.Producto;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IProductoDao;

public class ProductoDaoImp implements IProductoDao {
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

@Override
public void guardarProducto(Producto producto) {
	try {
		manager.getTransaction().begin();
		manager.persist(producto);
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
public void borrarProducto(Producto producto) {
	try {
		manager.getTransaction().begin();
		manager.remove(producto);
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
public List <Producto> obtenerProductos(){
	TypedQuery<Producto> query = manager.createQuery("SELECT e FROM Factura e", Producto.class);
	List<Producto> productos = query.getResultList();
	return productos;
}
}