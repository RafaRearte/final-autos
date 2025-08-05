[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/nnMVzae0)
# Sistema de Gestión Automotriz

Una aplicación web completa para gestionar el inventario de piezas automotrices y el sistema de facturación, desarrollada con Spring Boot y una interfaz web moderna y modular.

## 🚀 Características

### 📦 Gestión de Piezas
- **CRUD completo**: Crear, leer, actualizar y eliminar piezas
- **Búsqueda avanzada**: Buscar por nombre, código, descripción, marca, categoría
- **Filtros inteligentes**: Filtrar por categoría, marca, stock bajo, rango de precios
- **Control de stock**: Gestión automática del inventario
- **Estadísticas**: Métricas en tiempo real del inventario

### 🧾 Sistema de Facturación
- **Facturas profesionales**: Creación con múltiples items
- **Cálculo automático**: Subtotal, IVA (21%) y total
- **Gestión de clientes**: Información completa del cliente
- **Estados de factura**: Pendiente, Pagada, Anulada, Vencida
- **Control de stock**: Descuento automático al crear facturas
- **Números únicos**: Generación automática de números de factura
- **Búsqueda y filtros**: Por cliente, estado, fecha, monto
- **Estadísticas**: Reportes de ventas y facturación


## 🛠️ Tecnologías Utilizadas
- **Backend**: Spring Boot 3.5.4, Spring Data JPA, H2 Database
- **Herramientas**: Maven, Lombok, SpringDoc OpenAPI
- **Base de Datos**: H2 (en memoria)

## 📋 Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior


### 3. Acceder a la aplicación
- **API Swagger**: http://localhost:8080/swagger-ui.html
- **Consola H2**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: `password`

## 📊 Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/tup/examen/
│   │   ├── config/
│   │   │   └── DataInitializer.java          # Carga datos de ejemplo
│   │   ├── controller/
│   │   │   ├── PiezaController.java          # Controlador REST de piezas
│   │   │   └── FacturaController.java        # Controlador REST de facturas
│   │   ├── dto/
│   │   │   ├── PiezaDTO.java                 # DTO de piezas
│   │   │   ├── FacturaDTO.java               # DTO de facturas
│   │   │   └── ItemFacturaDTO.java           # DTO de items de factura
│   │   ├── model/
│   │   │   ├── Pieza.java                    # Entidad de piezas
│   │   │   ├── Factura.java                  # Entidad de facturas
│   │   │   └── ItemFactura.java              # Entidad de items de factura
│   │   ├── repository/
│   │   │   ├── PiezaRepository.java          # Repositorio de piezas
│   │   │   └── FacturaRepository.java        # Repositorio de facturas
│   │   ├── service/
│   │   │   ├── PiezaService.java             # Servicio de piezas
│   │   │   └── FacturaService.java           # Servicio de facturación
│   │   └── ExamenApplication.java            # Clase principal
│   └── resources/
│       ├── static/
│       │   ├── index.html                    # Dashboard principal
│       │   ├── piezas.html                   # Gestión de piezas
│       │   └── facturacion.html              # Sistema de facturación
│       └── application.properties            # Configuración
└── test/
    └── java/com/tup/examen/
        ├── service/
        │   ├── PiezaServiceTest.java         # Tests unitarios de piezas
        │   └── FacturaServiceTest.java       # Tests unitarios de facturación
        └── integration/
            ├── PiezaIntegrationTest.java     # Tests de integración de piezas
            └── FacturaIntegrationTest.java   # Tests de integración de facturación
```

## 🔧 Configuración

### Base de Datos
La aplicación utiliza H2 en memoria por defecto. Los datos se reinician cada vez que se inicia la aplicación.

### Datos de Ejemplo
Al iniciar la aplicación, se cargan automáticamente 8 piezas de ejemplo:
- Filtro de Aceite (Bosch)
- Bujía de Encendido (NGK)
- Pastilla de Freno (Brembo)
- Disco de Freno (Brembo)
- Amortiguador (Monroe)
- Batería (Varta)
- Espejo Retrovisor (OEM)
- Aceite de Transmisión (Castrol)

## 📡 API REST

### Endpoints de Piezas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/piezas` | Obtener todas las piezas |
| GET | `/api/piezas/{id}` | Obtener pieza por ID |
| GET | `/api/piezas/codigo/{codigo}` | Obtener pieza por código |
| POST | `/api/piezas` | Crear nueva pieza |
| PUT | `/api/piezas/{id}` | Actualizar pieza |
| DELETE | `/api/piezas/{id}` | Eliminar pieza |
| PUT | `/api/piezas/{id}/stock` | Actualizar stock |

