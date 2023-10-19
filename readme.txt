Pg avec docker

> docker run --name bd-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=riovas -p 5432:5432 -d postgres:16.0
> docker run -it --rm --link bd-postgres:postgres postgres:16.0 psql -h bd-postgres -U postgres

-----
Build image intervenant

mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker build -t m2/intervenant-service:1.0 .

----
docker compose up
