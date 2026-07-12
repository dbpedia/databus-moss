# Databus MOSS

The Databus MOSS (Metadata Overlay Search System) is a system of creating modular metadata extensions for the metadata graphs provided by [Databus](https://github.com/dbpedia/databus) entries. The general idea is that a Databus instance provides stable identifiers for your files along with the most basic metadata for file usage (format, compression, licensing). However, more domain or file-type specific metadata is often needed. MOSS provides a system for the management of additional metadata graphs. With MOSS, you can:

* Create schema for metadata extensions
* Create and manage additional metadata graphs
* Tailor a flexible indexed searcher directly to your specific RDF-metadata
* Enjoy enhanced data retrieval based on your metadata entries

Here you can find instructions for deployment of a MOSS instance with Docker and examples to get you started. The code for the two main components (server and frontend) are held in separate repositories.

* MOSS-Server: [https://github.com/dbpedia/databus-moss-server](https://github.com/dbpedia/databus-moss-server)
* MOSS-Frontend: [https://github.com/dbpedia/databus-moss-frontend](https://github.com/dbpedia/databus-moss-frontend)


## Deployment and Example


Run

```
git clone https://github.com/dbpedia/databus-moss.git
cd databus-moss
```

Adjust the environment variables in the `.env` file (see [Environment Variables](#environment-variables)).

Then run

```
docker compose pull
docker compose up
```


Go to

```
http://localhost:3000
```

MOSS configuration lives in `./config` and is mounted into the server container.


## Containers

`ghcr.io/dbpedia/databus-moss-frontend:dev`
Frontend using Svelte. Exposed on `${MOSS_FRONTEND_PORT}` (default `3000`).

`ghcr.io/dbpedia/databus-moss-server:dev`
Server with MOSS API. Expose port 8080 when running as standalone API server.

`ghcr.io/dbpedia/gstore:dev`
Gstore backend proxy.

`openlink/virtuoso-opensource-7:latest`
Triple store backend.


## Environment Variables

Create a `.env` file in the project root:

| Variable | Example | Description |
| --- | --- | --- |
| `MOSS_FRONTEND_PORT` | `3000` | Host port mapped to the frontend container. |
| `MOSS_BASE_URL` | `https://moss.example.org` | Public base URL of the MOSS instance. Used for generating resource identifiers. Most commonly the public URL of the frontend. |
| `VIRTUOSO_DBA_PASSWORD` | `...` | Password for the Virtuoso `dba` user. Also used by *gstore* to connect to Virtuoso. |
| `AUTH_OIDC_ISSUER` | `https://auth.dbpedia.org/realms/dbpedia` | OpenID Connect issuer URL of your identity provider. |
| `AUTH_OIDC_CLIENT_ID` | `moss-dev` | OIDC client ID registered with the identity provider. |
| `AUTH_OIDC_CLIENT_SECRET` | `...` | OIDC client secret for the registered client. |
| `AUTH_SECRET` | `...` | Secret used by the frontend to sign and encrypt session cookies. Generate a random string. |
| `AUTH_ADMIN_USERS` | `user1,user2` | Comma-separated list of usernames granted admin access in MOSS. |
| `AUTH_ORIGIN` | `https://moss.example.org` | Public URL of the frontend. Used for OIDC redirect validation. Must match how users access the app. |

