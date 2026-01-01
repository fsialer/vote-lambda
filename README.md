# Vote Lambda - Sistema de Votaciones Serverless

Sistema de votaciones distribuido construido con AWS Lambda, DynamoDB, Redis y SNS. Permite crear pools de votación, votar y obtener resultados en tiempo real mediante WebSocket.

## 🏗️ Arquitectura

```
                    ┌─────────────────┐
                    │ API Gateway     │
                    │ REST API        │
                    └─────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│ VoteCreate      │ │ PoolCreate      │ │ PoolGet         │
│ Function        │ │ Function        │ │ Function        │
│                 │ │                 │ │                 │
│ POST /vote/     │ │ POST /poll/     │ │ GET /poll/      │
│ create          │ │ create          │ │ {pollId}        │
└─────────────────┘ └─────────────────┘ └─────────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────────────┐
│                    DynamoDB - VoteTable                     │
│  PK: POOL_{id} | SK: METADATA      → Pool metadata         │
│  PK: POOL_{id} | SK: OP_{optId}    → Pool options          │
│  PK: POOL_{id} | SK: OP_{optId}_VOTE → Pool votes          │
│  PK: CONNECTION_{id} | SK: POOL_{id} → WebSocket connections│
└─────────────────────────────────────────────────────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│ Redis Cache     │ │ EventBridge     │ │ EventBridge     │
│ (Pool results)  │ │ (Vote events)   │ │ (Vote events)   │
└─────────────────┘ └─────────────────┘ └─────────────────┘
                            │                   │
                            ▼                   ▼
                 ┌─────────────────┐ ┌─────────────────┐
                 │ SNS Topic       │ │ SQS Queue       │
                 │ (Real-time)     │ │ (Vote batch)    │
                 └─────────────────┘ └─────────────────┘
                            │                   │
                            ▼                   ▼
                 ┌─────────────────┐ ┌─────────────────┐
                 │ PoolRealTime    │ │ Worker Function │
                 │ Handler Function│ │ (Process votes) │
                 │ (SNS Trigger)   │ │ (SQS Trigger)   │
                 └─────────────────┘ └─────────────────┘
                            │
                            ▼
                 ┌─────────────────┐
                 │ API Gateway     │
                 │ WebSocket API   │
                 └─────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│ ConnectHandler  │ │ DisconnectHandler│ │ WebSocket       │
│ Function        │ │ Function        │ │ Clients         │
│ $connect        │ │ $disconnect     │ │ (Real-time)     │
└─────────────────┘ └─────────────────┘ └─────────────────┘
```

## 🚀 Funciones Lambda

### 1. VoteCreateFunction
**Propósito**: Registrar votos y publicar eventos para procesamiento asíncrono y notificaciones en tiempo real.

**Endpoint**: `POST /vote/create`

**Request Body**:
```json
{
  "poll_id": "POOL_uuid",
  "option_id": "1"
}
```

**Características**:
- Validación de duplicados en Redis
- Verificación de fecha de cierre del pool
- Publicación a EventBridge para distribución de eventos
- EventBridge enruta eventos a SNS (tiempo real) y SQS (procesamiento batch)
- Cache de votos para prevenir duplicados
- Respuesta HTTP 204 para éxito

### 2. PoolCreateFunction
**Propósito**: Crear pools de votación con opciones personalizadas.

**Endpoint**: `POST /poll/create`

**Request Body**:
```json
{
  "question": "¿Cuál es tu lenguaje de programación favorito?",
  "options": ["Java", "Python", "JavaScript", "Go"]
}
```

**Funcionalidad**:
- Crea pool con pregunta y opciones
- Inicializa contadores de votos en 0
- Configura TTL para expiración automática
- Genera ID único para el pool

### 3. PoolGetFunction
**Propósito**: Obtener resultados de votación en tiempo real.

**Endpoint**: `GET /poll/{pollId}`

