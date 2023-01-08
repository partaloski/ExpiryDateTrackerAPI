#Expiry Date Tracker App's API for data storing and inputting.</h1>
---
[![Check Out, Build and Push Image to DockerHub](https://github.com/partaloski/ExpiryDateTrackerAPI/actions/workflows/update-docker-image.yml/badge.svg?event=push)](https://github.com/partaloski/ExpiryDateTrackerAPI/actions/workflows/update-docker-image.yml)
---
Made by: Petar Partaloski - 193181 for the aim of completing a project for the subjects:</p>

1. Mobile Platforms and Programming
2. Software Quality and Testing
3. DevOps - Continuous Integration and Delivery


---

##Running with Docker Compose

To run the application using the Docker-Compose application, you'll need to have installed:
1. Docker


After making sure that you have all the required prerequisites, first, run Docker Desktop, wait for it to turn to state READY, and when all of that has been done make sure that your current directory is the one containing the `docker-compose.yml` file, and then run the command: 
`docker-compose up`

The application should then be up and running on `localhost:9091`


---

##Running with Helm Chart

To run the application using Helm, you'll need to first install:
1. Docker
2. Helm
3. Minikube

After all of that has been done, proceed with the following steps:
1. Open up Terminal
2. Run the commang `minikube start`
3. Go to the directory of the helm chart, go inside of it using the commands `cd .\helm\edt`
4. Run the command `kubectl install edtapp .`
5. Check its status using `kubectl get pods`
6. When the status is `RUNNING`, run the following command: `kubectl port-forward service/edtapp 8080:8080`
7. The application should be running at `localhost:8080`