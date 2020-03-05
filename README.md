# customer-service
Customer Service

docker network create -d bridge social-insurance  
docker network ls  

docker build -t onlineinteract/customer-service .  
docker run -it -d -p 9082:9082 --network="social-insurance" --name customer-service onlineinteract/customer-service  

All optional:

docker create -it onlineinteract/customer-service bash  
docker ps -a  
docker start ####  
docker ps  
docker attach ####  
docker remove ####  
docker image rm onlineinteract/customer-service  
docker exec -it customer-service sh  