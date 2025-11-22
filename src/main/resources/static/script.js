
        let inventarioGlobal = []; 
        let carrito = []; 

        //cargar la pagina
        document.addEventListener("DOMContentLoaded", () => {
            cargarProductosDesdeJava();
        });

        // 1. Traer productos del Backend
        async function cargarProductosDesdeJava() {
            try {
                const response = await fetch('/lista'); 
                const productos = await response.json();
                
                inventarioGlobal = productos;

                dibujarCatalogo(productos);
            } catch (error) {
                console.error("Error:", error);
                document.getElementById('contenedor-productos').innerHTML = 
                    "<p>Error conectando con el servidor.</p>";
            }
        }

        //catálogo
        function dibujarCatalogo(productos) {
            const contenedor = document.getElementById('contenedor-productos');
            const avisoVacio = document.getElementById('aviso-vacio');
            contenedor.innerHTML = ''; 

            if(productos.length === 0) {
                if(avisoVacio) avisoVacio.style.display = 'block';
                return;
            }
            if(avisoVacio) avisoVacio.style.display = 'none';

            productos.forEach(prod => {
                const descripcion = prod.descri ? prod.descri : "Descripción no disponible.";
                
                const htmlTarjeta = `
                    <article class="card">
                        <div class="imagen-producto"><i class='bx bx-image'></i></div>
                        <div class="info-producto">
                            <h3>${prod.nombre}</h3>
                            <p>${descripcion}</p>
                        </div>
                        <div class="card-footer">
                            <span class="precio">$${prod.precio}</span>
                            <button class="btn-add" onclick="agregarAlCarrito(${prod.id})">
                                <i class='bx bx-plus'></i>
                            </button>
                        </div>
                    </article>
                `;
                contenedor.innerHTML += htmlTarjeta;
            });
        }

        //AGREGAR al carrito
        function agregarAlCarrito(idProducto) {
            const productoEncontrado = inventarioGlobal.find(p => p.id === idProducto);
            
            if (!productoEncontrado) return;
            const itemEnCarrito = carrito.find(item => item.id === idProducto);

            if (itemEnCarrito) {
                itemEnCarrito.cantidad++;
            } else {
                carrito.push({ ...productoEncontrado, cantidad: 1 });
            }

            actualizarContadorHeader();
            renderizarVistaCarrito(); 
        }

        //lista del Carrito 
        function renderizarVistaCarrito() {
            const contenedorItems = document.getElementById('lista-carrito-items');
            contenedorItems.innerHTML = '';

            let subtotalAcumulado = 0;

            carrito.forEach((item, index) => {
                // Calculamos totales
                const totalLinea = item.precio * item.cantidad;
                subtotalAcumulado += totalLinea;

                const htmlItem = `
                    <div class="cart-item">
                        <div class="cart-item-info">
                            <div class="thumb"></div> 
                            <div>
                                <h4>${item.nombre}</h4>
                                <span class="precio" style="font-size:1rem">$${item.precio} c/u</span>
                            </div>
                        </div>
                        
                        <div class="qty-selector">
                            <button class="qty-btn" onclick="cambiarCantidad(${index}, -1)">-</button>
                            <span>${item.cantidad}</span>
                            <button class="qty-btn" onclick="cambiarCantidad(${index}, 1)">+</button>
                        </div>
                        
                        <button class="btn-delete" onclick="eliminarDelCarrito(${index})">
                            <i class='bx bx-trash'></i>
                        </button>
                    </div>
                `;
                contenedorItems.innerHTML += htmlItem;
            });

            //(Basado en Factura.java: 16% IVA)
            const iva = subtotalAcumulado * 0.16;
            const total = subtotalAcumulado + iva;

            // Actualizar textos de dinero
            document.getElementById('cart-subtotal').innerText = "$" + subtotalAcumulado.toFixed(2);
            document.getElementById('cart-iva').innerText = "$" + iva.toFixed(2);
            document.getElementById('cart-total').innerText = "$" + total.toFixed(2);
        }

        // Funciones auxiliares del carrito
        function cambiarCantidad(index, delta) {
            carrito[index].cantidad += delta;
            if (carrito[index].cantidad <= 0) {
                eliminarDelCarrito(index);
            } else {
                renderizarVistaCarrito();
                actualizarContadorHeader();
            }
        }

        function eliminarDelCarrito(index) {
            carrito.splice(index, 1); // Quitar del array
            renderizarVistaCarrito();
            actualizarContadorHeader();
        }

        function actualizarContadorHeader() {
            const totalItems = carrito.reduce((acc, item) => acc + item.cantidad, 0);
            const badge = document.getElementById('contador-carrito');
            badge.innerText = totalItems;
            
            // Animacion
            badge.style.transform = "scale(1.5)";
            setTimeout(() => badge.style.transform = "scale(1)", 200);
        }

        // --- NAVEGACIoN ---
        function toggleCarrito() {
            const catalogo = document.getElementById('vista-catalogo');
            const carritoView = document.getElementById('vista-carrito');
            
            if (catalogo.style.display !== 'none') {
                catalogo.style.display = 'none';
                carritoView.style.display = 'block';
            } else {
                mostrarTienda();
            }
        }

        function mostrarTienda() {
            document.getElementById('vista-catalogo').style.display = 'block';
            document.getElementById('vista-carrito').style.display = 'none';
        }

        async function generarDatos() {
            await fetch('/addProd');
            location.reload();
        }