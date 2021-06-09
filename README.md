# Aplicación: Notas Online
## Lenguajes utilizados: 
JavaScript, Java, Ajax, XML
## Notas:
1. Directorio donde se guardan las imagenes en base64 de los usuarios ubicado en /home/user/tomcat/fotos.
2. Fichero tomcat-users.xml usado:
```xml
<role rolename="rolpro"/>
<role rolename="rolalu"/>
<user username="Ramon" password="1234" roles="rolpro"/>
<user username="Manoli" password="1234" roles="rolpro"/>
<user username="Pedro" password="1234" roles="rolpro"/>
<user username="Joan" password="1234" roles="rolpro"/>
<user username="Pepe" password="1234" roles="rolalu"/>
<user username="Maria" password="1234" roles="rolalu"/>
<user username="Miguel" password="1234" roles="rolalu"/>
<user username="Laura" password="1234" roles="rolalu"/>
<user username="Minerva" password="1234" roles="rolalu"/>
```
3. Aplicación exportada en formato .war, creada en eclipse y desplegada usando Tomcat v9.0.
