# Proyecto Final

Este repositorio contiene el proyecto final desarrollado como parte de [agregar el contexto, como una asignatura, curso o proyecto personal]. La aplicación está estructurada con microservicios y está diseñada para ejecutarse en contenedores Docker y en un entorno de Kubernetes.

## Contenido del Proyecto

- **frontend**: Contiene el código del frontend de la aplicación.
- **microusers**: Contiene el microservicio para gestionar usuarios.
- **kubernetes**: Archivos de configuración para el despliegue en Kubernetes.
- **docker-compose.yml**: Archivo de configuración para orquestar los servicios en Docker.

## Requisitos Previos

Asegúrate de tener instalados los siguientes programas en tu sistema:

- **Docker**: Para crear y gestionar contenedores.
- **Docker Compose**: Para orquestar los servicios.
- **Kubernetes** (opcional): Para el despliegue en un entorno de Kubernetes.
- **Minikube** (opcional): Si deseas probar Kubernetes en un entorno local.
- **Prometheus y Grafana** (opcional): Para monitoreo de la aplicación.

## Instalación y Ejecución

### Paso 1: Clonar el Repositorio

Clona este repositorio en tu máquina local:

```
git clone https://github.com/juankMolo/proyecto_final.git
cd proyecto_final
```

### Paso 2: Ejecución con Docker Compose

Para ejecutar el proyecto en Docker Compose, utiliza el siguiente comando:

```
docker-compose up -d
```

### Paso 3: Despliegue en Kubernetes

Si deseas desplegar el proyecto en Kubernetes, asegúrate de tener Minikube o un clúster de Kubernetes configurado y ejecuta:

```
kubectl apply -f kubernetes/
```

### Monitoreo con Prometheus y Grafana

Para monitorear la aplicación, puedes configurar Prometheus y Grafana. Asegúrate de que los servicios de monitoreo estén correctamente configurados en tu clúster.
