package ar.edu.unju.escmi.entities;

import javax.persistence.*;

@Entity
@Table(name = "detalles")

public class DetalleFactura {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Producto producto;
	private int cantidad;
	private double subtotal;
	
	public DetalleFactura() {};
	
	public DetalleFactura(Long id, Producto producto, int cantidad, double subtotal) {
		super();
		this.id = id;
		this.producto = producto;
		this.cantidad = cantidad;
		this.subtotal = subtotal;
	}

	public Long getId() {
		return id;
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public int calcularSubotal() {
		return 0;
	}
}
