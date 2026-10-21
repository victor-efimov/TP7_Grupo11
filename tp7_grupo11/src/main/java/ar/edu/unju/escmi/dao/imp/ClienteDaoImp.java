package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ar.edu.unju.escmi.entities.Cliente;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import java.util.Scanner;

public class ClienteDaoImp implements IClienteDao {
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
	Scanner sc = new Scanner(System.in);
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
		//manager.close();
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
		//manager.close();
	}
}
@Override
public List <Cliente> obtenerClientes(){
	TypedQuery<Cliente> query = manager.createQuery("SELECT c FROM Cliente c WHERE c.estado = true", Cliente.class);
	List<Cliente> clientes = query.getResultList();
	return clientes;
}
@Override
public void modificarCliente(Long mod) {
	int op;
	Cliente buscado = null;
	buscado = manager.find(Cliente.class, mod);
	if (buscado == null) {
		System.out.println("No se encontro el cliente");
	}
	else {
		System.out.println("Que dato desea modificar del cliente: " + buscado.getNombre() + " " + buscado.getApellido());
		System.out.println("1: Nombre");
		System.out.println("2: Apellido");
		System.out.println("3: DNI");
		System.out.println("4: Domicilio");
		op=leerNumero(sc);
		try {
		manager.getTransaction().begin();
		switch (op) {
		case 1:
			System.out.println("Ingrese nuevo Nombre:");
			buscado.setNombre(sc.nextLine());
		break;
		case 2:
            System.out.println("Ingrese nuevo Apellido:");
            buscado.setApellido(sc.nextLine());
            break;
        case 3:
            System.out.println("Ingrese nuevo DNI:");
            buscado.setDni(leerNumero(sc));
            break;
        case 4:
            System.out.println("Ingrese nuevo Domicilio:");
            buscado.setDomicilio(sc.nextLine());
            break;
        default:
            System.out.println("Opcion invalida");
            manager.getTransaction().rollback();
            return; 
		}
		 manager.merge(buscado);
         manager.getTransaction().commit();
         System.out.println("Cliente modificado correctamente");
		}catch(Exception e) {
			if (manager.getTransaction()!=null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo modificar el cliente");
		}finally {
			sc.close();
			//manager.close();
		}
	}
}
private static int leerNumero(Scanner sc) {
    String entrada = sc.nextLine();
    try {
        return  Integer.parseInt(entrada);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Debe ingresar un número entero válido.");
    }
}
}