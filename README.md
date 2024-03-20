Descripción

Esta es una aplicación para un negocio que repara productos electrónicos. Permite gestionar clientes, productos, presupuestos, órdenes de trabajo y usuarios con acceso mediante inicio de sesión.
Tecnologías Utilizadas

    Backend: Java con Spring Boot
    Frontend: Vue.js 3
    Base de Datos: PostgreSQL
    Contenedorización: Docker Compose

Requisitos Previos

Asegúrate de tener instalado lo siguiente en tu sistema:

    Docker
    Docker Compose
    Git

Instrucciones de Uso
1. Clonar el Repositorio

Clona este repositorio en tu máquina local utilizando el siguiente comando:

bash

git clone https://github.com/tu-usuario/tu-repositorio.git

2. Configurar la Base de Datos

El servicio de base de datos PostgreSQL se ejecuta en un contenedor Docker. No es necesario realizar ninguna configuración adicional.
3. Ejecutar la Aplicación

Navega hasta el directorio raíz del repositorio clonado y ejecuta el siguiente comando para levantar los contenedores Docker:

bash

docker-compose up -d

Esto creará y ejecutará los contenedores para el backend, frontend y base de datos. Puedes acceder a la aplicación a través de tu navegador web visitando http://localhost:5175.
4. Acceso a la Aplicación

    Usuario Administrador:
        Correo Electrónico: admin@example.com
        Contraseña: admin123

5. Detener la Aplicación

Para detener los contenedores Docker y apagar la aplicación, ejecuta el siguiente comando en el directorio raíz del repositorio:

bash

docker-compose down

Contribución

¡Las contribuciones son bienvenidas! Si encuentras algún problema o deseas mejorar esta aplicación, no dudes en enviar un pull request.
