Förklaring:

- Testcontainers: Används för att köra en riktig MongoDB-instans i en Docker-container under tester. Detta ger en mer realistisk testmiljö.
- Spring Boot Starter Test: Inkluderar JUnit, Mockito och andra testverktyg.
- Embedded MongoDB (Flapdoodle): Ett alternativ till Testcontainers som kör en in-memory MongoDB-instans.

**Steg 3: Konfigurera Testmiljön**
- För integrationstester kan du skapa en separat konfigurationsfil, till exempel application-test.properties, för att ställa in testspecifika inställningar.

