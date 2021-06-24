# argus db
 Regression results analyzer database

## schema
 <img src="https://raw.github.com/Keysight/argus/main/db/schema.svg">
 
 
## sql server
preferably use mysql 8.0+, if not possible mariadb will also work. (other sql implementations may work as well but were not tested)<br>
for ease of use the examples here will use mysql docker container (a vm or bare metal deployment can be used as well)


https://hub.docker.com/r/mysql/mysql-server
```
docker pull mysql/mysql-server
```

### data will be stored in the host machine outside of the container
```
mkdir -p /storage/docker/mysql-argus-data
```

### start the container 
```
#docker rm mysql-argus (optional just in case it needs to be started again with same name)
docker run \
--detach \
--name=mysql-argus \
--env="MYSQL_ROOT_PASSWORD=argus" \
--publish 3306:3306 \
--volume=/storage/docker/mysql-argus-data:/var/lib/mysql \
mysql/mysql-server
```

### check at any time the status of the mysql container, by checking if the status is "healthy"
```
docker ps
```

### must be done once to allow access from remote machines if needed.
```
docker exec -it mysql-argus mysql -u root -p

CREATE USER 'root'@'%' IDENTIFIED BY 'argus';
GRANT ALL ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;
exit
```

### import schema, this must be done only once.
```
docker clone https://github.com/Keysight/argus.git
docker exec -i mysql-argus sh -c 'exec mysql -uroot -p"argus"' < ./argus/db/schema.sql
```

### stop the container without corrupting the data
```
docker exec mysql-argus /usr/bin/mysqladmin -u root -p"argus" shutdown
docker container logs mysql-argus
#Shutdown complete
docker stop mysql-argus
```