### Endpoints de Búsqueda de Piezas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/piezas/buscar/nombre?nombre=...` | Buscar por nombre |
| GET | `/api/piezas/buscar/marca?marca=...` | Buscar por marca |
| GET | `/api/piezas/buscar/categoria?categoria=...` | Buscar por categoría |
| GET | `/api/piezas/buscar/stock-bajo?stockMinimo=...` | Buscar stock bajo |
| GET | `/api/piezas/buscar/precio?precioMin=...&precioMax=...` | Buscar por rango de precio |
| GET | `/api/piezas/buscar/termino?termino=...` | Búsqueda general |

### Endpoints de Facturación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/facturas` | Obtener todas las facturas |
| GET | `/api/facturas/{id}` | Obtener factura por ID |
| GET | `/api/facturas/numero/{numeroFactura}` | Obtener factura por número |
| POST | `/api/facturas` | Crear nueva factura |
| PUT | `/api/facturas/{id}/estado` | Actualizar estado de factura |
| PUT | `/api/facturas/{id}/anular` | Anular factura |
| GET | `/api/facturas/generar-numero` | Generar número de factura |

### Endpoints de Búsqueda de Facturas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/facturas/cliente?nombre=...` | Buscar por cliente |
| GET | `/api/facturas/estado/{estado}` | Buscar por estado |
| GET | `/api/facturas/periodo?fechaInicio=...&fechaFin=...` | Buscar por período |
| GET | `/api/facturas/buscar?termino=...` | Búsqueda general |
| GET | `/api/facturas/monto-minimo?montoMinimo=...` | Buscar por monto mínimo |
| GET | `/api/facturas/estadisticas/periodo` | Estadísticas por período |

### Ejemplo de Uso de la API

#### Crear una nueva pieza
```bash
curl -X POST http://localhost:8080/api/piezas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Filtro de Aire",
    "codigo": "FIL009",
    "descripcion": "Filtro de aire de alto rendimiento",
    "precio": 30.00,
    "stock": 25,
    "marca": "K&N",
    "modelo": "Performance",
    "categoria": "Motor"
  }'
```

#### Crear una nueva factura
```bash
curl -X POST http://localhost:8080/api/facturas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroFactura": "FAC-20241201-000001",
    "clienteNombre": "Juan Pérez",
    "clienteDocumento": "12345678",
    "clienteEmail": "juan@email.com",
    "clienteTelefono": "123456789",
    "items": [
      {
        "piezaId": 1,
        "cantidad": 2,
        "descripcion": "Filtro de aceite Bosch"
      }
    ]
  }'
```

#### Marcar factura como pagada
```bash
curl -X PUT "http://localhost:8080/api/facturas/1/estado?nuevoEstado=PAGADA"
```

## 📋 Documentación Detallada de Endpoints

### 🔧 Endpoints de Gestión de Piezas

#### **GET** `/api/piezas`
**Funcionalidad**: Obtiene todas las piezas del inventario
**Respuesta esperada**:
```json
[
  {
    "id": 1,
    "nombre": "Filtro de Aceite",
    "codigo": "FIL001",
    "descripcion": "Filtro de aceite de motor",
    "precio": 25.00,
    "stock": 50,
    "marca": "Bosch",
    "modelo": "Premium",
    "categoria": "Motor",
    "fechaRegistro": "2024-01-01T10:00:00",
    "fechaActualizacion": "2024-01-01T10:00:00"
  }
]
```

#### **GET** `/api/piezas/{id}`
**Funcionalidad**: Obtiene una pieza específica por su ID
**Parámetros**: `id` (Long) - ID único de la pieza
**Respuesta exitosa (200)**:
```json
{
  "id": 1,
  "nombre": "Filtro de Aceite",
  "codigo": "FIL001",
  "descripcion": "Filtro de aceite de motor",
  "precio": 25.00,
  "stock": 50,
  "marca": "Bosch",
  "modelo": "Premium",
  "categoria": "Motor"
}
```
**Respuesta error (404)**: Pieza no encontrada

