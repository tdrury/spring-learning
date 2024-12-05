
### URLs

`GET /` - returns "public page"
`GET /private` - returns "private page"
`GET /user` - shows user info from google oauth2

### Prerequisites

Need to create file `src/main/resources/application-secrets.yml` with contents:

```
spring:
  security.oauth2.client.registration.google:
    client-id: <client-id>
    client-secret: <client-secret>
    scope:
      - email
      - profile
```
where `<client-id>` and `<client-secret>` are generated from the [Google Console](https://console.cloud.google.com/apis/credentials). Read more [here](https://www.baeldung.com/spring-security-5-oauth2-login).

