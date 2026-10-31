# Despliegue — pendiente de crear (fases 1 y 7)

- `compose/`: paquete Docker Compose (aplicación, PostgreSQL, Nginx, almacenamiento de archivos), `.env.example`, script de instalación, respaldos programados.
- `offline/`: instalador para servidores sin internet (imágenes empaquetadas).
- `aws/`: Terraform de referencia (VPC, RDS PostgreSQL, S3, SES, ECS Fargate o EC2, CloudFront, Secrets Manager).
Ver `docs/ARQUITECTURA.md` §6.