#### **GET** `/api/piezas/codigo/{codigo}`
**Funcionalidad**: Busca una pieza por su código único
**Parámetros**: `codigo` (String) - Código de la pieza
**Respuesta exitosa (200)**:
```json
{
  "id": 1,
  "nombre": "Filtro de Aceite",
  "codigo": "FIL001",
  "descripcion": "Filtro de aceite de motor",
  "precio": 25.00,
  "stock": 50,
  "marca": "Bosch",
  "categoria": "Motor"
}
```

#### **POST** `/api/piezas`
**Funcionalidad**: Crea una nueva pieza en el inventario
**Body requerido**:
```json
{
  "nombre": "Nueva Pieza",
  "codigo": "NUE001",
  "descripcion": "Descripción de la pieza",
  "precio": 30.00,
  "stock": 25,
  "marca": "Bosch",
  "modelo": "Standard",
  "categoria": "Motor"
}
```
**Respuesta exitosa (201)**:
```json
{
  "id": 9,
  "nombre": "Nueva Pieza",
  "codigo": "NUE001",
  "descripcion": "Descripción de la pieza",
  "precio": 30.00,
  "stock": 25,
  "marca": "Bosch",
  "modelo": "Standard",
  "categoria": "Motor",
  "fechaRegistro": "2024-01-01T10:00:00"
}
```
**Respuesta error (400)**: Código duplicado o datos inválidos

#### **PUT** `/api/piezas/{id}`
**Funcionalidad**: Actualiza una pieza existente
**Parámetros**: `id` (Long) - ID de la pieza a actualizar
**Body requerido**: Mismo formato que POST
**Respuesta exitosa (200)**: Pieza actualizada
**Respuesta error (404)**: Pieza no encontrada

#### **PATCH** `/api/piezas/{id}/stock`
**Funcionalidad**: Actualiza únicamente el stock de una pieza
**Parámetros**: 
- `id` (Long) - ID de la pieza
- `nuevoStock` (Integer) - Nuevo valor de stock
**Respuesta exitosa (200)**: Stock actualizado
**Respuesta error (404)**: Pieza no encontrada

#### **DELETE** `/api/piezas/{id}`
**Funcionalidad**: Elimina permanentemente una pieza
**Parámetros**: `id` (Long) - ID de la pieza a eliminar
**Respuesta exitosa (204)**: Sin contenido
**Respuesta error (404)**: Pieza no encontrada

### 🔍 Endpoints de Búsqueda de Piezas

#### **GET** `/api/piezas/buscar/nombre?nombre={nombre}`
**Funcionalidad**: Busca piezas por nombre (búsqueda parcial, case-insensitive)
**Parámetros**: `nombre` (String) - Nombre a buscar
**Respuesta**:
```json
[
  {
    "id": 1,
    "nombre": "Filtro de Aceite",
    "codigo": "FIL001",
    "precio": 25.00,
    "stock": 50,
    "marca": "Bosch",
    "categoria": "Motor"
  }
]
```

#### **GET** `/api/piezas/buscar/marca?marca={marca}`
**Funcionalidad**: Busca piezas por marca específica
**Parámetros**: `marca` (String) - Marca a buscar
**Respuesta**: Lista de piezas de la marca especificada

#### **GET** `/api/piezas/buscar/categoria?categoria={categoria}`
**Funcionalidad**: Busca piezas por categoría
**Parámetros**: `categoria` (String) - Categoría: Motor, Frenos, Suspensión, Eléctrico, Carrocería, Transmisión
**Respuesta**: Lista de piezas de la categoría especificada

#### **GET** `/api/piezas/buscar/stock-bajo?stockMinimo={stockMinimo}`
**Funcionalidad**: Busca piezas con stock menor al especificado
**Parámetros**: `stockMinimo` (Integer) - Stock mínimo para considerar como "bajo"
**Respuesta**: Lista de piezas con stock bajo

#### **GET** `/api/piezas/buscar/precio?precioMin={min}&precioMax={max}`
**Funcionalidad**: Busca piezas dentro de un rango de precios
**Parámetros**: 
- `precioMin` (Double) - Precio mínimo
- `precioMax` (Double) - Precio máximo
**Respuesta**: Lista de piezas dentro del rango de precios

