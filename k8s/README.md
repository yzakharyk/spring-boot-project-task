# Kubernetes Deployment for Demo Project

## Prerequisites

- Docker
- Kubernetes cluster (local or remote)
- kubectl configured for your cluster

## Build Docker Image

```sh
./mvnw clean package
docker build -t demo-app:latest .
```

## Push Docker Image (if using remote cluster)

```sh
docker tag demo-app:latest <your-docker-registry>/demo-app:latest
docker push <your-docker-registry>/demo-app:latest
```

Update the image name in `deployment.yaml` if using a remote registry.

## Create Database Secrets

Edit `k8s/db-secret.yaml` and encode your values using:

```sh
echo -n 'your-value' | base64
```

Apply the secret:

```sh
kubectl apply -f k8s/db-secret.yaml
```

## Deploy to Kubernetes

```sh
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

## Access the Application

Find the NodePort for the app service:

```sh
kubectl get svc demo-app-service
```

Open `http://<your-node-ip>:<node-port>/swagger-ui/index.html` in your browser.

## Notes

- Database credentials and URL are managed via Kubernetes Secrets.
- Database data is stored in an ephemeral volume (`emptyDir`). For production, use a PersistentVolumeClaim.
- Adjust resource requests/limits in `deployment.yaml` as needed.
