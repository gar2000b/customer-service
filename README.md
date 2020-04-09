# customer-service
Customer Service

docker network create -d bridge social-insurance  
docker network ls  

docker build -t gar2000b/customer-service .  
docker run -it -d -p 9082:9082 --network="social-insurance" --name customer-service gar2000b/customer-service  

All optional:

docker create -it gar2000b/customer-service bash  
docker ps -a  
docker start ####  
docker ps  
docker attach ####  
docker remove ####  
docker image rm gar2000b/customer-service  
docker exec -it customer-service sh  
docker login  
docker push gar2000b/customer-service  

mySQL database:

Dockerfile and init.sql files are in the database directory. 

cd database
docker pull mysql:latest
docker run --name db -d -p3306:3306 -e MYSQL_ROOT_PASSWORD=123 mysql:latest
docker exec -it db /bin/bash
exit
docker rm db
docker build -t gar2000b/mysql .
docker run --name db -d -p3306:3306 gar2000b/mysql:latest

Re-connect to running datbase:
docker exec -it db /bin/bash
mysql -usocial -p1234
show databases;
use users;
show tables;
select * from users;



