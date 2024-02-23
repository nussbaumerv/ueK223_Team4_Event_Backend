# README #

## Starter Project Spring Boot


### Schritt 1
Zuerst müssen Sie Ihre Postgres-Datenbank erstellen. Verwenden Sie den unten stehenden Command. Sobald Sie das gemacht haben, können Sie die Docker App öffnen und den Docker starten.
### Docker command
```
docker run --name postgres_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```
### Schritt 2
Öffnen Sie Ihren Backend Ordner in Ihrer Umgebung und klicken Sie auf den Button Gradle, der sich auf der rechten Seite ihrer Umgebung befindet. Öffnen Sie die Ordner in der Navigationsleiste in der folgenden Reihenfolge: demo>Tasks>application>bootrun. Klicken Sie auf die Schaltfläche bootrun, um das Programm zu starten.
![image](https://github.com/nussbaumerv/ueK223_Team4_Event_Backend/assets/113606362/56b44c91-eb42-4c9d-999e-4cf89c6210c7)

### Schritt 3
Öffnen Sie ihr geclontes Frontend Projekt und öffnen ein Terminal und geben sie Folgendes ein:
```
yarn start
```

### Schritt 4
| E-Mail            | Password | Authorities                                      |   |   |
|-------------------|----------|--------------------------------------------------|---|---|
| user@example.com  | 1234     | Create/Read/Update/Delete/Participate/Join Event |   |   |
| admin@example.com | 1234     | Hat alle Admin Rechte                            |   |   |
|                   |          |                                                  |   |   |
### Troubleshooting

```
org.postgresql.util.PSQLException: ERROR: relation "role_authority" does not exist
```
Starten Sie einfach die Anwendung neu. Hibernate initialisiert die Tabellen manchmal nicht schnell genug und verursacht diesen Fehler. Ein Neustart der Anwendung behebt dies.

