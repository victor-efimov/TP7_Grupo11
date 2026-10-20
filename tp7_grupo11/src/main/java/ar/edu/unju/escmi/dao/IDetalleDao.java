package ar.edu.unju.escmi.dao;

import java.util.List;
import ar.edu.unju.escmi.entities.DetalleFactura;

public interface IDetalleDao {
	
	public void guardarDetalle(DetalleFactura factura);
	public void borrarDetalle(DetalleFactura factura);
	public List <DetalleFactura> obtenerDetalles();
	
}