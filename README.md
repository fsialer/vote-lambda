# Vote Lambda - Sistema de Votaciones Serverless

Sistema de votaciones distribuido construido con AWS Lambda, DynamoDB, Redis y SQS. Permite crear encuestas, votar y obtener resultados en tiempo real.

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ VoteCreate      │    │ PoolCreate      │    │ PoolGet         │
│ Function        │    │ Function        │    │ Function        │
│                 │    │                 │    │                 │
│ POST /vote/     │    │ POST /pool/     │    │ GET /pool/{id}  │
│ create          │    │ create          │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
       │                       │                       │
       ▼                       ▼                       ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              DynamoDB - VoteTable                                   │
│  PK: POOL_{id} | SK: METADATA    → Pool metadata                                    │
│  PK: POOL_{id} | SK: OP#{optId}  → Pool options                                     │
│  PK: POOL_{id} | SK: OP_{optId}_vote  → Pool votes with vote counts                 │
└─────────────────────────────────────────────────────────────────────────────────────┘
         │                       │                       │                       │
         ▼                       ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Redis Cache     │    │ SQS Queue       │    │ Worker Function │    │ Redis Cache     │
│ (Pool results)  │    │ (Vote events)   │    │ (Process votes) │    │ (Fast reads)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Funciones Lambda

### 1. VoteCreateFunction
**Propósito**: Registrar votos y enviarlos a cola SQS para procesamiento asíncrono.

**Endpoint**: `POST /vote/create`

**Request Body**:
```json
{
  "poolId": "POOL#uuid",
  "optionId": 1,
  "userId": "user123"
}
```

**Características**:
- Validación de duplicados en Redis
- Envío asíncrono a SQS
- Cache de votos para prevenir duplicados

### 2. PoolCreateFunction
**Propósito**: Crear pools de votación basados en encuestas existentes.

**Endpoint**: `POST /pool/create`

**Request Body**:
```json
{
  "surveyId": "SURV#uuid",
  "title": "Pool de votación activo"
}
```

**Funcionalidad**:
- Copia opciones de encuesta a pool
- Inicializa contadores de votos
- Configura TTL para expiración automática

### 3. PoolGetFunction
**Propósito**: Obtener resultados de votación en tiempo real.

**Endpoint**: `GET /pool/{poolId}`

**Response**:
```json
{
  "poolId": "POOL#uuid",
  "title": "Pool de votación",
  "totalVotes": 150,
  "options": [
    {
      "optionId": 1,
      "description": "Java",
      "votes": 75,
      "percentage": 50.0
    },
    {
      "optionId": 2,
      "description": "Python",
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

### 4. WorkerFunction
**Propósito**: Procesar votos de forma asíncrona desde SQS.

**Trigger**: SQS Queue Events

**Funcionalidad**:
- Procesa votos en lotes
- Actualiza contadores en DynamoDB
- Invalida cache de Redis
- Manejo de errores y reintentos

## 🛠️ Tecnologías

- **Runtime**: Java 21
- **Framework**: AWS SAM
- **Base de datos**: Amazon DynamoDB
- **Cache**: Redis
- **Cola de mensajes**: Amazon SQS
- **Testing**: JUnit 4/5, Mockito, Testcontainers
- **Build**: Maven

## 📦 Estructura del Proyecto

```
vote-lambda/
├── VoteCreateFunction/       # Registrar votos (votecreate.App)
├── PoolCreateFunction/       # Crear pools (com.fernando.vote.poolcreate.App)
├── PoolGetFunction/          # Obtener resultados (com.fernando.vote.poolget.App)
├── WorkerFunction/           # Procesador asíncrono (com.fernando.vote.workerfunction.App)
├── events/                   # Eventos de prueba
├── template.yaml            # Infraestructura SAM
├── docker-compose.yml       # Servicios locales
└── run-tests.bat           # Script de testing
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
cd SurveyCreateFunction
mvn test
```

### Tests de Integración
- Utiliza Testcontainers para DynamoDB Local
- Simula eventos de API Gateway
- Valida integración completa

### Eventos de Prueba
```bash
# Crear encuesta
sam local invoke SurveyCreateFunction --event events/survey-create-event.json

# Registrar voto
sam local invoke VoteCreateFunction --event events/send-vote.json
```

## 📊 Modelo de Datos DynamoDB

### Tabla: VoteTable

| PK        | SK              | Atributos             | Descripción          |
|-----------|-----------------|-----------------------|----------------------|
| POOL_{id} | METADATA        | question, active, ttl | Metadatos de encuesta |
| POOL_{id} | OP_{optId}      | text                  | Opciones de encuesta |
| POOL#{id} | OP_{optId}_vote | votes, ttl            | Votes de encuesta    |

### Patrones de Acceso
- **Crear pool**: Transacción para POOL_ + opciones
- **Votar**: Incremento atómico en POOL_
- **Obtener resultados**: Query por PK=POOL_{id}

## 🔧 Configuración

### Variables de Entorno
```yaml
DB_TABLE_NAME: PoolTable
REDIS_HOST: localhost
REDIS_PORT: 6379
REDIS_SSL: false
QUEUE_URL: http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/votes-queue
```

### Servicios Locales (docker-compose.yml)
- **DynamoDB Local**: Puerto 8000
- **Redis**: Puerto 6379
- **LocalStack SQS**: Puerto 4566

## 📈 Monitoreo y Logs

### CloudWatch Logs
- Logs estructurados en JSON
- Métricas de latencia y errores
- Alertas configurables

### Métricas Clave
- Tiempo de respuesta por función
- Tasa de errores
- Throughput de votación
- Utilización de cache

## 🔒 Seguridad

### IAM Policies
- Acceso mínimo necesario por función
- Separación de permisos por recurso
- Roles específicos por Lambda

### Validación
- Validación de entrada con Bean Validation
- Sanitización de datos
- Prevención de votos duplicados

## 🚦 Flujo de Votación

1. **Crear Pool** → PoolCreateFunction (basado en encuesta)
2. **Votar** → VoteCreateFunction (envía a SQS)
3. **Procesar Voto** → WorkerFunction (actualiza contadores)
4. **Ver Resultados** → PoolGetFunction (desde cache/DB)

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