**Response**:
```json
{
  "poolId": "POOL_uuid",
  "question": "¿Cuál es tu lenguaje favorito?",
  "totalVotes": 150,
  "options": [
    {
      "optionId": "1",
      "text": "Java",
      "votes": 75,
      "percentage": 50.0
    },
    {
      "optionId": "2", 
      "text": "Python",
      "votes": 45,
      "percentage": 30.0
    }
  ]
}
```

**Características**:
- Cache en Redis para respuestas rápidas
- Fallback a DynamoDB si no hay cache
- Cálculo automático de porcentajes
- Manejo de pools no encontrados (404)

### 4. WorkerFunction
**Propósito**: Procesar votos de forma asíncrona desde SQS en lotes.

**Trigger**: SQS Queue Events

**Funcionalidad**:
- Procesa múltiples votos en lotes
- Actualiza contadores en DynamoDB de forma atómica
- Invalida cache de Redis por pool
- Manejo de errores y reintentos automáticos
- Deduplicación de pools en el mismo lote

### 5. ConnectHandlerFunction
**Propósito**: Manejar conexiones WebSocket para actualizaciones en tiempo real.

**Trigger**: WebSocket API Gateway `$connect` route

**Query Parameters**: `?poolId=POOL_uuid`

**Funcionalidad**:
- Registra conexiones WebSocket en DynamoDB
- Asocia conexiones con pools específicos
- Valida parámetros de conexión (poolId)
- Manejo de errores de conexión
- TTL automático para limpieza de conexiones

### 6. DisconnectHandlerFunction
**Propósito**: Limpiar conexiones WebSocket cuando se desconectan.

**Trigger**: WebSocket API Gateway `$disconnect` route

**Funcionalidad**:
- Elimina conexiones de DynamoDB
- Limpieza automática de recursos
- Logging de desconexiones para auditoría

### 7. PoolRealTimeHandlerFunction
**Propósito**: Enviar actualizaciones en tiempo real a clientes WebSocket.

**Trigger**: SNS Topic (eventos de votación)

**Funcionalidad**:
- Recibe eventos de votación desde SNS
- Obtiene conexiones activas del pool específico
- Recupera resultados actualizados de votación
- Envía datos en tiempo real via WebSocket API Gateway
- Manejo automático de conexiones inactivas
- Limpieza de conexiones expiradas

## 🛠️ Tecnologías

- **Runtime**: Java 21
- **Framework**: AWS SAM
- **Base de datos**: Amazon DynamoDB
- **Cache**: Redis
- **Cola de mensajes**: Amazon SQS
- **Notificaciones**: Amazon SNS
- **WebSocket**: API Gateway WebSocket API
- **Testing**: JUnit 4/5, Mockito, Testcontainers
- **Build**: Maven
- **Serialización**: Jackson
- **Logging**: AWS Lambda Logging

## 📦 Estructura del Proyecto

```
vote-lambda/
├── VoteCreateFunction/              # Registrar votos (votecreate.App)
├── PoolCreateFunction/              # Crear pools (com.fernando.vote.poolcreate.App)
├── PoolGetFunction/                 # Obtener resultados (com.fernando.vote.poolget.App)
├── WorkerFunction/                  # Procesador asíncrono (com.fernando.vote.workerfunction.App)
├── ConnectHandlerFunction/          # Conexiones WebSocket (com.fernando.vote.connecthandlerfunction.App)
├── DisconnectHandlerFunction/       # Desconexiones WebSocket (com.fernando.vote.disconnecthandlerfunction.App)
├── PoolRealTimeHandlerFunction/     # Actualizaciones tiempo real (com.fernando.vote.poolrealtimehandlerfunction.App)
├── events/                          # Eventos de prueba JSON
├── template.yaml                    # Infraestructura SAM
├── docker-compose.yml               # Servicios locales (Redis, LocalStack)
├── run-tests.bat                   # Script de testing
└── samconfig.toml                  # Configuración SAM
```

## 🚀 Instalación y Despliegue

### Prerrequisitos
- Java 21
- Maven 3.8+
- AWS SAM CLI
- Docker
- AWS CLI configurado

