package ar.edu.unju.escmi.dao;

import java.util.List;
import ar.edu.unju.escmi.entities.Producto;

public interface IProductoDao {
	
	public void guardarProducto(Producto productos);
	public void borrarProducto(Producto producto);
	public List <Producto> obtenerProductos();
	public void modificarPrecio(double precio);
}