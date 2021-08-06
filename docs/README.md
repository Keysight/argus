# argus
 Regression results analyzer

```
git clone argus.git
cd ./argus

docker-compose up -d
docker-compose down

docker ps
docker exec -it argus_db_1 bash
mysql -u root -p < /root/schema.sql
mysql -u root -p ARGUS < /root/demo_data.sql
```
