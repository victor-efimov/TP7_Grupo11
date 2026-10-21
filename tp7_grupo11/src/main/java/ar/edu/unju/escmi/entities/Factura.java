package ar.edu.unju.escmi.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "facturas")

public class Factura {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private LocalDate fecha = LocalDate.now();
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	private String domicilio;
	private double total;
	private boolean estado = true;
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "factura_id")
	private List<DetalleFactura> detalles = new ArrayList<>();

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



	@Override
	public String toString() {
		return "Factura [id=" + id + ", fecha=" + fecha + ", cliente=" + cliente + ", domicilio=" + domicilio
				+ ", total=" + total + ", estado=" + estado + ", detalles=" + detalles + "]";
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

	public double calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La factura debe tener al menos un detalle.");
        }
        double suma = 0;
        for (DetalleFactura detalle : detalles) {
            if (detalle == null || !Double.isFinite(detalle.getSubtotal()) || detalle.getSubtotal() <= 0) {
                throw new IllegalArgumentException("Los subtotales deben ser positivos y finitos.");
            }
            suma += detalle.getSubtotal();
        }
        if (!Double.isFinite(suma)) {
            throw new IllegalArgumentException("El total excede el valor permitido.");
        }
        total = suma;
        return total;
    }
}

