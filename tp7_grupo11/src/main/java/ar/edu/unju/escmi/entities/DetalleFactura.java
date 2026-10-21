package ar.edu.unju.escmi.entities;

import javax.persistence.*;

@Entity
@Table(name = "detalles")

public class DetalleFactura {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
    @JoinColumn(name = "producto_id")
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

	public double calcularSubTotal() {
        if (producto == null || cantidad <= 0 || !Double.isFinite(producto.getPrecioUnitario())
                || producto.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El detalle debe tener producto, cantidad y precio válidos.");
        }
        double calculado = cantidad * producto.getPrecioUnitario();
        if (!Double.isFinite(calculado)) {
            throw new IllegalArgumentException("El subtotal excede el valor permitido.");
        }
        subtotal = calculado;
        return subtotal;
    }
}