### Desarrollo Local

1. **Clonar repositorio**:
```bash
git clone <repository-url>
cd vote-lambda
```

2. **Iniciar servicios locales**:
```bash
docker-compose up -d
```

3. **Construir proyecto**:
```bash
sam build
```

4. **Ejecutar localmente**:
```bash
sam local start-api
```

5. **Ejecutar tests**:
```bash
run-tests.bat
```

### Despliegue en AWS

1. **Despliegue inicial**:
```bash
sam deploy --guided
```

2. **Despliegues posteriores**:
```bash
sam deploy
```

## 🧪 Testing

### Tests Unitarios
```bash
# Ejecutar todos los tests
run-tests.bat

# Test específico por función
cd VoteCreateFunction
mvn test
```

### Tests de Integración
- Utiliza Testcontainers para DynamoDB Local
- Simula eventos de API Gateway y SQS
- Valida integración completa con Redis
- Tests de WebSocket con LocalStack

### Eventos de Prueba
```bash
# Registrar voto
sam local invoke VoteCreateFunction --event events/send-vote.json

# Procesar votos en lote
sam local invoke WorkerFunction --event events/sqs-batch-event.json
```

## 📊 Modelo de Datos DynamoDB

### Tabla: VoteTable

| PK        | SK              | Atributos             | Descripción          |
|-----------|-----------------|-----------------------|----------------------|
| POOL_{id} | METADATA        | question, active, ttl, createdAt | Metadatos de pool    |
| POOL_{id} | OP_{optId}      | text, ttl             | Opciones de pool     |
| POOL_{id} | OP_{optId}_VOTE | votes, ttl            | Contadores de votos  |
| CONNECTION_{id} | POOL_{id}  | connectionId, poolId, ttl | Conexiones WebSocket |

### Patrones de Acceso
- **Crear pool**: Transacción para POOL_METADATA + opciones
- **Votar**: Incremento atómico en OP_{optId}_VOTE
- **Obtener resultados**: Query por PK=POOL_{id}
- **Registrar conexión WebSocket**: Put CONNECTION_{id} + POOL_{id}
- **Obtener conexiones de pool**: Query por SK=POOL_{id} con GSI
- **Limpiar conexión**: Delete CONNECTION_{id}

### Índices Secundarios Globales (GSI)
- **PoolConnectionsGSI**: SK (POOL_{id}) como PK para obtener conexiones por pool

## 🔧 Configuración

### Variables de Entorno
```yaml
DB_TABLE_NAME: VoteTable
REDIS_HOST: localhost
REDIS_PORT: 6379
REDIS_SSL: false
QUEUE_URL: http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/votes-queue
SNS_TOPIC_ARN: arn:aws:sns:us-east-1:000000000000:vote-notifications
WEBSOCKET_API_ENDPOINT: wss://localhost:4566
```

### Servicios Locales (docker-compose.yml)
- **Redis**: Puerto 6379 (cache y deduplicación)
- **LocalStack**: Puerto 4566
  - DynamoDB Local
  - SQS (procesamiento asíncrono)
  - SNS (notificaciones tiempo real)
  - API Gateway (REST y WebSocket)
  - Lambda (ejecución local)
  - IAM/STS (permisos)

### Configuración Redis
- **Persistencia**: Volumen Docker para datos
- **Red**: `votacion-network` compartida
- **TTL**: Configurado para limpieza automática

## 🔄 Capacidades de Tiempo Real

### WebSocket API
El sistema incluye capacidades de tiempo real mediante WebSocket API Gateway:

**Conexión**:
```javascript
const ws = new WebSocket('wss://your-api-id.execute-api.region.amazonaws.com/stage?poolId=POOL_123');

ws.onmessage = function(event) {
    const voteResults = JSON.parse(event.data);
    console.log('Resultados actualizados:', voteResults);
};
```

**Eventos en Tiempo Real**:
- Nuevos votos registrados
- Actualización de contadores por opción
- Cambios en porcentajes
- Estado del pool (activo/cerrado)

