package org.cibertec.edu.pe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cibertec.edu.pe.model.Detalle;
import org.cibertec.edu.pe.model.carrito;
import org.cibertec.edu.pe.model.Producto;
import org.cibertec.edu.pe.model.Venta;
import org.cibertec.edu.pe.repository.IDetalleRepository;
import org.cibertec.edu.pe.repository.IProductoRepository;
import org.cibertec.edu.pe.repository.IVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"carrito", "total","lista"})
public class ProductoController {
	@Autowired
	private IProductoRepository productoRepository;
	@Autowired
	private IVentaRepository ventaRepository;
	@Autowired
	private IDetalleRepository detalleRepository;
	
	@ModelAttribute("lista")
	public List<carrito> getLista(){
		return new ArrayList<carrito>();
	}
	
	@GetMapping("/index")
	public String listado(Model model) {
		List<Producto> lista = new ArrayList<>();
		lista = productoRepository.findAll();
		model.addAttribute("productos", lista);
		return "index";
	}
	
	@GetMapping("/agregar/{idProducto}")
	public String agregar(Model model, @PathVariable(name = "idProducto", required = true) int idProducto) {
		// Codigo para agregar un producto
	    Optional<Producto> optionalProducto = productoRepository.findById(idProducto);
	    // Verificar si el producto está presente
	    if (optionalProducto.isPresent()) {
	        Producto producto = optionalProducto.get();

	        // Obtener el carrito de la sesión
	        List<Detalle> carrito = (List<Detalle>) model.getAttribute("carrito");

	        // Si el carrito aún no ha sido inicializado, inicializarlo
	        if (carrito == null) {
	            carrito = new ArrayList<>();
	        }

	        // Verificar si el producto ya está en el carrito
	        boolean productoExistente = false;
	        for (Detalle detalle : carrito) {
	            if (detalle.getProducto().getIdProducto() == idProducto) {
	                // Si ya existe, aumentar la cantidad
	                detalle.setCantidad(detalle.getCantidad() + 1);
	                detalle.setSubtotal(detalle.getCantidad() * detalle.getProducto().getPrecio());
	                productoExistente = true;
	                break;
	            }
	        }

	        // Si no existe, agregar uno nuevo al carrito
	        if (!productoExistente) {
	            Detalle nuevoDetalle = new Detalle();
	            nuevoDetalle.setProducto(producto);
	            nuevoDetalle.setCantidad(1);
	            nuevoDetalle.setSubtotal(producto.getPrecio());
	            carrito.add(nuevoDetalle);
	        }

	        // Actualizar el modelo con el carrito modificado
	        model.addAttribute("carrito", carrito);

	        System.out.println("Contenido del carrito: " + carrito);

	    }
		return "redirect:/index";
	}
	
	@GetMapping("/carrito")
	public String carrito() {
		return "carrito";
	}
	
	@GetMapping("/pagar")
	public String pagar(Model model) {
	    // Codigo para pagar
	    return "pagar";
	}

	@PostMapping("/actualizarCarrito")
	public String actualizarCarrito(Model model) {
	    // Codigo para actualizar el carrito
	    return "carrito";
	}
	
	// Inicializacion de variable de la sesion
	@ModelAttribute("carrito")
	public List<Detalle> getCarrito() {
		return new ArrayList<Detalle>();
	}
	
	@ModelAttribute("total")
	public double getTotal() {
		return 0.0;
	}
}