#### **GET** `/api/piezas/buscar/termino?termino={termino}`
**Funcionalidad**: Búsqueda general en nombre, descripción y código
**Parámetros**: `termino` (String) - Término de búsqueda
**Respuesta**: Lista de piezas que coinciden con el término

### 🧾 Endpoints de Gestión de Facturas

#### **GET** `/api/facturas`
**Funcionalidad**: Obtiene todas las facturas del sistema
**Respuesta esperada**:
```json
[
  {
    "id": 1,
    "numeroFactura": "FAC-20241201-000001",
    "clienteNombre": "Juan Pérez",
    "clienteDocumento": "12345678",
    "clienteEmail": "juan@email.com",
    "clienteTelefono": "123456789",
    "subtotal": 150.00,
    "impuesto": 31.50,
    "total": 181.50,
    "estado": "PENDIENTE",
    "fechaCreacion": "2024-01-01T10:00:00",
    "fechaPago": null,
    "items": [
      {
        "id": 1,
        "piezaId": 1,
        "piezaNombre": "Filtro de Aceite",
        "piezaCodigo": "FIL001",
        "cantidad": 2,
        "precioUnitario": 75.00,
        "subtotal": 150.00,
        "descripcion": "Filtro de aceite Bosch"
      }
    ]
  }
]
```

#### **GET** `/api/facturas/{id}`
**Funcionalidad**: Obtiene una factura específica por su ID
**Parámetros**: `id` (Long) - ID único de la factura
**Respuesta exitosa (200)**: Factura completa con items
**Respuesta error (404)**: Factura no encontrada

#### **GET** `/api/facturas/numero/{numeroFactura}`
**Funcionalidad**: Busca una factura por su número único
**Parámetros**: `numeroFactura` (String) - Número de factura
**Respuesta exitosa (200)**: Factura encontrada
**Respuesta error (404)**: Factura no encontrada

#### **POST** `/api/facturas`
**Funcionalidad**: Crea una nueva factura y descuenta stock automáticamente
**Body requerido**:
```json
{
  "numeroFactura": "FAC-20241201-000001",
  "clienteNombre": "Juan Pérez",
  "clienteDocumento": "12345678",
  "clienteEmail": "juan@email.com",
  "clienteTelefono": "123456789",
  "items": [
    {
      "piezaId": 1,
      "cantidad": 2,
      "descripcion": "Filtro de aceite Bosch"
    }
  ]
}
```
**Respuesta exitosa (201)**:
```json
{
  "id": 1,
  "numeroFactura": "FAC-20241201-000001",
  "clienteNombre": "Juan Pérez",
  "subtotal": 150.00,
  "impuesto": 31.50,
  "total": 181.50,
  "estado": "PENDIENTE",
  "fechaCreacion": "2024-01-01T10:00:00",
  "items": [...]
}
```
**Respuesta error (400)**: Stock insuficiente o datos inválidos

#### **PUT** `/api/facturas/{id}/estado`
**Funcionalidad**: Actualiza el estado de una factura
**Parámetros**: 
- `id` (Long) - ID de la factura
- `nuevoEstado` (String) - Nuevo estado: PENDIENTE, PAGADA, ANULADA, VENCIDA
**Respuesta exitosa (200)**: Factura con estado actualizado
**Respuesta error (400)**: Estado inválido
**Respuesta error (404)**: Factura no encontrada

#### **PUT** `/api/facturas/{id}/anular`
**Funcionalidad**: Anula una factura y restaura el stock de las piezas
**Parámetros**: `id` (Long) - ID de la factura a anular
**Respuesta exitosa (200)**: Factura anulada
**Respuesta error (400)**: No se puede anular una factura pagada
**Respuesta error (404)**: Factura no encontrada

#### **GET** `/api/facturas/generar-numero`
**Funcionalidad**: Genera automáticamente un número único de factura
**Respuesta exitosa (200)**:
```
FAC-20241201-000001
```

### 🔍 Endpoints de Búsqueda de Facturas

#### **GET** `/api/facturas/cliente?nombre={nombre}`
**Funcionalidad**: Busca facturas por nombre del cliente
**Parámetros**: `nombre` (String) - Nombre del cliente
**Respuesta**: Lista de facturas del cliente