**Flujo de Notificaciones**:
1. Usuario vota → VoteCreateFunction
2. Evento publicado → SNS Topic
3. PoolRealTimeHandler procesa evento
4. Obtiene conexiones activas del pool
5. Recupera resultados actualizados
6. Envía datos a conexiones WebSocket activas
7. Clientes reciben actualización instantánea

### Manejo de Conexiones
- **Registro automático**: Al conectar WebSocket con poolId
- **Limpieza automática**: Al desconectar o por TTL
- **TTL en DynamoDB**: Expiración automática de conexiones (24h)
- **Manejo de errores**: Conexiones inactivas removidas automáticamente
- **Escalabilidad**: Soporte para múltiples conexiones por pool

## 🚦 Flujo de Votación

### Flujo Básico
1. **Crear Pool** → PoolCreateFunction (con pregunta y opciones)
2. **Votar** → VoteCreateFunction (publica a SNS)
3. **Procesar Voto** → WorkerFunction (actualiza contadores desde SQS)
4. **Ver Resultados** → PoolGetFunction (desde cache/DB)

### Flujo Tiempo Real
1. **Conectar WebSocket** → ConnectHandlerFunction (registra conexión)
2. **Votar** → VoteCreateFunction (publica evento SNS)
3. **Notificar** → PoolRealTimeHandlerFunction (envía a WebSocket)
4. **Desconectar** → DisconnectHandlerFunction (limpia conexión)

### Arquitectura de Eventos
```
Voto → EventBridge → SNS Topic → PoolRealTimeHandler → WebSocket Clients
                   ↘ SQS Queue → WorkerFunction → DynamoDB → Cache Invalidation
```

### Procesamiento Asíncrono
- **Inmediato**: Notificaciones WebSocket via SNS
- **Batch**: Actualización de contadores via SQS
- **Cache**: Invalidación selectiva por pool
- **Consistencia**: Eventual consistency para alta disponibilidad

## 📈 Monitoreo y Logs

### CloudWatch Logs
- Logs estructurados en JSON por función
- Métricas de latencia y errores
- Alertas configurables por función

### Métricas Clave
- Tiempo de respuesta por función
- Tasa de errores por endpoint
- Throughput de votación
- Utilización de cache Redis
- Conexiones WebSocket activas
- Mensajes procesados en SQS

### Dashboards
- Métricas de votación en tiempo real
- Estado de conexiones WebSocket
- Performance de cache Redis
- Errores y reintentos

## 🔒 Seguridad

### IAM Policies
- Acceso mínimo necesario por función
- Separación de permisos por recurso
- Roles específicos por Lambda
- Políticas de DynamoDB granulares

### Validación
- Validación de entrada con Jackson
- Sanitización de datos de entrada
- Prevención de votos duplicados via Redis
- Validación de parámetros WebSocket

### CORS
- Configuración CORS habilitada
- Headers de seguridad incluidos
- Métodos HTTP específicos permitidos

## 🎯 Casos de Uso

### Votaciones en Vivo
- Eventos corporativos
- Webinars interactivos
- Encuestas de audiencia
- Decisiones de equipo

### Características Destacadas
- **Escalabilidad**: Maneja miles de votos simultáneos
- **Tiempo Real**: Actualizaciones instantáneas via WebSocket
- **Durabilidad**: Persistencia en DynamoDB con TTL
- **Performance**: Cache Redis para respuestas rápidas
- **Observabilidad**: Logs y métricas completas

## 📝 Contribución

1. Fork del repositorio
2. Crear rama feature: `git checkout -b feature/nueva-funcionalidad`
3. Commit cambios: `git commit -am 'Agregar nueva funcionalidad'`
4. Push a la rama: `git push origin feature/nueva-funcionalidad`
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 🆘 Soporte

Para reportar bugs o solicitar features, crear un issue en el repositorio de GitHub.

---

**Desarrollado con ❤️ usando AWS Serverless**