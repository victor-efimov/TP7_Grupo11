package ar.edu.unju.escmi.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "facturas")

public class Factura {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private LocalDate fecha;
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	private String domicilio;
	private double total;
	private boolean estado;
	@OneToMany
	@JoinColumn(name = "factura_id")
	private List<DetalleFactura> detalles;
	
	public Factura() {}
	
	public Factura(Long id, LocalDate fecha, Cliente cliente, String domicilio, double total, boolean estado,
			List<DetalleFactura> detalles) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cliente = cliente;
		this.domicilio = domicilio;
		this.total = total;
		this.estado = estado;
		this.detalles = detalles;
	}



	public Long getId() {
		return id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public List<DetalleFactura> getDetalles() { return detalles; }
	public void setDetalles(List<DetalleFactura> detalles) {
		this.detalles = detalles; }

	public int calcularTotal() {
		return 0;
	}
}