#### **GET** `/api/facturas/estado/{estado}`
**Funcionalidad**: Busca facturas por estado
**Parámetros**: `estado` (String) - Estado: PENDIENTE, PAGADA, ANULADA, VENCIDA
**Respuesta**: Lista de facturas con el estado especificado

#### **GET** `/api/facturas/periodo?fechaInicio={fecha}&fechaFin={fecha}`
**Funcionalidad**: Busca facturas creadas en un período específico
**Parámetros**: 
- `fechaInicio` (DateTime) - Fecha de inicio (ISO format)
- `fechaFin` (DateTime) - Fecha de fin (ISO format)
**Respuesta**: Lista de facturas del período

#### **GET** `/api/facturas/buscar?termino={termino}`
**Funcionalidad**: Búsqueda general en nombre del cliente y número de factura
**Parámetros**: `termino` (String) - Término de búsqueda
**Respuesta**: Lista de facturas que coinciden

#### **GET** `/api/facturas/monto-minimo?montoMinimo={monto}`
**Funcionalidad**: Busca facturas con total mayor o igual al especificado
**Parámetros**: `montoMinimo` (Double) - Monto mínimo
**Respuesta**: Lista de facturas que cumplen el criterio

### 📊 Endpoints de Estadísticas

#### **GET** `/api/facturas/estadisticas/periodo?fechaInicio={fecha}&fechaFin={fecha}`
**Funcionalidad**: Obtiene estadísticas de facturación para un período
**Parámetros**: 
- `fechaInicio` (DateTime) - Fecha de inicio
- `fechaFin` (DateTime) - Fecha de fin
**Respuesta exitosa (200)**:
```json
{
  "cantidadFacturas": 10,
  "totalFacturasPagadas": 1500.0
}
```

## 🧾 Funcionalidades del Sistema de Facturación

### Creación de Facturas
- **Información del cliente**: Nombre, documento, email, teléfono
- **Items dinámicos**: Selección de piezas con stock disponible
- **Cálculo automático**: Precios, cantidades, subtotales
- **Validaciones**: Stock disponible, campos obligatorios
- **Número único**: Generación automática de números de factura

### Estados de Factura
- **PENDIENTE**: Factura creada, pendiente de pago
- **PAGADA**: Factura pagada con fecha de pago
- **ANULADA**: Factura cancelada (restaura stock)
- **VENCIDA**: Factura vencida por tiempo

### Control de Stock
- **Descuento automático**: Al crear factura se descuenta stock
- **Restauración**: Al anular factura se restaura stock
- **Validación**: No permite crear facturas con stock insuficiente

### Cálculo de Impuestos
- **IVA 21%**: Cálculo del iva sobre subtotal
- **Redondeo**: Aplicación de reglas de redondeo
- **Desglose**: Subtotal, impuesto y total claramente separados

## 🔍 Categorías de Piezas

- **Motor**: Filtros, bujías, aceites, etc.
- **Frenos**: Pastillas, discos, líquidos de freno
- **Suspensión**: Amortiguadores, resortes, bujes
- **Eléctrico**: Baterías, alternadores, luces
- **Carrocería**: Espejos, parachoques, molduras
- **Transmisión**: Aceites, filtros, embragues

## 🚨 Validaciones

### Piezas
- **Código único**: No se permiten códigos duplicados
- **Campos obligatorios**: Nombre, código, precio y stock son requeridos
- **Tipos de datos**: Validación de tipos numéricos y fechas
- **Stock**: No permite valores negativos

### Facturas
- **Número único**: No se permiten números de factura duplicados
- **Stock disponible**: Validación de stock antes de crear factura
- **Items requeridos**: Al menos un item es obligatorio
- **Cliente obligatorio**: Nombre del cliente es requerido
- **Estados válidos**: Solo estados predefinidos permitidos

## 🧪 Testing

### Tests Unitarios
- **PiezaServiceTest**
- **FacturaServiceTest**


### Cobertura
- **Cobertura**: mínimo 80% de métodos principales

## LAB3
- Desarrollar los TODOS -- 60%
- Completar Test unitarios -- 40%


## LAB4
- Desarrollar los TODOS -- 50%
- Completar Test unitarios -- 40%
- Generar dockerfile y compose -- 10%