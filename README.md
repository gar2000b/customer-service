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